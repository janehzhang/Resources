package tydic.portalCommon.audit;

public class ScopePO {
	
	private int scopeId;
	private String busFlag;
	private String moduleAddres;
	private String pageAddres;
	private String minDate;
	private String maxDate;
	private String maxEffect;
	private int aduitFlag;
	private String auditType;
	private String auditNote;
	private String auditProp;
	private String effectState;
	private String dataTime;
	
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public int getScopeId() {
		return scopeId;
	}
	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}
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
	public String getPageAddres() {
		return pageAddres;
	}
	public void setPageAddres(String pageAddres) {
		this.pageAddres = pageAddres;
	}
	public String getMinDate() {
		return minDate;
	}
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}
	public String getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}
	public String getMaxEffect() {
		return maxEffect;
	}
	public void setMaxEffect(String maxEffect) {
		this.maxEffect = maxEffect;
	}
	public int getAduitFlag() {
		return aduitFlag;
	}
	public void setAduitFlag(int aduitFlag) {
		this.aduitFlag = aduitFlag;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getAuditNote() {
		return auditNote;
	}
	public void setAuditNote(String auditNote) {
		this.auditNote = auditNote;
	}
	public String getAuditProp() {
		return auditProp;
	}
	public void setAuditProp(String auditProp) {
		this.auditProp = auditProp;
	}
	public String getEffectState() {
		return effectState;
	}
	public void setEffectState(String effectState) {
		this.effectState = effectState;
	}
}
