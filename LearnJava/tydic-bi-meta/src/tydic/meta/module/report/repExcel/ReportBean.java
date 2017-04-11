
/**   
 * @文件名: ReportBean.java
 * @包 tydic.meta.module.report.repExcel
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-6 下午04:56:21
 *  
 */
  
package tydic.meta.module.report.repExcel;

import tydic.frame.jdbc.Column;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportBean   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-6 下午04:56:21   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportBean {
	@Column("DIM_TABLE_ID")
	long dimTableId;
	
	@Column("COLUMN_NAME")
	String columnName;
	
	@Column("COLUMN_ID")
	long columnId;
	
	@Column("OUTPUT_ID")
	long outPutId;
	
	@Column("REPORT_ID")
	long reportId;
	
	@Column("OUTPUT_ORDER")
	long outPutOrder;
	
	int reportCols;//报表列数
	
	@Column("REPORT_NAME")
	String reportName;
	
	@Column("IS_LISTING")
	int isListing;//是否为清单：是-1；否-0
	
	public int getIsListing() {
		return isListing;
	}
	public void setIsListing(int isListing) {
		this.isListing = isListing;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public int getReportCols() {
		return reportCols;
	}
	public void setReportCols(int reportCols) {
		this.reportCols = reportCols;
	}
	public long getDimTableId() {
		return dimTableId;
	}
	public void setDimTableId(long dimTableId) {
		this.dimTableId = dimTableId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public long getColumnId() {
		return columnId;
	}
	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}
	public long getOutPutId() {
		return outPutId;
	}
	public void setOutPutId(long outPutId) {
		this.outPutId = outPutId;
	}
	public long getReportId() {
		return reportId;
	}
	public void setReportId(long reportId) {
		this.reportId = reportId;
	}
	public long getOutPutOrder() {
		return outPutOrder;
	}
	public void setOutPutOrder(long outPutOrder) {
		this.outPutOrder = outPutOrder;
	}
}
