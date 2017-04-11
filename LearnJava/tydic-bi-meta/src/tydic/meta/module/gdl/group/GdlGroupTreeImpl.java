package tydic.meta.module.gdl.group;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.common.term.TermConstant;
import tydic.meta.common.term.TermDataCall;
import tydic.meta.common.term.TermDataService;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标分类下拉树实现
 * @date 12-6-6
 * -
 * @modify
 * @modifyDate -
 */
public class GdlGroupTreeImpl extends TermDataService{

    public Object[][] getData(DataAccess access, Map<String, Object> params, TermDataCall call) throws Exception {
        String sql = "SELECT " +
                "A.GDL_GROUP_ID, " +
                "A.GROUP_NAME, " +
                "A.PAR_GROUP_ID, " +
                "(case " +
                "  when exists (select 1 " +
                "          from META_GDL_GROUP " +
                "         where PAR_GROUP_ID = A.GDL_GROUP_ID) then " +
                "   1 " +
                "  else " +
                "   0 " +
                "end) HAS_CHILD " +
                "  FROM META_GDL_GROUP A ";
        boolean bl = MapUtils.getBoolean(params,TermConstant.KEY_dynload,false);
        if(!bl){
            Object o = params.get(TermConstant.KEY_classRuleParams);
            if(o!=null && o instanceof Map){
                Map<String,Object> mp = (Map<String,Object>) o;
                bl = MapUtils.getBoolean(mp,TermConstant.KEY_dynload,false);
            }
        }
        if(!bl){
            sql += "CONNECT BY PRIOR A.GDL_GROUP_ID = A.PAR_GROUP_ID " +
                    " START WITH A.PAR_GROUP_ID = 0 ";
        }else{
            sql += " WHERE A.PAR_GROUP_ID = 0";
            call.coverTermAttribute(TermConstant.KEY_dynload,true);
            call.coverTermAttribute(TermConstant.KEY_treeChildFlag,true);
        }
        sql += " ORDER BY ORDER_ID";
        return access.queryForArray(sql,false);
    }

    /**
     * 一般的下拉框数据，此方法无用，不需要调用
     * 如果是树，并且设置成了动态加载，那么必须重写此方法，实现加载子节点数据
     * @param access
     * @param params
     * @param parentID
     * @param call
     * @return
     * @throws Exception
     */
    public Object[][] getChildData(DataAccess access,Map<String,Object> params,String parentID,TermDataCall call) throws Exception{
        String sql = "SELECT " +
                "A.GDL_GROUP_ID, " +
                "A.GROUP_NAME, " +
                "A.PAR_GROUP_ID, " +
                "(case " +
                "  when exists (select 1 " +
                "          from META_GDL_GROUP " +
                "         where PAR_GROUP_ID = A.GDL_GROUP_ID) then " +
                "   1 " +
                "  else " +
                "   0 " +
                "end) HAS_CHILD " +
                "  FROM META_GDL_GROUP A " +
                " WHERE A.PAR_GROUP_ID = ? " +
                " ORDER BY ORDER_ID";
        return access.queryForArray(sql,false, Convert.toInt(parentID));
    }
}
