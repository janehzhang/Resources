package tydic.meta.module.dim.intRelManger;

import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;

public class DimIntRelMangerDAO extends MetaBaseDAO{
	/**
	 * 按照条件查询出数据
	 * @param map (现在只包含了三项表类名称,接口表,源系统表)
	 * */
   public List<Map<String,Object>> queryDataByCondition(Map<String,Object>map,Page page){
	   StringBuffer sb = new StringBuffer();
	   sb.append("SELECT T.SYS_ID,T.DATA_SOURCE_ID,T.INT_TAB_ID,T.INT_TAB_NAME,T.SRC_TAB_NAME,T.DATA_MAPP_SQL,");
	   sb.append("T.DATA_MAPP_MARK,T.DIM_TABLE_ID,T.USER_ID,D.TABLE_NAME,S.SYS_NAME FROM META_DIM_TAB_INT_REL T ");
	   sb.append("LEFT JOIN META_DIM_TABLES D ON T.DIM_TABLE_ID = D.DIM_TABLE_ID ");
	   sb.append("LEFT JOIN META_TABLES MT ON MT.TABLE_NAME=D.TABLE_NAME ");
	   sb.append("LEFT JOIN META_SYS S ON T.SYS_ID=S.SYS_ID ");
	   sb.append("WHERE MT.TABLE_STATE=1");
	   if(map!=null&&map.size()!=0){
		   String tableName = null; //表类名称
		   String mappTableName = null; //接口表名称
		   String srcTableName  =null; //源系统表
	       Object tableNameObj = map.get("tableName");			
	       if(tableNameObj!=null){
	    	   tableName = tableNameObj.toString();
	    	   sb.append(" AND D.TABLE_NAME LIKE UPPER('%"+tableName.replaceAll("_", "/_").replaceAll("%","/%")+"%') escape '/' ");
	       }
	       Object mappTableNameObj = map.get("mappTableName");
	       if(mappTableNameObj!=null){
	    	   mappTableName = mappTableNameObj.toString(); 
	    	   sb.append(" AND T.INT_TAB_NAME LIKE UPPER('%"+mappTableName.replaceAll("_", "/_").replaceAll("%","/%")+"%') escape '/' ");
	       }
	       Object srcTableNameObj = map.get("srcTableName");
	       if(srcTableNameObj!=null){
	    	   srcTableName = srcTableNameObj.toString(); 
	    	   sb.append(" AND T.SRC_TAB_NAME LIKE UPPER('%"+srcTableName.replaceAll("_", "/_").replaceAll("%","/%")+"%') escape '/' ");
	       }
	   }
	   sb.append(" ORDER BY T.DIM_TABLE_ID");
	   String sql = sb.toString();
	   if(page!=null){
		     sql= SqlUtils.wrapPagingSql(sql, page);
	   }
	   return this.getDataAccess().queryForList(sql);
   }
   
