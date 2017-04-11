package tydic.portalCommon;

public class ReportPO{
	private String	localCode;
	private String	indexCd;
	private String	reportLevelId;
	private String	dateNo;
    private long tabId;

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	public String getIndexCd()
	{
		return indexCd;
	}

	public void setIndexCd(String indexCd)
	{
		this.indexCd = indexCd;
	}

	public String getReportLevelId()
	{
		return reportLevelId;
	}

	public void setReportLevelId(String reportLevelId)
	{
		this.reportLevelId = reportLevelId;
	}

	public String getDateNo()
	{
		return dateNo;
	}

	public void setDateNo(String dateNo)
	{
		this.dateNo = dateNo;
	}

    public long getTabId() {
        return tabId;
    }

    public void setTabId(long tabId) {
        this.tabId = tabId;
    }
}
