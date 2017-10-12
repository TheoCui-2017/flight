package flight.spider.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.*;

public interface flightInfoService {

	//flight
	flight saveFlight(String departure_code, String arrival_code, String departure_time, String arrival_time, 
			int duration, int seat_number, int seat_type, String airline_code, String source, String unique_flag, 
			String totalPrice, String search_flag, String currency_unit,
			String depart_airport_code, String arrival_airport_code, boolean round, 
			int stay_days, String stopover, Date create_at);
	
	flight saveFilght(flight flightInfo);
	
	flight getFlightByFlag(String unique_flag);
	
	void updateFlight(flight flightInfo);

	void updateFlightSeat(String unique_flag, int seat_num);
	
	void updateFlightPrice(String unique_flag, String priceTotal);
	
	List<flight> getFlightBySearchFlagLessThanUpdatetime(String search_flag, Date update_at);
	
	void deleteFlights(List<flight> flightsInfo);
	
	void deleteFlightByFlag(String unique_flag);
	
	List<flight> getFlightsByCondition(boolean round, String departure_code, String arrival_code, String go_date, String back_date, int seat_type, List<String> sources, int size);
	
	
	
	
	//segment
	segment saveSegment(String departure_code, String arrival_code, String departure_time, String arrival_time, int duration, 
			String airline_code, int stop_duration, String meal_type, Date create_at, String plane, String plane_code, String unique_flag, int flight_num);
	
	segment saveSegment(segment segmentInfo);
	
	List<segment> getSegmentsByFlag(String unique_flag);
	
	segment getSegmentByFlagAndFlightNum(String unique_flag, int flight_num);
	
	void updateSegment(segment segmentInfo);
	
	void updateSegmentMeal(String unique_flag, int flight_num, String meal);
	
	void deleteSegmentsByFlags(List<String> unique_flags);
	
	void deleteSegmentsByFlag(String unique_flag);
		
	List<segment> getSegmentsByFlags(List<String> unique_flags);
	
	
	
	
	
	
	
	
	
	
	
	//price
	price savePrice(float fare, float tax, String passenger_type, Date create_at, String unique_flag, 
			String carryOn, String firstBaggage, String secondBaggage, String currency_unit, String baggage_url);
	
	price savePrice(price priceInfo);
	
	price getPriceByFlag(String unique_flag);
	
	void updatePrice(price priceInfo);
	
	void updatePriceBaggage(String unique_flag, String carryOn, String firstBaggage, String secondBaggage);
	
	void deletePricesByFlags(List<String> unique_flags);
	
	List<price> getPriceByFlags(List<String> unique_flags);
	
	
	
	
	
	
	
	
	
	// 删除无用数据
	void deleteInutilInfo(String search_flag, Date update_at);
	
	
	// search
	List<flightDetail> getFlightDetails(boolean round, String departure_code, String arrival_code, String go_date, String back_date, int seat_type, List<String> sources, int size);


	// currency
	currency saveCurrency(currency currencyInfo);
	
	void updateCurrency(currency currencyInfo);
	
	List<currency> getCurrencys();
	
	void updateRateByCode(String code, String rate);
	
	currency saveCurrency(String code, String name, String rate);
	
	currency getCurrencyByCode(String code);
	
	void deleteCurrencyByCode(String code);
	
	
	// leg 航段
	List<leg> getLegs();
	
	leg saveLeg(leg legInfo);
	
	leg saveLeg(String depart_city, String depart_city_code, String arrival_city, String arrival_city_code, String area);

	void updateLeg (leg legInfo);
	
	leg getLegByCodes(String depart_city_code, String arrival_city_code);
	
	void deleteLegByCodes(String depart_city_code, String arrival_city_code);
	
	List<leg> getLegsIn(List<Integer> legsids);
	
	// report
	List<List<Object[]>> flightLegPricesExcel(String departure_city, String arrival_city, List<String> stopovers,
			String start_date, String end_date, List<String> stayDays, int seat_type);
	 
	JSONObject getLegPricesTrend(List<String> departure_city, List<String> arrival_city, String start_date, String end_date,
 		List<String> stayDays, List<String> airlineCodes, int seat_type, List<String> stopovers, int dimension);
	
	// city
	List<city> getCitys();
	
	city getCityById(String id);
	
	city getCityByCode(String code);
	
	city saveCityInfo(String id, String name, String code, String country, String area, String querys);
	
	void deleteCityById(String id);
	
	List<city> getCitysByAreaOrCountry(String areaOrCountry);
	
	// airport
	List<airport> getAirportByCityId(int cityId);
	
	airport getAirportById(int id);
	
	airport getAirportByCode(String code);
	
	void saveAirportInfo(String id, String cityId, String name, String code, String querys);
	
	void deleteAirportById(String id);
	
	List<airport> getAirports();
	
	// user
	void savePrivilegeByUsername(String username, List<String> privileges);
	
}
