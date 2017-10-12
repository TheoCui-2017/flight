package flight.spider.web.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class financeCrontab{
	@Autowired
	finance financeService;
	
	@Scheduled(cron="0 0 7  * * ? ")   //每天早上7点更新    
	public void run(){   
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    System.out.println(sdf.format(new Date())+"-- 开始更新汇率");
	    financeService.updateRate();
	    System.out.println(sdf.format(new Date())+"-- 更新汇率完成");
	}    
}