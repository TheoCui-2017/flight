package flight.spider.web.pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;







import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.flight;
import flight.spider.web.service.flightInfoService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class sqlServerPipeline implements Pipeline {
     @Autowired  
     protected flightInfoService flightService;

    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            String type = entry.getKey().toString();
            if(type.equals("flightInsert")){
                JSONObject flightInfo = (JSONObject) entry.getValue();
                Set<String> keys = flightInfo.keySet();
                for(String key: keys){
                    JSONObject flights = flightInfo.getJSONObject(key);
                    JSONArray flightInfos = flights.getJSONArray("flightInfo");
                    String currency = "";
                    int len = 0;
                    for(Object flight: flightInfos){
                    	JSONObject flightIndex = ((JSONObject) flight);
	                    String departure_code = flightIndex.getString("departureCode");
	                    String arrival_code = flightIndex.getString("arrivalCode");
	                    String depart_airport_code = flightIndex.getString("departAirportCode");
	                    String arrival_airport_code = flightIndex.getString("arrivalAirportCode");
	                    String departure_time = flightIndex.getString("departureTime");
	                    String arrival_time = flightIndex.getString("arrivalTime");
	                    int duration = flightIndex.getIntValue("duration");
	                    int stay_days = flightIndex.getIntValue("stay_days");
	                    currency = flightIndex.getString("currency");
	                    String price = flightIndex.getString("priceTotal");
	                    String search_flag = flightIndex.getString("search_flag");
	                    int seat_number = flightIndex.getIntValue("seat");
	                    String cabin = flightIndex.getString("cabinClass");
	                    int seat_type = 1;
	                    if(cabin.equals("BUS")){
	                    	seat_type = 2;
	                    }else if(cabin.equals("FST")){
	                    	seat_type = 3;
	                    }
	                    String airline_code = flightIndex.getString("airlineCode");
	                    String source = flightIndex.getString("source");
	                    String unique_flag = key;
	                    boolean round = false;
	                    if(flightInfos.size() > 1){
	                    	round = true;
	                    }
	                    if(flightInfos.size() > 1 && len == 0){
	                    	unique_flag = "1-" + key;
	                    }
	                    else if(flightInfos.size() > 1 && len == 1){
	                    	unique_flag = "2-" + key;
	                    	stay_days = 0;
	                    }
	                    String stopover = flightIndex.getString("stopover");
	                    Date create_at = new Date();
	                    flightService.saveFlight(departure_code, arrival_code, departure_time, arrival_time, duration, seat_number, seat_type, 
	                    		airline_code, source, unique_flag, price, search_flag ,currency,
	                    		depart_airport_code, arrival_airport_code,round, stay_days, stopover, create_at);
	                    JSONArray segments = flightIndex.getJSONArray("segment");
	                    for(Object segment : segments){
	                        JSONObject segmentInfo = (JSONObject) segment;
	                        String departureAirport = segmentInfo.getString("departureAirport");
	                        String arrivalAirport = segmentInfo.getString("arrivalAirport");
	                        String departureTime = segmentInfo.getString("departureTime");
	                        String arrivalTime = segmentInfo.getString("arrivalTime");
	                        int duration_segment = segmentInfo.getIntValue("duration");
	                        String airlineCode = segmentInfo.getString("airlineCode");
	                        int stop_duration = segmentInfo.getIntValue("layoverDuration");
	                        String meal_type = segmentInfo.getString("meal");
	                        Date create_at_segment = new Date();
	                        String plane = segmentInfo.getString("plane");
	                        String plane_code = segmentInfo.getString("planeCode");
	                        String unique_flag_segment = unique_flag;
	                        int flight_num = Integer.parseInt(segmentInfo.getString("flightNumber"));
	                        flightService.saveSegment(departureAirport, arrivalAirport, departureTime, arrivalTime, duration_segment, airlineCode, stop_duration, meal_type, create_at_segment, plane, plane_code, unique_flag_segment,flight_num);
	                    }
	                    len++;
                    }
                    
                    JSONObject priceFlight = flights.getJSONObject("priceInfo");
                    if(priceFlight != null){
                    	String fare = priceFlight.getString("fare");
                    	String tax = priceFlight.getString("tax");
                    	String passenger = priceFlight.getString("passenger");
                    	String baggageURL = priceFlight.getString("baggageURL");
                    	Date date = new Date();
                    	flightService.savePrice(Float.parseFloat(fare), Float.parseFloat(tax), passenger,  date, key, "", "", "",currency, baggageURL);
                    }
                    
                }
            }else if(type.equals("flightUpdate")){
            	JSONObject flightInfo = (JSONObject) entry.getValue();
                Set<String> keys = flightInfo.keySet();
                for(String key: keys){
					JSONObject flights = flightInfo.getJSONObject(key);
                	// 是否为往返
                	boolean round = flights.getBooleanValue("round");
					String tax = flights.getString("tax");
					String fare = flights.getString("fare");
					String seat_num = flights.getString("seat");
					if(seat_num != null && fare != null && tax != null){
						// update flight
						flightService.updateFlightSeat(key, Integer.parseInt(seat_num));
						
						// update segment
						JSONObject segments = flights.getJSONObject("segment");
						Set<String> flightNums = segments.keySet();
						for(String flightNum : flightNums){
							String meal = segments.getString(flightNum).trim();
							flightService.updateSegmentMeal(key, Integer.parseInt(flightNum), meal);
						}
						
						// 比较价钱
						String priceTotal = flights.getString("priceTotal");
						float price = Float.parseFloat(fare) + Float.parseFloat(tax);
						int priceFlight = (int)Float.parseFloat(priceTotal);
						if(priceFlight != (int)price){
							// update flight price
							flightService.updateFlightPrice(key, price+"");
						}
						
						// insert price
						String currency = flights.getString("currency");
						String passenger = flights.getString("passenger");
						Date date = new Date();
						flightService.savePrice(Float.parseFloat(fare), Float.parseFloat(tax), passenger,  date, key, "", "", "",currency,"");
					}
					else{
						System.out.println("delete flight----"+flights);
						// delete flight
						flightService.deleteFlightByFlag(key);
						// delete segment
						flightService.deleteSegmentsByFlag(key);
						// delete price
						//flightService.deletePricesByFlags(unique_flags);
					}
                }
            }else if(type.equals("priceUpdate")){
                // update price
            	JSONObject priceInfo = (JSONObject) entry.getValue();
            	Set<String> keys = priceInfo.keySet();
            	for(String key: keys){
            		JSONObject bagsInfo = priceInfo.getJSONObject(key);
            		String carryOn = bagsInfo.getString("carryOn");
            		String firstBaggage = bagsInfo.getString("firstCheckedBag");
            		String secondBaggage = bagsInfo.getString("secondCheckedBag");
            		flightService.updatePriceBaggage(key, carryOn, firstBaggage, secondBaggage);
            	}
            }
        }
    }

}
