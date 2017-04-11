package tydic.meta.module.gdl.examine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.gdl.composite.GdlCompositeMagDAO;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 刘弟伟
 * @description 指标审核Action 
 * @date 12-6-5 -
 * @modify
 * @modifyDate -
 */
public class GdlExamineAction {

	private GdlExamineDAO gdlExamineDAO;
	private GdlCompositeMagDAO gdlCompositeMagDAO;

	

	/**
	 * @Title: getExamineInfo
	 * @Description: 获取指标审核数据
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> getExamineInfo(Map<String, Object> data,
			Page page) {
		return gdlExamineDAO.getExamineInfo(data,page);
	}

	/**
	 * @Title: getExamineInfoByGdlId
	 * @Description: 根据指标ID及版本信息获取一条新增指标审核数据
	 * @return <Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getExamineInfoByGdlId(int gdlId, int gdlVersion) {
		  //Current：表时查询当前版本，供新增审核及查询界面用
		  Map<String,Object> gdlInfo = gdlExamineDAO.getExamineInfoByGdlId(gdlId, gdlVersion,"Current");
		  if(gdlInfo!=null){
	            gdlInfo.put("GDL_GROUPINFO",gdlExamineDAO.getGdlGroupIds(gdlId));
	            gdlInfo.put("SUPPORT_DIMS",gdlExamineDAO.getGdlDims_add(gdlId,gdlVersion));
	        }
	        return gdlInfo;
	}
	/**
	 * @Title: getAlertExamineInfo
	 * @Description: 根据指标ID及版本信息获取指标修改审核信息
	 * @return <Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getAlertExamineInfo(int gdlId, int gdlVersion) {
		//Current：表时查询当前版本，及修改之后版本
		Map<String,Object> gdlInfo = gdlExamineDAO.getExamineInfoByGdlId(gdlId, gdlVersion,"Current");
		  if(gdlInfo!=null){
			 //befor：表时查询之前版本，及修改之前版本--供修改审核界面用 
			  Map<String,Object> gdlbefor =  gdlExamineDAO.getExamineInfoByGdlId(gdlId, gdlVersion,"befor");
			  gdlInfo.put("GDL_ALERT_BOFOR_INFO", gdlbefor);
			  gdlInfo.put("GDL_GROUPINFO",gdlExamineDAO.getGdlGroupIds(gdlId));
	          gdlInfo.put("SUPPORT_DIMS",gdlExamineDAO.getGdlDims_alert(gdlId,gdlVersion,gdlbefor.get("GDL_VERSION").toString()));
	        }
	        return gdlInfo;
	}
	
	/**
	 * @Title: getAlertExamineInfo
	 * @Description: 根据指标ID及版本信息获取指标修改审核查看信息
	 * @return <Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getAlertExamineInfo_View(int gdlId, int gdlVersion) {
		//Current：表时查询当前版本，及修改之后版本
		Map<String,Object> gdlInfo = gdlExamineDAO.getExamineInfoByGdlId(gdlId, gdlVersion,"Current");
		  if(gdlInfo!=null){
			  //befor_fo_view：表时查询之前版本，及修改之前版本--供修改审核查看界面用 
			  Map<String,Object> gdlbefor =  gdlExamineDAO.getExamineInfoByGdlId(gdlId, gdlVersion,"befor_fo_view");
			  gdlInfo.put("GDL_ALERT_BOFOR_INFO", gdlbefor);
			  gdlInfo.put("GDL_GROUPINFO",gdlExamineDAO.getGdlGroupIds(gdlId));
	          gdlInfo.put("SUPPORT_DIMS",gdlExamineDAO.getGdlDims_alert(gdlId,gdlVersion,gdlbefor.get("GDL_VERSION").toString()));
	        }
	        return gdlInfo;
	}
	/**
	 * @Title: examineGdlInfo
	 * @Description: 新增指标审核
	 * @return int
	 * @throws
	 */
	public int addExamine(Map<String, Object> data,List<Map<String, Object>> dimMethods) {
		
		String audit = data.get("audit").toString(); // 审核是否通过
		int flag = 0;
		Object userId = SessionManager.getCurrentUserID();
		Map<String,Object> userInfo = new HashMap<String,Object>();
		userInfo = gdlExamineDAO.getUserInfo(userId.toString());
		try {
			BaseDAO.beginTransaction();
			
			//对指标的分组信息进行更新
			gdlExamineDAO.deleteGdlGroupInfo(data);
			String [] gdlGroupInfo =  data.get("GDL_GROUPINFO").toString().split(",");
			for(int i = 0;i<gdlGroupInfo.length;i++){
				gdlExamineDAO.saveGdlGroupInfo(data.get("gdl_id").toString(),gdlGroupInfo[i]);	
			}
			
			//对指标的维度计算方法进行更新
			gdlExamineDAO.deleteDimGroupMethod(data.get("gdl_id").toString(),data.get("gdl_version").toString());
			for(int i=0;i<dimMethods.size();i++)
			{
				gdlExamineDAO.saveDimGroupMethod(data,dimMethods.get(i));	
			}
			
			if (audit.equals("1")) {  //审核通过
				gdlExamineDAO.updateHistory(data,userInfo,"");
				flag = gdlExamineDAO.examineGdlInfo(data);
			} else {  //审核驳回
				flag = gdlExamineDAO.updateHistory(data,userInfo,"");
			}
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return -1;
		}
		return flag;
	}
	
