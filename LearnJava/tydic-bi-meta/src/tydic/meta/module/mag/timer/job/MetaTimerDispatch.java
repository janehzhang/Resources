package tydic.meta.module.mag.timer.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import tydic.frame.common.Log;
import tydic.meta.module.mag.timer.IMetaTimer;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description META系统任务总调度器，此类根据META_MAG_TIMER的配置调度ImetaTimer的实现类。Quartz触发器每次任务
 * 执行时都是调度的此类 <br>
 * @date 2012-03-05
 */
public class MetaTimerDispatch implements Job{

    /**
     * 各定时器的实现缓存
     */
    private final static  Map<String,IMetaTimer> metaTimerMap=new HashMap<String, IMetaTimer>();

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        //实例化
        String timerName=jobExecutionContext.getJobDetail().getName();
        IMetaTimer metaTimer=null;
        if(!metaTimerMap.containsKey(timerName)){
            JobDataMap jobDataMap= jobExecutionContext.getMergedJobDataMap();
            //获取执行任务的Class
            String className=(String)jobDataMap.get(MetaTimerAssign.CLASS_KEY);
            //实例化类
            try{
                metaTimer=(IMetaTimer)(Class.forName(className).newInstance());
                metaTimer.init();
                metaTimerMap.put(timerName,metaTimer);
            } catch(InstantiationException e){
                Log.error(null,e);
            } catch(IllegalAccessException e){
                Log.error(null,e);
            } catch(ClassNotFoundException e){
                Log.error(null,e);
            }
        }else {
            metaTimer=metaTimerMap.get(timerName);
        }
        if(metaTimer==null){// 如果任务对象为NULL，说明初始化失败，移除此调度器
             MetaTimerAssign.removeTimer(timerName);
        }else{
            Log.info("TIMER RUN START:"+timerName);
            try {
                metaTimer.run(timerName);
            } catch (Exception e) {
                Log.error(null,e);
            }
            Log.info("TIMER RUN END:"+timerName);
        }
    }
}
