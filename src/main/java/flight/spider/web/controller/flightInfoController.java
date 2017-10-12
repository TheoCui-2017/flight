package flight.spider.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import flight.spider.web.bean.airport;
import flight.spider.web.bean.city;
import flight.spider.web.bean.flight;
import flight.spider.web.bean.flightDetail;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.JSONresponse;
import flight.spider.web.utility.finance;
import flight.spider.web.utility.realTimeThread;

@Controller  
@RequestMapping("/flight")  
public class flightInfoController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;
	
	
	@RequestMapping(value="/search",method=RequestMethod.GET)   
    public ModelAndView search() {
    	ModelAndView mav = new ModelAndView("flightSearch");
        return mav;  
    }
	
	@RequestMapping(value="/searchDetail",method=RequestMethod.POST)   
    public ModelAndView searchDetail(@RequestParam boolean round, @RequestParam String departure_city,
    		@RequestParam String arrival_city,@RequestParam String go_date,
    		@RequestParam String back_date, @RequestParam int seat_type,
    		@RequestParam String sources, @RequestParam int size) {
    	ModelAndView mav = new ModelAndView("searchResult");
    	List<String> source_list= Arrays.asList(sources.split(","));
    	List<flightDetail> flightsDetails = flightService.getFlightDetails(round, departure_city, arrival_city, go_date, back_date, seat_type, source_list, size);
    	mav.addObject("flightsDetails", flightsDetails);
        return mav;  
    }
	
	@RequestMapping(value="/searchNow",method=RequestMethod.POST)   
    public ModelAndView searchRealTimeDetail(@RequestParam boolean round, @RequestParam String departure_city,
    		@RequestParam String arrival_city,@RequestParam String go_date,
    		@RequestParam String back_date, @RequestParam int seat_type,
    		@RequestParam String sources, @RequestParam int size) {
		long start=System.currentTimeMillis(); 
    	ModelAndView mav = new ModelAndView("searchResult");
    	List<String> source_list= Arrays.asList(sources.split(","));
    	
    	List<flightDetail> flightsDetails = flightService.getFlightDetails(round, departure_city, arrival_city, go_date, back_date, seat_type, source_list, size);
    	boolean search = false;
    	if(flightsDetails.size()>0){
    		long updateTime = flightsDetails.get(0).getUpdateTime().getTime();
    		long reqTime = new Date().getTime();
    		if((reqTime - updateTime)/(1000*60*60) > 1){
    			search = true;
    		}else{
    			List<String> tmp= new ArrayList(Arrays.asList(sources.split(",")));
    			for(flightDetail fd : flightsDetails){
    				if(tmp.contains(fd.getSource())){
    					tmp.remove(fd.getSource());
    				}
    			}
    			if(tmp.size() != 0){
    				search = true;
    			}
    		}
    	}else{
    		search = true;
    	}
    	if(search){
    		ExecutorService threadPool = Executors.newFixedThreadPool(source_list.size());
        	String cabinClass = "ECO";
        	if(seat_type == 2)
        		cabinClass = "BUS";
        	else if(seat_type == 3)
        		cabinClass = "FST";
        	
        	// departure_city 转化
        	String depart_city_code = departure_city;
        	airport airportInfo = flightService.getAirportByCode(departure_city);
        	if(airportInfo != null){
        		int cityId = airportInfo.getCityId();
        		city cityInfo = flightService.getCityById(cityId+"");
        		if(cityInfo != null){
        			depart_city_code = cityInfo.getCode();
        		}
        	}
        	
        	// arrival_city 转化
        	String arrival_city_code = arrival_city;
        	airport arrivalAirportInfo = flightService.getAirportByCode(arrival_city);
        	if(arrivalAirportInfo != null){
        		int cityId = arrivalAirportInfo.getCityId();
        		city cityInfo = flightService.getCityById(cityId+"");
        		if(cityInfo != null){
        			arrival_city_code = cityInfo.getCode();
        		}
        	}
        	
        	for(String source : source_list)
    		{
        		realTimeThread s=new realTimeThread(depart_city_code,arrival_city_code,go_date,back_date,cabinClass,"ADT",source,round,50);
    			threadPool.execute(s);
    		}
        	threadPool.shutdown();
        	
    		while (!threadPool.isTerminated()) { }
    		long end=System.currentTimeMillis(); //获取结束时间
    		
    		System.out.println("程序运行时间： "+(end-start)/1000+" s");
        	flightsDetails = flightService.getFlightDetails(round, departure_city, arrival_city, go_date, back_date, seat_type, source_list, size);
    	}
    	mav.addObject("flightsDetails", flightsDetails);
        return mav;
    }
}
