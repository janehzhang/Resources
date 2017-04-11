
/**   
 * @文件名: TreadEmailTimer.java
 * @包 tydic.meta.module.report.emailSubscription
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-25 上午09:26:47
 *  
 */
  
package tydic.meta.module.report.emailSubscription;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import tydic.frame.common.Log;
import tydic.meta.module.report.ReportConstant;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：TreadEmailTimer   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-25 上午09:26:47   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ThreadEmailTimer extends TimerTask{
	private ThreadPool threadPool = null;
	
	public ThreadEmailTimer(){
	}
	
	public void run() {
		//执行守护线程事务
		//获取同一时间点需要发送数据
		PushEmailDAO pushEmailDAO = new PushEmailDAO();
		Object[][] obj = null;
		try{
			obj = pushEmailDAO.getPushDataBySameTimeToObj(pushEmailDAO.getPushSql());
			if(obj != null && obj.length > 0){
				/**
				 * 开启处理线程
				 */
				//默认创建一个不超过最大工作线程的线程池  
				if(threadPool == null){
					if(obj.length <= ReportConstant.TIMERTHREADPOOLSIZE){
						threadPool = ThreadPool.getInstance();
						threadPool.init(obj.length);
					}else{
						threadPool = ThreadPool.getInstance();
						threadPool.init(ReportConstant.TIMERTHREADPOOLSIZE);
					}
				}
		        Thread.sleep(500); //休眠500毫秒,以便让线程池中的工作线程全部运行 
		        /*处理前时间（毫秒）*/
		        long startTimeMillis = System.currentTimeMillis();
		        //运行任务  
		        for (int i = 0; i < obj.length ; i++) {//任务  
		        	Object[][] o = new Object[1][obj[i].length];
		        	for(int c = 0; c < obj[i].length; c++){
		        		o[0][c] = obj[i][c];
		        	}
		            threadPool.execute(ThreadPush.createTask(o));
		        } 
		        /*处理完成时间（毫秒）*/
		        long endTimeMillis = System.currentTimeMillis();
		        Log.debug("任务数为>>>"+obj.length+"  总耗时为(毫秒)>>>>"+(endTimeMillis-startTimeMillis));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pushEmailDAO != null)
				pushEmailDAO.close();
		}
	}
}
