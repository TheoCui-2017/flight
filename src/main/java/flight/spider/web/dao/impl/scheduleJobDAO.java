package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class scheduleJobDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public scheduleJobDAO() {
		super();
	}
	
	public scheduleJob saveScheduleJob(scheduleJob scheduleJobInfo){
		this.save(scheduleJobInfo);
        return scheduleJobInfo;
	}
	
	public void updateScheduleJob(scheduleJob scheduleJobInfo){
		this.update(scheduleJobInfo);
	}
	
	public void deleteScheduleJob(scheduleJob scheduleJobInfo){
		this.delete(scheduleJobInfo);
	}
	
	public schedule saveSchedule(schedule scheduleInfo){
		this.save(scheduleInfo);
        return scheduleInfo;
	}
	
	public void updateSchedule(schedule scheduleInfo){
		this.update(scheduleInfo);
	}
	
	// 获取航段
	public List<scheduleJob> getScheduleJobs(){
		List<scheduleJob> list = this.getSession()
				.createQuery("from scheduleJob where status=1")
				.list();
		return list;
	}

	// 根据城市代码获取全部航段
	public List<scheduleJob> getAllScheduleJobsBySource(String source){
		schedule scheduleInfo= getScheduleBySource(source);
		if(scheduleInfo !=null){
			List<scheduleJob> list = this.getSession()
					.createQuery("from scheduleJob where scheduleid =:scheduleid")
					.setInteger("scheduleid", scheduleInfo.getId())
					.list();
			return list;
		}
		return null;
	}
	
	// 根据城市代码获取全部航段
		public List<scheduleJob> getAllScheduleJobsBySchedule(schedule scheduleInfo){
			if(scheduleInfo !=null){
				List<scheduleJob> list = this.getSession()
						.createQuery("from scheduleJob where scheduleid =:scheduleid")
						.setInteger("scheduleid", scheduleInfo.getId())
						.list();
				return list;
			}
			return null;
		}
	
	// 根据leg获取定时任务
	public List<scheduleJob> getScheduleJobsByLegId(int legid){
		List<scheduleJob> list = this.getSession()
				.createQuery("from scheduleJob where legid =:legid")
				.setInteger("legid", legid)
				.list();
		return list;
	}
	
	// 根据source修改状态
	public void updateScheduleJobStatusBySource(String source, int status){
		schedule scheduleInfo =getScheduleBySource(source);
		scheduleInfo.setStatus(status);
		updateSchedule(scheduleInfo);
		
	}
	
	// 根据source修改信息
	public void updateScheduleJobInfoBySource(String source, String crond_time, int concurrency_num, int days, 
			String interval_days, String ips, String start, String end){
		this.getSession().createSQLQuery("update schedule set crond_time=:crond_time, concurrency_num=:concurrency_num, days=:days, interval_days=:interval_days, ips=:ips, start_at=:start, end_at=:end, update_at=now() where source=:source and status=1")
		.setString("source", source)
		.setString("crond_time", crond_time)
		.setInteger("concurrency_num", concurrency_num)
		.setInteger("days", days)
		.setString("interval_days", interval_days)
		.setString("ips", ips)
		.setString("start", start)
		.setString("end", end)
		.executeUpdate();
	}
	
	public List<schedule> getSchedules(){
		List<schedule> list = this.getSession()
				.createQuery("from schedule where status=1")
				.list();
		return list;
	}
	
	public schedule getScheduleBySource(String source){
		List<schedule> list = this.getSession()
				.createQuery("from schedule where source=:source")
				.setString("source", source)
				.list();
		
		schedule scheduleInfo = (list==null || list.size()<1) ? null : list.get(0);
		if(scheduleInfo == null){
			scheduleInfo = new schedule(source, new Date());
			scheduleInfo = saveSchedule(scheduleInfo);
		}
		return scheduleInfo;
	}
	
	public void deleteScheduleJobsByscheduleAndIds(schedule scheduleInfo, String legIds){
		this.getSession().createSQLQuery("delete from scheduleJob where scheduleid=:scheduleid and legid in(:ids)")
		.setInteger("scheduleid", scheduleInfo.getId())
		.setString("ids", legIds)
		.executeUpdate();
	}
}
