package flight.spider.web.service.impl;


import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flight.spider.web.bean.leg;
import flight.spider.web.bean.schedule;
import flight.spider.web.bean.scheduleJob;
import flight.spider.web.dao.impl.legDAO;
import flight.spider.web.dao.impl.scheduleJobDAO;
import flight.spider.web.service.scheduleJobService;
import flight.spider.web.utility.scheduleUtils;
import flight.spider.web.utility.toolUtil;

@Service("scheduleJobService")
public class scheduleJobServiceImpl implements scheduleJobService{

	/*@Autowired
	private Scheduler scheduler;

	@Autowired
	private scheduleJobDAO scheduleJobDao;
	@Autowired
	private legDAO legDao;
	
	public void init() {
		List<scheduleJob> scheduleJobs = getScheduleJobs();
		for (scheduleJob scheduleJob : scheduleJobs) {
			try {
				scheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public scheduleJob saveScheduleJob(scheduleJob job){
		return scheduleJobDao.saveScheduleJob(job);
	}

	public List<scheduleJob> getScheduleJobs(){
		
		List<scheduleJob> scheduleJobs = scheduleJobDao.getScheduleJobs();
		List<leg> legs = legDao.getLegs();
		Map<Integer,leg> leg_map = getLegMap(legs);
		
		Iterator<scheduleJob> iter_job = scheduleJobs.iterator();
		while(iter_job.hasNext()){
			scheduleJob job = iter_job.next();
			if(leg_map.get(job.getLegid())!=null){
				job.setDeparture_code(leg_map.get(job.getLegid()).getDepartCityCode());
				job.setArrival_code(leg_map.get(job.getLegid()).getArrivalCityCode());
			}else{
				iter_job.remove();
			}
		}
		return scheduleJobs;
	}
	
	private Map<Integer,leg> getLegMap(List<leg> legs){
		
		Map<Integer,leg> leg_map= new HashMap<Integer,leg>();
		for(leg legInfo : legs){
			leg_map.put(legInfo.getId(), legInfo);
		}
		return leg_map;
	}

	public void deleteJobByLeg(leg legInfo){
		List<scheduleJob> scheduleJobs = scheduleJobDao.getScheduleJobsByLegId(legInfo.getId());
		try {
			for(scheduleJob job : scheduleJobs){
					scheduleUtils.deleteScheduleJob(scheduler, String.valueOf(job.getLegid()), job.getSource());
			}
			scheduleJobDao.deleteAll(scheduleJobs);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteJobBySourceLegId(String source, int legId){
		scheduleJob job = getScheduleJobBySourceLegId(source, legId);
		// 删除数据库该任务航段
		scheduleJobDao.deleteScheduleJob(job);
		try {
			// 删除该任务
			scheduleUtils.deleteScheduleJob(scheduler, String.valueOf(job.getLegid()), job.getSource());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteJobBySource(String source){
		List<scheduleJob> scheduleJobs = scheduleJobDao.getAllScheduleJobsBySource(source);
		try {
			for(scheduleJob job : scheduleJobs){
				scheduleUtils.deleteScheduleJob(scheduler, String.valueOf(job.getLegid()), job.getSource());
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void createJobBySource(String source){
		List<scheduleJob> scheduleJobs = scheduleJobDao.getScheduleJobsBySource(source);
		List<leg> legs = legDao.getLegs();
		Map<Integer,leg> leg_map = getLegMap(legs);
		
		Iterator<scheduleJob> iter_job = scheduleJobs.iterator();
		while(iter_job.hasNext()){
			scheduleJob job = iter_job.next();
			if(leg_map.get(job.getLegid())!=null){
				job.setDeparture_code(leg_map.get(job.getLegid()).getDepartCityCode());
				job.setArrival_code(leg_map.get(job.getLegid()).getArrivalCityCode());
			}else{
				iter_job.remove();
			}
		}
		try {
			for(scheduleJob job : scheduleJobs){
				scheduleUtils.createScheduleJob(scheduler, job);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void createJobByJob(scheduleJob job){
		try {
			scheduleUtils.createScheduleJob(scheduler, job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void restartScheduleJobBySource(String source){
		List<scheduleJob> scheduleJobs = scheduleJobDao.getScheduleJobsBySource(source);
		List<leg> legs = legDao.getLegs();
		Map<Integer,leg> leg_map = getLegMap(legs);
		
		Iterator<scheduleJob> iter_job = scheduleJobs.iterator();
		while(iter_job.hasNext()){
			scheduleJob job = iter_job.next();
			if(leg_map.get(job.getLegid())!=null){
				job.setDeparture_code(leg_map.get(job.getLegid()).getDepartCityCode());
				job.setArrival_code(leg_map.get(job.getLegid()).getArrivalCityCode());
			}else{
				iter_job.remove();
			}
		}
		for(scheduleJob job : scheduleJobs){
			try {
				// 删除任务
				scheduleUtils.deleteScheduleJob(scheduler, String.valueOf(job.getLegid()), job.getSource());
				// 添加任务
				scheduleUtils.createScheduleJob(scheduler, job);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<scheduleJob> getAllScheduleJobsBySource(String source){
		return scheduleJobDao.getAllScheduleJobsBySource(source);
	}
	
	public void updateScheduleJobStatusBySource(String source, int status){
		scheduleJobDao.updateScheduleJobStatusBySource(source, status);
	}
	
	public void updateScheduleJobInfoBySource(String source, String crond_time, int concurrency_num, int days){
		scheduleJobDao.updateScheduleJobInfoBySource(source, crond_time, concurrency_num, days);
	}
	
	public scheduleJob getScheduleJobBySourceLegId(String source, int legId){
		return scheduleJobDao.getScheduleJobBySourceLegId(source, legId);
	}*/
	@Autowired
	private Scheduler scheduler;

