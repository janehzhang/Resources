
/**   
 * @文件名: TimerListener.java
 * @包 tydic.meta.web.session
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-18 下午05:26:16
 *  
 */
  
package tydic.meta.web.session;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import tydic.frame.SystemVariable;
import tydic.meta.module.report.emailSubscription.ThreadEmailTimer;



/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：TimerListener   
 * 类描述：   
 * 创建人：wuxl@tydic.com 
 * 创建时间：2012-4-18 下午05:26:16   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

@SuppressWarnings("serial")
public class TimerListener extends HttpServlet implements ServletContextListener{
	private final long delayDefault = 5*60*1000;//毫秒  (5分钟)
	private Timer timer = null;
	public void contextDestroyed(ServletContextEvent sce) {
		timer.cancel();
		sce.getServletContext().log("定时器销毁");
	}
	public void contextInitialized(ServletContextEvent sce) {
		timer = new Timer(true);
		sce.getServletContext().log("定时器已启动");
		/******************************************************************************
		timer.schedule(task, delay)
		task:任务
		delay：隔好久执行
		*******************************************************************************/
		long delay = SystemVariable.getLong("timer.delay", delayDefault);
		timer.schedule(new ThreadEmailTimer(),0,delay);
//		timer.schedule(new EmailTimer(sce.getServletContext()),0,delay);
		sce.getServletContext().log("已经添加任务调度表");
	}
}
