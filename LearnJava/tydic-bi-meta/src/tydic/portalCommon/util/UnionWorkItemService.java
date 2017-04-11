package tydic.portalCommon.util;
import java.util.UUID;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.meta.common.DateUtil;
public class UnionWorkItemService implements IUnionWorkItemService{
	
	public static  String  methodName="";
	private String url = SystemVariable.getString("daibanServiceUrl","");
	private String namespace = SystemVariable.getString("daibanNamespace","");
	/**
	 *  新建待办
	 * @param bean
	 * @return
	 */
    public  void createWorkItems(WorkItemsBean bean) {
		 methodName= "CreateWorkItems";
		 SendDataThread sdt = new SendDataThread(methodName,getCreateWorkItemsXml(bean),url,namespace);
		 sdt.start();
	}
   /**
    *  删除待办
    * @param bean
    * @return
    */
	public  void deleteWorkItem(WorkItemsBean bean) {
		 methodName= "DeleteWorkItem";
		 SendDataThread sdt = new SendDataThread(methodName, getDeleteWorkItemXml(bean),url,namespace);
		 sdt.start();
	}
   public static void main(String[] args) {
	   
/*	    String title="广东客户服务分析系统指标批示待办";
	    String GUID=UUID.randomUUID().toString();
	    WorkItemsBean bean=new WorkItemsBean();
	    bean.setRequestType("2");
	    bean.setAppProcInstID(GUID);
	    bean.setTitle(title);
	    bean.setCreateTime(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
	    bean.setInitiator("admin");
	    bean.setInitiatorName("管理员");
	    
	    WorkItemBean workItem =new  WorkItemBean();
	    workItem.setAppWorkQueueID("eiactest_"+GUID);
	    workItem.setCurrActivity("查阅");
	    workItem.setPendingItemURL("http://132.121.165.45:8081/tydic-bi-meta/portal/login_sso.jsp");
	    workItem.setExecutor("eiactest");
	    workItem.setExecutorName("eiactest");
	    bean.addWorkItems(workItem);
	    createWorkItems(bean);*/
		

	    
	  /*  WorkItemsBean bean=new WorkItemsBean();
	    bean.setAppSystemID("AP1500043558");
	    bean.setRequestType("1");
	    bean.setAppProcInstID("fbc3867b-10d8-4018-96d5-a99449df38f4");
	    bean.setAppWorkQueueID("eiactest_fbc3867b-10d8-4018-96d5-a99449df38f4");
	    bean.setModelAlias("统一支撑平台待办");
	    bean.setModelName("广东客户服务分析系统待办");
	    bean.setTitle("广东客户服务分析系统指标批示待办");
	    bean.setExecutor("eiactest");
	    bean.setExecutorName("eiactest");
	    
	    System.out.println("================>" + deleteWorkItem(bean));*/
	}
   
   private static String getCreateWorkItemsXml(WorkItemsBean bean) {
	 String str="<RequestMessage>\n"+
					"<RequestData>\n"+
						"<Version><![CDATA[V0.8]]></Version>\n"+
						"<AppSystemID><![CDATA["+ bean.getAppSystemID() +"]]></AppSystemID>\n"+
						"<RequestType><![CDATA["+ bean.getRequestType() +"]]></RequestType>\n"+
						"<AppProcInstID><![CDATA["+ bean.getAppProcInstID() +"]]></AppProcInstID>\n"+
						"<ModelAlias><![CDATA["+ bean.getModelAlias() +"]]></ModelAlias>\n"+
						"<ModelName><![CDATA["+ bean.getModelName() +"]]></ModelName>\n"+
						"<Title><![CDATA["+ bean.getTitle() +"]]></Title>\n"+
						"<SecurityFlag><![CDATA[1]]></SecurityFlag>\n"+
						"<OrigDeptName></OrigDeptName>\n"+
						"<AssessFlag><![CDATA[0]]></AssessFlag>\n"+
						"<CreateTime><![CDATA["+ bean.getCreateTime() +"]]></CreateTime>\n"+
						"<DeadLineTime></DeadLineTime>\n"+
						"<Initiator><![CDATA["+ bean.getInitiator() +"]]></Initiator>\n"+
						"<InitiatorName><![CDATA["+bean.getInitiatorName() +"]]></InitiatorName>\n"+
						"<InitDeptID></InitDeptID>\n"+
						"<InitDeptName></InitDeptName>\n"+
					  "<WorkItems>\n";
				  for(WorkItemBean workItem : bean.getWorkItems()){			
					 str +="<WorkItem>\n"+
								"<AppWorkQueueID><![CDATA["+ workItem.getAppWorkQueueID()+"]]></AppWorkQueueID>\n"+
								"<FileType><![CDATA[2]]></FileType>\n"+
								"<CurrActivity><![CDATA["+  workItem.getCurrActivity() +"]]></CurrActivity>\n"+
								"<PendingItemURL><![CDATA["+ workItem.getPendingItemURL()+"]]></PendingItemURL>\n"+
								"<Executor><![CDATA["+ workItem.getExecutor()+"]]></Executor>\n"+
								"<ExecutorName><![CDATA["+ workItem.getExecutorName()+"]]></ExecutorName>\n"+
								"<ExecDeptID></ExecDeptID>\n"+
								"<ExecDeptName></ExecDeptName>\n"+
							"</WorkItem>\n";
						}
				str +="</WorkItems>\n"+
				    "</RequestData>\n"+
			     "</RequestMessage>";
				Log.info(str);
      return str;
    }
   
   
   private static String getDeleteWorkItemXml(WorkItemsBean bean) {
		String str="<?xml version='1.0'?>\n"+
					"<RequestMessage>\n"+
						"<RequestData>\n"+
							"<Version><![CDATA[V0.8]]></Version>\n"+
							"<AppSystemID><![CDATA["+ bean.getAppSystemID() +"]]></AppSystemID>\n"+
							"<RequestType><![CDATA["+ bean.getRequestType() +"]]></RequestType>\n"+
							"<AppProcInstID><![CDATA["+ bean.getAppProcInstID() +"]]></AppProcInstID>\n"+
							"<AppWorkQueueID><![CDATA["+ bean.getAppWorkQueueID() +"]]></AppWorkQueueID>\n"+
							"<ModelAlias><![CDATA["+ bean.getModelAlias() +"]]></ModelAlias>\n"+
							"<ModelName><![CDATA["+ bean.getModelName() +"]]></ModelName>\n"+
							"<Title><![CDATA["+ bean.getTitle() +"]]></Title>\n"+
							"<Executor><![CDATA["+ bean.getExecutor() +"]]></Executor>\n"+
							"<ExecutorName><![CDATA["+ bean.getExecutorName() +"]]></ExecutorName>\n"+
						 "</RequestData>\n"+
					 "</RequestMessage>";
		Log.info(str);
	    return str;						
   }
}
