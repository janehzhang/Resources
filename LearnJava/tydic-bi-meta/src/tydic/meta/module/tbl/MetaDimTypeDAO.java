package tydic.meta.module.tbl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.dim.DimConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-27
 * @description META_DIM_TYPE操作DAO
 */
public class MetaDimTypeDAO extends MetaBaseDAO {

    /**
     * 查询区域类型
     *
     * @param data
     * @return
     */
    public List<Map<String, Object>> queryDimType(int dimTableId) {
        StringBuffer sql = new StringBuffer("SELECT A.DIM_TYPE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TYPE_CODE," +
                "NVL(B.MAX_LEVEL,0) MAX_LEVEL,A.DIM_TABLE_ID FROM META_DIM_TYPE A ");
        //关联LEVEL表，求出最大层级
        sql.append("LEFT JOIN " +
                "(SELECT DIM_TYPE_ID,DIM_TABLE_ID,COUNT(*) MAX_LEVEL FROM META_DIM_LEVEL GROUP BY DIM_TYPE_ID,DIM_TABLE_ID) B " +
                "ON B.DIM_TABLE_ID=A.DIM_TABLE_ID AND A.DIM_TYPE_ID=B.DIM_TYPE_ID ");
        sql.append("WHERE 1=1 ");
        List params = new ArrayList();

        sql.append("AND A.DIM_TABLE_ID = ? AND A.DIM_TYPE_STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID + " ");
        params.add(dimTableId);
        sql.append("ORDER BY A.DIM_TYPE_ID,A.DIM_TYPE_NAME ");

        return getDataAccess().queryForList(sql.toString(), params.toArray());
    }

    public int[] insertBatch(final List<Map<String, Object>> data) {
        String sql = "INSERT INTO META_DIM_TYPE(" +
                "DIM_TYPE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TABLE_ID,DIM_TYPE_CODE) VALUES(SEQ_DIM_TYPE_ID.NEXTVAL,?,?,?,?) ";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setObject(1, data.get(i).get("dimTypeName"));
                preparedStatement.setObject(2, data.get(i).get("dimTypeDesc"));
                preparedStatement.setObject(3, data.get(i).get("dimTableId"));
                preparedStatement.setObject(4, data.get(i).get("dimTypeCode"));
            }

            public int batchSize() {
                return data.size();
            }
        });
    }

    public long insertDimType(Map<String, Object> data) throws Exception {
        long dimType = queryForNextVal("SEQ_DIM_TYPE_ID");
        String sql = "INSERT INTO META_DIM_TYPE(" +
                "DIM_TYPE_ID,DIM_TYPE_NAME,DIM_TYPE_DESC,DIM_TABLE_ID,DIM_TYPE_CODE," +
                "DIM_TYPE_STATE) VALUES(?,?,?,?,?, ?) ";
        Object[] proParams = new Object[]{
                dimType, data.get("dimTypeName"), data.get("dimTypeDesc"), data.get("dimTableId"), data.get("dimTypeCode"),
                data.get("dimTypeState") == null || data.get("dimTypeState").equals("") ? DimConstant.DIM_TYPE_INVALID : data.get("dimTypeState")
        };
        getDataAccess().execUpdate(sql, proParams);
        return dimType;
    }

    /**
     * 修改归并类型
     *
     * @param data
     * @return 返回类型ID
     * @throws Exception
     */
    public long updateDimType(Map<String, Object> data) throws Exception {
        long dimType = Long.parseLong(data.get("dimTypeId").toString());
        String sql = "UPDATE META_DIM_TYPE " +
                "SET DIM_TYPE_NAME=?,DIM_TYPE_DESC=?,DIM_TABLE_ID=?,DIM_TYPE_CODE=? " +
                "WHERE DIM_TYPE_ID=? ";
        Object[] proParams = new Object[]{
                data.get("dimTypeName"), data.get("dimTypeDesc"), data.get("dimTableId"), data.get("dimTypeCode"), dimType};
        getDataAccess().execUpdate(sql, proParams);
        return dimType;
    }

    public void deleteDimTypeByTableId(int tableId) throws Exception {
        String sql = "DELETE FROM META_DIM_TYPE WHERE DIM_TABLE_ID=" + tableId;
        getDataAccess().execUpdate(sql);
    }


    /**
     * 置指定表类ID不在指定范围内的类型ID失效。
     *
     * @param tableId
     * @param exculdeTypeList
     * @return
     * @throws Exception
     */
    public int inValidDimTypeByNotInclude(int tableId, List<Long> exculdeTypeList) throws Exception {
        String sql = "UPDATE  META_DIM_TYPE SET DIM_TYPE_STATE="
                + TblConstant.META_DIM_TYPE_STATE_INVALID + " WHERE DIM_TABLE_ID =" + tableId + " ";
        if (exculdeTypeList != null && exculdeTypeList.size() > 0) {
            sql += " AND DIM_TYPE_ID NOT IN (";
            for (int i = 0; i < exculdeTypeList.size(); i++) {
                sql += exculdeTypeList.get(i) + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
        }
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 查询指定表的归并类型信息，并关联层级表，对层级做指定字符串处理
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryDimTypeAndLevelByTableId(int tableId) {
        String sql = "SELECT B.DIM_TYPE_ID,B.DIM_TYPE_NAME,B.DIM_TYPE_DESC,B.DIM_TYPE_CODE,C.DIM_LEVEL FROM META_DIM_TYPE B " +
                "LEFT JOIN (SELECT DIM_TYPE_ID , MAX(SUBSTR(SYS_CONNECT_BY_PATH(DIM_LEVEL||'--'||RTRIM(DIM_LEVEL_NAME), ','), 2)) DIM_LEVEL " +
                "FROM " +
                "(SELECT A.DIM_LEVEL,A.DIM_LEVEL_NAME,A.DIM_TYPE_ID,ROW_NUMBER() OVER(PARTITION BY DIM_TYPE_ID ORDER BY DIM_LEVEL) RN FROM META_DIM_LEVEL A " +
                "WHERE DIM_TABLE_ID=" + tableId + " ) START WITH RN = 1 CONNECT BY RN = PRIOR RN + 1 AND DIM_TYPE_ID= PRIOR DIM_TYPE_ID " +
                "GROUP BY DIM_TYPE_ID) C ON C.DIM_TYPE_ID=B.DIM_TYPE_ID WHERE B.DIM_TABLE_ID= " + tableId + " AND DIM_TYPE_STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查找有效的归并类型
     *
     * @param dimTableId
     * @return
     */
    public int[] queryValidDimType(long dimTableId) {
        String sql = "SELECT DIM_TYPE_ID FROM META_DIM_TYPE WHERE DIM_TABLE_ID=? AND　DIM_TYPE_STATE=" + Constant.META_ENABLE;
        Object[][] rs = getDataAccess().queryForArray(sql, false, dimTableId);
        int[] dimTypes=new int[rs.length];
        for(int i=0;i<rs.length;i++){
             dimTypes[i]= Convert.toInt(rs[i][0]);
        }
        return dimTypes;
    }

}
