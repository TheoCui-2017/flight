package flight.spider.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import flight.spider.web.processor.expedia_orbitzProcessor;
import flight.spider.web.processor.priceline;
import flight.spider.web.redis.redisHelper;
import flight.spider.web.service.scheduleJobService;
import flight.spider.web.utility.JSONresponse;
import flight.spider.web.utility.toolUtil;
@Controller  
@RequestMapping("/api")
public class apiController {  
    
	@Autowired 
	private JSONresponse jsonresponse;
	@Autowired 
	private scheduleJobService scheduleService;
    @RequestMapping(value="/user",method=RequestMethod.GET)   
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	System.out.println("------------------------------------------------");
    	System.out.println(redisHelper.get("linkshan"));
    	System.out.println(toolUtil.getIp());
    	try {
    		JSONObject obj = new JSONObject();
			obj.put("name", "linshan");
			obj.put("haha", "hahahah");
			scheduleService.deleteJobBySource("priceline");
			jsonresponse.PrintData(request,response,obj);
			
		} catch (JSONException e) {
			jsonresponse.PrintStatus(request,response,false);
			e.printStackTrace();
		}
    	return null;
    }  
} 