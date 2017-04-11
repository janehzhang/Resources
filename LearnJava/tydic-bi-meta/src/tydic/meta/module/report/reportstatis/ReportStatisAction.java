
/**   
 * @文件名: ReportStatisAction.java
 * @包 tydic.meta.module.report.reportstatis
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-2-21 上午11:33:31
 *  
 */
  
package tydic.meta.module.report.reportstatis;

import java.util.List;
import java.util.Map;

import tydic.meta.common.DateUtil;
import tydic.meta.module.mag.login.LoginConstant;
import tydic.meta.web.session.SessionManager;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportStatisAction   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-2-21 上午11:33:31   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportStatisAction {
	private ReportStatisDao reportStatisDao;
	
	public ReportStatisDao getReportStatisDao() {
		return reportStatisDao;
	}
	public void setReportStatisDao(ReportStatisDao reportStatisDao) {
		this.reportStatisDao = reportStatisDao;
	}
	// 查询报表分类
	public List<Map<String,Object>> queryReportType(){
		List<Map<String,Object>> list = reportStatisDao.getAllRepConfigType();
		return list;
	}
	public List<Map<String,Object>> getQueryTables(Map<String,String> queryMessage){
		Map<String,Object> userMap = SessionManager.getCurrentUser();
		Map<String,Object> deptMap = (Map<String,Object>)SessionManager.getCurrentSession().getAttribute(LoginConstant.SESSION_META_DEPT_INFO);
		List<Map<String,Object>> list = reportStatisDao.getQueryTables(queryMessage, userMap);
		return list;
	}
	/**
	 * 
	 * @Title: getRepAppStaAnaMes 
	 * @Description: 获取报表应用分析数据信息
	 * @param queryMessage
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepAppStaAnaMes(Map<String,String> queryMessage){
		/*时间转换*/
		if("1".equals(queryMessage.get("dateTime")))//当天
			queryMessage.put("currentDay", DateUtil.getCurrentDay("yyyy-MM-dd"));
		else if("2".equals(queryMessage.get("dateTime"))){//本周
			String s[] = DateUtil.getCurrentWeekDays("yyyy-MM-dd");
			queryMessage.put("startday", s[0]);
			queryMessage.put("endday", s[1]);
		}else if("3".equals(queryMessage.get("dateTime"))){//本周
			String s[] = DateUtil.getCurrentMonthDays("yyyy-MM-dd");
			queryMessage.put("startday", s[0]);
			queryMessage.put("endday", s[1]);
		}
		List<Map<String,Object>> list = reportStatisDao.getRepAppStaAnaMes(queryMessage);
		if(list != null && list.size() > 1){//如果数据为空，还有一条合计信息需要去掉
			return list;
		}else
			return null;
	}
}
