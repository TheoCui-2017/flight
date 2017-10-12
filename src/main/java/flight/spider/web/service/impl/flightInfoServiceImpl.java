package flight.spider.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import flight.spider.web.bean.airport;
import flight.spider.web.bean.city;
import flight.spider.web.bean.currency;
import flight.spider.web.bean.flight;
import flight.spider.web.bean.flightDetail;
import flight.spider.web.bean.leg;
import flight.spider.web.bean.price;
import flight.spider.web.bean.privilege;
import flight.spider.web.bean.segment;
import flight.spider.web.dao.impl.*;
import flight.spider.web.service.flightInfoService;
import flight.spider.web.utility.toolUtil;

@Service("flightInfoService")
public class flightInfoServiceImpl implements flightInfoService{
	
	@Autowired
	private flightDAO flightDao;
	
	@Autowired
	private segmentDAO segmentDao;
	
	@Autowired
	private priceDAO priceDao;
	
	@Autowired
	private currencyDAO currencyDao;
	
	@Autowired
	private legDAO legDao;
	
	@Autowired
	private cityDAO cityDao;
	
	@Autowired
	private airportDAO airportDao;
	
	@Autowired
	private privilegeDAO privilegeDao;
	
	//flight-------------------------------------------------------------------------------
	
	@Override
	public flight saveFilght(flight flightInfo) {
		return flightDao.saveFlight(flightInfo);
	}

	@Override
	public flight getFlightByFlag(String unique_flag) {
		return flightDao.getFlightByFlag(unique_flag);
	}

	@Override
	public void updateFlight(flight flightInfo) {
		flightDao.updateFlight(flightInfo);
		
	}
	
	@Override
	public void updateFlightSeat(String unique_flag, int seat_num) {
		flightDao.updateFlightSeat(unique_flag, seat_num);
		
	}
	
	@Override
	public void updateFlightPrice(String unique_flag, String priceTotal){
		flightDao.updateFlightPrice(unique_flag, priceTotal);
	}

	@Override
	public flight saveFlight(String departure_code, String arrival_code,
			String departure_time, String arrival_time, int duration,
			int seat_number, int seat_type, String airline_code, String source,
			String unique_flag, String totalPrice, String search_flag, String currency_unit, 
			String depart_airport_code, String arrival_airport_code, boolean round, 
			int stay_days, String stopover, Date create_at) {
		flight flightInfo = null;
		flightInfo = this.getFlightByFlag(unique_flag);
		if(flightInfo == null){
			flightInfo = new flight(departure_code, arrival_code, departure_time, arrival_time, duration,
					seat_number, seat_type, airline_code, source, unique_flag, totalPrice, search_flag, currency_unit,
					depart_airport_code, arrival_airport_code, round, stay_days, stopover, create_at);
			return this.saveFilght(flightInfo);
		}else{
			flightInfo.reset(departure_code, arrival_code, departure_time, arrival_time, duration,
					seat_number, seat_type, airline_code, source, unique_flag, totalPrice, search_flag, currency_unit, 
					depart_airport_code, arrival_airport_code, round, stay_days, stopover, create_at);
			this.updateFlight(flightInfo);
			return flightInfo;
		}
	}

	@Override
	public List<flight> getFlightBySearchFlagLessThanUpdatetime(
			String search_flag, Date update_at) {
		return flightDao.getFlightBySearchFlagLessThanUpdatetime(search_flag, update_at);
	}

	public void deleteFlights(List<flight> flightsInfo){ 
		flightDao.deleteFlights(flightsInfo);
	}
	
	@Override
	public void deleteFlightByFlag(String unique_flag) {
		flightDao.deleteFlightByFlag(unique_flag);
	}
	
