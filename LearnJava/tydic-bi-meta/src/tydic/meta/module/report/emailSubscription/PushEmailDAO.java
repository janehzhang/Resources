
/**   
 * @文件名: PushEmailDAO.java
 * @包 tydic.meta.module.report.emailSubscription
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-10 下午06:01:24
 *  
 */
  
package tydic.meta.module.report.emailSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.mag.timer.TimerConstant;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.module.report.repExcel.ReportBean;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：PushEmailDAO   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-10 下午06:01:24   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class PushEmailDAO extends MetaBaseDAO{
	/**
	 * @Title: addMagTimerTabMes 
	 * @Description: 新增记录
	 * @param timerClass 调用类名（包括报路径）
	 * @param sendSequnce 调用类型
	 * @param timerRule 调用规则
	 * @param timerDesc 保存信息为：报表ID,用户ID
	 * @return long 主键ID
	 * @throws
	 */
	public long addMagTimerTabMes(String timerClass,String sendSequnce,String timerRule,String timerDesc,
			String timerStartTime,String timerEndTime){
		long timerId = -1;
		//由于索引键不明确，用max
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT MAX(TIMER_ID) TIMER_ID FROM META_MAG_TIMER");
		Map<String,Object> map = getDataAccess().queryForMap(buffer.toString());
		timerId = Long.valueOf(""+map.get("TIMER_ID")) + 1;
		//新增记录
		buffer = null;
		buffer = new StringBuffer();
		buffer.append(" INSERT INTO META_MAG_TIMER(TIMER_ID,TIMER_TYPE,TIMER_RULE,TIMER_STATE,");
		buffer.append(" TIMER_CLASS,TIMER_DESC,TIMER_START_TIME,TIMER_END_TIME)");
		buffer.append(" VALUES (?,?,?,?,?, ?,?,?)");
		Object[] obj = new Object[]{
				timerId,
				sendSequnce,
				timerRule,
				1,
				timerClass,
				timerDesc,
				timerStartTime,
				timerEndTime
		};
		getDataAccess().execNoQuerySql(buffer.toString(), obj);
		return timerId;
	}
	/**
	 * @Title: updateTimer 
	 * @Description: 根据报表ID+用户ID组合的TIMER_DESC更新TIMER_RULE（时间）
	 * @param reportIdAndUserId
	 * @param timerRule
	 * @param timerStartTime
	 * @param timerEndTime  
	 * @return boolean   
	 * @throws
	 */
	public boolean updateTimer(String reportIdAndUserId,String timerRule,String timerStartTime,String timerEndTime){
		String sql = "UPDATE META_MAG_TIMER SET TIMER_RULE=?,TIMER_START_TIME=?,TIMER_END_TIME=? WHERE TIMER_DESC=?";
		return getDataAccess().execNoQuerySql(sql,timerRule,timerStartTime,timerEndTime,reportIdAndUserId);
	}
	/**
	 * @Title: getSendMesByReportIdAndUserId 
	 * @Description: 根据报表ID和用户ID获取用户信息
	 * @param reportId
	 * @param userId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getSendMesByReportIdAndUserId(String reportId,String userId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.*,B.ZONE_CODE,B.ZONE_NAME FROM(");
		buffer.append(" SELECT U.USER_EMAIL,P.REPORT_ID,U.USER_ID,U.USER_NAMECN,U.USER_MOBILE,U.ZONE_ID,U.DEPT_ID,U.STATION_ID FROM");
		buffer.append(" META_RPT_PUSH_CONFIG P,META_MAG_USER U");
		buffer.append(" WHERE P.USER_ID=U.USER_ID");
		buffer.append(" AND P.REPORT_ID=? AND P.USER_ID=?) A,");
		buffer.append(" META_DIM_ZONE B WHERE A.ZONE_ID=B.ZONE_ID");
		return getDataAccess().queryForMap(buffer.toString(), new Object[]{reportId,userId});
	}
	/**
	 * @Title: getSendMesSameTime 
	 * @Description: 根据同一时间点获取需要发送的用户信息
	 * @param sendSequnce 周期类型
	 * @param time 时分秒（HH:mm:ss）
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getSendMesSameTime(String sendSequnce,String time){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT U.USER_EMAIL,P.REPORT_ID,U.USER_ID,U.USER_NAMECN,U.USER_MOBILE FROM");
		buffer.append(" META_RPT_PUSH_CONFIG P,META_MAG_USER U");
		buffer.append(" WHERE P.USER_ID=U.USER_ID");
		buffer.append(" AND P.SEND_SEQUNCE=?");
		//不管是年、月、日、一次性、时，统一按时分秒时刻来查询该时刻需要处理的信息
		//注意时间格式
		buffer.append(" AND SUBSTR(TO_CHAR(P.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss'),12)=?");
		return getDataAccess().queryForList(buffer.toString(), new Object[]{sendSequnce,time});
	}
	/**
	 * @Title: getReportMesByReportId 
	 * @Description: 根据报表ID获取报表表头信息
	 * @param reportId
	 * @return List<ReportBean>   
	 * @throws
	 */
	public List<ReportBean> getReportMesByReportId(String reportId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT R.REPORT_ID,R.REPORT_NAME,R.IS_LISTING,O.DIM_TABLE_ID,O.COLUMN_NAME,O.COLUMN_ID,");
		buffer.append(" O.OUTPUT_ID,O.OUTPUT_ORDER FROM");
		buffer.append(" META_RPT_TAB_REPORT_CFG R,META_RPT_TAB_OUTPUT_CFG O");
		buffer.append(" WHERE R.REPORT_ID=O.REPORT_ID");
		buffer.append(" AND R.REPORT_ID=?");
		buffer.append(" ORDER BY OUTPUT_ORDER");
		List<ReportBean> list = getDataAccess().queryForBeanList(buffer.toString(), ReportBean.class, new Object[]{reportId});
		List<ReportBean> resList = new ArrayList<ReportBean>();
		if(list != null && list.size() > 0){
			for(ReportBean b : list){
				ReportBean bean = b;
				bean.setReportCols(list.size());
				resList.add(bean);
			}
		}else
			resList = null;
		return resList;
	}
	
	public Object[][] getReportData(String sql){
		return getDataAccess().queryForArray(sql, false);
	}
	/**
	 * @Title: getCounts 
	 * @Description: 获取订阅总条数
	 * @return int   
	 * @throws
	 */
	public int getCounts(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT COUNT(ROWID) COUNTS FROM META_RPT_PUSH_CONFIG P WHERE EXISTS");
		buffer.append(" (SELECT 1 FROM META_RPT_TAB_REPORT_CFG PP WHERE PP.REPORT_STATE=1 AND PP.REPORT_ID=P.REPORT_ID)");
		buffer.append(" AND ABS(ROUND(TO_NUMBER(TO_DATE(SUBSTR(TO_CHAR(SYSDATE,'yyyymmddhh24miss'),9,6),'hh24miss')-");
		buffer.append(" TO_DATE(SUBSTR(TO_CHAR(SEND_BASE_TIME,'yyyymmddhh24miss'),9,6),'hh24miss'))*1440))<="+ReportConstant.TIMERDELAY/(60*1000));
		Map<String,Object> map = getDataAccess().queryForMap(buffer.toString());
		return Integer.valueOf(""+map.get("COUNTS"));
	}
	/**
	 * @Title: getTestReportData 
	 * @Description: 根据sql获取订阅数据
	 * @param sql
	 * @param startNum 开始条数
	 * @param endNum 结束条数
	 * @return Object[][]   
	 * @throws
	 */
	public Object[][] getTestReportData(String sql,int startNum,int endNum){
    	StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (select /*+ first_rows */ ");
        buffer.append(" a.*, rownum rn from ( ");
        buffer.append(sql);
        buffer.append("  ) a where rownum <= "+endNum+") where rn >= "+startNum);
        return getDataAccess().queryForArray(buffer.toString(), false);
    }
	
	public String getPushSql(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT P.REPORT_ID,P.USER_ID,P.PUSH_TYPE,P.SEND_SEQUNCE,P.SEND_BASE_TIME,");
		buffer.append(" P.SEND_TIME_ADD,P.PUSH_CONFIG_ID FROM META_RPT_PUSH_CONFIG P WHERE EXISTS");
		buffer.append(" (SELECT 1 FROM META_RPT_TAB_REPORT_CFG PP WHERE PP.REPORT_STATE=1 AND PP.REPORT_ID=P.REPORT_ID)");
		buffer.append(" AND ABS(ROUND(TO_NUMBER(TO_DATE(SUBSTR(TO_CHAR(SYSDATE,'yyyymmddhh24miss'),9,6),'hh24miss')-");
		buffer.append(" TO_DATE(SUBSTR(TO_CHAR(P.SEND_BASE_TIME,'yyyymmddhh24miss'),9,6),'hh24miss'))*1440))<="+ReportConstant.TIMERDELAY/(60*1000));
		buffer.append(" AND INSTR(P.PUSH_TYPE,'1',1,1)>0");//判断是否含有邮件订阅
		return buffer.toString();
	}
	/**
	 * @Title: getPushDataBySameTimeToObj 
	 * @Description: 获取同一时间需要发送的数据信息
	 * @param sql
	 * @return Object[][]
	 * @throws
	 */
	public Object[][] getPushDataBySameTimeToObj(String sql){
		return getDataAccess().queryForArray(sql, false);
	}
	/**
	 * @Title: getPhushDataBySameTime 
	 * @Description: 获取同一时间需要发送的数据信息
	 * @param sql
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getPushDataBySameTime(String sql){
		return getDataAccess().queryForList(sql);
	}
	/**
	 * @Title: savePushLog 
	 * @Description: 新增日志记录
	 * @param map
	 * @return boolean   
	 * @throws
	 */
	public boolean savePushLog(Map<String,Object> map){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" INSERT INTO META_RPT_PUSH_LOG(PUSH_LOG_ID,REPORT_ID,USER_ID,PUSH_CONFIG_ID,USER_DEPT_ID,");
		buffer.append(" USER_ZONE_ID,USER_STATION_ID,SEND_TIME,FACT_CONTENT,DATA_ANNEX,SEND_FLAG)");
		buffer.append(" VALUES(?,?,?,?,?, ?,?,sysdate,?,?, ?)");
		Object[] params = new Object[]{
				queryForNextVal("SEQ_RPT_PUSH_LOG_ID"),
				map.get("REPORT_ID"),
				map.get("USER_ID"),
				map.get("PUSH_CONFIG_ID"),
				map.get("DEPT_ID"),
				map.get("ZONE_ID"),
				map.get("STATION_ID"),
				map.get("FACT_CONTENT"),
				map.get("DATA_ANNEX"),
				map.get("SEND_FLAG")
		};
		return getDataAccess().execNoQuerySql(buffer.toString(), params);
	}
	/**
	 * @Title: getPushLog 
	 * @Description:获取发送后日志信息 
	 * @param pushConfigId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getPushLog(long pushConfigId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT NVL(SUM(CASE WHEN P.SEND_FLAG=1 THEN 1 ELSE 0 END),0) SUC_COUNTS,");
		buffer.append(" NVL(SUM(CASE WHEN P.SEND_FLAG=0 THEN 1 ELSE 0 END),0) ERR_COUNTS");
		buffer.append(" FROM META_RPT_PUSH_LOG P WHERE P.PUSH_CONFIG_ID=?");
		buffer.append(" AND ABS(ROUND(TO_NUMBER(SYSDATE-P.SEND_TIME)*1440))<="+ReportConstant.TIMERDELAY/(60*1000));
		return getDataAccess().queryForMap(buffer.toString(), new Object[]{pushConfigId});
	}
	/**
	 * @Title: isPushLog 
	 * @Description: 判断是否已经成功发送邮件（时间：在此发送周期）
	 * @param pushConfigId
	 * @return boolean  是--true;否--false 
	 * @throws
	 */
	public boolean isPushLog(long pushConfigId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT COUNT(ROWID) COUNTS FROM META_RPT_PUSH_LOG P WHERE P.PUSH_CONFIG_ID=?");
		buffer.append(" AND ABS(ROUND(TO_NUMBER(SYSDATE-P.SEND_TIME)*1440))<="+ReportConstant.TIMERDELAY/(60*1000));
		buffer.append(" AND P.SEND_FLAG=1");
		Map<String,Object> m = getDataAccess().queryForMap(buffer.toString(), new Object[]{pushConfigId});
		if(Integer.valueOf(""+m.get("COUNTS")) > 0)
			return true;
		else
			return false;
	}
	/**
	 * @Title: pushErrorCounts 
	 * @Description: 获取发送邮件失败次数（时间：在此发送周期）
	 * @param pushConfigId
	 * @return int   
	 * @throws
	 */
	public int pushErrorCounts(long pushConfigId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT COUNT(ROWID) COUNTS FROM META_RPT_PUSH_LOG P WHERE P.PUSH_CONFIG_ID=?");
		buffer.append(" AND ABS(ROUND(TO_NUMBER(SYSDATE-P.SEND_TIME)*1440))<="+ReportConstant.TIMERDELAY/(60*1000));
		buffer.append(" AND P.SEND_FLAG=0");
		Map<String,Object> m = getDataAccess().queryForMap(buffer.toString(), new Object[]{pushConfigId});
		return Integer.valueOf(""+m.get("COUNTS"));
	}
}
