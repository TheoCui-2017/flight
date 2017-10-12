package flight.spider.web.utility;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import flight.spider.web.service.scheduleJobService;

/**
 * 定时任务初始化
 */
@Component
public class scheduleJobInit {
	@Autowired
    private scheduleJobService  schedule_jobService;

    /**
     * 项目启动时初始化
     */
    @PostConstruct
    public void init() {
    	schedule_jobService.init();
    }
}
