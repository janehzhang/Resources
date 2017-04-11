package tydic.meta.module.tbl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-26
 * @description META_TABLE_COLS操作DAO
 */
public class MetaTableColsDAO extends MetaBaseDAO {

    /**
     * 差异类型常量定义：
     * 差异类型包括1：字段新增，2 字段删除，3字段类型不同，4字段可为空差异，5主键差异，6默认值差异，7维表关联差异,8注释不同
     */
    public final static int DIFF_TYPE_ADD_COLUMN = 1;
    public final static int DIFF_TYPE_DELETE_COLUMN = 2;
    public final static int DIFF_TYPE_DATATYPE = 3;
    public final static int DIFF_TYPE_NULLABLE = 4;
    public final static int DIFF_TYPE_PRIMARYKEY = 5;
    public final static int DIFF_TYPE_DEFAULTVAL = 6;
    public final static int DIFF_TYPE_TIM = 7;
    public final static int DIFF_TYPE_REMARK = 8;


    /**
     * @param data
     * @return
     * @description 向META_TABLE_COLS插入单条记录
     */
    public long insertMetaTableCols(Map<String, Object> data) throws Exception {
        long columnId = 0;
        if (data.get("colId") == null || data.get("colId").equals("")) {
            columnId = queryForNextVal("SEQ_TAB_COL_ID");
        } else {
            columnId = Long.parseLong(data.get("colId").toString());
        }
        String sql = "INSERT INTO META_TABLE_COLS( "
                + "COL_ID,TABLE_ID,COL_NAME,COL_NAME_CN,COL_DATATYPE,COL_SIZE,"
                + "COL_PREC,COL_BUS_COMMENT,COL_ORDER,COL_NULLABLED,COL_STATE,"
                + "DEFAULT_VAL,DIM_TABLE_ID,COL_BUS_TYPE,DIM_LEVEL,DIM_COL_ID,"
                + "IS_PRIMARY,DIM_TYPE_ID,TABLE_VERSION ) "
                + "VALUES(" + columnId + ",?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,   ?,?,?)";
        Object[] params = new Object[18];

        // 参数加入
        Object tableId = data.get("tableId");
        if (tableId == null) {
            //非空约束
            return -1;
        } else {
            params[0] = tableId;
        }

        String colName = Convert.toString(data.get("colName")).toUpperCase();
        params[1] = colName;
        String colNameCn = Convert.toString(data.get("colNameCn"));
        params[2] = colNameCn;
        String colDatatype = Convert.toString(data.get("colDatatype"));
        params[3] = colDatatype;
        params[4] = data.get("colSize") == null || data.get("colSize").equals("") ? 0 : data.get("colSize");
        params[5] = data.get("colPrec") == null || data.get("colPrec").equals("") ? 0 : data.get("colPrec");
        params[6] = data.get("colBusComment");
        params[7] = data.get("colOrder");
        params[8] = data.get("colNullabled") == null || data.get("colNullabled").equals("") ? 0 : data.get("colNullabled");
        params[9] = TblConstant.COL_STATE_VALID;
        params[10] = data.get("defaultVal");
        params[11] = data.get("dimTableId");
        params[12] = data.get("colBusType");
        params[13] = data.get("dimLevel");
        params[14] = data.get("dimColId");
        params[15] = data.get("isPrimary") == null || data.get("isPrimary").equals("") ? 0 : data.get("isPrimary");
        params[16] = data.get("dimTypeId");
        params[17] = data.get("tableVersion");
        getDataAccess().execUpdate(sql, params);
        return columnId;
    }

