
/**   
 * @文件名: TimerInit.java
 * @包 tydic.meta.web
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-28 上午09:55:50
 *  
 */
  
package tydic.meta.web;

import java.util.Timer;

import javax.servlet.ServletContext;

import tydic.frame.common.Log;
import tydic.frame.web.ISystemStart;
import tydic.meta.web.job.QQ10000TimerTask;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：TimerInit   
 * 类描述：   
 * 创建人：     颜海东
 * 创建时间：2012-4-28 上午09:55:50   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class TimerInit implements ISystemStart{
	
	private Timer timer = null;
	
	public void destory() {
		timer.cancel();
		Log.debug("定时器已销毁！");
	}

	public void init() {
		timer = new Timer(true);
		Log.debug("定时器已启动！");
		/******************************************************************************
		timer.schedule(task, delay)
		task:任务
		delay：隔好久执行
		*******************************************************************************/
		//long delay = ReportConstant.TIMERDELAY;
		//timer.schedule(new ThreadEmailTimer(),0,delay);
		timer.schedule(new QQ10000TimerTask(),0,1*24*60*60*1000);
	}

	public void setServletContext(ServletContext sce) {
	}
}
