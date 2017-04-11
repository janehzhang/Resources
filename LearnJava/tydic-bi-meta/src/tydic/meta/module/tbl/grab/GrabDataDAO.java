package tydic.meta.module.tbl.grab;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;

/**
*
* @author 李国民
* @date 2012-02-27
* @description 后台定时抓取数据DAO
*
*/
public class GrabDataDAO extends MetaBaseDAO{
	
	public String[] getOwnerList(){
		String sql = "SELECT distinct TABLE_OWNER FROM " +
				" (SELECT TABLE_OWNER FROM META_TABLES  WHERE TRIM(TABLE_OWNER) IS NOT NULL GROUP BY TABLE_OWNER " +
				" UNION ALL" +
				" SELECT TABLE_OWNER FROM META_TABLE_INST M WHERE TRIM(M.TABLE_OWNER) IS NOT NULL GROUP BY TABLE_OWNER)";
		return getDataAccess().queryForDataTable(sql).getColAsString(0);
	}
	
	/**
	 * 根据数据源id查询映射表中的对应数据
	 * @param dataSourceId 数据源id
	 * @return
	 */
	public List<Map<String,Object>> getTableInstList(int dataSourceId){
		String sql = "SELECT  M.TABLE_NAME AS TABLE_NAME, M.DATA_SOURCE_ID, T.TABLE_OWNER," +
			" T.TABLE_INST_ID,T.TABLE_NAME AS TABLE_INST_NAME " +
			" FROM META_TABLE_INST T LEFT JOIN " +
			" META_TABLES M ON T.TABLE_ID = M.TABLE_ID AND T.TABLE_VERSION = M.TABLE_VERSION " +
			" WHERE TRIM(T.TABLE_OWNER) IS NOT NULL AND M.DATA_SOURCE_ID=?";
		return getDataAccess().queryForList(sql,dataSourceId);
	}
	
	/**
	 * 根据传入数据源id及所属用户群得到该数据源下所有表信息
	 * @param dataSourceId 数据源id
	 * @param owners 所属用户群
	 * @return
	 */
	public List<Map<String,Object>> getAllTablesListByDataSource(int dataSourceId, String owners) throws Exception{
		String sql = "SELECT T.TABLE_NAME,T.OWNER,T.NUM_ROWS FROM ALL_TABLES T WHERE T.OWNER IN ("+owners+")";
        DataAccess access = getDataAccess(dataSourceId+"");
		return access.queryForList(sql);
	}
    
    /**
     * 根据传入数据源id及所属用户群得到该数据源下所有分区信息
     * @param dataSourceId 数据源id
	 * @param owners 所属用户群
     * @return
     */
    public List<Map<String,Object>> getAllTabPartition(int dataSourceId, String owners) throws Exception{
    	String sql = "SELECT T.NUM_ROWS,T.PARTITION_NAME,T.TABLE_OWNER,T.TABLE_NAME " +
    			"FROM ALL_TAB_PARTITIONS T WHERE T.TABLE_OWNER IN ("+owners+")";
        DataAccess access = getDataAccess(dataSourceId+"");
		return access.queryForList(sql);
    }    
    
    /**
     * 根据传入数据源id及所属用户群得到该数据源下所有子分区信息
     * @param dataSourceId 数据源id
	 * @param owners 所属用户群
     * @return
     */
    public List<Map<String,Object>> getAllTabSubpartition(int dataSourceId, String owners) throws Exception{
    	String sql = "SELECT T.NUM_ROWS,T.PARTITION_NAME,T.SUBPARTITION_NAME,T.TABLE_OWNER,T.TABLE_NAME " +
    			" FROM ALL_TAB_SUBPARTITIONS T WHERE T.TABLE_OWNER IN ("+owners+")";
        DataAccess access = getDataAccess(dataSourceId+"");
		return access.queryForList(sql);
    }
    
    /**
     * 根据条件查询数据是否在META_TABLE_INST_DATA表中存在，
     * @param data 查询条件
     * @param flag 标识，(1为无子分区查询，2为有子分区查询)
     * @return 如果存在返回true，不存在返回flase
     */
    public boolean isInTableData(Map<String,Object> data, int flag){
        List params = new ArrayList();
        params.add(data.get("tableInstId"));
        params.add(data.get("partition"));
    	String sql = "SELECT T.TABLE_INST_ID FROM META_TABLE_INST_DATA T WHERE " +
    			" T.TABLE_INST_ID=? AND T.PARTITION =?";
    	if(flag==1){		//无子分区查询，默认为null
    		 sql += " AND TRIM(T.SUBPARTITION) IS NULL";
    	}else if(flag==2){ 	//有子分区查询
   		 	 sql += " AND T.SUBPARTITION = ?";
   		 	 params.add(data.get("subpartition").toString());
    	}
    	Map<String,Object> map = getDataAccess().queryForMap(sql, params.toArray());
    	return (map!=null&&map.size()>0);
    }
	
