package tydic.portalCommon.serviceManage;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;
import tydic.portalCommon.Pagination;
import tydic.portalCommon.util.IUnionWorkItemService;
import tydic.portalCommon.util.UnionWorkItemService;
import tydic.portalCommon.util.WorkItemBean;
import tydic.portalCommon.util.WorkItemsBean;


/**
 * @description 服务管控模块
 */
public class SerProManageAction {
	private ServiceProblemDAO serviceProblemDAO;
    /**
     * setter
     *
     * @param serviceProblemDAO
     */
    public void setServiceProblemDAO(ServiceProblemDAO serviceProblemDAO) {
        this.serviceProblemDAO = serviceProblemDAO;
    } 
    /**
     * 服务问题单查询
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
    public List<Map<String, Object>> queryProblem(Map<String, Object> queryData, Page page) {
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	queryData.put("userId", userMap.get("userId"));
        if (page == null) {
            page = new Page(0, 20);
        }
        return serviceProblemDAO.queryProblem(queryData,page);
    }
    /**
     * 首页待办
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
    public List<Map<String, Object>> indexToDeal(Map<String, Object> queryData) {
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	queryData.put("userId", userMap.get("userId"));
        return serviceProblemDAO.indexToDeal(queryData);
    }
    /**
     * 首页服务问题单查询
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     * @throws ParseException 
     */
    public Object[] indexQueryProblem(String startDate,String endDate,int currentPage,int dataCounts,int pageCounts,int pageSize) throws ParseException {
    	Object[] res = new Object[3];
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	//queryData.put("userId", userMap.get("userId"));
    	//获取总数
    	int counts = dataCounts;
    	if(dataCounts == 0){
    		counts = serviceProblemDAO.getServiceCount(startDate,endDate);
    		Pagination pagination = new Pagination();
    		pageCounts = pagination.getPagesCount(counts, pageSize);
    	}
    	/*计算开始和结束下标值*/
    	int startSubscript = 0;
    	if(currentPage <= 0)
    		currentPage = 1;
    	if(currentPage == 1){
    		startSubscript = 1;
    	}else
    		startSubscript = (currentPage - 1) * pageSize + 1;
    	int endSubscript = currentPage * pageSize;
    	//数据
    	List<Map<String,Object>> data = serviceProblemDAO.indexQueryProblem(startDate,endDate,startSubscript,endSubscript);
    	//保存到数组中
    	res[0] = counts;//数据总数
    	res[1] = data;//数据
    	res[2] = pageCounts;//总页数
        return res;
    }
    /*
     * 按照id查询服务问题单
     */ 
    public List<Map<String, Object>> detailProblem(String mainProblemId) {
        return serviceProblemDAO.detailProblem(mainProblemId);
    }
    /**
     * 服务问题单评估报告详情查询
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
    public List<Map<String, Object>> queryEvaluationInfoById(Map<String,Object> queryData, Page page) {
        if (page == null) {
            page = new Page(0, 20);
        }
        return serviceProblemDAO.queryEvaluationInfoById(queryData,page);
    }
    /**
     * 新增一条服务问题单
     * @param data
     * @return
     */
    public OprResult<?,?> insertServiceProblem(Map<String,Object> data, Page page){
        OprResult<Integer,Object> result=null;
        String GUID=UUID.randomUUID().toString();
        data.put("GUID", GUID);
        try{
            result = new OprResult<Integer,Object>(null, Integer.parseInt(serviceProblemDAO.insertServiceProblem(data)+""), OprResult.OprResultType.insert);
            //发流程
	        WorkItemsBean bean=new WorkItemsBean();
	   	    bean.setRequestType("1");//新建提交
	   	    bean.setAppProcInstID(GUID);
	   	    bean.setTitle(MapUtils.getString(data,"theme",null));
	   	    bean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
	   	    String createActorName = MapUtils.getString(data,"createActorName", null);
			bean.setInitiator(createActorName);
	   	    bean.setInitiatorName(createActorName);
    	    WorkItemBean workItem =new  WorkItemBean();
    	    String acceptActorName= MapUtils.getString(data,"acceptActorName",null);
	   	    workItem.setAppWorkQueueID(GUID);
	   	    workItem.setCurrActivity("待处理");
	  	    workItem.setPendingItemURL("http://132.121.165.45:8083/tydic-bi-meta/portal/login_sso.jsp");
	   	    workItem.setExecutor(acceptActorName);
	   	    workItem.setExecutorName(acceptActorName);
	   	    bean.addWorkItems(workItem);
	        IUnionWorkItemService uwis=new UnionWorkItemService();
		    uwis.createWorkItems(bean);
            
            
            //查询刚新增的数据
			result.setSuccessData(serviceProblemDAO.queryProblem(data, page));
        }catch (Exception e){
            Log.error("新增系统信息失败", e);
			result = new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
        }
        return result;
    }  
    /**
     * 服务问题单详情查询
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
    public List<Map<String, Object>> queryDetailServiceProblem(
            Map<String, Object> queryData, Page page) {
        if (page == null) {
            page = new Page(0, 20);
        }
        return serviceProblemDAO.queryDetailServiceProblem(queryData,page);
    }
    
    /**
     * 主(副)单处理
     * @param data
     * @return
     */
    public OprResult<?,?> nextDealServiceProblem(Map<String,Object> data, Page page){
        OprResult<Integer,Object> result=null;
          String GUIDDeal=UUID.randomUUID().toString();
          data.put("GUID", GUIDDeal);
          String key =MapUtils.getString(data,"mainProblemId", null);
	      Map<String ,Object> map=	serviceProblemDAO.getMainPromblemById(key);
		  String  GUID=	MapUtils.getString(map,"GUID", null);
		  String  title=MapUtils.getString(map,"THEME");
		  String  userName=MapUtils.getString(data,"dealActorName",null);
          String acceptName="";
          String stateName="";
        try{
        	 String state="";
        	 BaseDAO.beginTransaction();
            result = new OprResult<Integer,Object>(null, Integer.parseInt(serviceProblemDAO.nextDealServiceProblem(data,page)+""), OprResult.OprResultType.insert); 
            //回到主界面
            if (data.get("nextActorName") != null&&!("".equals(data.get("nextActorName")))){
                state="2";//转办后处理单处于处理中
                stateName="待处理";
                acceptName=MapUtils.getString(data,"nextActorName", null);
            }else{
            	if(data.get("evaluationDetail") != null&&!("".equals(data.get("evaluationDetail")))){//待评估
            		state="6";//有评估计划待评估
            		stateName="待评估";
            	}else{
            	   state="3";//处理后处理单处于待确认
            	   stateName="待确认";
            	}
            	acceptName=MapUtils.getString(map,"CREATE_ACTOR_NAME", null);
            }
			serviceProblemDAO.mainServiceProblemState(data,state);	
			//1.结束流程
            WorkItemsBean bean=new WorkItemsBean();
    	    bean.setRequestType("1");//
    	    bean.setAppProcInstID(GUID);
    	    bean.setAppWorkQueueID(GUID);
    	    bean.setTitle(title);
    	    bean.setExecutor(userName);
    	    bean.setExecutorName(userName);
    	    IUnionWorkItemService uwis=new UnionWorkItemService();
		    uwis.deleteWorkItem(bean);
		    //2.发起流程
	        WorkItemsBean workItemsBean=new WorkItemsBean();
	        workItemsBean.setRequestType("1");//新建提交
	        workItemsBean.setAppProcInstID(GUIDDeal);
	        workItemsBean.setTitle(title);
	        workItemsBean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
	        workItemsBean.setInitiator(userName);
	        workItemsBean.setInitiatorName(userName);
	        //3.统一代办
    	    WorkItemBean workItem =new  WorkItemBean();
	   	    workItem.setAppWorkQueueID(GUIDDeal);
	   	    workItem.setCurrActivity(stateName);
	  	    workItem.setPendingItemURL("http://132.121.165.45:8083/tydic-bi-meta/portal/login_sso.jsp");
	   	    workItem.setExecutor(acceptName);
	   	    workItem.setExecutorName(acceptName);
	   	    workItemsBean.addWorkItems(workItem);
		    uwis.createWorkItems(workItemsBean);
		    
			result.setSuccessData(serviceProblemDAO.queryProblem(data, page));
			BaseDAO.commit();
        }catch (Exception e){
            Log.error("处理系统信息失败", e);
            BaseDAO.rollback();
			result = new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
        }
        return result;
    } 
    /**
     * 退回
     * @param data
     * @return
     */
    public boolean nextReturnServiceProblem(Map<String,Object> data, Page page){
    	String GUIDDeal=UUID.randomUUID().toString();
        data.put("GUID", GUIDDeal);
    	boolean flag=false;
    	String  userName=MapUtils.getString(data,"dealActorName",null);
    	String key =MapUtils.getString(data,"mainProblemId", null);
	    Map<String ,Object> map=serviceProblemDAO.getMainPromblemById(key);
	    String  GUID=	MapUtils.getString(map,"GUID", null);
		String  title=MapUtils.getString(map,"THEME");
		String acceptName=MapUtils.getString(map,"CREATE_ACTOR_NAME", null);

		BaseDAO.beginTransaction();
    	serviceProblemDAO.returnDealServiceProblem(data,page);
    	flag=serviceProblemDAO.nextReturnServiceProblem(data,page);
    	//1.结束流程
        WorkItemsBean bean=new WorkItemsBean();
	    bean.setRequestType("1");//
	    bean.setAppProcInstID(GUID);
	    bean.setAppWorkQueueID(GUID);
	    bean.setTitle(title);
	    bean.setExecutor(userName);
	    bean.setExecutorName(userName);
	    IUnionWorkItemService uwis=new UnionWorkItemService();
	    uwis.deleteWorkItem(bean);
	    //2.发起流程
        WorkItemsBean workItemsBean=new WorkItemsBean();
        workItemsBean.setRequestType("1");//新建提交
        workItemsBean.setAppProcInstID(GUIDDeal);
        workItemsBean.setTitle(title);
        workItemsBean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
        workItemsBean.setInitiator(userName);
        workItemsBean.setInitiatorName(userName);
        //3.统一代办
	    WorkItemBean workItem =new  WorkItemBean();
   	    workItem.setAppWorkQueueID(GUIDDeal);
   	    workItem.setCurrActivity("被退单");
  	    workItem.setPendingItemURL("http://132.121.165.45:8083/tydic-bi-meta/portal/login_sso.jsp");
   	    workItem.setExecutor(acceptName);
   	    workItem.setExecutorName(acceptName);
   	    workItemsBean.addWorkItems(workItem);
	    uwis.createWorkItems(workItemsBean);
    	BaseDAO.commit();
		return flag;
    }
    /**
     * 确认
     * @param data
     * @return
     */
    public boolean affirmServiceProblem(Map<String,Object> data, Page page){
    	String state="5";
    	data.put("dealState", state);
    	String  userName=MapUtils.getString(data,"dealActorName",null);
    	String key =MapUtils.getString(data,"mainProblemId", null);
	    Map<String ,Object> map=serviceProblemDAO.getMainPromblemById(key);
    	String  GUID=MapUtils.getString(map,"GUID", null);
    	String  title=MapUtils.getString(map,"THEME");
    	boolean flag=false;
    	BaseDAO.beginTransaction();
    	serviceProblemDAO.dealServiceProblemState(data,page);
    	flag=serviceProblemDAO.affirmServiceProblem(data,page);
    	//1.结束流程
        WorkItemsBean bean=new WorkItemsBean();
	    bean.setRequestType("1");//
	    bean.setAppProcInstID(GUID);
	    bean.setAppWorkQueueID(GUID);
	    bean.setTitle(title);
	    bean.setExecutor(userName);
	    bean.setExecutorName(userName);
	    IUnionWorkItemService uwis=new UnionWorkItemService();
	    uwis.deleteWorkItem(bean);
    	BaseDAO.commit();
		return flag;
		
    }
    /**
     * 评估满意度
     * @param data
     * @return
     */
   public boolean satisfiedEvaluationReport(Map<String,Object> data, Page page){
	   String GUIDDeal=UUID.randomUUID().toString();
	   String  userName=MapUtils.getString(data,"dealActorName",null);
       String key =MapUtils.getString(data,"mainProblemId", null);
	   Map<String ,Object> map=serviceProblemDAO.getMainPromblemById(key);
   	   String  GUID=MapUtils.getString(map,"GUID", null);
       String  title=MapUtils.getString(map,"THEME");
       String acceptName=MapUtils.getString(map,"FIRST_ACCEPT_ACTOR_NAME", null);
       String state="";
		if("0".equals(data.get("isSatisfiedEvaluation"))){
			state="5";//归档
			data.put("GUID", "");
		}else{
		    state="1";//待处理
		    data.put("GUID", GUIDDeal);
		}
		data.put("dealState", state);
    	boolean flag=false;
    	BaseDAO.beginTransaction();
    	serviceProblemDAO.dealServiceProblemState(data,page);
    	flag=serviceProblemDAO.affirmServiceProblem(data,page);
    	if("0".equals(data.get("isSatisfiedEvaluation"))){
    		//1.结束流程
            WorkItemsBean bean=new WorkItemsBean();
    	    bean.setRequestType("1");//
    	    bean.setAppProcInstID(GUID);
    	    bean.setAppWorkQueueID(GUID);
    	    bean.setTitle(title);
    	    bean.setExecutor(userName);
    	    bean.setExecutorName(userName);
    	    IUnionWorkItemService uwis=new UnionWorkItemService();
    	    uwis.deleteWorkItem(bean);
    	}else{
    		//1.结束流程
            WorkItemsBean bean=new WorkItemsBean();
    	    bean.setRequestType("1");//
    	    bean.setAppProcInstID(GUID);
    	    bean.setAppWorkQueueID(GUID);
    	    bean.setTitle(title);
    	    bean.setExecutor(userName);
    	    bean.setExecutorName(userName);
    	    IUnionWorkItemService uwis=new UnionWorkItemService();
    	    uwis.deleteWorkItem(bean);
    	    //2.发起流程
            WorkItemsBean workItemsBean=new WorkItemsBean();
            workItemsBean.setRequestType("1");//重新提交
            workItemsBean.setAppProcInstID(GUIDDeal);
            workItemsBean.setTitle(title);
            workItemsBean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
            workItemsBean.setInitiator(userName);
            workItemsBean.setInitiatorName(userName);
            //3.统一代办
    	    WorkItemBean workItem =new  WorkItemBean();
       	    workItem.setAppWorkQueueID(GUIDDeal);
       	    workItem.setCurrActivity("待处理");
      	    workItem.setPendingItemURL("http://132.121.165.45:8083/tydic-bi-meta/portal/login_sso.jsp");
       	    workItem.setExecutor(acceptName);
       	    workItem.setExecutorName(acceptName);
       	    workItemsBean.addWorkItems(workItem);
    	    uwis.createWorkItems(workItemsBean);
    	}
    	BaseDAO.commit();
	   return flag;
    }
    /**
     * 派送
     * @param data
     * @return
     */
    public boolean sendServiceProblem(Map<String,Object> data, Page page){
    	String GUIDDeal=UUID.randomUUID().toString();
        data.put("GUID", GUIDDeal);
    	String key =MapUtils.getString(data,"mainProblemId", null);
 	    Map<String ,Object> map=serviceProblemDAO.getMainPromblemById(key);
        String  userName=MapUtils.getString(map,"CREATE_ACTOR_NAME",null);
        
    	String  GUID=MapUtils.getString(map,"GUID", null);
        String  title=MapUtils.getString(map,"THEME");
        String acceptName=MapUtils.getString(data,"acceptActorName", null);
    	BaseDAO.beginTransaction();
    	boolean flag=serviceProblemDAO.sendServiceProblem(data,page);
    		//1.结束流程
            WorkItemsBean bean=new WorkItemsBean();
    	    bean.setRequestType("1");//
    	    bean.setAppProcInstID(GUID);
    	    bean.setAppWorkQueueID(GUID);
    	    bean.setTitle(title);
    	    bean.setExecutor(userName);
    	    bean.setExecutorName(userName);
    	    IUnionWorkItemService uwis=new UnionWorkItemService();
    	    uwis.deleteWorkItem(bean);
    	    //2.发起流程
            WorkItemsBean workItemsBean=new WorkItemsBean();
            workItemsBean.setRequestType("1");//重新提交
            workItemsBean.setAppProcInstID(GUIDDeal);
            workItemsBean.setTitle(title);
            workItemsBean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
            workItemsBean.setInitiator(userName);
            workItemsBean.setInitiatorName(userName);
            //3.统一代办
    	    WorkItemBean workItem =new  WorkItemBean();
       	    workItem.setAppWorkQueueID(GUIDDeal);
       	    workItem.setCurrActivity("待处理");
      	    workItem.setPendingItemURL("http://132.121.165.45:8083/tydic-bi-meta/portal/login_sso.jsp");
       	    workItem.setExecutor(acceptName);
       	    workItem.setExecutorName(acceptName);
       	    workItemsBean.addWorkItems(workItem);
    	    uwis.createWorkItems(workItemsBean);
    	    BaseDAO.commit();
		return flag;
    }
    /**
     * 删除(需要删除处理单)
     * @param noticeIdStr
     * @return
     */
    public OprResult<?,?>[] delServiceProblem(String problemIds){
	    Map<String ,Object> map=serviceProblemDAO.getMainPromblemById(problemIds);
    	String  GUID=MapUtils.getString(map,"GUID", null);
    	String  title=MapUtils.getString(map,"THEME");
    	String userName=MapUtils.getString(map,"CREATE_ACTOR_NAME");
        //前台传入的ID是字符串形式以逗号隔开
		int problemId[] = new int[problemIds.split(",").length];
        for(int i = 0; i < problemId.length; i ++){
        	problemId[i] = Integer.parseInt(problemIds.split(",")[i]);
		}
        OprResult<?,?> result[] = new OprResult[problemId.length];
        try{
			BaseDAO.beginTransaction();
			serviceProblemDAO.delViceServiceProblem(problemId);
			serviceProblemDAO.delServiceProblem(problemId);
			//1.结束流程
	        WorkItemsBean bean=new WorkItemsBean();
		    bean.setRequestType("1");//
		    bean.setAppProcInstID(GUID);
		    bean.setAppWorkQueueID(GUID);
		    bean.setTitle(title);
		    bean.setExecutor(userName);
		    bean.setExecutorName(userName);
		    IUnionWorkItemService uwis=new UnionWorkItemService();
		    uwis.deleteWorkItem(bean);
            BaseDAO.commit();
			for(int i = 0; i < result.length; i ++){
				result[i] = new OprResult<Integer,Object>(problemId[i], null, OprResult.OprResultType.delete);
			}
			return result;
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error("删除系统信息失败", e);
			for(int i = 0; i < result.length; i ++){
				result[i] = new OprResult<Integer,Object>(problemId[i], null, OprResult.OprResultType.error);
			}
            return result;
		}
    }
}
