
/**   
 * @文件名: ReportOfflineAction.java
 * @包 tydic.meta.module.report.reportOffline
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-3-20 下午03:44:18
 *  
 */
  
package tydic.meta.module.report.reportOffline;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportOfflineAction   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-3-20 下午03:44:18   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportOfflineAction {
	ReportOfflineDao reportOfflineDao;
	public ReportOfflineDao getReportOfflineDao() {
		return reportOfflineDao;
	}
	public void setReportOfflineDao(ReportOfflineDao reportOfflineDao) {
		this.reportOfflineDao = reportOfflineDao;
	}
	public List<Map<String,Object>> getReportMes(Map<String,Object> data,Page page){
		Map<String,Object> userMap = SessionManager.getCurrentUser();
		//如果关键字不为空，那么需要把信息新增到META_MAG_USER_SEARCH_LOG
		if(data.get("keyWord") != null && !"".equals(""+data.get("keyWord")) && !"null".equals(""+data.get("keyWord"))){
			reportOfflineDao.insertReportSearchLog(userMap, ""+data.get("keyWord"), "1");
		}
		return reportOfflineDao.getReportMes(data, page);
	}
	
	public Map<String,Object> getDataModelMesByRepId(String reportId){
		return reportOfflineDao.getDataModelMesByRepId(reportId);
	}
	
	public Map<String,Object> getReportMesByRepId(String reportId){
		return reportOfflineDao.getReportMesByRepId(reportId);
	}
	
	public Map<String,Object> getReportUseMesByRepId(String reportId){
		return reportOfflineDao.getReportUseMesByRepId(reportId);
	}
	
	public boolean updateReportStateByRepId(String reportId){
		Map<String,Object> userMap = SessionManager.getCurrentUser();
		if(reportOfflineDao.updateReportStateByRepId(reportId))
			return reportOfflineDao.insertReportLog(reportId, userMap);
		else
			return false;
	}
	
	public Map<String,Object> getIssueIdByReportId(String reportId){
		return reportOfflineDao.getIssueIdByReportId(reportId);
	}
}
