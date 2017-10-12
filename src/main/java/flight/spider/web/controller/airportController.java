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

import flight.spider.web.bean.airport;
import flight.spider.web.bean.city;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.JSONresponse;

@Controller  
@RequestMapping("/airport")  
public class airportController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;
	
	 @RequestMapping(value="/list",method=RequestMethod.GET)   
	 public ModelAndView list(@RequestParam int cityId) {
		 ModelAndView mav = new ModelAndView("airport");
		 List<airport> airportInfo = flightService.getAirportByCityId(cityId);
		 city cityInfo = flightService.getCityById(String.valueOf(cityId));
		 mav.addObject("airportInfo", airportInfo);
		 mav.addObject("city", cityInfo);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/edit",method=RequestMethod.GET)   
	 public ModelAndView edit(@RequestParam String cityId, @RequestParam String id) {
		 ModelAndView mav = new ModelAndView("airportEdit");
		 airport airportInfo = null;
		 if(!id.equals("0")){
			 int Id = Integer.parseInt(id);
			 airportInfo = flightService.getAirportById(Id);
		 }
		 mav.addObject("airport", airportInfo);
		 mav.addObject("cityId", cityId);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/delete",method=RequestMethod.POST)   
	 public ModelAndView delete(HttpServletRequest request,HttpServletResponse response, @RequestParam String id) throws IOException{
		 flightService.deleteAirportById(id);
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
			 @RequestParam String id, @RequestParam String cityId, 
			 @RequestParam String name, @RequestParam String code, @RequestParam String querys) throws IOException{
		 flightService.saveAirportInfo(id, cityId, name, code, querys);
		 try {
			 jsonresponse.PrintData(request,response, true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response, false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
}
