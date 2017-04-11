
/**   
 * @文件名: PushEmailUtil.java
 * @包 tydic.meta.module.report.emailSubscription
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-13 上午11:23:49
 *  
 */
  
package tydic.meta.module.report.emailSubscription;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.SystemVariable;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.mail.EmailData;
import tydic.meta.mail.SendMail;
import tydic.meta.module.mag.timer.MetaMagTimerDAO;
import tydic.meta.module.mag.timer.MetaTimerPO;
import tydic.meta.module.mag.timer.job.MetaTimerAssign;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.module.report.build.AbstractReportBuildSql;
import tydic.meta.module.report.build.ReportBuildDAO;
import tydic.meta.module.report.repExcel.OutPutExcel;
import tydic.meta.module.report.repExcel.ReportBean;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：PushEmailUtil   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-13 上午11:23:49   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class PushEmailUtil {
	OutPutExcel outPutExcel = new OutPutExcel();
	EmailData emailData = new EmailData();
	ReportBuildDAO reportBuildDAO = new ReportBuildDAO();
	MetaMagTimerDAO metaMagTimerDAO = new MetaMagTimerDAO();
	PushEmailDAO pushEmailDAO = new PushEmailDAO();
	public final String classP = "tydic.meta.module.report.emailSubscription.";
	/**
	 * @Title: getTimerClass 
	 * @Description: 根据订阅周期类型获取调度类
	 * @param sendSequnce
	 * @return String   
	 * @throws
	 */
	public String getTimerClass(String sendSequnce){
		String timerClassName = classP;
		switch(Integer.valueOf(sendSequnce)){
			case ReportConstant.SEND_SEQUNCE_ONEOFF:{
				timerClassName += "PushEmailOneOffTimer";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_YEAR:{
				timerClassName += "PushEmailYearTimer";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_HALFYEAR:{
				timerClassName += "PushEmailHalfYearTimer";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_MONTH:{
				timerClassName += "PushEmailMonthTimer";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_WEEK:{
				timerClassName += "PushEmailWeekTimer";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_DAY:{
				timerClassName += "PushEmailDayTimer";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_HOUR:{
				timerClassName += "PushEmailHourTimer";
				break;
			}
		}
		return timerClassName;
	}
	public String getTimeFormatToSql(String sendSequnce,String time){
		String sql = "";
		switch(Integer.valueOf(sendSequnce)){
			case ReportConstant.SEND_SEQUNCE_ONEOFF:{
				sql = " AND TO_CHAR(P.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss')='"+time+"'";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_YEAR:{
				sql = " AND SUBSTR(TO_CHAR(P.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss'),12)='"+time+"'";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_MONTH:{
				sql = " AND SUBSTR(TO_CHAR(P.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss'),12)='"+time+"'";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_DAY:{
				sql = " AND SUBSTR(TO_CHAR(P.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss'),12)='"+time+"'";
				break;
			}
			case ReportConstant.SEND_SEQUNCE_HOUR:{
				sql = " AND SUBSTR(TO_CHAR(P.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss'),12)='"+time+"'";
				break;
			}
		}
		return sql;
	}
	/**
	 * @Title: addMagTimerTabMes 
	 * @Description: 新增记录
	 * @param timerClass 调用类名（包括报路径）
	 * @param sendSequnce 调用类型
	 * @param timerRule 调用规则
	 * @param timerDesc 保存信息为：报表ID,用户ID
	 * @return MetaTimerPO
	 * @throws
	 */
	public MetaTimerPO addMagTimerTabMes(String timerClass,String sendSequnce,String timerRule,String timerDesc,
			String timerStartTime,String timerEndTime){
		String timerId = ("" + pushEmailDAO.addMagTimerTabMes(timerClass, sendSequnce, timerRule, 
				timerDesc,timerStartTime,timerEndTime)); 
		return metaMagTimerDAO.getTimerPOMesById(timerId);
	}
	/**
     * @Title: getTimerPOMesByTimerDesc 
     * @Description: 根据timerDesc获取调度信息
     * @param timerDesc ： 报表ID+用户ID组合（肯定唯一）
     * @return MetaTimerPO   
     * @throws
     */
	public MetaTimerPO getTimerPOMesByTimerDesc(String timerDesc){
		return metaMagTimerDAO.getTimerPOMesByTimerDesc(timerDesc);
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
		return pushEmailDAO.updateTimer(reportIdAndUserId, timerRule, timerStartTime, timerEndTime);
	}
	
	/**
	 * @Title: addTimer 
	 * @Description: 当最新订阅信息新增后，新增该调度信息
	 * @param metaTimerPO
	 * @return boolean   
	 * @throws
	 */
	public boolean addTimer(MetaTimerPO metaTimerPO){
		return MetaTimerAssign.addTimer(metaTimerPO);
	}
	/**
	 * @Title: sendEmail 
	 * @Description: 邮件发送
	 * @param emailData
	 * @return boolean   
	 * @throws
	 */
	public boolean sendEmail(EmailData emailData){
		if(emailData == null)
			return false;
		boolean jud = true;
		//邮件发送
		SendMail sendMail = new SendMail();
		sendMail.setSmtpHost(emailData.getSmtpHost());
		sendMail.setNeedAuth(emailData.isNeedAuth());
		sendMail.setPort(emailData.getMailPort());
		if(sendMail.createMimeMessage(emailData.getFname(), emailData.getFpwd()) == false){
			jud = false;
			return jud;
		}
		if(sendMail.setSubject(emailData.getSubject()) == false){
			jud = false;
			return jud;
		}
		sendMail.setDate();
		if(sendMail.setBody(emailData.getContent(), null) == false){
			jud = false;
			return jud;
		}
		if(sendMail.setTo(emailData.getRecipients()) == false){
			jud = false;
			return jud;
		}
		sendMail.setCC(emailData.getCc());
		if(sendMail.setFrom(emailData.getFromAddr()) == false){
			jud = false;
			return jud;
		}
		sendMail.addFileAffix(emailData.getFileAddr());
		if(sendMail.sendout() == false){
			jud = false;
			return jud;
		}
		return jud;
	}
	/**
	 * @Title: getPagesCount 
	 * @Description: 获取总页数
	 * @param countSum 记录总数
	 * @param pageSize 每页显示记录数
	 * @return int   
	 * @throws
	 */
	public int getPagesCount(int countSum, int pageSize){
		int pagescount = (int) Math.ceil((double) countSum / pageSize);//求总页数，ceil（num）取整不小于num
		return pagescount;
	}
	/**
	 * @Title: getCutArray 
	 * @Description: 根据起止位置，截取数组
	 * @param obj
	 * @param start
	 * @param end
	 * @return Object[][]   
	 * @throws
	 */
	public Object[][] getCutArray(Object[][] obj,int start,int end){
		Object[][] o = new Object[end][obj[0].length];
		int count = 0;
		for(int r = 0; r < obj.length; r++){
			if(r >= start && r <= end){
				for(int c = 0; c < obj[r].length; c++){
					o[count][c] = obj[r][c];
				}
				count++;
			}
		}
		return o;
	}
	/**
	 * @Title: getDelEmptyArray 
	 * @Description: 去掉数组的空行根据指定列
	 * @param obj 数组
	 * @param configCol 指定列（指定列必须有值）
	 * @return Object[][]   
	 * @throws
	 */
	public Object[][] getDelEmptyArray(Object[][] obj,int configCol){
		Object[][] o = null;
		int rows = 0;
		for(int r = 0; r < obj.length; r++){
			String s = "" + obj[r][configCol];
			if(StringUtils.isNotEmpty(s) && !"null".equals(s))
				rows++;
		}
		if(rows > 0){
			o = new Object[rows][obj[0].length];
			int row = 0;
			for(int r = 0; r < obj.length; r++){
				String s = "" + obj[r][configCol];
				if(StringUtils.isNotEmpty(s) && !"null".equals(s)){
					for(int c = 0; c < obj[r].length; c++){
						o[row][c] = obj[r][c];
					}
					row++;
				}
			}
		}
		return o;
	}
	/**
	 * @Title: pushEmail 
	 * @Description: 邮件发送
	 * @param obj    
	 * @return void   
	 * @throws
	 */
	public void pushEmail(Object[][] obj){
		if(obj != null && obj.length > 0){
			/*******************************************************************************************
			 * obj数据顺序：
			 * REPORT_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE,SEND_BASE_TIME,SEND_TIME_ADD,PUSH_CONFIG_ID
			 *******************************************************************************************/
			for(int r = 0; r < obj.length; r++){
				/**根据SEND_SEQUNCE判断需要发送的数据**/
				if(isPush(Integer.valueOf(""+obj[r][3]),""+obj[r][4])){
					String reportId = "" + obj[r][0];
					String userId = "" + obj[r][1];
					//报表信息
					Map<String,Object> repMap = reportBuildDAO.getReportSqlByReportId(reportId);
					//清单报表判断
//					if((""+ReportConstant.IS_LISTING_YES).equals(""+repMap.get("IS_LISTING"))){
//					}
					/*清单报表*/
					//报表表头信息
					List<ReportBean> repList = pushEmailDAO.getReportMesByReportId(reportId);
					//用户信息
					Map<String,Object> userMap = pushEmailDAO.getSendMesByReportIdAndUserId(reportId, userId);
					String sql = AbstractReportBuildSql.replaceMarcoSql("" + repMap.get("REPORT_TRANS_CODE_SQL"), null, Long.valueOf(reportId), userMap);
					String reportName = ""+repMap.get("REPORT_NAME");
					//报表数据
					Object[][] data = null;
					try{
						data = pushEmailDAO.getReportData(sql);
					}catch(Exception e){
					}
					//邮件信息封装
					String nowTime = DateUtil.getCurrentDay("yyyy-MM-dd");
					String title = reportName + "_" + nowTime;
					emailData.setSmtpHost(ReportConstant.HOSTNAME);
					emailData.setNeedAuth(true);
					String subject = title;
					emailData.setSubject(subject);
					String content = userMap.get("USER_NAMECN") + ",您好，您订阅的报表信息，敬请查收，谢谢！";
					emailData.setContent(content);
					String[] recipients = new String[]{""+userMap.get("USER_EMAIL")};
					emailData.setRecipients(recipients);
					emailData.setFromAddr(ReportConstant.FROMADDR);
					
					//生成excel文件（如果是清单只发送附件）
					String fileAddr = ReportConstant.MAILFILEPATH + title + ".xls";
					outPutExcel.save(repList, nowTime, reportName, data);
					emailData.setFileAddr(fileAddr);
					emailData.setFname(ReportConstant.FNAME);
					emailData.setFpwd(ReportConstant.FPWD);
					emailData.setMailPort(SystemVariable.getString("mail.port", ReportConstant.MAILPORT));
					
					//邮件发送
					if(sendEmail(emailData))
						userMap.put("SEND_FLAG", 1);
					else
						userMap.put("SEND_FLAG", 0);
					//删除附件
					outPutExcel.deletFile(fileAddr);
					
					/*封装日志表信息:注意直接放在userMap用户信息里面*/
					userMap.put("PUSH_CONFIG_ID", obj[r][6]);//订阅ID
					userMap.put("FACT_CONTENT", content);//实际发送内容
					userMap.put("DATA_ANNEX", title + ".xls");//实际发送附件名称
					//新增日志记录
					pushEmailDAO.savePushLog(userMap);
				}
			}
		}
		if(reportBuildDAO != null)
			reportBuildDAO.close();
		if(pushEmailDAO != null)
			pushEmailDAO.close();
	}
	/**
	 * @Title: isPush 
	 * @Description: 根据周期类型，基准时间判断是否需要发送
	 * @param sendSequnce
	 * @param time
	 * @return boolean   
	 * @throws
	 */
	public boolean isPush(int sendSequnce,String time){
		boolean jud = false;
		//现在时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = sdf.format(new Date());
		switch(sendSequnce){
			case ReportConstant.SEND_SEQUNCE_ONEOFF:{//一次性发送，必须匹配当天
				jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				break;
			}
			case ReportConstant.SEND_SEQUNCE_YEAR:{
				/*
				 * 先判断基准时间>=现在时间（到秒）  是--按该方法判断judMinutes；否--计算是否满1年
				 */
				if(!DateUtil.judDoubleTime(time, nowTime))
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				else{
					int counts = DateUtil.calMonthDiff(time);
					if(judMultiple(counts,12))
						jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				}
				break;
			}
			case ReportConstant.SEND_SEQUNCE_HALFYEAR:{
				if(!DateUtil.judDoubleTime(time, nowTime))
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				else{
					int counts = DateUtil.calMonthDiff(time);
					if(judMultiple(counts,6))
						jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				}
				break;
			}
			case ReportConstant.SEND_SEQUNCE_MONTH:{
				if(!DateUtil.judDoubleTime(time, nowTime))
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				else{
					int counts = DateUtil.calMonthDiff(time);
					if(judMultiple(counts,6))
						jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				}
				break;
			}
			case ReportConstant.SEND_SEQUNCE_WEEK:{
				if(!DateUtil.judDoubleTime(time, nowTime))
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				else if(DateUtil.judTimeIsSameWeek(time)){
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				}
				break;
			}
			case ReportConstant.SEND_SEQUNCE_DAY:{
				if(!DateUtil.judDoubleTime(time, nowTime))
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				else{
					time = time.replaceAll("-", "");
					time = time.replaceAll(":", "");
					time = time.replaceAll(" ", "");
					time = nowTime.substring(0,8) + time.substring(8,14);
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				}
				break;
			}
			case ReportConstant.SEND_SEQUNCE_HOUR:{
				if(!DateUtil.judDoubleTime(time, nowTime))
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				else{
					time = time.replaceAll("-", "");
					time = time.replaceAll(":", "");
					time = time.replaceAll(" ", "");
					time = nowTime.substring(0,10) + time.substring(10,14);
					jud = judMinutes(DateUtil.calTimeDiffBakMinutes(time, nowTime));
				}
				break;
			}
		}
		return jud;
	}
	private boolean judMinutes(long pmi){
		long delay = SystemVariable.getLong("timer.delay", 5*60*1000)/(60*1000);
		if(Math.abs(pmi) <= delay)
			return true;
		else
			return false;
	}
	private boolean judMultiple(int counts,int para){
		if(counts % para == 0)
			return true;
		else
			return false;
	}
}
