package flight.spider.web.utility;

import flight.spider.web.pipeline.sqlServerPipeline;
import flight.spider.web.processor.expedia_orbitzProcessor;
import flight.spider.web.processor.priceline;

public class realTimeThread implements Runnable{
	private String departCode ;
	private String arrivalCode ;
	private String goDate ;
	private String backDate;
	private String cabinClass ;
	private String passenger = "ADT";
	private String BASE_URL;
	private boolean round;
	private int threads = 10;
	
	public realTimeThread(String departCode, String arrivalCode, String goDate, String backDate, 
			String cabinClass, String passenger, String BASE_URL, boolean round, int threads){
		this.departCode = departCode;
		this.arrivalCode = arrivalCode;
		this.goDate = goDate;
		this.backDate = backDate;
		this.cabinClass = cabinClass;
		this.BASE_URL = BASE_URL;
		this.round = round;
		this.threads = threads;
	}
	
	@Override
	public void run() {
		sqlServerPipeline pipeline = (sqlServerPipeline)springContextUtil.getBean("sqlServerPipeline");
		if(BASE_URL.contains("priceline")){
			priceline instance = new priceline(departCode,arrivalCode,goDate,backDate,cabinClass,passenger,BASE_URL,round);
			instance.setPileline(pipeline);
			instance.setThreadNum(threads);
			instance.test11();
		}else if(BASE_URL.contains("orbitz") || BASE_URL.contains("expedia")){
			expedia_orbitzProcessor instance= new expedia_orbitzProcessor(departCode,arrivalCode,toolUtil.formatDate(goDate, "MM/dd/yyyy"),
					toolUtil.formatDate(backDate, "MM/dd/yyyy"),cabinClass,passenger,BASE_URL,round);
			instance.setPileline(pipeline);
			instance.setThreadNum(threads);
			instance.test();
		}else
			return;
		
	}

}
