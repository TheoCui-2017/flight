package flight.spider.web.service;

import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import flight.spider.web.bean.leg;
import flight.spider.web.bean.schedule;
import flight.spider.web.bean.scheduleJob;
import flight.spider.web.utility.scheduleUtils;

public interface scheduleJobService {

	public void init();
	
	public scheduleJob saveScheduleJob(scheduleJob job);

	public List<schedule> getScheduleJobs();
	
	public void deleteJobBySource(String source);
	
	public void deleteJobBySourceLegId(String source, String legIds);
	
	public void createJobBySource(String source);
	
	public void restartScheduleJobBySource(String source);
	
	public List<scheduleJob> getAllScheduleJobsBySource(String source);
	
	public List<scheduleJob> getAllScheduleJobsBySchedule(schedule scheduleInfo);
	
	public void updateScheduleJobStatusBySource(String source, int status);
	
	public void updateScheduleJobInfoBySource(String source, String crond_time, int concurrency_num, int days, String interval_days, String ips, String start, String end);
	
	public schedule searchScheduleBySource(String source);
	
	public void deleteJobByLeg(leg legInfo);

	public Scheduler getScheduler();
	
	public void runImmediately(String ota);
}
