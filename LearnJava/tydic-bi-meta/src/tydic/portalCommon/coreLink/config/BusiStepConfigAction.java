package tydic.portalCommon.coreLink.config;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;

/**
 * 
 * @author yanhd
 * 
 */
public class BusiStepConfigAction {
	
	private BusiStepConfigDAO busiStepConfigDAO;

	public Map<String,Object>  getInitDataList(String linkName) {
		return busiStepConfigDAO.getInitDataList(linkName);
	}
	
	public List<Map<String, Object>>  getLeaf1DataByParam(){
		return busiStepConfigDAO.getLeaf1DataByParam(null);
	}
	
	public List<Map<String, Object>>  findSonNote(String param){
	   return busiStepConfigDAO.getLeafNDataByParam(param);
	}
	//新增
   public boolean saveConfig(Map<String,Object> data){
		try {
			BaseDAO.beginTransaction();
			busiStepConfigDAO.insert(data);
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return false;
		}
		    return true;
	}
   


//新增
   public boolean saveThreeConfig(Map<String,Object> data){
		try {
			BaseDAO.beginTransaction();
			busiStepConfigDAO.insertThree(data);
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return false;
		}
		    return true;
	}
   //修改权重
  public boolean updateWeight(Map<String,Object> queryData){
	  try {
			BaseDAO.beginTransaction();
			busiStepConfigDAO.updateWeight(queryData);
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return false;
		}
		    return true;
  }
  //修改比重
  public boolean updatePercentate(Map<String,Object> queryData){
	  try {
			BaseDAO.beginTransaction();
			busiStepConfigDAO.updatePercentate(queryData);
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return false;
		}
		    return true;
  }
  
  //修改指向
  public boolean updateDirection(Map<String,Object> queryData){
	  try {
			BaseDAO.beginTransaction();
			busiStepConfigDAO.updateDirection(queryData);
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return false;
		}
		    return true;
  }
  
//删除  
public boolean deleteConfig(String id){
	  try {
			BaseDAO.beginTransaction();
			busiStepConfigDAO.deleteConfig(id);
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return false;
		}
		    return true;
  }



public void setBusiStepConfigDAO(BusiStepConfigDAO busiStepConfigDAO) {
		this.busiStepConfigDAO = busiStepConfigDAO;
	}

}
