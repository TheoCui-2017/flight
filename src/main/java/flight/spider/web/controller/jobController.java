package flight.spider.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.leg;
import flight.spider.web.bean.schedule;
import flight.spider.web.bean.scheduleJob;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.service.scheduleJobService;
import flight.spider.web.utility.JSONresponse;
import flight.spider.web.utility.toolUtil;

@Controller  
@RequestMapping("/job")  
public class jobController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;
	@Autowired 
	scheduleJobService scheduleService;
	
	 @RequestMapping(value="/info",method=RequestMethod.GET)   
	 public ModelAndView info(@RequestParam(required=false) String ota){
		 String source = ota;
		 if(source==null || source.length()==0)
			 source = "priceline";
		 ModelAndView mav = new ModelAndView("job");
		 schedule scheduleInfo = scheduleService.searchScheduleBySource(source);
		 List<scheduleJob> scheduleJobInfo = scheduleService.getAllScheduleJobsBySchedule(scheduleInfo);
		 List<leg> legInfo = flightService.getLegs();
		 JSONObject jobInfo = doManipulation(scheduleInfo,scheduleJobInfo,legInfo,source);
		 mav.addObject("jobInfo", jobInfo);
		 
		 return mav;  
	 }
	 
	 private JSONObject doManipulation(schedule scheduleInfo, List<scheduleJob> scheduleJobInfo, List<leg> legInfo, String source){
		 JSONObject json = new JSONObject();
		 JSONArray selectLegs = new JSONArray();
		 JSONArray leftLegs = new JSONArray();
		 Set<Integer> legIds = new HashSet<Integer>();
		 String crond_time = "";
		 json.put("source", source);
		 json.put("days", scheduleInfo.getDays());
		 json.put("threads", scheduleInfo.getConcurrency_num());
		 if(scheduleInfo.getStatus() == 1)
			 json.put("status", scheduleInfo.getStatus());
		 crond_time = scheduleInfo.getCrond_time();
		 if(!json.containsKey("status")){
			 json.put("status", 0);
		 }
		 if(crond_time.length() > 0 && crond_time != null){
			 json.put("duration", crond_time.split("0\\/")[1].split(" ")[0]);
		 }
		 json.put("interval_days", scheduleInfo.getInterval_days());
		 json.put("ips", scheduleInfo.getIps());
		 json.put("start", scheduleInfo.getStart_at());
		 json.put("end", scheduleInfo.getEnd_at());
		 for(scheduleJob job : scheduleJobInfo){
			 legIds.add(job.getLegid());
		 }
		 // 遍历legInfo
		 HashMap<Integer, leg> leg_map = new HashMap<Integer, leg>();
		 Set<Integer> legAllIds = new HashSet<Integer>();
		 for(leg legIndex : legInfo){
			 legAllIds.add(legIndex.getId());
			 leg_map.put(legIndex.getId(), legIndex);
		 }
		 Iterator<Integer> selectIt = legIds.iterator();
		 while (selectIt.hasNext()) {  
			 int id = selectIt.next();
			 selectLegs.add(leg_map.get(id));
		 }
		 json.put("selectLegs", selectLegs);
		 
		 legAllIds.removeAll(legIds);
		 
		 Iterator<Integer> leftIt = legAllIds.iterator();
		 while (leftIt.hasNext()) {  
			 int id = leftIt.next();
			 leftLegs.add(leg_map.get(id));
		 }
		 json.put("leftLegs", leftLegs);
		 
		 return json;
	 }
	 
	 @RequestMapping(value="/update",method=RequestMethod.POST)   
	 public ModelAndView updae(HttpServletRequest request,HttpServletResponse response, 
			 @RequestParam String source,  @RequestParam int status) throws IOException{
		 // 更新数据库状态
		 scheduleService.updateScheduleJobStatusBySource(source, status);
		 // 删除schedule job任务
		 if(status == 0){
			 scheduleService.deleteJobBySource(source);
		 }
		 else if(status == 1){
			 // 添加schedule job任务
			 scheduleService.createJobBySource(source);
		 }
		 try {
			 jsonresponse.PrintData(request,response,true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
	 
	 @RequestMapping(value="/leg",method=RequestMethod.POST)   
	 public ModelAndView legRequest(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam String source,@RequestParam String legIds, 
			 @RequestParam String flag) throws IOException{
		 String[] legids = legIds.split(",");
		 schedule scheduleInfo = scheduleService.searchScheduleBySource(source);
		 if(flag.equals("add")){
			 // 添加航段
			 for(String legId : legids){
				 scheduleJob job = new scheduleJob(Integer.parseInt(legId),scheduleInfo.getId(), new Date());
				 scheduleService.saveScheduleJob(job);
			 }
		 }
		 else if(flag.equals("del")){
				 scheduleService.deleteJobBySourceLegId(source,legIds);
		 }
		 try {
			 jsonresponse.PrintData(request,response,true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
	 
	 @RequestMapping(value="/save",method=RequestMethod.POST)   
	 public ModelAndView save(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam String source, @RequestParam int thread, 
			 @RequestParam String duration, @RequestParam int days, 
			 @RequestParam String interval_days, @RequestParam String ips,
			 @RequestParam String start, @RequestParam String end) throws IOException{
		 String crond_time = "0 0 0/" + duration + " * * ?";
		 start = toolUtil.formatDate(start, "yyyy-MM-dd HH:mm:ss");
		 end = toolUtil.formatDate(end, "yyyy-MM-dd HH:mm:ss");
		 scheduleService.updateScheduleJobInfoBySource(source, crond_time, thread, days, interval_days,ips, start, end);
		 scheduleService.restartScheduleJobBySource(source);
		 try {
			 jsonresponse.PrintData(request,response,true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
		 return null; 
	 }
	 
	 @RequestMapping(value="/run",method=RequestMethod.GET)   
	 public ModelAndView info(){
		 List<schedule> schedules = scheduleService.getScheduleJobs();
		 for(schedule s : schedules){
			 scheduleService.runImmediately(s.getSource());
		 }
		 return null;  
	 }
	 
}
