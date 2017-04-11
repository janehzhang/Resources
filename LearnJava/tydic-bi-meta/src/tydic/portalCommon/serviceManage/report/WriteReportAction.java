package tydic.portalCommon.serviceManage.report;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.Page;
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
public class WriteReportAction {

	
	 private WriteReportDAO  writeReportDAO;
	 
    private MakeReportDAO makeReportDAO;

	/**
	 * 查询
	 * @param queryData
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryWriteReports(Map<String, Object> queryData, Page page) {
		if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		}
	     int userId = SessionManager.getCurrentUserID();
	     String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	     queryData.put("createId", userId);
	     queryData.put("createName", userName);
		 return writeReportDAO.queryWriteReports(queryData , page);
	}
    public List<Map<String, Object>> queryMakeReports(Page page) {
		if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		}
		return writeReportDAO.queryMakeReports(page);
	}
	
    public Map<String,Object> getMakeReportById(String id){
		return writeReportDAO.getMakeReportById(id);
	}
	
    /**
	 * 填写报告
	 * @param map
	 * @return
	 */
	public boolean fillReport(Map<String, Object> map) {
		boolean flag = false;
	    IUnionWorkItemService uwis=new UnionWorkItemService();
	    String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
		String END_GUID="";
		String createName="";
		String title=MapUtils.getString(map,"title",null);
		String GUID=UUID.randomUUID().toString();
	    long makeId=MapUtils.getLongValue(map, "makeId", 0);
		map.put("status", "1");
		map.put("GUID", GUID);
		try {
			BaseDAO.beginTransaction();
			Map<String, Object> object=makeReportDAO.getMakeReportById(makeId);
			END_GUID=  MapUtils.getString(object, "GUID", null);
			createName=MapUtils.getString(object, "CREATENAME", null);
			
			//1.更新主表
			writeReportDAO.updateWriteReport(map);
			//2.结束流程
            WorkItemsBean bean=new WorkItemsBean();
    	    bean.setRequestType("1");//
    	    bean.setAppProcInstID(END_GUID);
    	    bean.setAppWorkQueueID(userName+"_"+END_GUID);
    	    bean.setTitle(title);
    	    bean.setExecutor(userName);
    	    bean.setExecutorName(userName);
		    uwis.deleteWorkItem(bean);
			
			//2.发起流程
	        WorkItemsBean workItemsBean=new WorkItemsBean();
	        workItemsBean.setRequestType("1");//新建提交
	        workItemsBean.setAppProcInstID(GUID);
	        workItemsBean.setTitle(title);
	        workItemsBean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
	        workItemsBean.setInitiator(userName);
	        workItemsBean.setInitiatorName(userName);
            /**
             * 统一待办
             */
    	    WorkItemBean workItem =new  WorkItemBean();
	   	    workItem.setAppWorkQueueID(createName+"_"+GUID);
	   	    workItem.setCurrActivity("待审核");
	  	    workItem.setPendingItemURL("http://132.121.165.45:8081/tydic-bi-meta/portal/login_sso.jsp");
	   	    workItem.setExecutor(createName);
	   	    workItem.setExecutorName(createName);
	   	    workItemsBean.addWorkItems(workItem);
		    uwis.createWorkItems(workItemsBean);
			BaseDAO.commit();
			flag = true;
		} catch (Exception e) {
			Log.error("", e);
			BaseDAO.rollback();
			flag = false;
		}
		return flag;
	}
	
