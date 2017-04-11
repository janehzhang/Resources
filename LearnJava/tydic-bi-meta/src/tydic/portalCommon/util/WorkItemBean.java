package tydic.portalCommon.util;

public class WorkItemBean {

	private String appWorkQueueID;
	private String currActivity;
	private String pendingItemURL;
	private String executor;
	private String executorName;
	
	//private String execDeptID;
	//private String execDeptName;
	public String getAppWorkQueueID() {
		return appWorkQueueID;
	}
	public void setAppWorkQueueID(String appWorkQueueID) {
		this.appWorkQueueID = appWorkQueueID;
	}
	public String getCurrActivity() {
		return currActivity;
	}
	public void setCurrActivity(String currActivity) {
		this.currActivity = currActivity;
	}
	public String getPendingItemURL() {
		return pendingItemURL;
	}
	public void setPendingItemURL(String pendingItemURL) {
		this.pendingItemURL = pendingItemURL;
	}
	public String getExecutor() {
		return executor;
	}
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
   
}
