package tydic.portalCommon.audit;

import java.util.Date;

public class AuditPO {
	
	private int auditId;
	private Date auditTime;
	private int auditUser;
	private String auditOpinion;
	private String auditConclude;
	private int scopeId;
	private String dataArea;
	private String showOpinion;
	private String dataDate;
	private String auditMark;

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

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getAuditMark() {
        return auditMark;
    }

    public void setAuditMark(String auditMark) {
        this.auditMark = auditMark;
    }
}