	@Override
	public List<flight> getFlightsByCondition(boolean round, String departure_code, String arrival_code, String go_date, String back_date, int seat_type, List<String> sources, int size){
		if(!round){
			return flightDao.getFlightsByCondition(round, departure_code, arrival_code, go_date, back_date, seat_type, sources, size, "");
		}
		else{
			List<flight> flightListInfo = null;
			List<flight> flightOW = flightDao.getFlightsByCondition(round, departure_code, arrival_code, go_date, back_date, seat_type, sources, size, "ow");
			List<flight> flightRT = flightDao.getFlightsByCondition(round, arrival_code, departure_code, back_date, go_date, seat_type, sources, size, "rt");
			System.out.println(flightOW.size()+"===="+flightRT.size());
			if(flightOW.size() > 0 && flightRT.size() > 0 ){
				if(flightOW.size() - flightRT.size() == 1 || flightOW.size() - flightRT.size() == -1 || flightOW.size() - flightRT.size() == 0){
					if(flightOW.size() - flightRT.size() == 1){
						// 获取flightRT的最后一个flight
						String unique = "2-" + flightOW.get(flightOW.size()-1).getUnique_flag().substring(2);
						flight lastFlightRT = flightDao.getFlightByFlag(unique);
						flightRT.add(lastFlightRT);
					}
					else if(flightRT.size() - flightOW.size() == 1){
						// 获取flightOW的最后一个flight
						String unique = "1-" + flightRT.get(flightRT.size()-1).getUnique_flag().substring(2);
						flight lastFlightOW = flightDao.getFlightByFlag(unique);
						flightOW.add(lastFlightOW);
					}
					System.out.println(flightOW.size()+"===="+flightRT.size());
					for(int i=0; i<flightOW.size(); i++){
						flight flightOWIndex = flightOW.get(i);
						flight flightRTIndex = flightRT.get(i);
						String departTime = flightOWIndex.getDeparture_time() + "$" + flightRTIndex.getDeparture_time();
						flightOWIndex.setDeparture_time(departTime);
						String unique_flag = flightOWIndex.getUnique_flag() + "$" + flightRTIndex.getUnique_flag();
						flightOWIndex.setUnique_flag(unique_flag);
						String stopoverOW = flightOWIndex.getStopover();
						String stopoverRT = flightRTIndex.getStopover();
						if(stopoverOW.length() == 0){
							stopoverOW = "0";
						}
						if(stopoverRT.length() == 0){
							stopoverRT = "0";
						}
						String stopovers = stopoverOW + "$" + stopoverRT;
						flightOWIndex.setStopover(stopovers);
						String airlineCodes = flightOWIndex.getAirline_code() + "$" + flightRTIndex.getAirline_code();
						flightOWIndex.setAirline_code(airlineCodes);
					}
					flightListInfo = flightOW;
				}
			}
			return flightListInfo;
		}
	}
	
	
	
	
	
	//segment-------------------------------------------------------------------------------
	
	@Override
	public segment saveSegment(segment segmentInfo) {
		return segmentDao.saveSegment(segmentInfo);
	}

	@Override
	public void updateSegmentMeal(String unique_flag, int flight_num,
			String meal) {
		segmentDao.updateSegmentMeal(unique_flag, flight_num, meal);
	}
	
	@Override
	public List<segment> getSegmentsByFlag(String unique_flag) {
		return segmentDao.getSegmentsByFlag(unique_flag);
	}
	
	@Override
	public segment getSegmentByFlagAndFlightNum(String unique_flag, int flight_num) {
		return segmentDao.getSegmentByFlagAndFlightNum(unique_flag, flight_num);
	}

	@Override
	public void updateSegment(segment segmentInfo) {
		segmentDao.updateSegment(segmentInfo);
		
	}
	
