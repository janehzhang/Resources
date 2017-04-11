
/**   
 * @文件名: ThreadPush.java
 * @包 tydic.meta.module.report.emailSubscription
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-25 上午11:21:38
 *  
 */
  
package tydic.meta.module.report.emailSubscription;

import java.util.List;
import java.util.Map;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.meta.common.DateUtil;
import tydic.meta.mail.EmailData;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.module.report.build.AbstractReportBuildSql;
import tydic.meta.module.report.build.ReportBuildDAO;
import tydic.meta.module.report.repExcel.OutPutExcel;
import tydic.meta.module.report.repExcel.ReportBean;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ThreadPush   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-25 上午11:21:38   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ThreadPush{
	public static Runnable createTask(final Object[][] obj) {  
		/**
		 * obj顺序
		 * REPORT_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE,SEND_BASE_TIME,SEND_TIME_ADD,PUSH_CONFIG_ID
		 */
        return new Runnable() {  
            public void run() { 
            	if(obj != null){
            		//执行任务
            		Log.debug("被调度的报表ID为>>>>>"+obj[0][0]+" 订阅ID为>>>>"+obj[0][6]);
            		
                	PushEmailUtil pushEmailUtil = new PushEmailUtil();
                	PushEmailDAO pushEmailDAO = new PushEmailDAO();
            		try{
                    	/**根据SEND_SEQUNCE判断需要发送的数据**/
                    	if(pushEmailUtil.isPush(Integer.valueOf(""+obj[0][3]),""+obj[0][4])){
                    		/**根据PUSH_CONFIG_ID判断是否已发送，如果发送出错需再次发送：超过规定出错次数需调用短信接口，短信提醒**/
                    		long pushConfigId = Long.valueOf(""+obj[0][6]);
                    		Map<String,Object> logMap = pushEmailDAO.getPushLog(pushConfigId);
                    		int suc_counts = Integer.valueOf(""+logMap.get("SUC_COUNTS"));
                    		int err_counts = Integer.valueOf(""+logMap.get("ERR_COUNTS"));
                    		if(suc_counts == 0 && err_counts <= ReportConstant.TIMERPUSHERRORNUMLOG){
                    			int push_errCounts = 0;
                    			if(!exePush(obj)){//已经执行了一次
                    				push_errCounts++;
                    				while(push_errCounts <= ReportConstant.TIMERPUSHERRORNUMLOG){
                    					if(!exePush(obj))
                    						push_errCounts++;
                    				}
                    				if(push_errCounts > ReportConstant.TIMERPUSHERRORNUMLOG){
                    					//短信提示
                    				}
                    			}
                    		}
                    		if(suc_counts == 0 && err_counts > ReportConstant.TIMERPUSHERRORNUMLOG){
                    			//短信提示
                    		}
                    	}
            		}catch(Exception e){
            			e.printStackTrace();
            		}finally{
            			if(pushEmailDAO != null)
            				pushEmailDAO.close();
            		}
            		
            	}else
            		Log.debug(ThreadPush.class+"邮件发送线程时，没有数据需要发送");
            }  
        };  
    } 
	
	static boolean exePush(Object[][] obj){
		/**
		 * obj顺序
		 * REPORT_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE,SEND_BASE_TIME,SEND_TIME_ADD,PUSH_CONFIG_ID
		 */
		boolean jud = true;
		PushEmailUtil pushEmailUtil = new PushEmailUtil();
    	ReportBuildDAO reportBuildDAO = new ReportBuildDAO();
    	PushEmailDAO pushEmailDAO = new PushEmailDAO();
    	EmailData emailData = new EmailData();
    	OutPutExcel outPutExcel = new OutPutExcel();
		try{
			String reportId = "" + obj[0][0];
			String userId = "" + obj[0][1];
			//报表信息
			Map<String,Object> repMap = reportBuildDAO.getReportSqlByReportId(reportId);
			//清单报表判断
//			if((""+ReportConstant.IS_LISTING_YES).equals(""+repMap.get("IS_LISTING"))){
//			}
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
			if(pushEmailUtil.sendEmail(emailData)){
				userMap.put("SEND_FLAG", 1);
				jud = true;
			}else{
				userMap.put("SEND_FLAG", 0);
				jud = false;
			}
			//删除附件
			outPutExcel.deletFile(fileAddr);
			
			/*封装日志表信息:注意直接放在userMap用户信息里面*/
			userMap.put("PUSH_CONFIG_ID", obj[0][6]);//订阅ID
			userMap.put("FACT_CONTENT", content);//实际发送内容
			userMap.put("DATA_ANNEX", title + ".xls");//实际发送附件名称
			//新增日志记录
			pushEmailDAO.savePushLog(userMap);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reportBuildDAO != null)
				reportBuildDAO.close();
			if(pushEmailDAO != null)
				pushEmailDAO.close();
		}
		return jud;
	}
}
