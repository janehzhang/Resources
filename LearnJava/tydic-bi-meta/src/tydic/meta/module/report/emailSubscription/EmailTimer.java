
/**   
 * @文件名: EmailTimer.java
 * @包 tydic.meta.module.report.emailSubscription
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-18 下午05:38:58
 *  
 */
  
package tydic.meta.module.report.emailSubscription;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import tydic.frame.SystemVariable;
import tydic.meta.common.DateUtil;
import tydic.meta.module.report.ReportConstant;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：EmailTimer   
 * 类描述：   
 * 创建人：wuxl@tydic.com 
 * 创建时间：2012-4-18 下午05:38:58   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class EmailTimer extends TimerTask{
	PushEmailDAO pushEmailDAO = new PushEmailDAO();
	PushEmailUtil pushEmailUtil = new PushEmailUtil();
	
	private final int getDataNumDefault = 100;//每次取数据100条
	private final int pushNumDefault = 10;//每次发送10封邮件
	
	@SuppressWarnings("unused")
	private ServletContext context;
	
	public EmailTimer(ServletContext servletContext){
		context = servletContext;
	}
	public void run() {
		//执行事务
		//获取数据的总条数
		int counts = pushEmailDAO.getCounts();
		int getDataNum = SystemVariable.getInt("timer.get.data.num", getDataNumDefault);//每次取数据条数
		int pagesCount = pushEmailUtil.getPagesCount(counts, getDataNum);//总取次数
		int startNum = 1;//开始取数值
		int endNum = 0;//结束取数值
		int pushNum = SystemVariable.getInt("timer.push.num", pushNumDefault);//每次发送条数
		/*******************************************************************************************
		 * dyData数据顺序：
		 * REPORT_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE,SEND_BASE_TIME,SEND_TIME_ADD,PUSH_CONFIG_ID
		 *******************************************************************************************/
		Object[][] dyData = null;
		for(int i = 1; i <= pagesCount; i++){//每循环一次，从数据库取对应条数据
			if(i >= 1)
				startNum = (i - 1) * getDataNum + 1;
			endNum = i * getDataNum;
			dyData = pushEmailDAO.getTestReportData(pushEmailDAO.getPushSql(), startNum, endNum);
			/**
			 * 按照配置的线程条数，开启线程：
			 * 1.线程处理完成，数据未完，线程接着处理下一条数据
			 * 2.数据全部处理完成，线程销毁
			 */
			
			
			
			int emailPushNum = pushEmailUtil.getPagesCount(dyData.length, pushNum);//总发送次数
			//处理邮件发送业务逻辑
			int start = 1;//起始
			int end = 0;//结束
			for(int ii = 1; ii <= emailPushNum; ii++){//需要将dyData按pushNum截取
				//获取此次循环需要的数组
				if(ii >= 1)
					start = (ii - 1) * pushNum + 1 - 1;
				end = ii * pushNum - 1;
				Object[][] obj = pushEmailUtil.getCutArray(dyData, start, end);
				obj = pushEmailUtil.getDelEmptyArray(obj, 0);
				//邮件发送处理
				pushEmailUtil.pushEmail(obj);
			}
		}
		//关闭数据库连接（先关闭）
		if(pushEmailDAO != null)
			pushEmailDAO.close();
	}
}
