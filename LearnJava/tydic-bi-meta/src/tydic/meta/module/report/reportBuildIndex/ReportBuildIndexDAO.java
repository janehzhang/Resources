package tydic.meta.module.report.reportBuildIndex;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 报表创建首页DAO
 * @date 2012-04-26
 */
public class ReportBuildIndexDAO extends MetaBaseDAO {

    /**
     * 查询推荐报表数据
     *
     * @param stationId 当前部门ID
     * @param page      分页对象
     * @return
     */
    public List<Map<String, Object>> queryRecommendData(String stationId, Page page) {

        //根据部门ID从操作日志表中获取订阅的报表ID
        int operateType = 21;
        
        String sql = "";
        List<Object> params = new ArrayList<Object>();
        if (stationId == null || stationId.equals("")) {
            sql = "SELECT REPORT_ID ,COUNT(REPORT_ID) AS COUNT FROM META_RPT_USE_LOG " +
                    "WHERE OPERATE_TYPE=? GROUP BY REPORT_ID ORDER BY COUNT";
            params.add(operateType);
        } else {
            sql = "SELECT REPORT_ID ,COUNT(REPORT_ID) AS COUNT FROM META_RPT_USE_LOG " +
                    "WHERE USE_STATION_ID=? AND OPERATE_TYPE=? GROUP BY REPORT_ID ORDER BY COUNT";
            params.add(Integer.parseInt(stationId));
            params.add(operateType);
        }

        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql.toString(), page);
        }
        List reportLists = getDataAccess().queryForList(sql, params.toArray());

        List<Map<String, Object>> resultLists = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < reportLists.size(); i++) {
            Map map = (Map) reportLists.get(i);
            int reportId = Integer.parseInt(map.get("REPORT_ID").toString());
            String tmpSql = "SELECT TABLE_ALIAS,ISSUE_NOTE,A.ISSUE_ID,to_char(ISSUE_TIME,'yyyy-MM-dd hh24:mi:ss') AS ISSUE_TIME " +
                    "FROM META_RPT_MODEL_ISSUE_CONFIG A  " +
                    "LEFT JOIN (SELECT ISSUE_ID, ISSUE_TIME FROM META_RPT_MODEL_ISSUE_LOG N WHERE N.ISSUE_OPERATE=11) B  " +
                    "ON B.ISSUE_ID=A.ISSUE_ID " +
                    "WHERE A.ISSUE_ID = " +
                    "(SELECT ISSUE_ID FROM META_RPT_TAB_REPORT_CFG WHERE REPORT_ID=" + reportId + ") AND ISSUE_STATE = 1";
            Map<String, Object> resultMap = getDataAccess().queryForMap(tmpSql);
            if (resultMap == null) {
                continue;
            }

            int issueId = Integer.parseInt(resultMap.get("ISSUE_ID").toString());
            String colSql = "SELECT COL_ALIAS FROM " +
                    "META_RPT_MODEL_ISSUE_COLS " +
                    "WHERE ISSUE_ID=" + issueId + " AND SELECTED_FLAG=1";
            List<Map<String, Object>> colLists = getDataAccess().queryForList(colSql);
            String colInfo = "";
            for (int j = 0, length = colLists.size(); j < length; j++) {
                if (j < length - 1) {
                    colInfo += colLists.get(j).get("COL_ALIAS").toString() + ",";
                } else {
                    colInfo += colLists.get(j).get("COL_ALIAS").toString();
                }
            }

            resultMap.put("COLS", colInfo);
            resultMap.put("COUNT", map.get("COUNT").toString());
            resultMap.put("RN_", map.get("RN_").toString());
            resultMap.put("TOTAL_COUNT_", map.get("TOTAL_COUNT_").toString());

            resultLists.add(resultMap);
        }
        return resultLists;
    }


    /**
     * 查询人气最旺报表数据
     *
     * @param userId 当前用户ID
     * @param page   分页对象
     * @return
     */
    public List<Map<String, Object>> queryPopularData(Page page) {

        //根据部门ID从操作日志表中获取订阅的报表ID
        int operateType = 21;
        String sql = "";
        sql = "SELECT REPORT_ID ,COUNT(REPORT_ID) AS COUNT FROM META_RPT_USE_LOG " +
                "WHERE OPERATE_TYPE=? GROUP BY REPORT_ID ORDER BY COUNT";
        List<Object> params = new ArrayList<Object>();
//		params.add(Integer.parseInt(stationId));
        params.add(operateType);
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        List reportLists = getDataAccess().queryForList(sql, params.toArray());

        List<Map<String, Object>> resultLists = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < reportLists.size(); i++) {
            Map map = (Map) reportLists.get(i);
            int reportId = Integer.parseInt(map.get("REPORT_ID").toString());
            String tmpSql = "SELECT TABLE_ALIAS,ISSUE_NOTE,ISSUE_ID " +
                    "FROM META_RPT_MODEL_ISSUE_CONFIG A " +
                    "WHERE A.ISSUE_ID = " +
                    "(SELECT ISSUE_ID FROM META_RPT_TAB_REPORT_CFG WHERE REPORT_ID=" + reportId + ") AND ISSUE_STATE = 1";
            Map<String, Object> resultMap = getDataAccess().queryForMap(tmpSql);
            if (resultMap == null) {
                continue;
            }
            int issueId = Integer.parseInt(resultMap.get("ISSUE_ID").toString());
            String colSql = "SELECT COL_ALIAS FROM " +
                    "META_RPT_MODEL_ISSUE_COLS " +
                    "WHERE ISSUE_ID=" + issueId + " AND SELECTED_FLAG=1";
            List<Map<String, Object>> colLists = getDataAccess().queryForList(colSql);
            String colInfo = "";
            for (int j = 0, length = colLists.size(); j < length; j++) {
                if (j < length - 1) {
                    colInfo += colLists.get(j).get("COL_ALIAS").toString() + ",";
                } else {
                    colInfo += colLists.get(j).get("COL_ALIAS").toString();
                }
            }

            resultMap.put("COLS", colInfo);
            resultMap.put("COUNT", map.get("COUNT").toString());
            resultMap.put("RN_", map.get("RN_").toString());
            resultMap.put("TOTAL_COUNT_", map.get("TOTAL_COUNT_").toString());
            resultLists.add(resultMap);
        }

        return resultLists;
    }

    /**
     * 查询最新报表数据
     *
     * @param page 分页对象
     * @return
     */
    public List<Map<String, Object>> queryNewestData(Page page) {

        String sql = "SELECT TABLE_ALIAS, ISSUE_NOTE, A.ISSUE_ID,to_char(B.ISSUE_TIME,'yyyy-MM-dd hh24:mi:ss') AS ISSUE_TIME " +
                "FROM META_RPT_MODEL_ISSUE_CONFIG A " +
                "LEFT JOIN (SELECT * FROM META_RPT_MODEL_ISSUE_LOG N WHERE N.ISSUE_OPERATE=11) B " +
                "ON A.ISSUE_ID = B.ISSUE_ID WHERE ISSUE_STATE = 1" +
                "ORDER BY ISSUE_TIME DESC";
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        List<Map<String, Object>> resultLists = getDataAccess().queryForList(sql);

        for (int i = 0; i < resultLists.size(); i++) {
            Map<String, Object> map = resultLists.get(i);
            if (resultLists.get(i) == null) {
                continue;
            }
            int issueId = Integer.parseInt(resultLists.get(i).get("ISSUE_ID").toString());
            String tmpSql = "SELECT COL_ALIAS FROM " +
                    "META_RPT_MODEL_ISSUE_COLS " +
                    "WHERE ISSUE_ID=" + issueId + " AND SELECTED_FLAG=1";

            List<Map<String, Object>> colLists = getDataAccess().queryForList(tmpSql);
            String colInfo = "";
            for (int j = 0, length = colLists.size(); j < length; j++) {
                if (j < length - 1) {
                    if (colLists.get(j).get("COL_ALIAS") != null && !colLists.get(j).get("COL_ALIAS").equals(""))
                        colInfo += colLists.get(j).get("COL_ALIAS").toString() + ",";
                } else {
                    if (colLists.get(j).get("COL_ALIAS") != null && !colLists.get(j).get("COL_ALIAS").equals(""))
                        colInfo += colLists.get(j).get("COL_ALIAS").toString();
                }
            }
            map.put("COLS", colInfo);
        }

        return resultLists;
    }


    /**
     * 根据用户输入的条件，查询报表模型数据
     *
     * @param page 分页对象
     * @return
     */
    public List<Map<String, Object>> queryRptModelDataByCondition(Page page, String conditon) {

        StringBuffer sqlBuffer = new StringBuffer("SELECT TABLE_ALIAS, ISSUE_NOTE, A.ISSUE_ID, B.ISSUE_TIME " +
                "FROM META_RPT_MODEL_ISSUE_CONFIG A " +
                "LEFT JOIN (SELECT * FROM META_RPT_MODEL_ISSUE_LOG N WHERE N.ISSUE_OPERATE=11) B " +
                "ON A.ISSUE_ID = B.ISSUE_ID  WHERE ISSUE_STATE = 1");
        List<Object> params = new ArrayList<Object>();
        if (conditon != null) {
            sqlBuffer.append(" AND (TABLE_KEYWORD LIKE ? ESCAPE '/' OR TABLE_ALIAS LIKE ? ESCAPE '/' OR ISSUE_NOTE LIKE ? ESCAPE '/' ");
            params.add(SqlUtils.allLikeBindParam(conditon.trim()));
            params.add(SqlUtils.allLikeBindParam(conditon.trim()));
            params.add(SqlUtils.allLikeBindParam(conditon.trim()));
        }
        sqlBuffer.append(" ) ORDER BY ISSUE_TIME DESC");

        String sql = sqlBuffer.toString();
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        List<Map<String, Object>> resultLists = getDataAccess().queryForList(sql, params.toArray());

        for (int i = 0; i < resultLists.size(); i++) {
            Map<String, Object> map = resultLists.get(i);
            int issueId = Integer.parseInt(resultLists.get(i).get("ISSUE_ID").toString());
            String tmpSql = "SELECT COL_ALIAS FROM " +
                    "META_RPT_MODEL_ISSUE_COLS " +
                    "WHERE ISSUE_ID=" + issueId + " AND SELECTED_FLAG=1";
            List<Map<String, Object>> colLists = getDataAccess().queryForList(tmpSql);
            String colInfo = "";
            for (int j = 0, length = colLists.size(); j < length; j++) {
                if (j < length - 1) {
                    if (colLists.get(j).get("COL_ALIAS") != null && !colLists.get(j).get("COL_ALIAS").equals(""))
                        colInfo += colLists.get(j).get("COL_ALIAS").toString() + ",";
                } else {
                    if (colLists.get(j).get("COL_ALIAS") != null && !colLists.get(j).get("COL_ALIAS").equals(""))
                        colInfo += colLists.get(j).get("COL_ALIAS").toString();
                }
            }
            map.put("COLS", colInfo);
        }

        return resultLists;
    }

    /**
     * 根据发布模型ID，查询出字段分类树，包含字段信息
     *
     * @param issueId 发布模型ID
     * @return
     */
    public List<Map<String, Object>> queryFields(String issueId) {
        String sql1 = "select TABLE_ALIAS from meta_rpt_model_issue_config where issue_id=" + issueId;
        String tableAlias = getDataAccess().queryForString(sql1);

        String sql = "SELECT COL_TYPE_ID || '_field' AS TREE_ID, COL_TYPE_ID, " +
                "COL_TYPE_NAME AS TREE_NAME, " +
                " PARENT_ID || '_field' AS PARENT_ID, " +
                " COL_TYPE_ORDER " +
                " FROM META_RPT_MODEL_COL_BUS_TYPE";
        List<Map<String, Object>> resultLists = getDataAccess().queryForList(sql);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("TREE_ID", "0_field");
        root.put("COL_TYPE_ID", "0");
        root.put("TREE_NAME", tableAlias);
        root.put("PARENT_ID", "0");
        root.put("COL_TYPE_ORDER", "0");

        resultLists.add(root);
        int size = resultLists.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = resultLists.get(i);
            int colTypeId = Convert.toInt(resultLists.get(i).get("COL_TYPE_ID").toString(), 0);
            String parentId = resultLists.get(i).get("TREE_ID").toString();

            String tmpSql = "SELECT COLUMN_ID || '_col' AS TREE_ID, " +
                    " COLUMN_ID, " +
                    " COL_ID, " +
                    " COL_ALIAS AS TREE_NAME, " +
                    " COL_BUS_TYPE, " +
                    " DIM_LEVELS, " +
                    " DIM_CODES, " +
                    " COL_TYPE_ID, " +
                    " AMOUNT_FLAG, " +
                    " DIM_TYPE, " +
                    " ISSUE_ID " +
                    " FROM META_RPT_MODEL_ISSUE_COLS " +
                    " WHERE COL_TYPE_ID = ? " +
                    "  AND ISSUE_ID = ? " +
                    " AND SELECTED_FLAG = 1";
            List<Object> params = new ArrayList<Object>();
            params.add(colTypeId);
            params.add(Integer.parseInt(issueId));
            List<Map<String, Object>> colLists = getDataAccess().queryForList(tmpSql, params.toArray());
            for (int j = 0; j < colLists.size(); j++) {
                colLists.get(j).put("PARENT_ID", parentId);
                resultLists.add(colLists.get(j));
            }
        }
        return resultLists;
    }

    /**
     * 根据字段ID，查询字段关联的维度名
     * @param colId
     * @return
     */
    public List<Map<String,Object>> queryDimName(String colId) {
        String sql = "SELECT TABLE_NAME, "+
                " TABLE_DIM_PREFIX　"+
                " FROM META_DIM_TABLES "+
                " WHERE DIM_TABLE_ID = "+
                " (SELECT DIM_TABLE_ID "+
                "  FROM META_TABLE_COLS "+
                " WHERE COL_ID = "+colId+" AND COL_STATE = 1) ";
        Map<String,Object> map = getDataAccess().queryForMap(sql);
        String tableName = map.get("TABLE_NAME").toString();
        String prefix = map.get("TABLE_DIM_PREFIX").toString();

        String tmpSql = "SELECT "+prefix+"_NAME AS DIM_NAME FROM "+tableName+" ";

        return getDataAccess().queryForList(tmpSql);
    }

    /**
     * 根据字段ID，查询字段关联的层级名
     * @param colId
     * @return
     */
    public List<Map<String,Object>> queryLevelName(String columnId,String colId) {
        String sql = "select DIM_LEVELS "+
                "from meta_rpt_model_issue_cols "+
                "WHERE COLUMN_ID = ? "+
                " AND COL_ID = ?";
        List<Object> params = new ArrayList<Object>();
        String levels = getDataAccess().queryForString(sql,Convert.toInt(columnId),Convert.toInt(colId));

        String arr[]  = levels.split(",");
        String a1 = "(";
        for(int i=0;i<arr.length;i++) {
            int dimLevel = Convert.toInt(arr[i]);
            if(i == arr.length-1) {
                a1 += dimLevel;
            }else {
                a1 += dimLevel+",";
            }

        }
        a1 += ")";
        String sql2 = "SELECT DIM_LEVEL_NAME,dim_level "+
                " FROM META_DIM_LEVEL "+
                " WHERE DIM_TABLE_ID = (SELECT DIM_TABLE_ID "+
                " FROM META_TABLE_COLS "+
                " WHERE COL_ID = "+colId+" "+
                "  AND COL_STATE = 1) "+
                "and dim_level not in "+a1+" " +
                "and dim_type_id=( " +
                "SELECT dim_type_id FROM META_TABLE_COLS WHERE COL_ID ="+colId+" AND COL_STATE = 1) order by dim_level";
        List<Map<String,Object>> list2 =  getDataAccess().queryForList(sql2);
        for(int i=0;i<list2.size();i++){
            list2.get(i).put("dimLevels", a1);
        }

        String sql1 = "SELECT DIM_LEVEL_NAME,dim_level "+
                " FROM META_DIM_LEVEL "+
                " WHERE DIM_TABLE_ID = (SELECT DIM_TABLE_ID "+
                " FROM META_TABLE_COLS "+
                " WHERE COL_ID = "+colId+" "+
                "  AND COL_STATE = 1) "+
                "and dim_level in "+a1+" " +
                "and dim_type_id=( " +
                "SELECT dim_type_id FROM META_TABLE_COLS WHERE COL_ID ="+colId+" AND COL_STATE = 1) order by dim_level";
        List<Map<String,Object>> list =  getDataAccess().queryForList(sql1);
        for(int i=0;i<list.size();i++){
            list.get(i).put("dimLevels", a1);
            list.get(i).put("EXIST", "true");
        }

        list2.addAll(list);
        return list2;
    }

    /**
     * 查询当前层级下的低层级
     * @param level
     * @param colId
     * @return
     */
    public List<Map<String,Object>> queryLowLevelNameByLevel(String level,String colId) {
        String sql = "SELECT DIM_LEVEL_NAME,dim_level ,dim_level-1 as PARENT_ID"+
                " FROM META_DIM_LEVEL "+
                " WHERE DIM_TABLE_ID = (SELECT DIM_TABLE_ID "+
                " FROM META_TABLE_COLS "+
                " WHERE COL_ID = ? "+
                "  AND COL_STATE = 1) "+
                " and dim_type_id=( " +
                "SELECT dim_type_id FROM META_TABLE_COLS WHERE COL_ID =? AND COL_STATE = 1) ";

        if(level != null) {
            sql += "and dim_level in "+level;
        }
        sql +=" order by dim_level";
        List<Map<String,Object>> list =  getDataAccess().queryForList(sql,colId,colId);
        return list;
    }


    /**
     * 查询当前编码下的直接子编码
     * @param level
     * @param colId
     * @return
     */
    public List<Map<String,Object>> queryLowCodesByCode(String codeId,String colId) {

        String sql = "select table_name, table_dim_prefix "+
                " from meta_dim_tables "+
                " where dim_table_id = (select dim_table_id "+
                "from META_TABLE_COLS "+
                "where col_id = ? "+
                " and col_state = 1)";
        Map map = getDataAccess().queryForMap(sql, Convert.toInt(colId));

        String tableName = map.get("TABLE_NAME").toString();
        String pre = map.get("TABLE_DIM_PREFIX").toString();


        String sql1 = "select "+pre+"_Id AS TREE_ID,"+pre+"_PAR_ID AS PARENT_ID,"+pre+"_NAME AS NAME , " +
                "DIM_LEVEL FROM "+tableName+" WHERE state=1 and "+pre+"_par_id = "+codeId+"  ";

        List<Map<String,Object>> list =  getDataAccess().queryForList(sql1);
        return list;
    }

    /**
     * 查询编码
     * @param columnId
     * @param colId
     * @return
     */
    public List<Map<String,Object>> queryCodes(String dimLevel,String colId) {
        String sql = "select table_name, table_dim_prefix "+
                " from meta_dim_tables "+
                " where dim_table_id = (select dim_table_id "+
                "from META_TABLE_COLS "+
                "where col_id = ? "+
                " and col_state = 1)";
        Map map = getDataAccess().queryForMap(sql, Convert.toInt(colId));

        String tableName = map.get("TABLE_NAME").toString();
        String pre = map.get("TABLE_DIM_PREFIX").toString();
        String sql1 = "select "+pre+"_Id AS TREE_ID,"+pre+"_PAR_ID AS PARENT_ID,"+pre+"_NAME AS NAME ,DIM_LEVEL FROM "+tableName+" WHERE state=1 ";

        if(	dimLevel != null) {
            sql1 += " AND DIM_LEVEL IN "+dimLevel;
        }
        List<Map<String,Object>> list =  getDataAccess().queryForList(sql1);
        for(int i=0;i<list.size();i++) {
            list.get(i).put("EXIST", "true");
        }
        String sql2 = "";
        if(dimLevel != null) {
            sql2 = "select "+pre+"_Id AS TREE_ID,"+pre+"_PAR_ID AS PARENT_ID,"+pre+"_NAME AS NAME,DIM_LEVEL " +
                    " FROM "+tableName+" WHERE state=1  AND DIM_LEVEL NOT IN "+dimLevel;
        }
        List<Map<String,Object>> list1 =  getDataAccess().queryForList(sql2);
        list.addAll(list1);
        return list;
    }


    /**
     * 查询实体表数据
     * @return
     */
    public List<Map<String,Object>> queryInstTable(Map<?,?> data) {
        String colId = "("+data.get("colId").toString()+")";

        ArrayList outAreaAlreadyDragInArray = (ArrayList)data.get("OutAreaAlreadyDragInArray");

        String sql = "SELECT COL_NAME,COL_ID FROM META_TABLE_COLS WHERE COL_ID IN "+colId+" AND COL_STATE=1";

        List<Map<String,Object>> list =  getDataAccess().queryForList(sql);
        String[] a = data.get("colId").toString().split(",");
        String colInfo = "";
        ArrayList groupByClause = new ArrayList();
        for(int j=0;j<a.length;j++) {
            for(int i=0;i<list.size();i++) {
                if(list.get(i).get("COL_ID").toString().equals(a[j])) {
                    String tag = null;
                    Map<String,Object> map = (Map<String, Object>) outAreaAlreadyDragInArray.get(j);
                    if(map.get("colId").toString().equals(list.get(i).get("COL_ID").toString()) && Integer.parseInt((map.get("type")).toString()) == 1 ) {
                        if(!map.get("tag").toString().equals("direct")){
                            tag = map.get("tag").toString();
                        }
                    }
                    if(j == a.length-1) {
                        if(tag != null ) {
                            colInfo += tag+"("+list.get(i).get("COL_NAME")+")";
                        }else {
                            colInfo += list.get(i).get("COL_NAME");
                            groupByClause.add(list.get(i).get("COL_NAME"));
                        }
                    }else {
                        if(tag != null ) {
                            colInfo += tag+"("+list.get(i).get("COL_NAME")+"),";
                        }else {
                            colInfo += list.get(i).get("COL_NAME")+",";
                            groupByClause.add(list.get(i).get("COL_NAME"));
                        }
                    }
                }
            }
        }

        String issueId = data.get("issueId").toString();
        String sql1 = "SELECT TABLE_NAME FROM META_TABLE_INST " +
                "WHERE TABLE_ID=" +
                "(SELECT TABLE_ID FROM META_RPT_MODEL_ISSUE_CONFIG WHERE ISSUE_ID= "+issueId+")";
        String tableName = getDataAccess().queryForString(sql1);
        String sql2 = "SELECT "+colInfo+"  FROM "+tableName+" WHERE ROWNUM < 100 ";
        if(groupByClause.size() > 0) {
            String tmpGroupByClause = "";
            for(int i=0;i<groupByClause.size();i++) {
                if(i== groupByClause.size()-1) {
                    tmpGroupByClause += groupByClause.get(i).toString();
                }else {
                    tmpGroupByClause += groupByClause.get(i).toString()+",";
                }
            }
            sql2 += "GROUP BY "+tmpGroupByClause;
        }
        List<Map<String,Object>> result  = getDataAccess().queryForList(sql2);
        for(int i=0;i<result.size();i++) {
            result.get(i).put("colInfo", colInfo);
        }

        return result;
    }

    public String querySumValue(String curTd,String issueId,String colInfo) {
        String sql1 = "SELECT TABLE_NAME FROM META_TABLE_INST " +
                "WHERE TABLE_ID=" +
                "(SELECT TABLE_ID FROM META_RPT_MODEL_ISSUE_CONFIG WHERE ISSUE_ID= "+issueId+")";
        String tableName = getDataAccess().queryForString(sql1);
        String sql2 = "SELECT SUM("+colInfo+")  FROM "+tableName;
        String result  = getDataAccess().queryForString(sql2);
        result += ","+curTd;
        return result;
    }



    public List<Map<String,Object>> getCodeByColumnId(String transColumnId,String dimLevels,String dimCodeFilters){

        String sql = "SELECT TABLE_NAME,TABLE_DIM_PREFIX FROM META_DIM_TABLES " +
                " WHERE DIM_TABLE_ID = (SELECT DIM_TABLE_ID FROM META_RPT_MODEL_ISSUE_COLS WHERE COLUMN_ID=?";
        Map<String,Object> map =  getDataAccess().queryForMap(sql.toString(), new Object[]{transColumnId});

        String tableName = (String) map.get("TABLE_NAME");
        String colName = map.get("TABLE_DIM_PREFIX").toString() + "_NAME";
        String code = map.get("TABLE_DIM_PREFIX").toString() + "_CODE";

        String sql1 = "SELECT "+colName+" AS NAME FROM "+tableName+" WHERE DIM_LEVEL IN "+dimLevels+" AND "+code+" NOT IN "+dimCodeFilters+" ";

        return getDataAccess().queryForList(sql1);
    }

}
