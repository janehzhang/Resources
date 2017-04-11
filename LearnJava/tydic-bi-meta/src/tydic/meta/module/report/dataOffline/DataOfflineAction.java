
/**   
 * @文件名: DataOfflineAction.java
 * @包 tydic.meta.module.report.dataOffline
 * @描述: TODO
 * @author wuxl@tydic.com
 * @创建日期 2012-3-14 下午03:50:19
 *  
 */
  
package tydic.meta.module.report.dataOffline;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;
import tydic.meta.module.report.reportOffline.ReportOfflineDao;
import tydic.meta.web.session.SessionManager;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：DataOfflineAction   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-3-14 下午03:50:19   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class DataOfflineAction {
	DataOfflineDao dataOfflineDao;
	ReportOfflineDao reportOfflineDao;
	public DataOfflineDao getDataOfflineDao() {
		return dataOfflineDao;
	}
	public void setDataOfflineDao(DataOfflineDao dataOfflineDao) {
		this.dataOfflineDao = dataOfflineDao;
	}
	public ReportOfflineDao getReportOfflineDao() {
		return reportOfflineDao;
	}
	public void setReportOfflineDao(ReportOfflineDao reportOfflineDao) {
		this.reportOfflineDao = reportOfflineDao;
	}
	public List<Map<String,Object>> getDataModelMes(Map<String,Object> data,Page page){
		Map<String,Object> userMap = SessionManager.getCurrentUser();
		//如果关键字不为空，那么需要把信息新增到META_MAG_USER_SEARCH_LOG
		if(data.get("keyword") != null && !"".equals(""+data.get("keyword")) && !"null".equals(""+data.get("keyword"))){
			reportOfflineDao.insertReportSearchLog(userMap, ""+data.get("keyword"), "2");
		}
		return dataOfflineDao.getDataModelMes(data, page);
	}
	
	public Map<String,Object> exeDataOffline(String tableId,String issueId){
		return dataOfflineDao.getModelMes(tableId, issueId);
	}
	
	public List<Map<String,Object>> getColMes(String issueId){
		return dataOfflineDao.getColMes(issueId);
	}
	
	public List<Map<String,Object>> getReportMes(String issueId,Page page){
		return dataOfflineDao.getReportMes(issueId,null);
	}
	
	public boolean offLine(String issueId){
		Map<String,Object> userMap = SessionManager.getCurrentUser();
		if(dataOfflineDao.updateModel(issueId))
			return dataOfflineDao.insertModelLog(issueId, userMap);
		else
			return false;
	}
}