	@Autowired
	private scheduleJobDAO scheduleJobDao;
	@Autowired
	private legDAO legDao;
	
	public void init() {
		List<schedule> scheduleJobs = getScheduleJobs();
		String ip = toolUtil.getIp();
		for (schedule job : scheduleJobs) {
			try {
				if(job.getIps() == null || job.getIps().length()==0 || job.getIps().contains(ip))
				scheduleUtils.createScheduleJob(scheduler, job);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public scheduleJob saveScheduleJob(scheduleJob job){
		return scheduleJobDao.saveScheduleJob(job);
	}

	public List<schedule> getScheduleJobs(){
		
		List<schedule> schedules = scheduleJobDao.getSchedules();
		return schedules;
	}
	
	public void deleteJobBySource(String source){
		schedule job = scheduleJobDao.getScheduleBySource(source);
		if (job!=null){
			try {
					scheduleUtils.deleteScheduleJob(scheduler, job.getSource());
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteJobBySourceLegId(String source, String legIds){
		schedule scheduleInfo = scheduleJobDao.getScheduleBySource(source);
		// 删除数据库该任务航段
		scheduleJobDao.deleteScheduleJobsByscheduleAndIds(scheduleInfo,legIds);
	}
	
	public void createJobBySource(String source){
		schedule job = scheduleJobDao.getScheduleBySource(source);
		try {
				scheduleUtils.createScheduleJob(scheduler, job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void restartScheduleJobBySource(String source){
		schedule job = scheduleJobDao.getScheduleBySource(source);
		try {
			// 删除任务
			scheduleUtils.deleteScheduleJob(scheduler, job.getSource());
			// 添加任务
			scheduleUtils.createScheduleJob(scheduler, job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public List<scheduleJob> getAllScheduleJobsBySource(String source){
		return scheduleJobDao.getAllScheduleJobsBySource(source);
	}
	
	public List<scheduleJob> getAllScheduleJobsBySchedule(schedule scheduleInfo){
		return scheduleJobDao.getAllScheduleJobsBySchedule(scheduleInfo);
	}
	
	public void updateScheduleJobStatusBySource(String source, int status){
		scheduleJobDao.updateScheduleJobStatusBySource(source, status);
	}
	
	public void updateScheduleJobInfoBySource(String source, String crond_time, int concurrency_num, int days, 
			String interval_days, String ips, String start, String end){
		scheduleJobDao.updateScheduleJobInfoBySource(source, crond_time, concurrency_num, days, interval_days, ips, start, end);
	}
	
	public schedule searchScheduleBySource(String source){
		return scheduleJobDao.getScheduleBySource(source);
	}

	@Override
	public void deleteJobByLeg(leg legInfo) {
		List<scheduleJob> scheduleJobs = scheduleJobDao.getScheduleJobsByLegId(legInfo.getId());
		scheduleJobDao.deleteAll(scheduleJobs);
	}
	
	public Scheduler getScheduler(){
		return scheduler;
	}

	@Override
	public void runImmediately(String ota) {
		
		try {
			scheduleUtils.runOnce(scheduler, ota);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
