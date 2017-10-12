package flight.spider.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.airport;
import flight.spider.web.bean.city;
import flight.spider.web.bean.flightDetail;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.finance;
import flight.spider.web.utility.realTimeThread;

@Controller  
@RequestMapping("/report")  
public class reportController {

	@Autowired
	flightInfoService flightService;
	
	@RequestMapping(value="/excel/trend",method=RequestMethod.GET)   
    public ModelAndView excel() {
    	ModelAndView mav = new ModelAndView("third_report");
        return mav;  
    }
	
	@RequestMapping(value="/leg/trend",method=RequestMethod.GET)   
    public ModelAndView trend() {
    	ModelAndView mav = new ModelAndView("first_report");
        return mav;  
    }
	
	@RequestMapping(value="/area/trend",method=RequestMethod.GET)   
    public ModelAndView areaTrend() {
    	ModelAndView mav = new ModelAndView("second_report");
        return mav;  
    }
	
	@RequestMapping(value="/leg/query",method=RequestMethod.POST)   
    public ModelAndView searchTrendDetail(@RequestParam String departure_city,@RequestParam String arrival_city,
    		@RequestParam String start_date, @RequestParam String end_date,
    		@RequestParam String stay_days, @RequestParam String stopover,
    		@RequestParam String airline_codes, @RequestParam int seat_type,
    		@RequestParam int dimension) {
		ModelAndView mav = new ModelAndView("trend");
    	List<String> stayDays= Arrays.asList(stay_days.split(","));
    	List<String> airlineCodes = null;
    	if(airline_codes.length() > 0){
    		airlineCodes= Arrays.asList(airline_codes.split(","));
    	}
    	List<String> departure_city_list = new ArrayList<String>();
    	List<String> arrival_city_list = new ArrayList<String>();
    	departure_city_list.add(departure_city);
    	arrival_city_list.add(arrival_city);
    	List<String> stopover_list = new ArrayList<String>();
    	if(stopover != null && stopover.length() > 0){
    		city cityInfo = flightService.getCityByCode(stopover);
    		if(cityInfo != null){
	    		List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
	    		if(airports!=null && airports.size()>0){
	    			for(airport airportInfo : airports){
	    				stopover_list.add(airportInfo.getCode());
	    			}
	    		}
    		}
    		else{
    			stopover_list.add(stopover);
    		}
    	}
    	JSONObject flightLegPrices = flightService.getLegPricesTrend(departure_city_list,arrival_city_list,start_date,end_date,
    			stayDays,airlineCodes,seat_type,stopover_list,dimension);
    	// airlines
    	airline_codes = flightLegPrices.getString("airline_codes");
    	flightLegPrices.remove("airline_codes");
    	mav.addObject("airlines", airline_codes);
    	mav.addObject("start_date", start_date);
    	mav.addObject("end_date", end_date);
    	mav.addObject("departure", departure_city);
    	mav.addObject("arrival", arrival_city);
    	mav.addObject("flightLegPrices", flightLegPrices);
        return mav;  
    }
	
	@RequestMapping(value="/area/query",method=RequestMethod.POST)   
    public ModelAndView searchAreaTrendDetail(@RequestParam String departure_city, @RequestParam String arrival_area,
    		@RequestParam String start_date, @RequestParam String end_date,
    		@RequestParam String stay_days, @RequestParam String airline_codes, 
    		@RequestParam int seat_type, @RequestParam int dimension) {
		ModelAndView mav = new ModelAndView("trend");
    	List<String> stayDays= Arrays.asList(stay_days.split(","));
    	List<String> airlineCodes = null;
    	if(airline_codes.length() > 0){
    		airlineCodes= Arrays.asList(airline_codes.split(","));
    	}
    	List<String> departure_city_list = new ArrayList<String>();
    	departure_city_list.add(departure_city);
    	List<String> arrival_city_list = new ArrayList<String>();
    	List<city> arrivalCities = flightService.getCitysByAreaOrCountry(arrival_area);
 
    	if(arrivalCities!=null && arrivalCities.size()>0){
    		for(city c: arrivalCities){
    			arrival_city_list.add(c.getCode());
    		}
    	}
    	JSONObject flightLegPrices = flightService.getLegPricesTrend(departure_city_list,arrival_city_list,start_date,end_date,
    			stayDays,airlineCodes,seat_type,null,dimension);
    	
    	// airlines
    	airline_codes = flightLegPrices.getString("airline_codes");
    	flightLegPrices.remove("airline_codes");
    	System.out.println(airline_codes+"===="+flightLegPrices);
    	mav.addObject("airlines", airline_codes);
    	mav.addObject("start_date", start_date);
    	mav.addObject("end_date", end_date);
    	mav.addObject("departure", departure_city);
    	mav.addObject("arrival", arrival_area);
    	mav.addObject("flightLegPrices", flightLegPrices);
        return mav;  
    }
}