	/**
	 * @Title: examineGdlInfo
	 * @Description: 修改指标审核
	 * @return int
	 * @throws
	 */
	public int alertExamine(Map<String, Object> data) {
		
		String audit = data.get("audit").toString(); // 审核是否通过
		int flag = 0;
		Object userId = SessionManager.getCurrentUserID();
		Map<String,Object> userInfo = new HashMap<String,Object>();
		userInfo = gdlExamineDAO.getUserInfo(userId.toString());
		try {
			BaseDAO.beginTransaction();
			if ("1".equals(audit)) {  //如果这审核通过
				gdlExamineDAO.setGdlState(data);
				gdlExamineDAO.updateHistory(data,userInfo,"alert");
				flag = gdlExamineDAO.alertExamine(data);
				
				//当基础指标修改审核通过后，对其所有的复合指标的支撑维度进行重新分配
				List<Map<String, Object>> list_child = gdlExamineDAO.getGdl_Child(data);
				if(list_child.size()>0){
					for(int i=0;i<list_child.size();i++){
						gdlExamineDAO.deleteDimGroupMethod(list_child.get(i).get("GDL_ID").toString(),
								                           list_child.get(i).get("GDL_VERSION").toString());
						gdlCompositeMagDAO.extendGdlMethod(MapUtils.getInteger(list_child.get(i), "GDL_ID"),
														   MapUtils.getInteger(list_child.get(i), "GDL_VERSION"),
														   MapUtils.getInteger(data, "gdl_id"),
														   MapUtils.getInteger(data, "gdl_version"));	
					}	
				}
					
			} else {  //审核驳回
				flag = gdlExamineDAO.updateHistory(data,userInfo,"alert");
			}
			BaseDAO.commit();
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error(null, e);
			return -1;
		}
		return flag;
	}


	public GdlExamineDAO getGdlExamineDAO() {
		return gdlExamineDAO;
	}

	public void setGdlExamineDAO(GdlExamineDAO gdlExamineDAO) {
		this.gdlExamineDAO = gdlExamineDAO;
	}
	public GdlCompositeMagDAO getGdlCompositeMagDAO() {
		return gdlCompositeMagDAO;
	}

	public void setGdlCompositeMagDAO(GdlCompositeMagDAO gdlCompositeMagDAO) {
		this.gdlCompositeMagDAO = gdlCompositeMagDAO;
	}
}
