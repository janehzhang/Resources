package tydic.meta.module.dim.dimtype;

import tydic.frame.BaseDAO;
import tydic.frame.DataSourceManager;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.module.tbl.MetaDimTypeDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 维度归并类型的action
 * @author 王晶
 * */
public class DimTypeAction {

	private DimTypeDAO dimTypeDAO;

    private MetaDimTypeDAO metaDimTypeDAO;
	/**
	 * 查询所有的维表
	 * 
	 * */
	public List<Map<String,Object>> queryDimTable(){
		return dimTypeDAO.queryDimTable();
	}
	/**
	 * 查询维度表内的归并类型
	 * @param tableId 表的id
	 * @return Map
	 * */
	public List<Map<String,Object>> queryDimTypeByTableId(int tableId){
		return dimTypeDAO.queryDimTypeByTableId(tableId);
	}
	/**
	 * 查询归并类型的层次关系
	 * @param typeId 归并类型的id
	 * */
	public List<Map<String,Object>> queryDimLevelByTypeId(Map<String,Object> map){
		int tableId = 0;
		int typeId = 0;
		if(map!=null){
			tableId = Integer.valueOf(map.get("tableId").toString());
			typeId = Integer.valueOf(map.get("typeId").toString());
		}
		return dimTypeDAO.queryDimLevelByTypeId(tableId,typeId);
	}
	
	/**
	 * 更新归并类型的状态
	 * @param tableId 表类的id
	 * @param typeId 归并类型的id
	 * */
	public boolean updateDimTypeState(Map<String,Object> map){
		int tableId = 0;
		int typeId = 0;
		int flag =-1;
		BaseDAO.beginTransaction();
		try{
			if(map!=null){
				tableId = Integer.valueOf(map.get("tableId").toString());
				typeId = Integer.valueOf(map.get("typeId").toString());
				flag = Integer.valueOf(map.get("flag").toString());
				dimTypeDAO.updateDimTypeState(tableId, typeId,flag);
				BaseDAO.commit();
		        return true;
			}
		}catch(Exception e){
            Log.error(null,e);
            BaseDAO.rollback();
        }finally{
            DataSourceManager.destroy();
        }
		return false;
	}
	/**
	 * 保存的处理方法(包括对表中归并类型的添加 修改,对层级的添加和修改)
	 * @param map<String,List> key为操作的类型,如:添加层级(这里包括了添加和修改归并类型,添加和修改层级)
	 * @throws Exception 
	 * */
	public Map<String,Object> saveData(Map<String,Object> map) throws Exception{
		Map<String,Object> resMap = null;
		if(map!=null&&map.size()!=0){
			resMap = new HashMap<String,Object>();
		    if(map.containsKey("addType")){
				Object addTypeListObj = map.get("addType");
				if(addTypeListObj!=null){
					List<Map<String,Object>> addTypeList = (List<Map<String, Object>>) addTypeListObj;
					int tableId = 0;
					String typeName =null;
					String typeDesc =null;
					if(addTypeList!=null&&addTypeList.size()!=0){
						for(int i = 0;i<addTypeList.size();i++){
							  Map<String,Object> dataMap = addTypeList.get(i);
								  if(dataMap!=null){
								  tableId  = Integer.parseInt(dataMap.get("dimTableId").toString());
								  typeName = Convert.toString(dataMap.get("dimTypeName"));
								  if(typeName!=null&&typeName!=""&&!typeName.equalsIgnoreCase("")){
									  long typeId = metaDimTypeDAO.insertDimType(dataMap);
									  if(typeId!=0){
										  List<Map<String,Object>> levelLsit = (List<Map<String, Object>>) dataMap.get("level");
										  if(levelLsit!=null&&levelLsit.size()!=0){
											  for(int j = 0;j<levelLsit.size();j++){
												  if(levelLsit.get(j)!=null){
													  String levelName = Convert.toString(levelLsit.get(j).get("levelName"));
													  if(levelName!=null&&levelName!=""&&!levelName.equalsIgnoreCase("")){
														  boolean b = dimTypeDAO.insertDimTypeLevel(tableId,typeId,levelName);
														  if(b==false){
															  resMap.put("addType", false);
														  }
												      }  
												  }
											 }
										  }
									  }else{
										  resMap.put("addType", true);
									  }
								  }
							}
					}
				  }
				}
		    }//end addtype
		    	if(map.containsKey("updateType")){
					Object updateTypeListObj = map.get("updateType");
					if(updateTypeListObj!=null){
						List<Map<String,Object>> updateTypeList = (List<Map<String, Object>>) updateTypeListObj;
						int tableId = 0;
						int typeId = 0;
						String typeName =null;
						String typeDesc =null;
						if(updateTypeList!=null&&updateTypeList.size()!=0){
							for(int i = 0;i<updateTypeList.size();i++){
								  Map<String,Object> dataMap = updateTypeList.get(i);
								  tableId  = Integer.parseInt(dataMap.get("dimTableId").toString());
								  typeId  = Integer.parseInt(dataMap.get("typeId").toString());
								  typeName = Convert.toString(dataMap.get("dimTypeName")).trim();
								  typeDesc = Convert.toString(dataMap.get("dimTypeDesc")).trim();
								  if(typeName!=null&&typeName!=""&&!typeName.equalsIgnoreCase("")){
									  int count = dimTypeDAO.updateDimType(tableId,typeId,typeName, typeDesc);
									  if(count==1){
										  List<Map<String,Object>> levelLsit = (List<Map<String, Object>>) dataMap.get("level");
										  dimTypeDAO.deleteDimLevel(tableId, typeId);
										  for(int j = 0;j<levelLsit.size();j++){
										    if(levelLsit.get(j)!=null){
										    	  if(levelLsit.get(j).get("levelId")!=null){
										    		  int levelId =Integer.parseInt(levelLsit.get(j).get("levelId").toString());
										    	  }
												  String levelName = Convert.toString(levelLsit.get(j).get("levelName")).trim();
												  if(levelName!=null&&levelName!=""&&!levelName.equalsIgnoreCase("")){
													 boolean b = dimTypeDAO.insertDimTypeLevel(tableId, typeId, levelName.trim());
													  if(b==false){
														  resMap.put("updateType", false);
													  }
												  }
											  }
										   }
									  }else{
										  resMap.put("updateType", true);
									  }
								  }
							}
						}
					}
			    }//end addtype
//		    if(map.containsKey("updateLevel")){
//		    	Object updateLevelListObj = map.get("updateLevel");
//				if(updateLevelListObj!=null){
//					List<Map<String,Object>> updateLevelList = (List<Map<String, Object>>) updateLevelListObj;
//					if(updateLevelList!=null&&updateLevelList.size()!=0){
//						int updateLevel = dimTypeDAO.updateDimLevel(updateLevelList);
//						resMap.put("updateLevel",updateLevel);
//					}
//				}
//		   }//end updateType
		}//end map
		return resMap;
	}

    /**
     * 查询一个维度表类的所有归并类型，并关联其层级信息，以字符串“--”连接
     * @author 张伟
     * @param tableId
     * @return
     */
    public List<Map<String,Object>>  queryTypeInfos(int tableId){
       return metaDimTypeDAO.queryDimTypeAndLevelByTableId(tableId);
    }

	public void setDimTypeDAO(DimTypeDAO dimTypeDAO) {
		this.dimTypeDAO = dimTypeDAO;
	}

    public void setMetaDimTypeDAO(MetaDimTypeDAO metaDimTypeDAO){
        this.metaDimTypeDAO = metaDimTypeDAO;
    }
}
