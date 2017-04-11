package tydic.meta.module.tbl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 熊久亮
 * @date 2011-11-7
 * @description 表分区状态查询
 *
 */
public class MetaTablePartStatusDAO extends MetaBaseDAO{
	
	/**
	 *  查询所有表的分区状态
	 * @param
	 * @return	返回结果集
	 */
	public List<Map<String, Object>> queryTablePartStatueById(int tableInstId,Page page)
	{
		String sql = "SELECT P.TABLE_INST_DATA_ID,P.PARTITION,P.SUBPARTITION,P.DATA_CYCLE_NO,"
					+ "P.DATA_LOCAL_CODE,P.STATE_DATE,P.TABLE_INST_DATA_STATE,Z.ZONE_NAME,P.TABLE_INST_ID "
					+ "FROM META_TABLE_INST_DATA P LEFT JOIN META_DIM_ZONE Z ON " 
					+ "P.DATA_LOCAL_CODE = Z.ZONE_ID WHERE P.TABLE_INST_ID = "+tableInstId+" "
					+ " ORDER BY P.TABLE_INST_ID";
		if(null != page)
		{
			sql = SqlUtils.wrapPagingSql(sql, page);
		}
		return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 根据表ID查询表名
	 * @return
	 */
	public List<Map<String, Object>> queryAllTableName(int tableId){
		String sql = "SELECT T.TABLE_INST_ID,T.TABLE_NAME FROM META_TABLE_INST T WHERE T.TABLE_ID = "+tableId;
		return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 查询实例表字段
	 * @param tableId
	 * @return
	 */
	public List<Map<String, Object>> queryDataAnalysisByTableId(int tableId){
        String sql = "SELECT * FROM meta_table_cols c WHERE c.table_id = "+tableId+" AND c.col_state = " + TblConstant.COL_STATE_VALID;
        return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 分析数据字段
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryDataClosBySql(String sql, Map<String, Object> dataSourceInfo) throws Exception{
        String user = Convert.toString(dataSourceInfo.get("DATA_SOURCE_USER"));
        String url = Convert.toString(dataSourceInfo.get("DATA_SOURCE_RULE"));
        String pwd = Convert.toString(dataSourceInfo.get("DATA_SOURCE_PASS"));
        Connection connection = null;
        try{
            connection = getConnection(user, pwd, url, Constant.ORACLE_DRIVER_STRING);
            return getDataAccessInstance(connection).queryForList(sql);
        }catch (Exception e){
            throw e;
        }finally {
            try{
                if(connection!=null){
                    connection.close();
                }
            }catch (Exception e){}
        }
	}
}
