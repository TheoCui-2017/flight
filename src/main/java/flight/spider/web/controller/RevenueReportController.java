package flight.spider.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import flight.spider.web.bean.airport;
import flight.spider.web.bean.city;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.finance;

@Controller  
@RequestMapping("/report")
public class RevenueReportController{

	@Autowired
	flightInfoService flightService;
	@Autowired
	finance financeService;
	
	@RequestMapping(value="/excel/showExcel",method=RequestMethod.GET)   
	public ModelAndView showReport(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String departure_city,@RequestParam String arrival_city,
			@RequestParam String start_date, @RequestParam String end_date, @RequestParam int seat_type,
    		@RequestParam String stay_days, @RequestParam String add_list,
    		@RequestParam String spa_list, @RequestParam String stopover) throws Exception {
		ModelAndView mav = new ModelAndView("excelResult");
    	List<String> stays= Arrays.asList(stay_days.split(","));
    	List<String> addList = null;
    	List<String> spaList = null;
    	double rate = financeService.getRate("USD");
    	List<String> stopovers = new ArrayList<String>();
    	if(!add_list.isEmpty()){
    		addList = Arrays.asList(add_list.split(","));
    		city cityInfo = flightService.getCityByCode(departure_city);
    		if(cityInfo != null){
	    		List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
	    		if(airports!=null && airports.size()>0){
	    			for(airport airportInfo : airports){
	    				stopovers.add(airportInfo.getCode());
	    			}
	    		}
    		}
    		else{
    			stopovers.add(departure_city);
    		}
    	}else{
    		addList = new ArrayList<String>();
    		addList.add(departure_city);
    	}
    	if(add_list.isEmpty() && !spa_list.isEmpty()){
    		spaList = Arrays.asList(spa_list.split(","));
    		city cityInfo = flightService.getCityByCode(arrival_city);
    		if(cityInfo !=null) {
    			List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
    			if(airports!=null && airports.size()>0){
    				for(airport airportInfo : airports){
    					stopovers.add(airportInfo.getCode());
    				}
    			}
    		}
    		else{
    			stopovers.add(arrival_city);
    		}
    	}else{
    		spaList = new ArrayList<String>();
    		spaList.add(arrival_city);
    	}
    	if(!stopover.isEmpty()){
    		city cityInfo = flightService.getCityByCode(stopover);
    		if(cityInfo != null){
	    		List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
	    		if(airports!=null && airports.size()>0){
	    			for(airport airportInfo : airports){
	    				stopovers.add(airportInfo.getCode());
	    			}
	    		}
    		}
    		else{
    			stopovers.add(stopover);
    		}
    	}
    	List<List<List<Object[]>>> lists= new ArrayList<List<List<Object[]>>>();
    	List<Map> DataRes = new ArrayList<Map>();
    	List<String> d_as = new ArrayList<String>();
    	int week = 0;
    	for(String depart : addList){
    		for(String arrive : spaList){
    			d_as.add(depart+","+arrive);
    			List<List<Object[]>> list = flightService.flightLegPricesExcel(depart,arrive,stopovers,start_date,end_date,stays,seat_type);
    			week = list.size();
    			lists.add(list);
    		}
    	}
    	int index = 1;
    	while(index <= week){
    		Map<String,List> airline_price = new LinkedHashMap<String,List>();
    		for(int i=0;i<lists.size();i++){
    			List<Object[]> a_ps = lists.get(i).get(index-1);
    			for(Object[] a_p : a_ps){
    				if(airline_price.containsKey(a_p[0].toString())){
    					airline_price.get(a_p[0].toString()).add(financeService.toRMBPrice(rate,a_p[1].toString()));
    				}else{
    					List<String> prices = new ArrayList<String>();
    					for(int j=0;j<i;j++){
    						prices.add("");
    					}
    					prices.add(financeService.toRMBPrice(rate,a_p[1].toString()));
    					airline_price.put(a_p[0].toString(), prices);
    				}
    			}
    		}
    		DataRes.add(airline_price);
    		index++;
    	}
    	mav.addObject("DataRes",DataRes);
    	mav.addObject("d_as", d_as);
    	mav.addObject("d_asize",d_as.size());
    	return mav;
	        	
	}
	        
