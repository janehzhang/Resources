package tydic.meta.module.dim.intRelManger;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;

public class DimIntRelMangerAction {
  
	private DimIntRelMangerDAO mangerDAO;
    
	/**
	 * 按条件查询加载表格数据
	 * @param map 包含了输入的表类名称tableName,输入的接口表的名称mappTableName,输入的源系统表名srcTableName
	 * @return 接口表中的需要显示的数据
	 * */
	public List<Map<String,Object>> queryDataByCondition(Map<String,Object>map,Page page){
		if(page==null){
			page = new Page(0,20);
		}
		return mangerDAO.queryDataByCondition(map, page);
	}
	/**
	 * 查询应用系统
	 * */
	public List<Map<String,Object>> querySystemData(){
		return mangerDAO.querySystemData();
		
	}
	/**
	 * 查询数据源
	 * */
	public List<Map<String,Object>> queryDataSource(int sysId){
		return mangerDAO.queryDataSource(sysId);
		
	}
	/**
	 * 查询接口表的数据
	 * */
	public List<Map<String,Object>> queryIntTableNameData(int dsId){
		return mangerDAO.queryIntTableNameData(dsId);
	}
	/**
	 * 修改时加载接口维度表数据,加载自己和同一个系统的同一个数据源下的维度接口表 queryIntTableNameDataByUpdate
	 * */
	public List<Map<String,Object>> queryIntTableNameDataByUpdate(int dsId,int intabId){
		return mangerDAO.queryIntTableNameDataByUpdate(dsId,intabId);
	}
	/**
	 * 查询应用维度表
	 * */
	public List<Map<String,Object>> queryDimTableData(int dsId){
		return mangerDAO.queryDimTableData(dsId);
		
	}
	/**
	 * 修改数据
	 * @throws Exception 
	 * */
	public int updateData(Map<String,Object>map) throws Exception{
		int sysId = 0 ;
		int dataSouceId = 0 ;
		int tableId = 0 ;
		int intTableId = 0 ;
		String intTableName = null;
		String srcTableName = null;
		String mappMask = null;
		String mappSql = null;
		int oldSysId=0;
		int oldDataSourceId = 0;
		int oldTableId = 0;
		int userId = 0 ; 
		try{
		BaseDAO.beginTransaction();
		int count = -1 ;
		if(map!=null&&map.size()!=0){
			Object sysIdObj = map.get("sysId");
			if(sysIdObj!=null){
				sysId = Integer.parseInt(sysIdObj.toString());
			}
			Object dataSouceIdObj = map.get("dataId");
			if(dataSouceIdObj!=null){
				dataSouceId = Integer.parseInt(dataSouceIdObj.toString());
			}
			Object tableIdObj = map.get("tableId");
			if(tableIdObj!=null){
				tableId = Integer.parseInt(tableIdObj.toString());
			}
			Object intTableIdObj = map.get("intTableId");
			if(intTableIdObj!=null){
				intTableId = Integer.parseInt(intTableIdObj.toString());
			}
			Object intTableNameObj = map.get("intTableName");
			if(intTableNameObj!=null){
				intTableName = intTableNameObj.toString();
			}
			Object srcTableNameObj = map.get("srcTableName");
			if(srcTableNameObj!=null){
				srcTableName = srcTableNameObj.toString();
			}
			Object mappMaskObj = map.get("mappMark");
			if(mappMaskObj!=null){
				mappMask = mappMaskObj.toString();
			}
			Object mappSqlObj = map.get("mappSql");
			if(mappSqlObj!=null){
				mappSql = mappSqlObj.toString();
			}
			Object OldSysIdObj = map.get("oldSysId");
			if(OldSysIdObj!=null){
				oldSysId = Integer.parseInt(OldSysIdObj.toString());
			}
			Object oldDataSourceIdObj = map.get("oldDataSourceId");
			if(oldDataSourceIdObj!=null){
				oldDataSourceId = Integer.parseInt(oldDataSourceIdObj.toString());
			}
			Object oldTableIdObj = map.get("oldTableId");
			if(oldTableIdObj!=null){
				oldTableId = Integer.parseInt(oldTableIdObj.toString());
			}
			Object userIdObj = map.get("userId");
			if(userIdObj!=null){
				userId = Integer.parseInt(userIdObj.toString());
			}			
		}
		  count = mangerDAO.updateData(sysId,dataSouceId,tableId,intTableId,intTableName,srcTableName,mappMask,mappSql,oldSysId,oldDataSourceId,oldTableId,userId);
		  BaseDAO.commit();
		  return count;
	   }catch(Exception e){
           Log.error(null, e);
           BaseDAO.rollback();
           return -1;
       }
		
	}
	/**
	 * 删除数据
	 * @throws Exception 
	 * */
	public int deleteData(Map<String,Object>map) throws Exception{
		int sysId = 0 ;
		int dataSouceId = 0 ;
		int tableId = 0 ;
		int userId = 0 ;
		int count = 0 ;
		try{
	    BaseDAO.beginTransaction();
		if(map!=null&&map.size()!=0){
			Object sysIdObj = map.get("sysId");
			if(sysIdObj!=null){
				sysId = Integer.parseInt(sysIdObj.toString());
			}
			Object dataSouceIdObj = map.get("dataId");
			if(dataSouceIdObj!=null){
				dataSouceId = Integer.parseInt(dataSouceIdObj.toString());
			}
			Object tableIdObj = map.get("tableId");
			if(tableIdObj!=null){
				tableId = Integer.parseInt(tableIdObj.toString());
			}
			Object userIdObj = map.get("userId");
			if(userIdObj!=null){
				userId = Integer.parseInt(userIdObj.toString());
			}
		}
		count =  mangerDAO.deleteData(sysId,dataSouceId,tableId,userId);
		 BaseDAO.commit();
		  return count;
	   }catch(Exception e){
           Log.error(null, e);
           BaseDAO.rollback();
           return -1;
       }
	}
	/**
	 * 添加数据
	 * @throws Exception 
	 * */
	public int insertData(Map<String,Object>map) throws Exception{
		int sysId = 0 ;
		int dataSouceId = 0 ;
		int tableId = 0 ;
		int userId = 0 ; 
		String intTableName = null;
		String mappMask = null;
		String mappSql = null;
		int intTableId = 0;
		String srcTableName =null;
		int  count = -1;
		try{
			BaseDAO.beginTransaction();
		if(map!=null&&map.size()!=0){
			Object sysIdObj = map.get("sysId");
			if(sysIdObj!=null){
				sysId = Integer.parseInt(sysIdObj.toString());
			}
			Object dataSouceIdObj = map.get("dataId").toString().equals("")?0:map.get("dataId");
			if(dataSouceIdObj!=null){
				dataSouceId = Integer.parseInt(dataSouceIdObj.toString());
			}
			Object tableIdObj = map.get("tableId");
			if(tableIdObj!=null){
				tableId = Integer.parseInt(tableIdObj.toString());
			}
			Object userIdObj = map.get("userId");
			if(userIdObj!=null){
				userId = Integer.parseInt(userIdObj.toString());
			}
			Object intTableIdObj = map.get("intTableId");
			if(intTableIdObj!=null){
				intTableId = Integer.parseInt(intTableIdObj.toString());
				int intTableUsed = mangerDAO.queryTableIntId(intTableId);
				if(intTableUsed!=-1){
					return -2;
				}
			}
			Object mappMaskObj = map.get("mappMark");
			if(mappMaskObj!=null){
				mappMask = mappMaskObj.toString();
			}
			Object mappSqlObj = map.get("mappSql");
			if(mappSqlObj!=null){
				mappSql = mappSqlObj.toString();
			}
			Object srcTableNameObj = map.get("srcTableName");
			if(srcTableNameObj!=null){
				srcTableName = srcTableNameObj.toString();
			}
			Object intTableNameObj = map.get("intTableName");
			if(intTableNameObj!=null){
				intTableName = intTableNameObj.toString();
			}
		}
		 count =  mangerDAO.insertData(sysId,dataSouceId,tableId,userId,intTableId,intTableName,srcTableName,mappMask,mappSql);
		 BaseDAO.commit();
		  return count;
	   }catch(Exception e){
          Log.error(null, e);
          BaseDAO.rollback();
          return -1;
      }
	}
	public void setMangerDAO(DimIntRelMangerDAO mangerDAO) {
		this.mangerDAO = mangerDAO;
	}
}