	@Override
	public segment saveSegment(String departure_code, String arrival_code,
			String departure_time, String arrival_time, int duration,
			String airline_code, int stop_duration, String meal_type,
			Date create_at, String plane, String plane_code, String unique_flag, int flight_num) {
		segment segmentInfo = null;
		segmentInfo = this.getSegmentByFlagAndFlightNum(unique_flag,flight_num);
		if(segmentInfo == null){
			segmentInfo = new segment( departure_code,  arrival_code, departure_time,  arrival_time,  duration,
					 airline_code,  stop_duration,  meal_type,create_at, plane_code, plane,  unique_flag, flight_num);
			return this.saveSegment(segmentInfo);
		}else{
			segmentInfo.reset(departure_code, arrival_code, departure_time, arrival_time, duration, airline_code, 
					stop_duration, meal_type, plane_code, plane, unique_flag, flight_num, create_at);
			this.updateSegment(segmentInfo);
			return  segmentInfo;
		}
	}

	@Override
	public void deleteSegmentsByFlags(List<String> unique_flags) {
		segmentDao.deleteSegmentsByFlags(unique_flags);
	}

	@Override
	public void deleteSegmentsByFlag(String unique_flag){
		segmentDao.deleteSegmentsByFlag(unique_flag);
	}
	
	@Override
	public List<segment> getSegmentsByFlags(List<String> unique_flags){
		return segmentDao.getSegmentsByFlags(unique_flags);
	}
	
	
	
	
	
	
	
	
	
	
	//price-------------------------------------------------------------------------------

	@Override
	public price savePrice(price priceInfo) {
		return priceDao.savePrice(priceInfo);
	}

	@Override
	public void updatePriceBaggage(String unique_flag, String carryOn, String firstBaggage, String secondBaggage) {
		priceDao.updatePriceBaggage(unique_flag, carryOn, firstBaggage, secondBaggage);
	}
	
	@Override
	public price getPriceByFlag(String unique_flag) {
		return priceDao.getPriceByFlag(unique_flag);
	}
	
	@Override
	public void updatePrice(price priceInfo) {
		priceDao.updatePrice(priceInfo);
		
	}

	@Override
	public price savePrice(float fare, float tax, String passenger_type, Date create_at, String unique_flag, 
			String carryOn, String firstBaggage, String secondBaggage, String currency_unit, String baggage_url) {
		price priceInfo = null;
		priceInfo = this.getPriceByFlag(unique_flag);
		if(priceInfo == null){
			priceInfo = new price( fare,  tax,  passenger_type,  create_at, unique_flag, carryOn, firstBaggage, secondBaggage, currency_unit, baggage_url);
			return this.savePrice(priceInfo);
		}else{
			priceInfo.reset(fare,  tax,  passenger_type,  unique_flag, carryOn, firstBaggage, secondBaggage, currency_unit, baggage_url, create_at);
			this.updatePrice(priceInfo);
			return  priceInfo;
		}
	}

	@Override
	public void deletePricesByFlags(List<String> unique_flags) {
		priceDao.deletePricesByFlags(unique_flags);
	}
	
	@Override
	public List<price> getPriceByFlags(List<String> unique_flags){
		return priceDao.getPriceByFlags(unique_flags);
	}
	
	
	
	
	
	
	//other-------------------------------------------------------------------------------
	//删除无用的数据
	@Override
	public void deleteInutilInfo(String search_flag, Date update_at) {
		// TODO Auto-generated method stub
		List<flight> flights = this.getFlightBySearchFlagLessThanUpdatetime(search_flag, update_at);
		List<String> flags = new ArrayList<String>();
		for(flight fl : flights){
			flags.add(fl.getUnique_flag());
		}
		this.deleteFlights(flights);
		this.deleteSegmentsByFlags(flags);
		this.deletePricesByFlags(flags);
	}
	
