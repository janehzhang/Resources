package tydic.home.myCenter.myShare;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 报表管理 —— 个人中心 —— 共享给我的报表DAO <br>
 * @date 2012-4-17
 */
public class MyShareDAO extends MetaBaseDAO {

    /**
     * 查询别人共享给我的报表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyShareReport(Map<String, Object> queryData, Page page){
        List<Object> param = new ArrayList<Object>();
        String keyWord = Convert.toString(queryData.get("keyWord"));
        param.add(Convert.toInt(queryData.get("userId"),0));
        String sql = "SELECT A.REPORT_ID,A.SHARE_LIST_ID,B.REPORT_NAME,B.REPORT_STATE,C.USER_NAMECN CREATE_USER,E.ZONE_NAME, D.DEPT_NAME, " +
                " TO_CHAR(B.CREATE_TIME, 'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,TO_CHAR(A.SHARE_TIME, 'YYYY-MM-DD HH24:MI:SS') SHARE_TIME " +
                " FROM META_RPT_SHARE_LIST A " +
                " LEFT JOIN META_RPT_TAB_REPORT_CFG B ON A.REPORT_ID = B.REPORT_ID " +
                " LEFT JOIN META_MAG_USER C ON B.USER_ID = C.USER_ID " +
                " LEFT JOIN META_DIM_USER_DEPT D ON C.DEPT_ID = D.DEPT_CODE " +
                " LEFT JOIN META_DIM_ZONE E ON C.ZONE_ID = E.ZONE_ID " +
                " WHERE B.REPORT_STATE <> -1 AND A.USER_ID = ? ";
        if(!keyWord.equals("")){
            sql = sql + " AND  B.REPORT_NAME LIKE  "+SqlUtils.allLikeParam(keyWord) ;
        }
        sql = sql + " ORDER BY B.REPORT_STATE DESC, A.SHARE_TIME DESC,B.CREATE_TIME DESC,A.SHARE_LIST_ID DESC ";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, param.toArray());
    }

    /**
     * 删除一条别人共享给我的报表
     * @param shareListId
     */
    public void deleteOneShare(int shareListId){
        String sql = "DELETE FROM META_RPT_SHARE_LIST WHERE SHARE_LIST_ID = "+shareListId;
        getDataAccess().execUpdate(sql);
    }



}