    /**
     * 批量修改映射表数据
     * @param datas 修改数据
     * @return
     */
    public int[] updateTableInst(final List<Map<String,Object>> datas) {
        String sql = "UPDATE META_TABLE_INST T SET T.TABLE_RECORDS=? WHERE T.TABLE_INST_ID=?";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,data.get("tableRecords")); 	//记录数
                preparedStatement.setObject(2,data.get("tableInstId")); 	//映射表id
            }
            public int batchSize(){
                return datas.size();
            }
        });
    }
    
    /**
     * 批量修改记录表数据
     * @param datas 修改数据
     * @param flag 修改标志(1为修改条件中没有子分区，2为修改条件中包含子分区)
     * @return
     */
    public int[] updateTableInstData(final List<Map<String,Object>> datas, int flag) {
    	if(flag==1){
    		String sql  = "UPDATE META_TABLE_INST_DATA T SET T.ROW_RECORDS=?, T.STATE_DATE=sysdate " +
            		" WHERE T.TABLE_INST_ID = ? AND T.PARTITION =? AND TRIM(T.SUBPARTITION) IS NULL";
    		
            return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                    Map<String,Object> data=datas.get(i);
                    preparedStatement.setObject(1,data.get("rowRecords")); 	//记录数
                    preparedStatement.setObject(2,data.get("tableInstId")); //映射表id
                    preparedStatement.setObject(3,data.get("partition")); 	//分区名
                }
                public int batchSize(){
                    return datas.size();
                }
            });
            
    	}else if(flag==2){
    		String sql  = "UPDATE META_TABLE_INST_DATA T SET T.ROW_RECORDS=?, T.STATE_DATE=sysdate " +
    		" WHERE T.TABLE_INST_ID = ? AND T.PARTITION =? AND T.SUBPARTITION =?";
    		
		    return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
		        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
		            Map<String,Object> data=datas.get(i);
		            preparedStatement.setObject(1,data.get("rowRecords")); 	//记录数
		            preparedStatement.setObject(2,data.get("tableInstId")); //映射表id
		            preparedStatement.setObject(3,data.get("partition")); 	//分区名
	                preparedStatement.setObject(4,data.get("subpartition"));	//子分区名
		        }
		        public int batchSize(){
		            return datas.size();
		        }
		    });
		    
    	}else{
    		return null;
    	}
    }    
	
    /**
     * 批量新增记录表数据	
     * @param datas 修改数据
     * @return
     */
    public int[] insterTableInstData(final List<Map<String,Object>> datas) {
        String sql = "INSERT INTO META_TABLE_INST_DATA(TABLE_INST_DATA_ID,TABLE_INST_ID," +
        		" TABLE_INST_DATA_STATE,STATE_DATE,PARTITION,SUBPARTITION,CREATE_DATE,ROW_RECORDS)" +
        		" VALUES(?,?,1,sysdate,?,?,sysdate,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,queryForNextVal("SEQ_TAB_INST_DATA_ID")); 	//自动生成id
                preparedStatement.setObject(2,data.get("tableInstId")); 	//映射表id
                preparedStatement.setObject(3,data.get("partition")); 		//分区名
                preparedStatement.setObject(4,data.get("subpartition"));	//子分区名
                preparedStatement.setObject(5,data.get("rowRecords")); 		//记录数
            }
            public int batchSize(){
                return datas.size();
            }
        });
    }
    
	/**
	 * 根据数据源id查询对应的实体表关联信息
	 * @param dataSourceId 数据源id
	 * @return
	 */
	public List<Map<String,Object>> getTablesList(int dataSourceId){
		String sql = "SELECT M.TABLE_ID, M.TABLE_VERSION, M.DATA_SOURCE_ID, T.TABLE_NAME,T.TABLE_OWNER" +
				" FROM META_TABLE_INST T LEFT JOIN META_TABLES M ON T.TABLE_ID = M.TABLE_ID" +
				" WHERE TRIM(T.TABLE_OWNER) IS NOT NULL AND M.TABLE_STATE=1 AND M.DATA_SOURCE_ID=?";
		return getDataAccess().queryForList(sql,dataSourceId);
	}

	/**
	 * 批量插入预警信息
	 * @param datas
	 * @return
	 */
	public int[] insterTableDiff(final List<Map<String,Object>> datas) {
        String sql = "INSERT INTO META_TABLE_DIFF(DIFF_ID,CREATE_DATE,TABLE_ID,TABLE_VERSION,STATE) VALUES(?,sysdate,?,?,?)";
		return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
		    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
		        Map<String,Object> data=datas.get(i);
		        preparedStatement.setObject(1,queryForNextVal("SEQ_TABLE_DIFF_ID")); //自动生成id
		        preparedStatement.setObject(2,data.get("TABLE_ID")); 					//表类id
		        preparedStatement.setObject(3,data.get("TABLE_VERSION")); 				//表类版本号
		        preparedStatement.setObject(4,1);										//是否预警
		    }
		    public int batchSize(){
		        return datas.size();
		    }
		});
	}
}