     /**
	 * 查询应用系统
	 * */
	public List<Map<String,Object>> querySystemData(){
		String sql ="SELECT S.SYS_ID,S.SYS_NAME FROM META_SYS S";
		return this.getDataAccess().queryForList(sql);
	}
	/**
	 * 查询数据源
	 * */
	public List<Map<String,Object>> queryDataSource(int sysId){
		String sql = null;
		if(sysId==0){
			sql ="SELECT DS.DATA_SOURCE_ID,DS.DATA_SOURCE_NAME FROM META_DATA_SOURCE DS";
			return this.getDataAccess().queryForList(sql);
		}
		if(sysId>0){ 
			sql ="SELECT DS.DATA_SOURCE_ID,DS.DATA_SOURCE_NAME FROM META_DATA_SOURCE DS WHERE DS.SYS_ID=?";
			return this.getDataAccess().queryForList(sql,sysId);
		}
		return null;
	}
	/**
	 * 查询接口表的数据
	 * */
	public List<Map<String,Object>> queryIntTableNameData(int dsId){
		String sql = null;
		if(dsId>0||dsId==0){
		 sql="SELECT T.TABLE_ID,T.TABLE_NAME FROM META_TABLES T WHERE T.TABLE_STATE=1 AND T.DATA_SOURCE_ID=? AND T.TABLE_TYPE_ID="+TblConstant.META_TABLE_TYPE_ID_INT+
		     " AND T.TABLE_ID NOT IN(SELECT RT.INT_TAB_ID FROM META_DIM_TAB_INT_REL RT)";
		 return this.getDataAccess().queryForList(sql,dsId);
		}
		if(dsId==-1){
			 sql="SELECT T.TABLE_ID,T.TABLE_NAME FROM META_TABLES T WHERE T.TABLE_STATE=1 AND T.TABLE_TYPE_ID="+TblConstant.META_TABLE_TYPE_ID_INT;
			 //"AND T.TABLE_ID NOT IN(SELECT RT.INT_TAB_ID FROM META_DIM_TAB_INT_REL RT)";
			 return this.getDataAccess().queryForList(sql);
		}
		return null;
	}
	/**
	 * 修改查询接口表的数据,排除自己
	 * @param dsId 数据源id
	 * @param intabId 当前接口表的id
	 * */
	public List<Map<String,Object>> queryIntTableNameDataByUpdate(int dsId,int intabId){
		String sql = null;
		if(dsId>0||dsId==0){
		 sql="SELECT T.TABLE_ID,T.TABLE_NAME FROM META_TABLES T WHERE T.TABLE_STATE=1 AND T.DATA_SOURCE_ID=? AND T.TABLE_TYPE_ID="+TblConstant.META_TABLE_TYPE_ID_INT+
		     " AND T.TABLE_ID NOT IN(SELECT RT.INT_TAB_ID FROM META_DIM_TAB_INT_REL RT AND RT.INT_TAB_ID<>?)";
		 return this.getDataAccess().queryForList(sql,dsId,intabId);
		}
		if(dsId==-1){
			 sql="SELECT T.TABLE_ID,T.TABLE_NAME FROM META_TABLES T WHERE T.TABLE_STATE=1 AND T.TABLE_TYPE_ID="+TblConstant.META_TABLE_TYPE_ID_INT+
			 " AND T.TABLE_ID NOT IN(SELECT RT.INT_TAB_ID FROM META_DIM_TAB_INT_REL RT AND RT.INT_TAB_ID<>?)";
			 return this.getDataAccess().queryForList(sql,intabId);
		}
		return null;
	}
	/**
	 * 查询应用维度表
	 * */
	public List<Map<String,Object>> queryDimTableData(int dsId){
		String sql = null;
		if(dsId==-1){
			   sql ="SELECT T.TABLE_NAME,MT.TABLE_NAME_CN,T.DIM_TABLE_ID FROM META_DIM_TABLES T";
		       sql+=" LEFT JOIN META_TABLES MT ON MT.TABLE_ID=T.DIM_TABLE_ID";
		       sql+=" WHERE MT.TABLE_STATE=1 AND MT.TABLE_TYPE_ID = "+TblConstant.META_TABLE_TYPE_ID_DIM;
		       //sql+=" AND T.DIM_TABLE_ID NOT IN(SELECT RT.DIM_TABLE_ID FROM META_DIM_TAB_INT_REL RT)";
		       return this.getDataAccess().queryForList(sql);
		}
		if(dsId>0||dsId==0){
		       sql ="SELECT T.TABLE_NAME,MT.TABLE_NAME_CN,T.DIM_TABLE_ID FROM META_DIM_TABLES T";
		       sql+=" LEFT JOIN META_TABLES MT ON MT.TABLE_ID=T.DIM_TABLE_ID";
		       sql+=" WHERE MT.TABLE_STATE=1 AND MT.DATA_SOURCE_ID =? AND MT.TABLE_TYPE_ID = "+TblConstant.META_TABLE_TYPE_ID_DIM;
		       sql+=" AND T.DIM_TABLE_ID NOT IN(SELECT RT.DIM_TABLE_ID FROM META_DIM_TAB_INT_REL RT)";
		      return this.getDataAccess().queryForList(sql,dsId);
		}
		return null;
	}
	
