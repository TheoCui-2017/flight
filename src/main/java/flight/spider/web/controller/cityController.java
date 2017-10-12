package flight.spider.web.controller;

import java.io.IOException;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.airport;
import flight.spider.web.bean.city;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.JSONresponse;

@Controller  
@RequestMapping("/city")  
public class cityController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;
	
	 @RequestMapping(value="/list",method=RequestMethod.GET)   
	 public ModelAndView list() {
		 ModelAndView mav = new ModelAndView("city");
		 List<city> cityInfo = flightService.getCitys();
		 mav.addObject("cityInfo", cityInfo);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/edit",method=RequestMethod.GET)   
	 public ModelAndView edit(@RequestParam String id) {
		 ModelAndView mav = new ModelAndView("cityEdit");
		 city cityInfo = null;
		 if(!id.equals("0")){
			 cityInfo = flightService.getCityById(id);
		 }
		 mav.addObject("city", cityInfo);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/delete",method=RequestMethod.POST)   
	 public ModelAndView delete(HttpServletRequest request,HttpServletResponse response, @RequestParam String id) throws IOException{
		 flightService.deleteCityById(id);
		 try {
			 jsonresponse.PrintData(request,response,true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;
	 }
	 
	 @RequestMapping(value="/save",method=RequestMethod.POST)   
	 public ModelAndView edit(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam String id, @RequestParam String name, @RequestParam String code,
			 @RequestParam String country, @RequestParam String area, @RequestParam String querys) throws IOException{
		 city cityInfo = flightService.saveCityInfo(id, name, code, country, area, querys);
		 try {
			 jsonresponse.PrintData(request,response,cityInfo.getId());
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
	 
	 @RequestMapping(value="/query",method=RequestMethod.GET)
	 public ModelAndView query(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 List<city> cityInfo = flightService.getCitys();
		 List<airport> airportInfo = flightService.getAirports();
		 JSONArray jsonArr = new JSONArray();
		 // 遍历城市列表
		 HashMap<Integer , city> cityMap = new HashMap<Integer , city>(); 
		 for(city cityIndex : cityInfo){
			 int id = cityIndex.getId();
			 cityMap.put(id, cityIndex);
			 // 定义JSONObject 存放code, name, airportName, airportCode, querys
			 JSONObject queryInfo = new JSONObject();
			 queryInfo.put("CityCode", cityIndex.getCode());
			 queryInfo.put("Name", cityIndex.getName());
			 queryInfo.put("AirportCode", cityIndex.getCode());
			 queryInfo.put("AirportName", cityIndex.getName());
			 queryInfo.put("Qureys", cityIndex.getQuerys());
			 jsonArr.add(queryInfo);
		 }
		 
		 // 遍历机场列表
		 for(airport airportIndex : airportInfo){
			 int cityId = airportIndex.getCityId();
			 // 定义JSONObject 存放code, name, airportName, airportCode, querys
			 String cityCode = cityMap.get(cityId).getCode();
			 String cityName = cityMap.get(cityId).getName();
			 JSONObject queryInfo = new JSONObject();
			 queryInfo.put("CityCode", cityCode);
			 queryInfo.put("Name", cityName);
			 queryInfo.put("AirportCode", airportIndex.getCode());
			 queryInfo.put("AirportName", airportIndex.getName());
			 queryInfo.put("Qureys", airportIndex.getQuerys());
			 jsonArr.add(queryInfo);
		 }
		 try {
			 jsonresponse.PrintData(request,response,jsonArr.toJSONString());
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
	 
}
