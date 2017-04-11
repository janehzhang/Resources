package tydic.meta.module.mag.timer;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 定时任务总接口类，对于每一个定时任务而言，需要实现此接口,每个定时任务实现类运行时采用单例模式，
 * 即系统执行同一个定时任务其类是单例的。<br>
 * @date 2012-03-05
 */
public interface IMetaTimer{

    /**
     * 第一次初始化调用的方法
     */
    public void init();

    /**
     * 定时任务每次执行调用的方法
     * @param timerName timer唯一标识,如果是数据库中配置的Timer，其TIMER构成表达式为
     * TIMERID+"_"+System.nanoTime()
     */
    public void run(String timerName);

}
