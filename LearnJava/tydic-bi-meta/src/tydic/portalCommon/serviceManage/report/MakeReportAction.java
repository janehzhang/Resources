package tydic.portalCommon.serviceManage.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.Page;
import tydic.meta.module.mag.role.RoleDAO;
import tydic.meta.web.session.SessionManager;
import tydic.portalCommon.util.IUnionWorkItemService;
import tydic.portalCommon.util.UnionWorkItemService;
import tydic.portalCommon.util.WorkItemBean;
import tydic.portalCommon.util.WorkItemsBean;

/**
 * 
 * @author yhd
 * 
 */
public class MakeReportAction {

	private MakeReportDAO makeReportDAO;
	
	private RoleDAO roleDAO;
	
    private WriteReportDAO writeReportDAO;

	/**
	 * 查询
	 * @param queryData
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryReports(Map<String , Object> queryData, Page page) {
		if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		}
	    int userId = SessionManager.getCurrentUserID();
		queryData.put("createId", userId);
		return makeReportDAO.queryReports(queryData, page);
	}
	
	/**
	 * 新增
	 * @param map
	 * @return
	 */
	public boolean addReport(Map<String,Object> map){
		 long keyId=makeReportDAO.queryForNextVal("seq_app_id");
		 String GUID=UUID.randomUUID().toString();
		 int userId = SessionManager.getCurrentUserID();
	     String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	     map.put("keyId", keyId);
	     map.put("createId", userId);
	     map.put("createName", userName);
	     map.put("GUID",GUID);
	     map.put("status","0");//草稿
	     return makeReportDAO.insertReport(map);
	}	
    /**
     * 发布
     * @param id
     * @return
     */
    public boolean publishReport(Map<String,Object> map){
    	 boolean flag=false;
    	 long keyId;
    	 String GUID="";
    	 String title="";
    	 String monthId="";
		 int userId = SessionManager.getCurrentUserID();
	     String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
    	 Object id = map.get("id");	 
   try{ 
        BaseDAO.beginTransaction();
    	if(id != null && !"".equals(id)){	 
    		  keyId=Long.parseLong(id.toString());
    		  Map<String, Object> object=makeReportDAO.getMakeReportById(keyId);
    		  GUID=MapUtils.getString(object, "GUID", null);
    		  title=MapUtils.getString(object, "TITLE", null);
    		  monthId=MapUtils.getString(object, "MONTHID", null);
    	}else{
    		 keyId=makeReportDAO.queryForNextVal("seq_app_id");
   		     GUID=UUID.randomUUID().toString();
   		     title=MapUtils.getString(map, "title", null);
   		     monthId=DateUtil.getDateforStampMon(MapUtils.getString(map, "monthId", null));
    	}
	     map.put("keyId", keyId);
	     map.put("createId", userId);
	     map.put("createName", userName);
	     map.put("GUID",GUID);
	     map.put("status","1");//发布

	    //1.新增主表信息
	    	if(id != null && !"".equals(id)){	 
	    		 makeReportDAO.updateReport(map);//修改
	    	}else{
	    		 makeReportDAO.insertReport(map); //新增
	    	}  
	     //2.新增副表信息
	       Map<String,Object> writeMap =new HashMap<String ,Object>();  
	       writeMap.put("makeId", keyId);
	       writeMap.put("makeUserId", userId);
	       writeMap.put("makeUserName", userName);
	       writeMap.put("title", title);
	       writeMap.put("monthId", monthId);
	       /**
	        * 统一待办
	        */
	        WorkItemsBean bean=new WorkItemsBean();
	   	    bean.setRequestType("1");//新建提交
	   	    bean.setAppProcInstID(GUID);
	   	    bean.setTitle(title);
	   	    bean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
	   	    bean.setInitiator(userName);
	   	    bean.setInitiatorName(userName);
	       
	        //2.1 角色下面人员-各分公司客服部填写上报 roleId:213523
	        List<Map<String, Object>> userList=roleDAO.queryUserByRoleId("213523");
		       for(Map<String, Object> obj :userList){
		    	   String createId=    obj.get("USER_ID").toString();
		    	   String createName=  obj.get("USER_NAMECN").toString();
		    	   writeMap.put("createId", createId);
		    	   writeMap.put("createName", createName);
		    	   writeReportDAO.insertReport(writeMap);
	                /**
	                 * 统一待办
	                 */
		    	    WorkItemBean workItem =new  WorkItemBean();
			   	    workItem.setAppWorkQueueID(createName+"_"+GUID);
			   	    workItem.setCurrActivity("待上报");
			  	    workItem.setPendingItemURL("http://132.121.165.45:8081/tydic-bi-meta/portal/login_sso.jsp");
			   	    workItem.setExecutor(createName);
			   	    workItem.setExecutorName(createName);
			   	    bean.addWorkItems(workItem);
		      }
		    //3.统一待办流程提交     
		       IUnionWorkItemService uwis=new UnionWorkItemService();
		       uwis.createWorkItems(bean);
	           BaseDAO.commit();
	           flag=true;
      }catch(Exception e){
         Log.error("",e);
         BaseDAO.rollback();
         flag=false;
      }     
    	 return flag;
   }
    
	/**
     * 删除
     * @param idStr
     * @return
     */
  public boolean deleteReport(String idStr) {
		boolean flag = false;
		try {
			BaseDAO.beginTransaction();
			 if (idStr != null &&  !"".equals(idStr)) {
				 String[] idStrs = idStr.split(",");
				 for(String makeId: idStrs){
			       //1.删除副表信息
					 writeReportDAO.deleteReportByMakeId(makeId);
				   //2.删除主表信息
					 makeReportDAO.deleteReport(makeId);
				 }
			 }	
			BaseDAO.commit();
			flag = true;
		} catch (Exception e) {
			Log.error("", e);
			BaseDAO.rollback();
			flag = false;
		}
		return flag;
	}
    
  
  
	public void setMakeReportDAO(MakeReportDAO makeReportDAO) {
		this.makeReportDAO = makeReportDAO;
	}
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public void setWriteReportDAO(WriteReportDAO writeReportDAO) {
		this.writeReportDAO = writeReportDAO;
	}
    
}