	//
	@Override
	public List<flightDetail> getFlightDetails(boolean round, String departure_code, String arrival_code, String go_date, String back_date, int seat_type, List<String> sources, int size){
		List<flightDetail> flightDetails = new ArrayList<flightDetail>();
		List<flight> flights = this.getFlightsByCondition(round, departure_code, arrival_code, go_date, back_date, seat_type, sources, size);
		if(flights !=null && flights.size()>0){
			List<String> unique_flags = new ArrayList<String>();
			List<String> uniqueFlags = new ArrayList<String>();
			for(flight f : flights){
				if(round){
					uniqueFlags.add(f.getUnique_flag().split("\\$")[0].substring(2));
					unique_flags.add(f.getUnique_flag().split("\\$")[0]);
					unique_flags.add(f.getUnique_flag().split("\\$")[1]);
				}
				else{
					uniqueFlags.add(f.getUnique_flag());
					unique_flags.add(f.getUnique_flag());
				}
			}
			List<segment> segments = this.getSegmentsByFlags(unique_flags);
			List<price> prices = this.getPriceByFlags(uniqueFlags);
			
			for(flight f : flights){
				flightDetail flight_detail = new flightDetail();
				flight_detail.setRound(round);
				flight_detail.setFlightInfo(f);
				List<segment> tmp_segs = new ArrayList<segment>();
				String unique_flag = f.getUnique_flag();
				Iterator<segment> iter_seg = segments.iterator();
				while(iter_seg.hasNext()){
					segment s = iter_seg.next();
					if(!round){
						if(s.getUnique_flag().equals(unique_flag)){
							tmp_segs.add(s);
							iter_seg.remove();
						}
					}
					else{
						if(s.getUnique_flag().contains(unique_flag.split("\\$")[0].substring(2))){
							tmp_segs.add(s);
							iter_seg.remove();
						}
					}
				}
				flight_detail.setSegments(tmp_segs);
				Iterator<price> iter_price = prices.iterator();
				if(round)
					unique_flag = unique_flag.split("\\$")[0].substring(2);
				while(iter_price.hasNext()){
					price p = iter_price.next();
					if(p.getUnique_flag().equals(unique_flag)){
						flight_detail.setPriceInfo(p);
						iter_price.remove();
						break;
					}
				}
				flightDetails.add(flight_detail);
			}
			
		}
		return flightDetails;
	}
	
	// currency------------------------------------------------
	
	@Override 
	public currency saveCurrency(currency currencyInfo){
		return currencyDao.saveCurrency(currencyInfo);
	}
	
	@Override
	public List<currency> getCurrencys(){
		return currencyDao.getCurrencys();
	}
	
	@Override
	public void updateRateByCode(String code, String rate){
		currencyDao.updateRateByCode(code, rate);
	}
	
	@Override
	public currency getCurrencyByCode(String code){
		return currencyDao.getCurrencyByCode(code);
	}
	
	@Override 
	public void updateCurrency(currency currencyInfo){
		currencyDao.updateCurrency(currencyInfo);
	}
	
	@Override
	public currency saveCurrency(String code, String name, String rate){
		currency currencyInfo = null;
		currencyInfo = this.getCurrencyByCode(code);
		if(currencyInfo == null){
			currencyInfo = new currency(code, name, rate, new Date());
			return this.saveCurrency(currencyInfo);
		}else{
			currencyInfo.reset(code, name, rate, new Date());
			this.updateCurrency(currencyInfo);
			return  currencyInfo;
		}
	}

	@Override
	public void deleteCurrencyByCode(String code){
		currencyDao.deleteCurrencyByCode(code);
	}
	
	
	// leg------------------------------------------------
	
	@Override
	public List<leg> getLegs(){
		return legDao.getLegs();
	}
	
	@Override
	public leg saveLeg(leg legInfo){
		return legDao.saveLeg(legInfo);
	}
	
	@Override
	public void updateLeg(leg legInfo){
		legDao.updateLeg(legInfo);
	}
	
	@Override
	public leg getLegByCodes(String depart_city_code, String arrival_city_code){
		return legDao.getLegByCodes(depart_city_code, arrival_city_code);
	}
	
