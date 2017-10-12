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

@Controller  
@RequestMapping("/user")  
public class userController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;

	@RequestMapping(value="/privilege",method=RequestMethod.GET)   
	public ModelAndView info(@RequestParam(required=false) String username){
		ModelAndView mav = new ModelAndView("privilege_user");
		return mav;  
	}
	
	@RequestMapping(value="/savePrivilege",method=RequestMethod.POST)   
	 public ModelAndView savePrivilege(HttpServletRequest request,HttpServletResponse response, 
			 @RequestParam String username, @RequestParam String privileges) throws IOException{
		List<String> privilege_list= Arrays.asList(privileges.split(","));
		flightService.savePrivilegeByUsername(username,privilege_list);
		try {
			jsonresponse.PrintData(request,response,true);
		} catch (IOException e) {
			jsonresponse.PrintStatus(request,response,false);
			e.printStackTrace();
		}
		 return null;
	 }
}
