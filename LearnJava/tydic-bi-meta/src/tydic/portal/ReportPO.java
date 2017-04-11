package tydic.portal;

public class ReportPO
{
	private String	indexTypeId;
	private String	localCode;
	private String areaId;
	private String	indexCd;
	private String	reportLevelId;
	private String	dateNo;

	public String getIndexTypeId()
	{
		return indexTypeId;
	}

	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}


	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setIndexTypeId(String indexTypeId)
	{
		this.indexTypeId = indexTypeId;
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
}
