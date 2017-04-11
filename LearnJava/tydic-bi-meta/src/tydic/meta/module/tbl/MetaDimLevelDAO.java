package tydic.meta.module.tbl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.StringUtils;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-27
 * @description META_DIM_LEVEL操作DAO
 *
 */
public class MetaDimLevelDAO extends MetaBaseDAO {

    /**
     * 查询维度层级记录
     *
     * @param dimTableId
     * @param dimTypeId
     * @return
     */
    public List<Map<String, Object>> queryDimLevel(int dimTableId, int dimTypeId) {
        StringBuffer sql = new StringBuffer("SELECT DIM_LEVEL,DIM_LEVEL_NAME FROM META_DIM_LEVEL WHERE 1=1 ");

        sql.append("AND DIM_TABLE_ID = ? ");

        sql.append("AND DIM_TYPE_ID = ? ");

        sql.append("ORDER BY DIM_LEVEL, DIM_LEVEL_NAME ");

        return getDataAccess().queryForList(sql.toString(), dimTableId,dimTypeId);
    }

    /**
     * 查询指定层级的维度层次信息
     * @param dimTableId
     * @param dimTypeId
     * @param dimLevels
     * @return
     */
    public List<Map<String, Object>> queryDimLevel(int dimTableId, int dimTypeId,String dimLevels) {
        StringBuffer sql = new StringBuffer("SELECT DIM_LEVEL,DIM_LEVEL_NAME FROM META_DIM_LEVEL WHERE 1=1 ");

        sql.append("AND DIM_TABLE_ID = ? ");

        sql.append("AND DIM_TYPE_ID = ? ");
        if(!StringUtils.isEmpty(dimLevels)) {
            sql.append(" AND DIM_LEVEL IN "+ SqlUtils.inParamDeal(dimLevels.split(",")));
        }
        sql.append("ORDER BY DIM_LEVEL, DIM_LEVEL_NAME ");

        return getDataAccess().queryForList(sql.toString(), dimTableId,dimTypeId);
    }

    /**
     * 向META_DIM_LEVEL批量插入记录
     * @param data
     * @return 插入的记录条数
     */
    public int[] insertBatch(final List<Map<String,Object>> data){
        String sql = "INSERT INTO META_DIM_LEVEL( " +
                     "DIM_TABLE_ID,DIM_TYPE_ID,DIM_LEVEL, " +
                     "DIM_LEVEL_NAME) VALUES(?,?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                preparedStatement.setObject(1,data.get(i).get("dimTableId"));
                preparedStatement.setObject(2,data.get(i).get("dimTypeId"));
                preparedStatement.setObject(3,data.get(i).get("dimLevel"));
                preparedStatement.setObject(4,data.get(i).get("dimLevelName"));

            }
            public int batchSize(){
                return data.size();
            }
        });
    }

    /**
     * 根据tableId删除层级数据
     * @param tableId
     * @throws Exception
     */
    public void deleteLevelByTableId(int tableId) throws Exception{
        String sql="DELETE FROM META_DIM_LEVEL WHERE DIM_TABLE_ID="+tableId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 向META_DIM_LEVEL插入单条记录
     *
     * @param data 待插入的参数，key为驼峰式命名标准
     * @throws Exception 插入失败
     */
    public void insertMetaDimLevel(final Map<String, Object> data) throws Exception {
        String sql = "INSERT INTO META_DIM_LEVEL("
                     + "DIM_TABLE_ID,DIM_TYPE_ID,DIM_LEVEL,"
                     + "DIM_LEVEL_NAME) VALUES(?,?,?,?)";
        Object[] params = new Object[]{
                data.get("dimTableId"), data.get("dimTypeId"),data.get("dimLevel"),data.get("dimLevelName")
        };
        getDataAccess().execUpdate(sql,params);
    }
//    /**
//     * 置所有的表ID表状态失效
//     * @param tableIds
//     * @return
//     */
//    public int inValidTable(Object[] tableIds) throws Exception{
//        String sql="UPDATE  META_DIM_LEVEL SET TABLE_STATE="+TblConstant.META_TABLE_STATE_INVAILD+" WHERE TABLE_ID IN (";
//        for(int i=0;i<tableIds.length;i++){
//            sql+="?";
//            if(i!=tableIds.length-1){
//                sql+=",";
//            }
//        }
//        sql+=")";
//        return getDataAccess().execUpdate(sql,tableIds);
//    }
}
