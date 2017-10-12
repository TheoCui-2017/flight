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

import flight.spider.web.bean.currency;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.JSONresponse;
import flight.spider.web.utility.finance;

@Controller  
@RequestMapping("/finance")  
public class financeController {

	@Autowired
	flightInfoService flightService;
	@Autowired 
	private JSONresponse jsonresponse;
	@Autowired
	finance financeService;
	
	 @RequestMapping(value="/list",method=RequestMethod.GET)   
	 public ModelAndView list() {
		 ModelAndView mav = new ModelAndView("finance");
		 List<currency> currencyInfo = flightService.getCurrencys();
		 mav.addObject("currencyInfo", currencyInfo);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/delete",method=RequestMethod.POST)   
	 public ModelAndView delete(HttpServletRequest request,HttpServletResponse response, @RequestParam String code) throws IOException{
		 flightService.deleteCurrencyByCode(code);
		 try {
			jsonresponse.PrintData(request,response,true);
		} catch (IOException e) {
			jsonresponse.PrintStatus(request,response,false);
			e.printStackTrace();
		}
		 return null;
	 }
	 
	 @RequestMapping(value="/edit",method=RequestMethod.GET)   
	 public ModelAndView edit(@RequestParam String code) {
		 ModelAndView mav = new ModelAndView("financeEdit");
		 currency currency = flightService.getCurrencyByCode(code);
		 mav.addObject("currency", currency);
	     return mav;  
	 }
	 
	 @RequestMapping(value="/save",method=RequestMethod.POST)   
	 public ModelAndView save(HttpServletRequest request,HttpServletResponse response, @RequestParam String name, @RequestParam String code, @RequestParam String rate) throws IOException{
		 flightService.saveCurrency(code, name, rate);
		 financeService.updateSingleRate(code, "CNY", rate);
		 try {
			 jsonresponse.PrintData(request,response,true);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
	 
	 @RequestMapping(value="/check",method=RequestMethod.POST)   
	 public ModelAndView check(HttpServletRequest request,HttpServletResponse response, @RequestParam String code) throws IOException{
		 String rate = financeService.getRate(code.toUpperCase(), "CNY");
		 try {
			 jsonresponse.PrintData(request,response,rate);
		 } catch (IOException e) {
			 jsonresponse.PrintStatus(request,response,false);
			 e.printStackTrace();
		 }
	     return null;  
	 }
}
