package flight.spider.web.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.leg;
import flight.spider.web.bean.schedule;
import flight.spider.web.bean.scheduleJob;
import flight.spider.web.pipeline.sqlServerPipeline;
import flight.spider.web.processor.*;
import flight.spider.web.redis.redisHelper;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.service.scheduleJobService;

/**
 * 任务工厂类,非同步
 */
@DisallowConcurrentExecution
public class jobFactory implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
    	/*
    	sqlServerPipeline pipeline = (sqlServerPipeline)springContextUtil.getBean("sqlServerPipeline");
    	int days = context.getMergedJobDataMap().getInt("days");
    	int thread_num = context.getMergedJobDataMap().getInt("thread_num");
    	if("priceline".equals(context.getMergedJobDataMap().getString("groupname"))){
    		for(int i=1;i<=days;i++){
    			priceline instance = new priceline(context.getMergedJobDataMap().getString("departure_code"), 
            			context.getMergedJobDataMap().getString("arrival_code"),toolUtil.getFetureDate(i, "yyyy-MM-dd"),toolUtil.getFetureDate(i+7, "yyyy-MM-dd"), "ECO", "ADT","www.priceline.com",true);
            	instance.setPileline(pipeline);
            	if(thread_num>0)
            		instance.setThreadNum(thread_num);
            	System.out.println("thread_num===="+thread_num);
                System.out.println("jobName:" + context.getMergedJobDataMap().getString("jobname") + "  group:" + context.getMergedJobDataMap().getString("groupname")+
                		"-----line:"+context.getMergedJobDataMap().getString("departure_code")+"===="+context.getMergedJobDataMap().getString("arrival_code"));
                instance.test11();
                
//                priceline instance1 = new priceline(context.getMergedJobDataMap().getString("departure_code"), 
//            			context.getMergedJobDataMap().getString("arrival_code"),toolUtil.getFetureDate(i, "yyyy-MM-dd"), "BUS", "ADT","www.priceline.com");
//            	instance1.setPileline(pipeline);
//            	instance1.test11();
//            	
//            	priceline instance2 = new priceline(context.getMergedJobDataMap().getString("departure_code"), 
//            			context.getMergedJobDataMap().getString("arrival_code"),toolUtil.getFetureDate(i, "yyyy-MM-dd"), "FST", "ADT","www.priceline.com");
//            	instance2.setPileline(pipeline);
//            	instance2.test11();
    		}
    		System.out.println("priceline done");
    	}
    	else{
    		String source = "www.orbitz.com";
    		if("expedia".equals(context.getMergedJobDataMap().getString("groupname")))
    			source = "www.expedia.com";
    		for(int i=1;i<=days;i++){
	    		expedia_orbitzProcessor instance = new expedia_orbitzProcessor(context.getMergedJobDataMap().getString("departure_code"), 
	        			context.getMergedJobDataMap().getString("arrival_code"),toolUtil.getFetureDate(i, "MM/dd/yyyy"), toolUtil.getFetureDate(i+7, "MM/dd/yyyy"), "ECO", "ADT",source,true);
	        	instance.setPileline(pipeline);
	        	if(thread_num>0)
            		instance.setThreadNum(thread_num);
	        	 System.out.println("jobName:" + context.getMergedJobDataMap().getString("jobname") + "  group:" + context.getMergedJobDataMap().getString("groupname")+
	                		"-----line:"+context.getMergedJobDataMap().getString("departure_code")+"===="+context.getMergedJobDataMap().getString("arrival_code"));
	            instance.test();
	            
//	            expedia_orbitzProcessor instance1 = new expedia_orbitzProcessor(context.getMergedJobDataMap().getString("departure_code"), 
//	        			context.getMergedJobDataMap().getString("arrival_code"),toolUtil.getFetureDate(i, "MM/dd/yyyy"), "ECO", "ADT",source);
//	        	instance1.setPileline(pipeline);
//	        	instance1.test();
//	        	
//	        	expedia_orbitzProcessor instance2 = new expedia_orbitzProcessor(context.getMergedJobDataMap().getString("departure_code"), 
//	        			context.getMergedJobDataMap().getString("arrival_code"),toolUtil.getFetureDate(i, "MM/dd/yyyy"), "ECO", "ADT",source);
//	        	instance2.setPileline(pipeline);
//	        	instance2.test();
    		}
    		System.out.println("orbitz and expedia done");
    	}
    	 */
    	scheduleJobService scheduleService = (scheduleJobService)springContextUtil.getBean("scheduleJobService");
    	flightInfoService flightService = (flightInfoService)springContextUtil.getBean("flightInfoService");
    	String source = context.getMergedJobDataMap().getString("groupname");
    	System.out.println(source + " job start:" + new Date());
    	long start=System.currentTimeMillis();
    	schedule scheduleInfo = scheduleService.searchScheduleBySource(source);
    	String ip = toolUtil.getIp();
    	if(scheduleInfo.getIps() == null || scheduleInfo.getIps().length()==0 || scheduleInfo.getIps().contains(ip)){
    		initRedisJob(scheduleInfo,scheduleService,flightService);
    		ExecutorService threadPool = Executors.newFixedThreadPool(scheduleInfo.getConcurrency_num());
    		String job= "";
    		boolean hasNext = true;
    		int i=0;
    		while(hasNext){
    			job = redisHelper.spop(source+"_jobs");
    			if(job!=null && job.length()>0){
    				 
    				JSONObject jobJson = JSONObject.parseObject(job);
    				realTimeThread thread = new realTimeThread(jobJson.getString("departCode"),jobJson.getString("arrivalCode"),
    						jobJson.getString("goDate"),jobJson.getString("backDate"),jobJson.getString("cabinClass"),jobJson.getString("passenger"),
    						jobJson.getString("BASE_URL"),jobJson.getBoolean("round"),scheduleInfo.getConcurrency_num());
    				threadPool.execute(thread);
    			}else{
    				hasNext = false;
    			}
    			i++;
    		}
    		System.out.println(i+" jobs to run for "+source);
    		threadPool.shutdown();
    		while (!threadPool.isTerminated()) { }
    		long end=System.currentTimeMillis(); //获取结束时间
    		System.out.println(source+" job finish:"+new Date());
    		System.out.println("程序运行时间： "+(end-start)/(1000*60)+" 分钟");
    	}else
    		return ;
    	
    	
    }
    
    private void initRedisJob(schedule scheduleInfo, scheduleJobService scheduleService, flightInfoService flightService){
    	List<scheduleJob> scheduleJobs = scheduleService.getAllScheduleJobsBySource(scheduleInfo.getSource());
    	List<Integer> legids= new ArrayList<Integer>();
    	for(scheduleJob job : scheduleJobs){
    		legids.add(job.getLegid());
    	}
    	List<leg> legs = flightService.getLegsIn(legids);
    	for(leg legInfo : legs){
    		makeJsonJob(scheduleInfo,legInfo, false);
    		makeJsonJob(scheduleInfo,legInfo, true);
    	}
    	
    }
    
    private void makeJsonJob(schedule scheduleInfo, leg legInfo, boolean round){
    	 int days = scheduleInfo.getDays();
    	 String[] interval_days = scheduleInfo.getInterval_days().split(",");
    	for(int day=1;day<=days;day++){
    		JSONObject json = new JSONObject();
        	json.put("source", scheduleInfo.getSource());
        	json.put("departCode", legInfo.getDepartCityCode());
        	json.put("arrivalCode", legInfo.getArrivalCityCode());
        	json.put("passenger", "ADT");
        	json.put("BASE_URL", getSourceSite(scheduleInfo.getSource()));
        	json.put("round",round);
        	json.put("goDate", toolUtil.getFetureDate(day,"yyyy-MM-dd"));
        	if(!round){
        		saddAllTypeJson(json);
        	}else{
        		for(String interval : interval_days){
        			json.put("backDate",toolUtil.getFetureDate(day+Integer.parseInt(interval), "yyyy-MM-dd"));
        			saddAllTypeJson(json);
        		}
        	}
    	}
    	
    }
    
    private String getSourceSite(String source){
    	if(source.equals("orbitz"))
    		return "www.orbitz.com";
    	else if(source.equals("expedia"))
    		return "www.expedia.com";
    	else 
    		return "www.priceline.com";
    }
    
    private void saddEcoJson(JSONObject json){
    	JSONObject eco = new JSONObject(json);
    	eco.put("cabinClass", "ECO");
    	saddToRdis(eco);
    }
    
    private void saddBusJson(JSONObject json){
    	JSONObject bus = new JSONObject(json);
    	bus.put("cabinClass", "BUS");
    	saddToRdis(bus);
    }

    private void saddFstJson(JSONObject json){
    	JSONObject fst = new JSONObject(json);
    	fst.put("cabinClass", "FST");
    	saddToRdis(fst);
    }
    
    private void saddToRdis(JSONObject json){
    	String source = json.getString("source");
    	String jsonString = json.toJSONString();
    	if(!redisHelper.sismember(source+"_jobs", jsonString)){
    		redisHelper.sadd(source+"_jobs", jsonString);
    	}
    }
    
    private void saddAllTypeJson(JSONObject json){
    	saddEcoJson(json);
		saddBusJson(json);
		saddFstJson(json);
    }
}