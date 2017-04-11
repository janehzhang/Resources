package tydic.meta.module.tbl;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.sys.code.CodeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description
 * @date 12-5-27
 * -
 * @modify
 * @modifyDate -
 */
public class TestWcsDAO extends MetaBaseDAO{

    public List<Map<String, Object>> queryMetaTables(Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                "D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                //关联维度表，获取其维度信息
                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID " +
                //寻找有效版本
                ",(SELECT TABLE_ID,CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
                " VALID_VERSION  FROM " +
                "(SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  " +
                "VALID_VERSION FROM META_TABLES WHERE TABLE_STATE<>" + TblConstant.META_TABLE_STATE_MODIFY + "  GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)  GROUP BY TABLE_ID) E " +
                "WHERE A.TABLE_ID=E.TABLE_ID AND A.TABLE_VERSION=E.VALID_VERSION ");
        List params = new ArrayList();
        if (queryData != null) {
            if (queryData.get("DATASOURCE") != null && !queryData.get("DATASOURCE").toString().equals("")) {
                sql.append("AND A.DATA_SOURCE_ID = ? ");
                int dataSourceId = MapUtils.getInteger(queryData,"DATASOURCE");
                params.add(dataSourceId);
            }

            if (queryData.get("TABLEGROUP") != null && !queryData.get("TABLEGROUP").toString().equals("")) {
                //业务类型
                sql.append("AND A.TABLE_GROUP_ID = ? ");
                int tableGroupId = MapUtils.getInteger(queryData,"TABLEGROUP");
                params.add(tableGroupId);
            }

            if (queryData.get("OWNER") != null && !queryData.get("OWNER").toString().equals("")) {
                //用户
                String owner = MapUtils.getString(queryData,"OWNER");
                sql.append("AND A.TABLE_OWNER = ? ");
                params.add(owner);
            }

            if (queryData.get("TABLETYPE") != null && !queryData.get("TABLETYPE").toString().equals("")) {
                //层次分类
                sql.append("AND A.TABLE_TYPE_ID = ? ");
                int tableTypeId = MapUtils.getInteger(queryData,"TABLETYPE");
                params.add(tableTypeId);
            }

            //关键字处理，仅支持单个关键字
            if (queryData.get("KEYWORD") != null && !queryData.get("KEYWORD").toString().equals("")) {
                String key = queryData.get("KEYWORD").toString();
                if (!key.contains("%") && !key.contains("_")) {
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                } else {
                    key = key.replaceAll("_", "/_").replaceAll("%", "/%");
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?) ESCAPE '/' ) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                }
            }
        }
        String sort = MapUtils.getString(queryData,"COLUMN_SORT","");
        if(!"".equals(sort)){
            sql.append("ORDER BY "+sort);
        }else{
            sql.append("ORDER BY A.TABLE_ID DESC ");
        }

        String pageSql = sql.toString();
        //分页包装
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(pageSql, page);
        }

        List<Map<String, Object>> rs = getDataAccess().queryForList(pageSql, params.toArray());
        if (rs != null && rs.size() > 0) {
            for (Map<String, Object> map : rs) {
                map.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(map, "TABLE_TYPE_ID")));
            }
        }
        return rs;
    }

    public Object[][] queryMetaTableArray(Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                "D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                //关联维度表，获取其维度信息
                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID " +
                //寻找有效版本
                ",(SELECT TABLE_ID,CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
                " VALID_VERSION  FROM " +
                "(SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  " +
                "VALID_VERSION FROM META_TABLES WHERE TABLE_STATE<>" + TblConstant.META_TABLE_STATE_MODIFY + "  GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)  GROUP BY TABLE_ID) E " +
                "WHERE A.TABLE_ID=E.TABLE_ID AND A.TABLE_VERSION=E.VALID_VERSION ");
        List params = new ArrayList();
        if (queryData != null) {
            if (queryData.get("DATASOURCE") != null && !queryData.get("DATASOURCE").toString().equals("")) {
                sql.append("AND A.DATA_SOURCE_ID = ? ");
                int dataSourceId = MapUtils.getInteger(queryData,"DATASOURCE");
                params.add(dataSourceId);
            }

            if (queryData.get("TABLEGROUP") != null && !queryData.get("TABLEGROUP").toString().equals("")) {
                //业务类型
                sql.append("AND A.TABLE_GROUP_ID = ? ");
                int tableGroupId = MapUtils.getInteger(queryData,"TABLEGROUP");
                params.add(tableGroupId);
            }

            if (queryData.get("OWNER") != null && !queryData.get("OWNER").toString().equals("")) {
                //用户
                String owner = MapUtils.getString(queryData,"OWNER");
                sql.append("AND A.TABLE_OWNER = ? ");
                params.add(owner);
            }

            if (queryData.get("TABLETYPE") != null && !queryData.get("TABLETYPE").toString().equals("")) {
                //层次分类
                sql.append("AND A.TABLE_TYPE_ID = ? ");
                int tableTypeId = MapUtils.getInteger(queryData,"TABLETYPE");
                params.add(tableTypeId);
            }

            //关键字处理，仅支持单个关键字
            if (queryData.get("KEYWORD") != null && !queryData.get("KEYWORD").toString().equals("")) {
                String key = queryData.get("KEYWORD").toString();
                if (!key.contains("%") && !key.contains("_")) {
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                } else {
                    key = key.replaceAll("_", "/_").replaceAll("%", "/%");
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?) ESCAPE '/' ) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                }
            }
        }
        String sort = MapUtils.getString(queryData,"COLUMN_SORT","");
        if(!"".equals(sort)){
            sql.append("ORDER BY "+sort);
        }else{
            sql.append("ORDER BY A.TABLE_ID DESC ");
        }

        String pageSql = sql.toString();
        //分页包装
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(pageSql, page);
        }

        Object[][] rs = getDataAccess().queryForArray(pageSql,false,params.toArray());


        if (rs != null && rs.length > 0) {
            for(int i=0;i<rs.length;i++){
                Object[] _d = new Object[rs[i].length+1];
                int j=0;
                for(Object _o : rs[i]){
                    _d[j] = _o;
                    j++;
                }
                _d[j] = CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, rs[i][10].toString());
                rs[i] = _d;
            }
        }

        return rs;
    }
}
