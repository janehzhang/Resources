package tydic.meta.module.report.parse;

import java.util.List;
import java.util.Map;


/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:报表解析Action
 * @date 2012-04-06
 */
public class ReportParseAction {
	private ReportParseDAO reportParseDAO;
	
    public ReportParseDAO getReportParseDAO() {
		return reportParseDAO;
	}

	public void setReportParseDAO(ReportParseDAO reportParseDAO) {
		this.reportParseDAO = reportParseDAO;
	}

	/**
     * 获取报表数据
     * @param reportId  报表ID
     * @param queryParam 查询格式为[{
     *   queryId:--查询控件ID,
     *   values:--数据，多个以","分隔。
     * }]
     * @return  返回一个二维格式的数组
     */
    public Object[][] getReportData(long reportId,List<Map<String,Object>> queryParam,long dataSourceId){
    	String sql = getReportSql(reportId,queryParam);
        return reportParseDAO.getReportData(sql,dataSourceId);
    }

    /**
     * 获取包含报表转换编码的完整SQL
     * @param reportId
     * @param queryParam 查询格式为[{
     *   queryId:--查询控件ID,
     *   values:--数据，多个以","分隔。
     * }]
     * @return
     */
    public  String getTranCodeCompleteSQL(long reportId,List<Map<String,Object>> queryParam){
        return null;
    }

    /**
     * 获取一个报表的报表SQl，此SQL不包含编码转换信息。
     * @param reportId
     * @param queryParam 查询格式为[{
     *   queryId:--查询控件ID,
     *   values:--数据，多个以","分隔。
     * }]
     * @return
     */
    public String getReportSql(long reportId,List<Map<String,Object>> queryParam){
        return null;
    }
    /**
     * 获取包含报表转换编码的数据
     * @param sql Object[][]
     * @return
     */
    public Object[][] getReportDataByTranCodeComplete(long reportId,List<Map<String,Object>> queryParam,long dataSourceId){
    	String sql = getTranCodeCompleteSQL(reportId,queryParam);
    	return reportParseDAO.getReportDataByTranCodeComplete(sql,dataSourceId);
    }
}