    public int[] insertBatch(final List<Map<String, Object>> datas) {
        String sql = "INSERT INTO META_TABLE_COLS( "
                + "COL_ID,TABLE_ID,COL_NAME,COL_NAME_CN,COL_DATATYPE,COL_SIZE,"
                + "COL_PREC,COL_BUS_COMMENT,COL_ORDER,COL_NULLABLED,COL_STATE,"
                + "DEFAULT_VAL,DIM_TABLE_ID,COL_BUS_TYPE,DIM_LEVEL,DIM_COL_ID,"
                + "IS_PRIMARY,DIM_TYPE_ID,TABLE_VERSION ) "
                + "VALUES(?,?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,   ?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Map<String, Object> data = datas.get(i);
                if (data.get("colId") != null && !data.get("colId").equals("")) {
                    preparedStatement.setObject(1, data.get("colId"));
                } else {
                    preparedStatement.setObject(1, queryForNextVal("SEQ_TAB_COL_ID"));
                }
                preparedStatement.setObject(2, data.get("tableId"));
                String colName = Convert.toString(data.get("colName")).toUpperCase();
                preparedStatement.setObject(3, colName);
                String colNameCn = Convert.toString(data.get("colNameCn"));
                preparedStatement.setObject(4, colNameCn);
                String colDatatype = Convert.toString(data.get("colDatatype"));
                preparedStatement.setObject(5, colDatatype);
                preparedStatement.setObject(6, data.get("colSize") == null || data.get("colSize").equals("") ? 0 : data.get("colSize"));
                preparedStatement.setObject(7, data.get("colPrec") == null || data.get("colPrec").equals("") ? 0 : data.get("colPrec"));
                preparedStatement.setObject(8, data.get("colBusComment"));
                preparedStatement.setObject(9, data.get("colOrder"));
                preparedStatement.setObject(10, data.get("colNullabled") == null || data.get("colNullabled").equals("") ? 0 : data.get("colNullabled"));
                preparedStatement.setObject(11, TblConstant.COL_STATE_VALID);
                preparedStatement.setObject(12, data.get("defaultVal"));
                preparedStatement.setObject(13, data.get("dimTableId"));
                preparedStatement.setObject(14, data.get("colBusType"));
                preparedStatement.setObject(15, MapUtils.getString(data, "dimLevel").equals("") ? 1 : data.get("dimLevel"));
                preparedStatement.setObject(16, data.get("dimColId"));
                preparedStatement.setObject(17, data.get("isPrimary") == null || data.get("isPrimary").equals("") ? 0 : data.get("isPrimary"));
                preparedStatement.setObject(18, data.get("dimTypeId"));
                preparedStatement.setObject(19, data.get("tableVersion"));
            }

            public int batchSize() {
                return datas.size();
            }
        });
    }

