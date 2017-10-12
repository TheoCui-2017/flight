package flight.spider.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.leg;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.service.scheduleJobService;
import flight.spider.web.utility.JSONresponse;

@Controller  
@RequestMapping("/leg")  
public class legController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;
	@Autowired 
	scheduleJobService scheduleService;
	
	 @RequestMapping(value="/list",method=RequestMethod.GET)   
	 public ModelAndView list() {
		 ModelAndView mav = new ModelAndView("leg");
		 List<leg> legInfo = flightService.getLegs();
		 mav.addObject("legInfo", legInfo);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/delete",method=RequestMethod.POST)   
	 public ModelAndView delete(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam String depart_city_code, @RequestParam String arrival_city_code) throws IOException{
		 leg legInfo = flightService.getLegByCodes(depart_city_code, arrival_city_code);
		 flightService.deleteLegByCodes(depart_city_code, arrival_city_code);
		 scheduleService.deleteJobByLeg(legInfo);
		 try {
			jsonresponse.PrintData(request,response,true);
		} catch (IOException e) {
			jsonresponse.PrintStatus(request,response,false);
			e.printStackTrace();
		}
		 return null;
	 }
	 
	 @RequestMapping(value="/edit",method=RequestMethod.GET)   
	 public ModelAndView edit(@RequestParam String depart_city_code, @RequestParam String arrival_city_code) {
		 ModelAndView mav = new ModelAndView("legEdit");
		 leg leg = flightService.getLegByCodes(depart_city_code, arrival_city_code);
		 mav.addObject("leg", leg);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/save",method=RequestMethod.POST)   
	 public ModelAndView save(HttpServletRequest request,HttpServletResponse response, 
			 @RequestParam String depart_city, @RequestParam String depart_city_code, 
			 @RequestParam String arrival_city, @RequestParam String arrival_city_code,
			 @RequestParam String area) throws IOException{
		 flightService.saveLeg(depart_city, depart_city_code, arrival_city, arrival_city_code, area);
		 try {
			 jsonresponse.PrintData(request,response,true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
	 
}