	/**
	 * 更新
	 * @throws Exception 
	 * */
	public int updateData(int sysId,int dataSouceId,int tableId,int intTableId,String intTableName,String srcTableName,String mappMask,String mappSql,int oldSysId,int oldDataSourceId,int oldTableId,int userId) throws Exception{
		String sql = "UPDATE META_DIM_TAB_INT_REL SET SYS_ID="+sysId+",DATA_SOURCE_ID="+dataSouceId+",DIM_TABLE_ID="+tableId;
		       sql+= ",SRC_TAB_NAME='"+srcTableName+"',DATA_MAPP_SQL='"+mappSql+"',DATA_MAPP_MARK='"+mappMask+"',INT_TAB_NAME='"+intTableName+"',";
		       sql+="INT_TAB_ID="+intTableId+" WHERE SYS_ID="+oldSysId+" AND DIM_TABLE_ID="+oldTableId+" AND DATA_SOURCE_ID ="+oldDataSourceId+" AND USER_ID="+userId;
		return this.getDataAccess().execUpdate(sql);
	}
	/**
	 * 新增记录,如果原先数据库已经存在该条数据,则提示不用新增,不存在则新增
	 * @param sysId 源系统的id
	 * @param dataSouceId 数据源id
	 * @param tableId 元数据维度表类id
	 * @param userId 当前登录的用户id
	 * @param intTableId 接口表类Id
	 * @param intTableName 接口表类名称
	 * @param srcTabName 源系统表名称
	 * @param mappMask 映射规则
	 * @param mappSql 映射sql
	 * @throws Exception 
	 * */
	public int insertData(int sysId,int dataSouceId,int tableId,int userId,int intTableId,String intTableName,String srcTabName,String mappMask,String mappSql) throws Exception{
		int count =-1 ;
		int insertCount = 0;
		String querySql="SELECT COUNT(INT_TAB_ID) FROM META_DIM_TAB_INT_REL R WHERE R.SYS_ID=? AND R.DATA_SOURCE_ID=? AND R.DIM_TABLE_ID=? AND R.INT_TAB_ID=?";
		Object[] param = {sysId, dataSouceId, tableId, intTableId};
		count = this.getDataAccess().queryForInt(querySql,param);
		if(count==0){
			String sql = "INSERT INTO META_DIM_TAB_INT_REL(SYS_ID,DATA_SOURCE_ID,DIM_TABLE_ID,INT_TAB_ID,INT_TAB_NAME,SRC_TAB_NAME,USER_ID,DATA_MAPP_SQL,DATA_MAPP_MARK) VALUES(?,?,?,?,?,?,?,?,?)";
			Object[] params = {sysId, dataSouceId, tableId, intTableId, intTableName, srcTabName, userId, mappSql, mappMask};
			insertCount = this.getDataAccess().execUpdate(sql,params);
		}else{
			insertCount = -1;
		}
		return insertCount;
	}
	/**
	 * 查询当前接口表是否已经被关联了具体的维度表
	 * @param intTableId 选择的接口表的id
	 * */
	public int queryTableIntId(int intTableId){
		try{
		   String sql ="SELECT T.INT_TAB_ID FROM META_DIM_TAB_INT_REL T WHERE T.INT_TAB_ID=?";
		  return this.getDataAccess().queryForInt(sql,intTableId);
		}catch(Exception e){
			return -1;
		}
	}
	/**
	 * 删除
	 * @throws Exception 
	 * **/
	public int deleteData(int sysId,int dataSouceId,int tableId,int userId) throws Exception{
		String sql="DELETE META_DIM_TAB_INT_REL T WHERE T.SYS_ID="+sysId+" AND T.DATA_SOURCE_ID="+dataSouceId+" AND T.DIM_TABLE_ID="+tableId+" AND T.USER_ID="+userId;
		return this.getDataAccess().execUpdate(sql);
	}
}