	@Override
	public leg saveLeg(String depart_city, String depart_city_code, String arrival_city, String arrival_city_code, String area){
		leg legInfo = null;
		legInfo = this.getLegByCodes(depart_city_code, arrival_city_code);
		if(legInfo == null){
			legInfo = new leg(depart_city, depart_city_code, arrival_city, arrival_city_code, area, new Date());
			return this.saveLeg(legInfo);
		}else{
			legInfo.reset(depart_city, depart_city_code, arrival_city, arrival_city_code, area, new Date());
			this.updateLeg(legInfo);
			return  legInfo;
		}
	}
	
	@Override
	public void deleteLegByCodes(String depart_city_code, String arrival_city_code){
		legDao.deleteLegByCodes(depart_city_code, arrival_city_code);
	}

	@Override
	public List<leg> getLegsIn(List<Integer> legsids) {
		return legDao.getLegsIn(legsids);
	}
	
	
	
	// report
		
	@Override
	public List<List<Object[]>> flightLegPricesExcel(String departure_city, String arrival_city, List<String> stopovers,
			String start_date, String end_date, List<String> stayDays, int seat_type){
		List<List<Object[]>> list= new ArrayList<List<Object[]>>();	
		// 根据一周维度获取价钱
		int days = toolUtil.compareDate(end_date, start_date, "yyyy-MM-dd");
		double daytoweek = (double)days/7;
		int weeks = (int)Math.ceil(daytoweek);
		String startDate = start_date;
		String endDate = "";
		for(int i=0; i<weeks; i++){
			if(toolUtil.compareDate(end_date, startDate, "yyyy-MM-dd") <= 7){
				endDate = end_date;
			}
			else{
				endDate = toolUtil.getFetureDateForDate(startDate, 7, "yyyy-MM-dd");
			}
			Date start = toolUtil.formatDate(startDate);
			Date end = toolUtil.formatDate(endDate);
			List<Object[]> flightufs = flightDao.getFlightInfoByConditions(departure_city, arrival_city, start, end, seat_type, stayDays, stopovers);
			// 获取返程信息
			List<Object[]> flightPricesInfo = new ArrayList<Object[]>();
			Map<String,List<String>> map = new HashMap<String,List<String>>();
			for(Object[] flightInfo:flightufs){
				if(!map.containsKey(flightInfo[0].toString())){
					List<String> flags = new ArrayList<String>();
					map.put(flightInfo[0].toString(), flags);
				}
				map.get(flightInfo[0].toString()).add(flightInfo[1].toString().replaceFirst("1", "2"));
				
			}
			Set<String> airlineKeys = map.keySet();
			for(String airline : airlineKeys){
				Object[] res = flightDao.getFlightInfoByAirlineAndUfs(airline, map.get(airline), stopovers);
				if(res!=null && res[1]!=null){
					flightPricesInfo.add(res);
					
				}
			}
			startDate = endDate;
			list.add(flightPricesInfo);
		}
		return list;
	}
	
	@Override
	public JSONObject getLegPricesTrend(List<String> departure_city, List<String> arrival_city, String start_date, String end_date,
	 		List<String> stayDays, List<String> airlineCodes, int seat_type, List<String> stopovers, int dimension){
		// 获取去程航班信息
		Date start = toolUtil.formatDate(start_date);
		Date end = toolUtil.formatDate(end_date);
		List<Object[]> flightPriceInfos = flightDao.getLegPricesTrend(departure_city, arrival_city, start, end, airlineCodes, seat_type, stayDays, stopovers);
		// 获取返程信息
		List<Object[]> result = flightPriceInfos;
		if(stopovers != null && stopovers.size() > 0){
			// flightPriceInfos to unique_flag map
			HashMap<String , Object[]> mapInfo = new HashMap<String , Object[]>(); 
			List<String> ufs = new ArrayList<String>();
			for(Object[] flightInfo : flightPriceInfos){
				ufs.add(flightInfo[3].toString().replaceFirst("1", "2"));
				mapInfo.put(flightInfo[3].toString().replaceFirst("1", "2"), flightInfo);
			}
			
			List<Object[]> flightPrices = flightDao.getFlightPricesByUfs(ufs, stopovers);
			// 返程时间赋值
			for(Object[] index : flightPrices){
				String unique_flag = index[3].toString();
				index[2] = mapInfo.get(unique_flag)[2];
			}
			result = flightPrices;
		}
		// 获取唯一airline_code
		List<String> airline_codes = new ArrayList<String>();
		for(Object[] flight : result){
			if(airline_codes.indexOf(flight[0].toString()) == -1){
				airline_codes.add(flight[0].toString());
			}
		}
		airlineCodes = airline_codes;
		// 根据显示维度组合json
		JSONObject json = toolUtil.flightObjToJson(result,start_date, end_date, dimension, airlineCodes);
		String str = "";
		for(String index : airlineCodes){
			str += index + ",";
		}
		if(!str.isEmpty()){
			str = str.substring(0, str.length()-1);
		}
		json.put("airline_codes", str);
		System.out.println(json);
		return json;
	}
	