//    /**
//     * 当修改维表时，修改关联的维表列的ID，使其更新到最新的维度列。
//     * @param colIdsMapping
//     * @throws Exception
//     */
//    public void updateDimColId(Map<Long,Long> colIdsMapping) throws Exception{
//        String sql="UPDATE META_TABLE_COLS SET DIM_COL_ID=? WHERE DIM_COL_ID=? AND COL_BUS_TYPE=0";
//        ProParams[] params=new ProParams[colIdsMapping.size()];
//        int count=0;
//        for(Map.Entry<Long,Long> cols:colIdsMapping.entrySet()){
//            ProParams param=new ProParams();
//            param.addValue(cols.getValue());
//            param.addValue(cols.getKey());
//            params[count++]=param;
//        }
//        getDataAccess().execUpdateBatch(sql,params);
//    }

    /**
     * 查询当前维度表的的前缀
     */
    public String queryDimPrefixByTableId(int tableId) {
        String sql = "SELECT T.TABLE_DIM_PREFIX FROM META_DIM_TABLES T WHERE T.DIM_TABLE_ID =?";
        return this.getDataAccess().queryForString(sql, tableId);
    }

    /**
     * 根据tableId查询特定列信息，其中的列以_CODE或_ID结尾，大小写不敏感
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryIdNameByTableId(int tableId, int tableVersion) {
        String tablePreFix = this.queryDimPrefixByTableId(tableId);
        String sql = "SELECT TABLE_ID,COL_NAME,COL_ID FROM META_TABLE_COLS "
                + "WHERE TABLE_ID = ? AND TABLE_VERSION=? AND COL_STATE = ? "
                + "AND (UPPER(COL_NAME) LIKE '" + tablePreFix.toUpperCase() + "_CODE' OR UPPER(COL_NAME) LIKE '" + tablePreFix.toUpperCase() + "_ID')";
        List<Map<String, Object>> list = getDataAccess().queryForList(sql, tableId,
                tableVersion, TblConstant.META_TABLE_COLS_STATE_COMMON);
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        dataList.add(list.get(1));
        dataList.add(list.get(0));
        return dataList;
    }


    /**
     * @param proto 要匹配的字符串
     * @return
     * @description 匹配已有的相似字段 ,从META_TABLE_COLS中读取列信息自动填写类型字段，
     * 匹配方式：前缀相同
     */
    public List<Map<String, Object>> matchCols(String proto) {
        String sql = "SELECT  C.COL_ID, C.COL_NAME,C.COL_NAME_CN,C.COL_SIZE,  C.COL_PREC," +
                " C.COL_ORDER,C.COL_NULLABLED,C.COL_STATE,C.DEFAULT_VAL,C.COL_BUS_TYPE,C.IS_PRIMARY," +
                "(CASE WHEN COL_SIZE > 0 AND COL_PREC > 0 THEN C.COL_DATATYPE || '(' || COL_SIZE || ',' || COL_PREC || ')'WHEN " +
                " COL_SIZE > 0 THEN C.COL_DATATYPE || '(' || COL_SIZE || ')' ELSE C.COL_DATATYPE END) DATATYPE," +
                " C.COL_BUS_COMMENT FROM (SELECT a.*,row_number() over(partition by A.COL_NAME,A.COL_DATATYPE,A.COL_PREC order by 1 asc) C FROM META_TABLE_COLS A) C WHERE  C=1 AND UPPER(C.COL_NAME) LIKE UPPER(?)";
        return getDataAccess().queryForList(sql, proto + "%");
    }

    /**
     * 根据tableId查询字段信息
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryMetaTableColsByTableId(int tableId, int tableVersion, Page page) {
        StringBuffer sql = new StringBuffer();
        sql = sql.append("SELECT DISTINCT C.COL_ID, ");
        sql.append("C.TABLE_ID, ");
        sql.append("C.COL_NAME, ");
        sql.append("(CASE ");
        sql.append("WHEN C.COL_DATATYPE = 'DATE' THEN ");
        sql.append("'DATE' ");
        sql.append("WHEN C.COL_SIZE > 0 AND C.COL_PREC > 0 THEN ");
        sql.append("C.COL_DATATYPE || '(' || C.COL_SIZE || ',' || C.COL_PREC || ')' ");
        sql.append("WHEN C.COL_SIZE > 0 THEN ");
        sql.append("C.COL_DATATYPE || '(' || C.COL_SIZE || ')' ");
        sql.append("ELSE ");
        sql.append("C.COL_DATATYPE ");
        sql.append("END) COL_DATATYPE, ");
        sql.append("C.COL_NAME_CN, ");
        sql.append("C.COL_SIZE, ");
        sql.append("C.COL_PREC, ");
        sql.append("C.COL_DATATYPE DATA_TYPE, ");
        sql.append("C.COL_ORDER, ");
        sql.append("C.COL_NULLABLED, ");
        sql.append("C.COL_STATE, ");
        sql.append("C.DEFAULT_VAL, ");
        sql.append("C.COL_BUS_TYPE, ");
        //sql.append("C.DIM_LEVEL, ");
        sql.append("C.DIM_COL_ID, ");
        sql.append("C.IS_PRIMARY, ");
        sql.append("C.DIM_TYPE_ID, ");
        sql.append("C.TABLE_VERSION, ");
        sql.append("C.DIM_TABLE_ID, ");
        sql.append("C.COL_BUS_COMMENT, ");
        sql.append("D.TABLE_NAME DIM_TABLE_NAME, ");
        sql.append("E.COL_NAME DIM_COL_NAME, ");
        sql.append("F.DIM_TYPE_NAME, ");
        sql.append("dimLevel.Dim_Level, ");
        sql.append("(case when dimLevel.Dim_Level_Name is not null then  C.DIM_LEVEL || '--' || dimLevel.Dim_Level_Name else '' end) DIM_LEVEL_NAME ");

        sql.append("FROM META_TABLE_COLS C ");
        sql.append("LEFT JOIN meta_tables t ");
        sql.append("ON t.table_id = c.table_id ");
        sql.append("AND t.table_state = 1 ");
        sql.append("LEFT JOIN meta_tables D ");
        sql.append("ON c.dim_table_id = D.table_id ");
        sql.append("AND D.table_state = 1 ");
        sql.append("LEFT JOIN meta_table_cols E ");
        sql.append("ON c.dim_col_id = E.Col_Id ");
        sql.append("AND E.Col_State = 1 ");
        sql.append("left join META_DIM_TYPE F ");
        sql.append("on C.DIM_TYPE_ID = F.DIM_TYPE_ID and c.dim_table_id = f.dim_table_id ");
        sql.append("LEFT JOIN META_DIM_LEVEL dimLevel ");
        sql.append("ON C.DIM_LEVEL = dimLevel.Dim_Level and c.dim_table_id = dimLevel.Dim_Table_Id and c.dim_type_id = dimlevel.dim_type_id ");

        sql.append("WHERE 1 = 1 ");
        sql.append("AND C.TABLE_ID = ? ");
        sql.append("AND C.TABLE_VERSION = ? ");
        sql.append("ORDER BY C.COL_ORDER ASC");

        //分页包装
        String pageSql = sql.toString();
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(sql.toString(), page);
        }
        return getDataAccess().queryForList(pageSql, tableId, tableVersion);
    }

    /**
     * 根据表类ID查询维度表动态字段
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryDimTableDycCols(int tableId) {
        StringBuffer sql = new StringBuffer();
        sql = sql.append("SELECT DISTINCT C.COL_ID, ");
        sql.append("C.TABLE_ID, ");
        sql.append("C.COL_NAME, ");
        sql.append("(CASE ");
        sql.append("WHEN C.COL_DATATYPE = 'DATE' THEN ");
        sql.append("'DATE' ");
        sql.append("WHEN C.COL_SIZE > 0 AND C.COL_PREC > 0 THEN ");
        sql.append("C.COL_DATATYPE || '(' || C.COL_SIZE || ',' || C.COL_PREC || ')' ");
        sql.append("WHEN C.COL_SIZE > 0 THEN ");
        sql.append("C.COL_DATATYPE || '(' || C.COL_SIZE || ')' ");
        sql.append("ELSE ");
        sql.append("C.COL_DATATYPE ");
        sql.append("END) COL_DATATYPE, ");
        sql.append("C.COL_NAME_CN, ");
        sql.append("C.COL_SIZE, ");
        sql.append("C.COL_PREC, ");
        sql.append("C.COL_DATATYPE DATA_TYPE, ");
        sql.append("C.COL_ORDER, ");
        sql.append("C.COL_NULLABLED, ");
        sql.append("C.COL_STATE, ");
        sql.append("C.DEFAULT_VAL, ");
        sql.append("C.COL_BUS_TYPE, ");
        //sql.append("C.DIM_LEVEL, ");
        sql.append("C.DIM_COL_ID, ");
        sql.append("C.IS_PRIMARY, ");
        sql.append("C.DIM_TYPE_ID, ");
        sql.append("C.TABLE_VERSION, ");
        sql.append("C.DIM_TABLE_ID, ");
        sql.append("C.COL_BUS_COMMENT, ");
        sql.append("D.TABLE_NAME DIM_TABLE_NAME, ");
        sql.append("E.COL_NAME DIM_COL_NAME, ");
        sql.append("F.DIM_TYPE_NAME, ");
        sql.append("dimLevel.Dim_Level, ");
        sql.append("(case when dimLevel.Dim_Level_Name is not null then  C.DIM_LEVEL || '--' || dimLevel.Dim_Level_Name else '' end) DIM_LEVEL_NAME ");

        sql.append("FROM META_TABLE_COLS C ");
        sql.append("LEFT JOIN meta_tables t ");
        sql.append("ON t.table_id = c.table_id ");
        sql.append("AND t.table_state = 1 ");
        sql.append("LEFT JOIN meta_tables D ");
        sql.append("ON c.dim_table_id = D.table_id ");
        sql.append("AND D.table_state = 1 ");
        sql.append("LEFT JOIN meta_table_cols E ");
        sql.append("ON c.dim_col_id = E.Col_Id ");
        sql.append("AND E.Col_State = 1 ");
        sql.append("left join META_DIM_TYPE F ");
        sql.append("on C.DIM_TYPE_ID = F.DIM_TYPE_ID and c.dim_table_id = f.dim_table_id ");
        sql.append("LEFT JOIN META_DIM_LEVEL dimLevel ");
        sql.append("ON C.DIM_LEVEL = dimLevel.Dim_Level and c.dim_table_id = dimLevel.Dim_Table_Id and c.dim_type_id = dimlevel.dim_type_id ");

        sql.append("WHERE 1 = 1 ");
        sql.append("AND C.TABLE_ID = ? ");
        sql.append("AND C.COL_STATE =  " + TblConstant.COL_STATE_VALID + " ");
        sql.append("ORDER BY C.COL_ORDER ASC");
        String pageSql = sql.toString();
        List<Map<String, Object>> codeList = getDataAccess().queryForList(pageSql, tableId);
        List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < codeList.size(); i++) {
            if (i > 11) { //过滤掉前面12个字段列
                Map<String, Object> map = codeList.get(i);
                outList.add(map);
            }
        }
        return outList;
    }

    /**
     * 当前版本的表类和前一个版本的差异比较
     *
     * @param curTables 当前表类
     * @param preTables 前版本表类
     * @return
     */
    public static List<Map<String, Object>> diffCompare(List<Map<String, Object>> curTables,
                                                        List<Map<String, Object>> preTables) {
        //当前表类Map集合
        Map<String, Map<String, Object>> curTablesMap = new HashMap<String, Map<String, Object>>();

        for (Map<String, Object> column : curTables) {
            curTablesMap.put(Convert.toString(column.get("COL_NAME")).toUpperCase(), column);
        }

        //前版本表类Map集合
        Map<String, Map<String, Object>> preTablesMap = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> column : preTables) {
            preTablesMap.put(Convert.toString(column.get("COL_NAME")).toUpperCase(), column);
        }

        //构造返回结果。
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        //差异数
        int diffConunt = 0;

        //遍历当前表类集合，取出数据到结果集.并于前版本表类做比较
        for (Map.Entry<String, Map<String, Object>> entry : curTablesMap.entrySet()) {
            Map<String, Object> itemResult = new HashMap<String, Object>();
            String columnName = entry.getKey();
            Map<String, Object> configColumn = entry.getValue();
            int configColSize = configColumn.get("COL_SIZE") == null || configColumn.get("COL_SIZE").equals("") ? 0 : Integer.parseInt(configColumn.get("COL_SIZE").toString());
            int configColPrec = configColumn.get("COL_PREC") == null || configColumn.get("COL_PREC").equals("") ? 0 : Integer.parseInt(configColumn.get("COL_PREC").toString());
            itemResult.put("COL_NAME", configColumn.get("COL_NAME"));
            itemResult.put("COL_NULLABLED", configColumn.get("COL_NULLABLED"));
            itemResult.put("DEFAULT_VAL", configColumn.get("DEFAULT_VAL"));
            itemResult.put("IS_PRIMARY", configColumn.get("IS_PRIMARY"));
            itemResult.put("COL_BUS_COMMENT", configColumn.get("COL_BUS_COMMENT"));
            itemResult.put("THIS_DIM", configColumn.get("thisDim"));
            itemResult.put("COL_DATA_TYPE", TblCommon.buildDataType(Convert.toString(configColumn.get("DATA_TYPE")),
                    configColSize, configColPrec));


            //如果前版本表类中存在当前字段名，则做具体的字段比较，否则判断为新增字段
            if (preTablesMap.containsKey(columnName)) {
                Map<String, Object> instColumn = preTablesMap.get(columnName);
                int instColSize = instColumn.get("COL_SIZE") == null || instColumn.get("COL_SIZE").equals("") ? 0 : Integer.parseInt(instColumn.get("COL_SIZE").toString());
                int instColPrec = instColumn.get("COL_PREC") == null || instColumn.get("COL_PREC").equals("") ? 0 : Integer.parseInt(instColumn.get("COL_PREC").toString());
                itemResult.put("PRE_COL_NAME", instColumn.get("COL_NAME"));
                itemResult.put("PRE_COL_DATA_TYPE", instColumn.get("DATA_TYPE"));
                itemResult.put("PRE_COL_NULLABLED", instColumn.get("COL_NULLABLED"));
                itemResult.put("PRE_DEFAULT_VAL", instColumn.get("DEFAULT_VAL"));
                itemResult.put("PRE_IS_PRIMARY", instColumn.get("IS_PRIMARY"));
                itemResult.put("PRE_COL_BUS_COMMENT", instColumn.get("COL_BUS_COMMENT"));
                itemResult.put("PRE_THIS_DIM", instColumn.get("thisDim"));
                itemResult.put("PRE_COL_DATA_TYPE", TblCommon.buildDataType(Convert.toString(instColumn.get("DATA_TYPE")),
                        instColSize, instColPrec));

                String diff = "";
                //数据类型差异比较
                if (!configColumn.get("DATA_TYPE").toString().equalsIgnoreCase(instColumn.get("DATA_TYPE").toString())) {
                    //数据类型差异
                    diff += DIFF_TYPE_DATATYPE;
                } else {//比较长度和精度
                    if (!instColumn.get("DATA_TYPE").toString().equalsIgnoreCase("DATE") && !(configColSize == instColSize)
                            && configColPrec == instColPrec) {
                        diff += DIFF_TYPE_DATATYPE;
                    }
                }
                int configIsPrimary = configColumn.get("IS_PRIMARY") == null || configColumn.get("IS_PRIMARY").equals("") ? 0 : Integer.parseInt(configColumn.get("IS_PRIMARY").toString());
                int intIsPrimary = instColumn.get("IS_PRIMARY") == null || instColumn.get("IS_PRIMARY").equals("") ? 0 : Integer.parseInt(instColumn.get("IS_PRIMARY").toString());
                //是否为主键比对
                if (configIsPrimary != intIsPrimary) {
                    diff += diff.equals("") ? DIFF_TYPE_PRIMARYKEY : ("," + DIFF_TYPE_PRIMARYKEY);
                }
                int configColNullabled = configColumn.get("COL_NULLABLED") == null || configColumn.get("COL_NULLABLED").equals("") ? 0 : Integer.parseInt(configColumn.get("COL_NULLABLED").toString());
                int intColNullabled = instColumn.get("COL_NULLABLED") == null || instColumn.get("COL_NULLABLED").equals("") ? 0 : Integer.parseInt(instColumn.get("COL_NULLABLED").toString());
                //是否为空比对
                if (configColNullabled != intColNullabled) {
                    diff += diff.equals("") ? DIFF_TYPE_NULLABLE : ("," + DIFF_TYPE_NULLABLE);
                }
                //默认值比对
                if (!Convert.toString(instColumn.get("DEFAULT_VAL")).trim().equals(Convert.toString(configColumn.get("DEFAULT_VAL")).trim())) {
                    diff += diff.equals("") ? DIFF_TYPE_DEFAULTVAL : ("," + DIFF_TYPE_DEFAULTVAL);
                }

                //维度表关联对比
                if (!Convert.toString(instColumn.get("thisDim")).trim().equals(Convert.toString(configColumn.get("thisDim")).trim())) {
                    diff += diff.equals("") ? DIFF_TYPE_TIM : ("," + DIFF_TYPE_TIM);
                }

                //维度表关联对比
                if (!Convert.toString(instColumn.get("COL_BUS_COMMENT")).trim().equals(Convert.toString(configColumn.get("COL_BUS_COMMENT")).trim())) {
                    diff += diff.equals("") ? DIFF_TYPE_REMARK : ("," + DIFF_TYPE_REMARK);
                }


                itemResult.put("DIFF", diff);
                //判断是否存在差异
                if (!diff.equals("")) {
                    diffConunt++;
                }
                //删除已经匹配的字段。
                preTablesMap.remove(columnName);
            } else {
                //不存在则表示此列为新增列
                itemResult.put("PRE_COL_NAME", "--");
                itemResult.put("PRE_COL_DATA_TYPE", "--");
                itemResult.put("PRE_COL_NULLABLED", "--");
                itemResult.put("PRE_DEFAULT_VAL", "--");
                itemResult.put("PRE_IS_PRIMARY", "--");
                itemResult.put("PRE_COL_BUS_COMMENT", "--");
                itemResult.put("PRE_THIS_DIM", "--");
                itemResult.put("PRE_COL_DATA_TYPE", "--");
                itemResult.put("DIFF", Convert.toString(DIFF_TYPE_ADD_COLUMN));
                diffConunt++;
            }
            result.add(itemResult);
        }
        //如果实例表中还存在字段，则计算为删除的字段。
        if (preTablesMap.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : preTablesMap.entrySet()) {
                String columnName = entry.getKey();
                Map<String, Object> itemResult = new HashMap<String, Object>();
                Map<String, Object> instColumn = preTablesMap.get(columnName);

                itemResult.put("COL_NAME", "--");
                itemResult.put("COL_DATA_TYPE", "--");
                itemResult.put("COL_NULLABLED", "--");
                itemResult.put("DEFAULT_VAL", "--");
                itemResult.put("IS_PRIMARY", "--");
                itemResult.put("COL_BUS_COMMENT", "--");
                itemResult.put("THIS_DIM", "--");
                itemResult.put("COL_DATA_TYPE", "--");

                itemResult.put("PRE_COL_NAME", columnName);
                itemResult.put("PRE_COL_DATA_TYPE", TblCommon.buildDataType(Convert.toString(instColumn.get("DATA_TYPE")),
                        instColumn.get("COL_SIZE") == null || instColumn.get("COL_SIZE").equals("") ? 0 : Integer.parseInt(instColumn.get("COL_SIZE").toString()),
                        instColumn.get("COL_PREC") == null || instColumn.get("COL_PREC").equals("") ? 0 : Integer.parseInt(instColumn.get("COL_PREC").toString())));
                itemResult.put("PRE_COL_NULLABLED", instColumn.get("COL_NULLABLED") == null || instColumn.get("COL_NULLABLED").equals("") ? 0 : Integer.parseInt(instColumn.get("COL_NULLABLED").toString()));
                itemResult.put("PRE_DEFAULT_VAL", instColumn.get("DEFAULT_VAL"));
                itemResult.put("PRE_IS_PRIMARY", instColumn.get("IS_PRIMARY") == null || instColumn.get("IS_PRIMARY").equals("") ? 0 : Integer.parseInt(instColumn.get("IS_PRIMARY").toString()));
                itemResult.put("PRE_COL_BUS_COMMENT", instColumn.get("COL_BUS_COMMENT"));
                itemResult.put("PRE_THIS_DIM", instColumn.get("thisDim"));

                itemResult.put("DIFF", Convert.toString(DIFF_TYPE_DELETE_COLUMN));
                diffConunt++;
                result.add(itemResult);
            }
        }
        //加入差异数字段。
        Map<String, Object> diffInfo = new HashMap<String, Object>();
        diffInfo.put("DIFF_COUNT", diffConunt);
        //result.add(diffInfo);
        return result;
    }

    /**
     * 根据维度列ID查询维度信息
     * 应该查出唯一的列
     *
     * @param colId
     * @return
     */
    public Map<String, Object> queryDimInfoByColId(int colId, int tableVersion) {
        String sql = "SELECT C.DIM_TYPE_ID, C.DIM_LEVEL,DT.TABLE_NAME, DT.TABLE_NAME_CN,DT.TABLE_BUS_COMMENT,DC.COL_NAME, " +
                " DC.COL_NAME_CN,A.DIM_TYPE_NAME,B.DIM_LEVEL_NAME FROM META_TABLE_COLS C " +
                " ,META_TABLES DT,META_TABLE_COLS DC,META_DIM_TYPE A,META_DIM_LEVEL B " +
                " WHERE C.DIM_TABLE_ID=DT.TABLE_ID AND C.DIM_COL_ID=DC.COL_ID " +
                " AND A.DIM_TYPE_ID = C.DIM_TYPE_ID AND A.DIM_TABLE_ID = C.DIM_TABLE_ID  " +
                " AND B.DIM_TABLE_ID = C.DIM_TABLE_ID AND B.DIM_TYPE_ID = C.DIM_TYPE_ID AND B.DIM_LEVEL=C.DIM_LEVEL " +
                " AND DC.COL_STATE = 1 AND DT.TABLE_STATE = 1" +
                " AND C.COL_ID=" + colId + " AND C.TABLE_VERSION= " + tableVersion;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 根据列ID取列信息
     * 2011-11-23修改：只取当前有效版本的列信息
     *
     * @param colId
     * @return
     */
    public Map<String, Object> queryColByColId(int colId, int tableVersion) {
        String sql = "SELECT T.COL_ID, T.TABLE_ID, T.COL_NAME, T.COL_NAME_CN, T.COL_DATATYPE, T.COL_SIZE, T.COL_PREC, T.COL_BUS_COMMENT, " +
                "T.COL_ORDER, T.COL_NULLABLED, T.COL_STATE, T.DEFAULT_VAL, T.DIM_TABLE_ID, T.COL_BUS_TYPE, T.DIM_LEVEL, T.DIM_COL_ID, " +
                "T.IS_PRIMARY, T.DIM_TYPE_ID, T.TABLE_VERSION FROM META_TABLE_COLS T,META_TABLES A WHERE A.TABLE_ID=T.TABLE_ID AND A.TABLE_VERSION=T.TABLE_VERSION" +
                " AND T.COL_ID =" + colId + " AND A.TABLE_VERSION=" + tableVersion;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 根据表类ID构造一颗列树，子节点是该表类的列，父节点是该表，并且只有一层叶子节点
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryColTreeByTableId(int tableId) {
        String sql = "SELECT t.col_id as col_id,t.table_id,t.col_name || ' 类型：' || t.col_datatype as col_name,'0' AS children FROM meta_table_cols t WHERE t.table_id=" + tableId
                + " AND t.table_version IN (" +
                "SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=" + tableId + " GROUP BY TABLE_ID " +
                ")";
        return getDataAccess().queryForList(sql);
    }


    /**
     * 根据表ID和状态查询字段信息
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryTableColsByTableIdAndState(long tableId) {
        String sql = "SELECT T.COL_ID,T.COL_NAME,T.COL_NAME_CN,T.COL_DATATYPE,T.COL_SIZE, "
                + "T.COL_NULLABLED,T.COL_STATE,T.IS_PRIMARY,T.COL_BUS_TYPE,T.DEFAULT_VAL,T.COL_BUS_COMMENT "
                + "FROM META_TABLE_COLS T "
                + "WHERE T.COL_STATE = 0 AND T.TABLE_ID = " + tableId
                + "ORDER BY T.COL_ORDER";
        return getDataAccess().queryForList(sql);
    }

    public List<Map<String, Object>> queryValidCols(long tableId) {
        String sql = "SELECT T.COL_ID,T.COL_NAME,T.COL_NAME_CN,T.COL_DATATYPE,T.COL_SIZE, "
                + "T.COL_NULLABLED,T.COL_STATE,T.IS_PRIMARY,T.COL_BUS_TYPE,T.DEFAULT_VAL,T.COL_BUS_COMMENT "
                + "FROM META_TABLE_COLS T "
                + "WHERE T.COL_STATE = 1 AND T.TABLE_ID = " + tableId
                + "ORDER BY T.COL_ORDER";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表类ID集合查询一些列的表类有效列信息
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryValidColsByTableIds(Set<Long> tableId) {
        String sql = "SELECT T.COL_ID,T.COL_NAME,T.COL_NAME_CN,T.COL_DATATYPE,T.COL_SIZE, "
                + "T.COL_NULLABLED,T.COL_STATE,T.IS_PRIMARY,T.COL_BUS_TYPE,T.DEFAULT_VAL,T.COL_BUS_COMMENT,T.TABLE_ID," +
                "T.DIM_LEVEL,T.DIM_COL_ID,T.DIM_TABLE_ID,T.DIM_TYPE_ID  "
                + "FROM META_TABLE_COLS T "
                + "WHERE T.COL_STATE = 1 AND T.TABLE_ID IN " + SqlUtils.inParamDeal(tableId.toArray()) + " "
                + "ORDER BY T.COL_ORDER";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表ID和状态查询列ID信息
     *
     * @param tableId
     * @param tableVersion
     * @return
     */
    public List<Map<String, Object>> queryColIdByTableId(long tableId, int tableVersion) {
        String sql = "SELECT C.COL_ID,C.COL_NAME FROM META_TABLE_COLS C WHERE " +
                "C.TABLE_ID = ?" +
                " AND C.TABLE_VERSION = ?";
        return getDataAccess().queryForList(sql, tableId, tableVersion);
    }

    /**
     * 根据列ID查询关联唯独的列ID和维度表ID。该SQL在执行之前已经根据表ID和版本过滤的列ID。
     *
     * @param colsId
     * @return
     */
    public Map<String, Object> queryDimByColsId(int colsId) {
        String sql = "SELECT M.DIM_TABLE_ID,M.DIM_COL_ID FROM META_TABLE_COLS M WHERE M.COL_ID = ?";
        return getDataAccess().queryForMap(sql, colsId);
    }

    /**
     * @param tableId
     * @param tableVersion
     * @return void
     * @throws Exception
     * @throws
     * @Title: updateMetaTableColsByTableIdAndVersion
     * @Description: 根据表ID+版本号更新列状态
     */
    public void updateMetaTableColsByTableIdAndVersion(long tableId, int tableVersion, int colState) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update meta_table_cols set COL_STATE=" + colState + " where TABLE_ID=? and TABLE_VERSION=?");
        getDataAccess().execUpdate(buffer.toString(), tableId, tableVersion);
    }

    /**
     * 根据TableId将对应列信息设为无效
     *
     * @param tableId
     * @throws Exception
     */
    public void invalidTableCols(long tableId) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update meta_table_cols set COL_STATE=0 where TABLE_ID=?");
        getDataAccess().execUpdate(buffer.toString(), tableId);
    }

    public List<Map<String, Object>> queryValidColInfoByTableId(int tableId, Page page) {
        StringBuffer sql = new StringBuffer();
        sql = sql.append("SELECT C.COL_ID, ");
        sql.append("C.TABLE_ID, ");
        sql.append("C.COL_NAME, ");
        sql.append("(CASE ");
        sql.append("WHEN C.COL_DATATYPE = 'DATE' THEN ");
        sql.append("'DATE' ");
        sql.append("WHEN C.COL_SIZE > 0 AND C.COL_PREC > 0 THEN ");
        sql.append("C.COL_DATATYPE || '(' || C.COL_SIZE || ',' || C.COL_PREC || ')' ");
        sql.append("WHEN C.COL_SIZE > 0 THEN ");
        sql.append("C.COL_DATATYPE || '(' || C.COL_SIZE || ')' ");
        sql.append("ELSE ");
        sql.append("C.COL_DATATYPE ");
        sql.append("END) COL_DATATYPE, ");
        sql.append("C.COL_NAME_CN, ");
        sql.append("C.COL_SIZE, ");
        sql.append("C.COL_PREC, ");
        sql.append("C.COL_DATATYPE DATA_TYPE, ");
        sql.append("C.COL_ORDER, ");
        sql.append("C.COL_NULLABLED, ");
        sql.append("C.COL_STATE, ");
        sql.append("C.DEFAULT_VAL, ");
        sql.append("C.COL_BUS_TYPE, ");
        //sql.append("C.DIM_LEVEL, ");
        sql.append("C.DIM_COL_ID, ");
        sql.append("C.IS_PRIMARY, ");
        sql.append("C.DIM_TYPE_ID, ");
        sql.append("C.TABLE_VERSION, ");
        sql.append("C.DIM_TABLE_ID, ");
        sql.append("C.COL_BUS_COMMENT, ");
        sql.append("D.TABLE_NAME DIM_TABLE_NAME, ");
        sql.append("E.COL_NAME DIM_COL_NAME, ");
        sql.append("F.DIM_TYPE_NAME, ");
        sql.append("dimLevel.Dim_Level, ");
        sql.append("(case when dimLevel.Dim_Level_Name is not null then  C.DIM_LEVEL || '--' || dimLevel.Dim_Level_Name else '' end) DIM_LEVEL_NAME ");

        sql.append("FROM META_TABLE_COLS C ");
        sql.append("LEFT JOIN meta_tables t ");
        sql.append("ON t.table_id = c.table_id ");
        sql.append("AND t.table_state = 1 ");
        sql.append("LEFT JOIN meta_tables D ");
        sql.append("ON c.dim_table_id = D.table_id ");
        sql.append("AND D.table_state = 1 ");
        sql.append("LEFT JOIN meta_table_cols E ");
        sql.append("ON c.dim_col_id = E.Col_Id ");
        sql.append("AND E.Col_State = 1 ");
        sql.append("left join META_DIM_TYPE F ");
        sql.append("on C.DIM_TYPE_ID = F.DIM_TYPE_ID and c.dim_table_id = f.dim_table_id ");
        sql.append("LEFT JOIN META_DIM_LEVEL dimLevel ");
        sql.append("ON C.DIM_LEVEL = dimLevel.Dim_Level and c.dim_table_id = dimLevel.Dim_Table_Id and c.dim_type_id = dimlevel.dim_type_id ");

        sql.append("WHERE 1 = 1 ");
        sql.append("AND C.TABLE_ID = ? ");
        sql.append("AND C.COL_STATE = 1 ");
        sql.append("ORDER BY C.COL_ID");

        //分页包装
        String pageSql = sql.toString();
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(sql.toString(), page);
        }
        return getDataAccess().queryForList(pageSql, tableId);
    }
}
