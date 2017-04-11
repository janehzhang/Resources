package tydic.portal.audit;

import java.util.Date;

public class AuditPO {
	
	private int auditId;
	private Date auditTime;
	private int auditUser;
	private String auditOpinion;
	private String auditConclude;
	private int auditDept;
	private int scopeId;
	private String dataArea;
	private String showOpinion;
	private String dataTime;
	
	//下面是查询页面需要返回的参数
	private String dataDate;
	private String busFlag;
	private String moduleAddres;
	private String effectState;
	private String audittype;

	public String getBusFlag() {
		return busFlag;
	}
	public void setBusFlag(String busFlag) {
		this.busFlag = busFlag;
	}
	public String getModuleAddres() {
		return moduleAddres;
	}
	public void setModuleAddres(String moduleAddres) {
		this.moduleAddres = moduleAddres;
	}
	public String getEffectState() {
		return effectState;
	}
	public void setEffectState(String effectState) {
		this.effectState = effectState;
	}
	public String getAudittype() {
		return audittype;
	}
	public void setAudittype(String audittype) {
		this.audittype = audittype;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public int getAuditId() {
		return auditId;
	}
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public int getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(int auditUser) {
		this.auditUser = auditUser;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	public String getAuditConclude() {
		return auditConclude;
	}
	public void setAuditConclude(String auditConclude) {
		this.auditConclude = auditConclude;
	}
	public int getAuditDept() {
		return auditDept;
	}
	public void setAuditDept(int auditDept) {
		this.auditDept = auditDept;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public int getScopeId() {
		return scopeId;
	}
	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}
	public String getDataArea() {
		return dataArea;
	}
	public void setDataArea(String dataArea) {
		this.dataArea = dataArea;
	}
	public String getShowOpinion() {
		return showOpinion;
	}
	public void setShowOpinion(String showOpinion) {
		this.showOpinion = showOpinion;
	}
}
