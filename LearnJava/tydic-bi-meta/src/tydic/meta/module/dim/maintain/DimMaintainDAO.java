package tydic.meta.module.dim.maintain;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 作用:XXX <br>
 * @date 2011-12-21
 */
public class DimMaintainDAO extends MetaBaseDAO{

    /**
     * 维度维度视图列数据查询。
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryDimView(Map<String,Object> queryData ,Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                   "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                   "D.DATA_SOURCE_NAME,A.TABLE_VERSION FROM META_TABLES A " +
                   "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                   "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                   "WHERE A.TABLE_STATE="+TblConstant.META_DIM_TYPE_STATE_VALID+" AND  A.TABLE_TYPE_ID= "+TblConstant.META_TABLE_TYPE_ID_DIM+" "
                   );
       List<Object> list = new ArrayList<Object>();
        if(queryData != null){
            if(queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").toString().equals("")){
                //业务类型
                sql.append(" AND A.TABLE_GROUP_ID IN (SELECT E.TABLE_GROUP_ID  FROM  META_TABLE_GROUP  E START WITH E.TABLE_GROUP_ID =?" +
                		" CONNECT BY PRIOR E.TABLE_GROUP_ID = E.PAR_GROUP_ID)");
                list.add(queryData.get("tableGroupId"));
            }
            //关键字处理，仅支持单个关键字
            if(queryData.get("keyWord") != null && !queryData.get("keyWord").toString().equals("")){
                String key = queryData.get("keyWord").toString();
                if(!key.contains("%")&&!key.contains("_")){
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
                    list.add(("%"+key+"%"));
                    list.add(("%"+key+"%"));
                    list.add(("%"+key+"%"));

                }else{
                    key = key.replaceAll("_","/_").replaceAll("%","/%");
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?) ESCAPE '/' ) ");
                    list.add(("%"+key+"%"));
                    list.add(("%"+key+"%"));
                    list.add(("%"+key+"%"));
                }
            }
        }
        sql.append(" ORDER BY A.TABLE_ID DESC");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, list.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE,MapUtils.getString(map,"TABLE_TYPE_ID")));
            }
        }
        return rs;
    }

}