	@Override
	public List<city> getCitys(){
		return cityDao.getCitys();
	}
	
	@Override 
	public city getCityById(String id){
		int Id = 0;
		if(id != null && id.length() > 0){
			Id = Integer.parseInt(id);
		}
		return cityDao.getCityById(Id);
	}
	
	@Override
	public city saveCityInfo(String id, String name, String code, String country, String area, String querys){
		city result = null;
		if(id != null && id.length() > 0){
			int Id = Integer.parseInt(id);
			city cityInfo = cityDao.getCityById(Id);
			if(cityInfo!=null){
				cityInfo.reset(name, code, country, area, querys, new Date());
				cityDao.updateCity(cityInfo);
			}
			return cityInfo;
		}
		city cityInfo = new city(name, code, country, area, querys, new Date());
		result = cityDao.saveCity(cityInfo);
		return result;
	}
	
	@Override
	public void deleteCityById(String id){
		if(id != null && id.length() > 0){
			int Id = Integer.parseInt(id);
			cityDao.deleteCityById(Id);
		}
	}
	
	@Override
	public List<airport> getAirportByCityId(int cityId){
		return airportDao.getAirportByCityId(cityId);
	}
	
	@Override 
	public airport getAirportById(int id){
		return airportDao.getAirportById(id);
	}
	
	@Override
	public void saveAirportInfo(String id, String cityId, String name, String code, String querys){
		int cityID = Integer.parseInt(cityId);
		if(id != null && id.length() > 0){
			int Id = Integer.parseInt(id);
			airport airportInfo = airportDao.getAirportById(Id);
			if(airportInfo!=null){
				airportInfo.reset(cityID, name, code, querys, new Date());
				airportDao.updateAirport(airportInfo);
			}
		}
		else{
			airport airportInfo = new airport(cityID, name, code, querys, new Date());
			airportDao.saveAirport(airportInfo);
		}
	}
	
	@Override
	public void deleteAirportById(String id){
		if(id != null && id.length() > 0){
			int Id = Integer.parseInt(id);
			airportDao.deleteAirportById(Id);
		}
	}

	
	@Override
	public List<airport> getAirports(){
		return airportDao.getAirports();
	}

	@Override
	public city getCityByCode(String code) {
		return cityDao.getCityByCode(code);
	}

	@Override
	public airport getAirportByCode(String code) {
		return airportDao.getAirportByCode(code);
	}

	@Override
	public List<city> getCitysByAreaOrCountry(String areaOrCountry) {
		return cityDao.getCitysByAreaOrCountry(areaOrCountry);
	}
	
	
	// privilege
	public void savePrivilegeByUsername(String username, List<String> privileges){
		// 根据username删除权限
		privilegeDao.deletePrivilegByUsername(username);
		// 添加权限
		for(String index:privileges){
			privilege privilegeInfo = new privilege(username,index,new Date());
			privilegeDao.save(privilegeInfo);
		}
	}
}
