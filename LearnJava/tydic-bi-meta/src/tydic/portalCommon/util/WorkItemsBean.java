package tydic.portalCommon.util;

import java.util.ArrayList;
import java.util.List;

import tydic.frame.SystemVariable;

public class WorkItemsBean {
	
	private String appSystemID = SystemVariable.getString("daibanAppSystemID","");
	private String appProcInstID;
	private String modelAlias="FWFX0000000001";
	private String modelName="客户服务分析系统";
	private String title;
	private String createTime;
	private String initiator;
	private String initiatorName;
	
	private String requestType;
	private String executor;
	private String executorName;
	private String appWorkQueueID;
	
	
	
	private List<WorkItemBean> workItems=new ArrayList<WorkItemBean>();
	
	public String getAppSystemID() {
		return appSystemID;
	}
	public void setAppSystemID(String appSystemID) {
		this.appSystemID = appSystemID;
	}
	public String getAppProcInstID() {
		return appProcInstID;
	}
	public void setAppProcInstID(String appProcInstID) {
		this.appProcInstID = appProcInstID;
	}
	public String getModelAlias() {
		return modelAlias;
	}
	public void setModelAlias(String modelAlias) {
		this.modelAlias = modelAlias;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getInitiatorName() {
		return initiatorName;
	}
	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}
	public List<WorkItemBean> getWorkItems() {
		return workItems;
	}
	public void addWorkItems(WorkItemBean workItem){
		this.workItems.add(workItem);
	}
	public void setWorkItems(List<WorkItemBean> workItems) {
		this.workItems = workItems;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
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
	public String getAppWorkQueueID() {
		return appWorkQueueID;
	}
	public void setAppWorkQueueID(String appWorkQueueID) {
		this.appWorkQueueID = appWorkQueueID;
	}
	
}