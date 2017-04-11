package tydic.meta.module.tbl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.dim.DimConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-27
 * @description META_DIM_TABLES操作DAO
 */
public class MetaDimTablesDAO extends MetaBaseDAO {

    /**
     * 根据表ID查询维度表信息
     *
     * @return
     */
    public Map<String, Object> queryMetaDimTablesByTableId(int tableId) {
        StringBuffer sql = new StringBuffer(
                "SELECT T.DIM_TABLE_ID,T.TABLE_NAME,S.DATA_SOURCE_NAME,T.TABLE_DIM_LEVEL, "
                        + "T.TABLE_DIM_PREFIX,T.DIM_KEY_COL_ID,T.DIM_PAR_KEY_COL_ID,T.LAST_LEVEL_FLAG,D.TABLE_OWNER "
                        + "FROM META_DIM_TABLES T "
                        + "LEFT JOIN META_DATA_SOURCE S ON T.DATA_SOURCE_ID = S.DATA_SOURCE_ID "
                        + "LEFT JOIN META_TABLE_INST D " +
                        "  ON T.DIM_TABLE_ID=D.TABLE_ID AND D.STATE=1"
                        + "WHERE T.DIM_TABLE_ID=" + tableId + " ");
        sql.append("ORDER BY T.DIM_TABLE_ID ");
        return getDataAccess().queryForMap(sql.toString());
    }

    public String queryDimTableNameByTableId(int tableId) {
        String sql = "SELECT TABLE_NAME FROM META_DIM_TABLES WHERE DIM_TABLE_ID=" + tableId;
        return getDataAccess().queryForString(sql);
    }

    /**
     * 向META_DIM_TABLES插入单条记录
     *
     * @param data
     * @return
     */
    public void insertMetaDimTables(Map<String, Object> data) throws Exception {
        String sql = "INSERT INTO META_DIM_TABLES("
                + "DIM_TABLE_ID,TABLE_NAME,DATA_SOURCE_ID,TABLE_DIM_LEVEL,TABLE_DIM_PREFIX,"
                + "DIM_KEY_COL_ID,DIM_PAR_KEY_COL_ID,LAST_LEVEL_FLAG) "
                + "VALUES(?,?,?,?,? ,?,?,?)";
        Object[] params = new Object[8];
        params[0] = Convert.toString(data.get("dimTableId"));
        params[1] = Convert.toString(data.get("tableName")).toUpperCase();
        params[2] = data.get("dataSourceId").toString();
        params[3] = data.get("tableDimLevel").toString();
        params[4] = Convert.toString(data.get("tableDimPrefix"));
        params[5] = Convert.toString(data.get("dimKeyColId"));
        params[6] = Convert.toString(data.get("dimParKeyColId"));
        params[7] = data.get("lastLevelFlag") == null || data.get("lastLevelFlag").equals("") ? DimConstant.DIM_LAST_LEVEL_FLAG_DISPLAY : Integer.parseInt(data.get("lastLevelFlag").toString());
//        params.addValue(Convert.toInt(data.get("tableVersion")));

        getDataAccess().execUpdate(sql, params);
    }

    /**
     * 根据tableId删除记录
     *
     * @param tableId
     */
    public void deleteMetaDimTablesByTableId(int tableId) throws Exception {
        String sql = "DELETE FROM META_DIM_TABLES WHERE　DIM_TABLE_ID=" + tableId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 向META_DIM_TABLES批量插入记录
     *
     * @param data
     * @return
     */
    public int[] insertBatch(final List<Map<String, Object>> data) {
        String sql = "INSERT INTO META_DIM_TABLES(" +
                "DIM_TABLE_ID,TABLE_NAME,DATA_SOURCE_ID,TABLE_DIM_LEVEL,TABLE_DIM_PREFIX," +
                "DIM_KEY_COL_ID,DIM_PAR_KEY_COL_ID,LAST_LEVEL_FLAG) " +
                "VALUES(SEQ_DIM_TABLES_ID.NEXTVAL,?,?,?,?,? ,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Map<String, Object> dataRow = data.get(i);
                preparedStatement.setObject(1, Convert.toString(dataRow.get("tableName")));
                preparedStatement.setObject(2, dataRow.get("dataSourceId").toString());
                preparedStatement.setObject(3, dataRow.get("tableDimLevel").toString());
                preparedStatement.setObject(4, dataRow.get("tableDimPrefix"));
                preparedStatement.setObject(5, dataRow.get("dimKeyColId").toString());
                preparedStatement.setObject(6, dataRow.get("dimParKeyColId").toString());
                preparedStatement.setObject(7, dataRow.get("lastLevelFlag") == null || dataRow.get("lastLevelFlag").equals("") ? DimConstant.DIM_LAST_LEVEL_FLAG_DISPLAY :
                        dataRow.get("lastLevelFlag").toString());
            }

            public int batchSize() {
                return data.size();
            }
        });
    }

    /**
     * 查询所有有效的维度表，查询基本信息，包括表名，表中文名，表ID即可
     *
     * @return
     */
    public List<Map<String, Object>> queryAllValidDimTables() {
        String sql = "SELECT A.TABLE_ID,A.TABLE_NAME,A.TABLE_NAME_CN,A.TABLE_VERSION FROM META_TABLES A,META_DIM_TABLES B " +
                "WHERE B.DIM_TABLE_ID=A.TABLE_ID AND A.TABLE_STATE=" + TblConstant.META_TABLE_STATE_VAILD;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据维度表类ID查询维度表信息
     *
     * @param tableId
     * @return
     */
    public Map<String, Object> queryDimTableInfo(long tableId) {
        String sql = "SELECT I.TABLE_OWNER,T.TABLE_NAME,T.DATA_SOURCE_ID,T.TABLE_DIM_LEVEL," +
                " T.TABLE_DIM_PREFIX,T.DIM_KEY_COL_ID,T.DIM_PAR_KEY_COL_ID,T.DIM_TABLE_ID,T.LAST_LEVEL_FLAG" +
                " FROM META_DIM_TABLES T" +
                " LEFT JOIN META_TABLE_INST I ON T.DIM_TABLE_ID=I.TABLE_ID" +
                " WHERE I.STATE = 1 AND T.DIM_TABLE_ID = ?";
        return getDataAccess().queryForMap(sql, tableId);
    }

    /**
     * 根据表类ID查询其
     *
     * @param tableId 查询维度表类ID
     * @return
     */
    public int queryDimTableDataSourceId(long tableId) {
        String sql = "SELECT T.DATA_SOURCE_ID " +
                " FROM META_DIM_TABLES T" +
                " WHERE T.DIM_TABLE_ID = ?";
        return getDataAccess().queryForInt(sql, tableId);
    }
}
