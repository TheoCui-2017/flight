package flight.spider.web.utility;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import flight.spider.web.bean.schedule;
import flight.spider.web.bean.scheduleJob;

/**
 * 定时任务辅助类
 */
public class scheduleUtils {

    /**
     * 获取触发器key
     * 
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static TriggerKey getTriggerKey( String jobGroup) {
        return TriggerKey.triggerKey("garpJob", jobGroup);
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler the scheduler
     * @param jobName the job name
     * @param jobGroup the job group
     * @return cron trigger
     * @throws SchedulerException 
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobGroup) throws SchedulerException {
    	TriggerKey triggerKey = TriggerKey.triggerKey("garpJob", jobGroup);
        return (CronTrigger) scheduler.getTrigger(triggerKey);
    }


    /**
     * 创建定时任务
     * @param scheduler
     * @param scheduleJob
     * @throws SchedulerException
     */

	public static void createScheduleJob(Scheduler scheduler, schedule scheduleInfo) throws SchedulerException {
		createScheduleJob(scheduler, scheduleInfo.getSource(), scheduleInfo.getCrond_time());
    }
	
	
    /**
     * 创建定时任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @param isSync
     * @param param
     * @param sch_classname
     * @param sch_classnamesync
     * @throws SchedulerException
     */

	public static void createScheduleJob(Scheduler scheduler, String jobGroup, String cronExpression) throws SchedulerException {
        //同步或异步
        Class<? extends Job> jobClass = jobFactory.class;
//        Class<? extends Job> jobClass = isSync ? JobSyncFactory.class : JobFactory.class;

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity("garpJob", jobGroup).build();

        //放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put("groupname", jobGroup);
        System.out.println("create task:"+jobGroup+".garpJob");
        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("garpJob", jobGroup)
            .withSchedule(scheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, trigger);
//        runOnce(scheduler, jobGroup);
    }
    
    /**
     * 运行一次任务
     * 
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException 
     */
    public static void runOnce(Scheduler scheduler, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("garpJob", jobGroup);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 暂停任务
     * 
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException 
     */
    public static void pauseJob(Scheduler scheduler, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("garpJob", jobGroup);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException 
     */
    public static void resumeJob(Scheduler scheduler, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("garpJob", jobGroup);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 获取jobKey
     *
     * @param jobName the job name
     * @param jobGroup the job group
     * @return the job key
     */
    public static JobKey getJobKey(String jobGroup) {
        return JobKey.jobKey("garpJob", jobGroup);
    }
    

    /**
     * 删除定时任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException 
     */
    public static void deleteScheduleJob(Scheduler scheduler,  String jobGroup) throws SchedulerException {
    	scheduler.deleteJob(getJobKey(jobGroup));
    	System.out.println("delete task:"+jobGroup+".garpJob");
    }
    
}