package tydic.meta.module.tbl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 熊久亮
 * @date 2011-12-21
 * @description 
 *
 */
public class MetaUpdateDimTableDao extends MetaBaseDAO {
	/**
	 * 修改表结构。添加维度表结构
	 * @param sqls
	 * @return
	 * @throws Exception 
	 */
	public void addDimTableCol(List<String> sqls) throws Exception{
		 Connection connection = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).getConnection();
	        Statement statement = null;
	        try{
	            statement = connection.createStatement();
	            for(int i = 0; i < sqls.size(); i++){
	                statement.addBatch(sqls.get(i));
	            }
	            statement.executeBatch();
	        } catch(Exception e){
	            throw e;
	        } finally{
	            if(statement != null){
	                statement.close();
	            }
	            if(connection != null){
	                connection.close();
	            }
	        }
	}
	
	/**
	 * 修改表结构基本信息
	 * @param sqls
	 * @return
	 * @throws Exception 
	 */
	public void updateDimTableCol(List<String> sqls) throws Exception{
		
		Connection connection = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).getConnection();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            for(int i = 0; i < sqls.size(); i++){
                statement.addBatch(sqls.get(i));
            }
            statement.executeBatch();
        } catch(Exception e){
            throw e;
        } finally{
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
	}
	
	/**
	 * 新增或者修改维度表列基本信息
	 * @throws 	Exception
	 * @param	newDimColsData 新的维度列数据
	 * @param	oldDimColsData	旧的维度列数据
	 */
	public void addOrUpdateDimTableCol(List<String> sql) throws Exception {
        String[] tmpSql = new String[sql.size()] ;
        try{
            for(int i = 0; i < sql.size(); i++){
                tmpSql[i] = sql.get(i);
            }
            getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID).execUpdateBatch(tmpSql);
        } catch(Exception e){
            throw e;
        }
	}
	
}