	@RequestMapping(value="/excel/download",method=RequestMethod.GET)   
	public View getReport(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String departure_city,@RequestParam String arrival_city,
			@RequestParam String start_date, @RequestParam String end_date, @RequestParam int seat_type,
    		@RequestParam String stay_days, @RequestParam String add_list,
    		@RequestParam String spa_list, @RequestParam String stopover) throws Exception {
		return new AbstractXlsView() {
	        @Override
	        protected void buildExcelDocument(Map<String, Object> model,Workbook workbook, HttpServletRequest request,HttpServletResponse response) throws Exception {
	        	response.addHeader("X-Content-Type-Options", "nosniff");
	        	response.addHeader("X-Frame-Options", "deny");
	        	List<String> stays= Arrays.asList(stay_days.split(","));
	        	List<String> addList = null;
	        	List<String> spaList = null;
	        	List<String> stopovers = new ArrayList<String>();
	        	if(!add_list.isEmpty()){
	        		addList = Arrays.asList(add_list.split(","));
	        		city cityInfo = flightService.getCityByCode(departure_city);
	        		if(cityInfo !=null) {
	        			List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
	        			if(airports!=null && airports.size()>0){
	        				for(airport airportInfo : airports){
	        					stopovers.add(airportInfo.getCode());
	        				}
	        			}
	        		}
	        		else{
	        			stopovers.add(departure_city);
	        		}
	        	}else{
	        		addList = new ArrayList<String>();
	        		addList.add(departure_city);
	        	}
	        	if(add_list.isEmpty() && !spa_list.isEmpty()){
	        		spaList = Arrays.asList(spa_list.split(","));
	        		city cityInfo = flightService.getCityByCode(arrival_city);
	        		if(cityInfo!=null) {
	        			List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
	        			if(airports!=null && airports.size()>0){
	        				for(airport airportInfo : airports){
	        					stopovers.add(airportInfo.getCode());
	        				}
	        			}
	        		}
	        		else{
	        			stopovers.add(arrival_city);
	        		}
	        	}else{
	        		spaList = new ArrayList<String>();
	        		spaList.add(arrival_city);
	        	}
	        	if(!stopover.isEmpty()){
	        		city cityInfo = flightService.getCityByCode(stopover);
	        		if(cityInfo != null){
	    	    		List<airport> airports = flightService.getAirportByCityId(cityInfo.getId());
	    	    		if(airports!=null && airports.size()>0){
	    	    			for(airport airportInfo : airports){
	    	    				stopovers.add(airportInfo.getCode());
	    	    			}
	    	    		}
	        		}
	        		else{
	        			stopovers.add(stopover);
	        		}
	        	}
	        	Map<String,List<List<Object[]>>> maps= new HashMap<String,List<List<Object[]>>>();
	        	for(String depart : addList){
	        		for(String arrive : spaList){
	        			maps.put(depart+","+arrive, flightService.flightLegPricesExcel(depart,arrive,stopovers,start_date,end_date,stays,seat_type));
	        		}
	        	}
	        	List<HSSFSheet> sheets = new ArrayList<HSSFSheet>();
	        	List<HSSFRow> headers = new ArrayList<HSSFRow>();
	        	Set<String> keys = maps.keySet();
	        	Map<String,String> codePrice = new LinkedHashMap<String,String>();
	        	int index=1;
	        	for(String key: keys){
	        		List<List<Object[]>> result = maps.get(key);
		        	for(int i=0; i<result.size(); i++){
		        		List<Object[]> list = result.get(i);
		        		int sheetNum = i+1;
		        		if(sheets.size()<=i){
		        			sheets.add((HSSFSheet) workbook.createSheet("第"+sheetNum+"周"));
		        		}
		        		HSSFSheet sheet = sheets.get(i);
		        		if(headers.size()<=i){
		        			HSSFRow header = sheet.createRow(0);
				    		header.createCell(0).setCellValue("始发地");
				    		header.createCell(1).setCellValue("到达地");
		        			headers.add(header);
		        		}
		        		HSSFRow header = headers.get(i);
		        		int startColumn = 2;
			    		for(Object[] o : list){
		        			codePrice.put(o[0].toString(), o[1].toString());
		        		}
			    		Set<String> airlineCodes = codePrice.keySet();
			    		int j=0;
			    		for(String airlineCode : airlineCodes){
			    			int column = startColumn+j;
			    			header.createCell(column).setCellValue(airlineCode);
			    			j++;
			    		}
			    		header = sheet.createRow(index);
			    		String[] d_a = key.split(",");
			    		header.createCell(0).setCellValue(d_a[0]);
			    		header.createCell(1).setCellValue(d_a[1]);
			    		// 找出价钱最低的位置
			    		int y = 0;
			    		String str = "1000000";
			    		HSSFCell cell = null;
			    		double rate = financeService.getRate("USD");
			    		j=0;
			    		for(String airlineCode : airlineCodes){
		        			int column = startColumn+j;
		        			if(codePrice.get(airlineCode).isEmpty()){
		        				header.createCell(column).setCellValue(codePrice.get(airlineCode));
		        			}else{
			        			String price = financeService.toRMBPrice(rate, codePrice.get(airlineCode));
			        			if(Double.valueOf(price) <= Double.valueOf(str)){
			        				str = price;
			        				y = column;
			        				cell = header.createCell(column);
			        				cell.setCellValue(Double.valueOf(price));
			        			}else{
			        				header.createCell(column).setCellValue(Double.valueOf(price));
			        			}
		        			}
		        			j++;
		        		}
		        		if(y != 0){
		        			HSSFCellStyle cellStyle = (HSSFCellStyle) workbook.createCellStyle();
		        			// 背景色
		        			cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		        			// 填充背景色
				    		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				    		cell.setCellStyle(cellStyle); 
		        		}
		        		resetCodePriceMap(codePrice);
		        	}
		        	index++;
	        	}
	        };
	        
	        private void resetCodePriceMap(Map<String,String> codePrice){
	        	Set<String> airlineCodes = codePrice.keySet();
	    		int j=0;
	    		for(String airlineCode : airlineCodes){
	    			codePrice.put(airlineCode, "");
	    		}
	        }
		
		};
	}
	
}