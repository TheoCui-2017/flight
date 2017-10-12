package flight.spider.web.service;

import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import flight.spider.web.bean.leg;
import flight.spider.web.bean.schedule;
import flight.spider.web.bean.scheduleJob;
import flight.spider.web.bean.user;
import flight.spider.web.utility.scheduleUtils;

public interface userService {

	public user saveUser(user userInfo);
	
	public user getUserByUsername(String username);

	public void updateUser(user userInfo);

}