	/**
     * 删除
     * @param IdStr
     * @return
     */
    public boolean deleteReport(String IdStr){
           return writeReportDAO.deleteReport(IdStr);
    }

    
   /**
    * 待审核列表
    */
	public List<Map<String, Object>> queryVettintReports(Map<String, Object> queryData, Page page) {
		if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		}
		int userId = SessionManager.getCurrentUserID();
		String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
		queryData.put("createId", userId);
		queryData.put("createName", userName);
		return writeReportDAO.queryVettintReports(queryData, page);
	}
	
	/**
	 * 查看审核报告
	 */
	public Map<String, Object> queryMakeReportById(String id, String makeId) {
	     return writeReportDAO.queryMakeReportById(id,makeId);
	}
	
   
   /**
    * 查看报告
    */
   public List<Map<String, Object>>  queryLookReports(Map<String, Object> queryData, Page page) {
		if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		}
		return writeReportDAO.queryLookReports(queryData, page);
	}
   
	
   
   //审核同意
   public boolean agreeReport(Map<String,Object> map){
		boolean flag = false;
		String GUID="";
		String id="";
		String title=MapUtils.getString(map, "title", null);
		IUnionWorkItemService uwis=new UnionWorkItemService();
	    String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	    map.put("status", "2");
		try {
			BaseDAO.beginTransaction();
			id=MapUtils.getString(map, "id", null);
			Map<String, Object> object=writeReportDAO.getWriteReportById(id);
			GUID=  MapUtils.getString(object, "GUID", null);
			//1.更新主表
			writeReportDAO.vettingWriteReport(map);
			//2.删除流程
		    WorkItemsBean bean=new WorkItemsBean();
	        bean.setRequestType("1");//
	        bean.setAppProcInstID(GUID);
	    	bean.setAppWorkQueueID(userName+"_"+GUID);
	        bean.setTitle(title);
	    	bean.setExecutor(userName);
	    	bean.setExecutorName(userName);
			uwis.deleteWorkItem(bean);
			BaseDAO.commit();
			flag = true;
		} catch (Exception e) {
			Log.error("", e);
			BaseDAO.rollback();
			flag = false;
		}
		return flag;	
   }
   
   //审核不同意
   public boolean disAgreeReport(Map<String,Object> map){
		boolean flag = false;
		String GUID="";
		String id="";
		String makeId="";
		String createName="";
		String ADD_GUID="";
		String title=MapUtils.getString(map, "title", null);
		IUnionWorkItemService uwis=new UnionWorkItemService();
	    String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	    map.put("status", "0");
		try {
			BaseDAO.beginTransaction();
			id=MapUtils.getString(map, "id", null);
			Map<String, Object> object=writeReportDAO.getWriteReportById(id);
			GUID=  MapUtils.getString(object, "GUID", null);
			createName=MapUtils.getString(object, "CREATENAME", null);
			//1.更新主表
			writeReportDAO.vettingWriteReport(map);
			//2.删除流程
		    WorkItemsBean bean=new WorkItemsBean();
	        bean.setRequestType("1");//
	        bean.setAppProcInstID(GUID);
	    	bean.setAppWorkQueueID(userName+"_"+GUID);
	        bean.setTitle(title);
	    	bean.setExecutor(userName);
	    	bean.setExecutorName(userName);
			uwis.deleteWorkItem(bean);
			
		    makeId=MapUtils.getString(map, "makeId", null);	
		    Map<String, Object> makeBean=writeReportDAO.getMakeReportById(makeId);
		    ADD_GUID=MapUtils.getString(makeBean, "GUID", null);
			//3.新增流程
		    WorkItemsBean workItemsBean=new WorkItemsBean();
		    workItemsBean.setRequestType("1");//新建提交
		    workItemsBean.setAppProcInstID(ADD_GUID);
		    workItemsBean.setTitle(title);
		    workItemsBean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
		    workItemsBean.setInitiator(userName);
		    workItemsBean.setInitiatorName(userName);
	   	    
	   	    WorkItemBean workItem =new  WorkItemBean();
	   	    workItem.setAppWorkQueueID(createName+"_"+ADD_GUID);
	   	    workItem.setCurrActivity("待上报");
	  	    workItem.setPendingItemURL("http://132.121.165.45:8081/tydic-bi-meta/portal/login_sso.jsp");
	   	    workItem.setExecutor(createName);
	   	    workItem.setExecutorName(createName);
	   	    workItemsBean.addWorkItems(workItem);
	   	    uwis.createWorkItems(workItemsBean);
			
			BaseDAO.commit();
			flag = true;
		} catch (Exception e) {
			Log.error("", e);
			BaseDAO.rollback();
			flag = false;
		}
		return flag;	
  }
   public Map<String,Object> getWriteReportById(String id){
		return writeReportDAO.getWriteReportById(id);
	}
    
	public void setWriteReportDAO(WriteReportDAO writeReportDAO) {
		this.writeReportDAO = writeReportDAO;
	}
	public void setMakeReportDAO(MakeReportDAO makeReportDAO) {
		this.makeReportDAO = makeReportDAO;
	}
	
	
	
}
