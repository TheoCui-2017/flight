package flight.spider.web.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;
import flight.spider.web.utility.flightComparator;
import flight.spider.web.utility.toolUtil;


@Repository
public class flightDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private flightComparator comparator = new flightComparator();
	public flightDAO() {
		super();
	}
	
	public flight saveFlight(flight flightInfo){ 
        this.save(flightInfo);
        return flightInfo;
	}
	
	public flight getFlightByFlag(String unique_flag){
		List<flight> list = this.getSession()
				.createQuery("from  flight where unique_flag =:unique_flag")
				.setString("unique_flag", unique_flag)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public void updateFlight(flight flightInfo){
		this.update(flightInfo);
	}
	
	public void updateFlightSeat(String unique_flag, int seat_num){
		String sql = "update flight set seat_number=:seat_number where unique_flag in (" +"'1-"+unique_flag+"','2-"+unique_flag+ "')";
		this.getSession().createSQLQuery(sql)
		.setInteger("seat_number", seat_num).executeUpdate();
	}
	
	public void updateFlightPrice(String unique_flag, String priceTotal){
		String sql = "update flight set totalPrice=:totalPrice where unique_flag in (" +"'1-"+unique_flag+"','2-"+unique_flag+ "')";
		this.getSession().createSQLQuery(sql)
		.setString("totalPrice", priceTotal).executeUpdate();
	}
	
	public List<flight> getFlightBySearchFlagLessThanUpdatetime(String search_flag, Date update_at){
		List<flight> list = this.getSession()
				.createQuery("from  flight where search_flag =:search_flag and update_at<:update_at")
				.setString("search_flag", search_flag)
				.setDate("update_at", update_at)
				.list();
		return list;
	}
	
	public void deleteFlights(List<flight> flightsInfo){ 
        this.deleteAll(flightsInfo);
	}
	
	public void deleteFlightByFlag(String unique_flag){
		String sql = "delete from flight where unique_flag in (" +"'1-"+unique_flag+"','2-"+unique_flag+ "')";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public List<flight> getFlightsByCondition(boolean round, String departure_code, String arrival_code, String go_time, String back_time, int seat_type, List<String> sources, int size, String comment){
        String source = "";
        String dep_time = go_time+'%';
		String sql="from flight where (departure_code=:departure_code or depart_airport_code=:departure_code) and (arrival_code=:arrival_code or arrival_airport_code=:arrival_code) "+
        "and departure_time like :departure_time and seat_type=:seat_type and round=:round and unique_flag like :unique_flag";
		if(sources != null && sources.size()>0){
			for(String s:sources){
				source+="'"+s+"',";
			}
			source = source.substring(0, source.length()-1);
		}
		if(!source.isEmpty()){
			sql+=" and source in("+source+")";
		}
		String unique_flag = "%" + go_time;
		if(comment.equals("rt")){
			unique_flag = "%" + back_time;
			if(round){
				unique_flag += "%" + go_time;
			}
		}
		else{
			if(round){
				unique_flag += "%" + back_time;
			}
		}
		unique_flag += "%";
		List<flight> list = this.getSession()
				.createQuery(sql)
				.setString("departure_code", departure_code)
				.setString("arrival_code", arrival_code)
				.setString("departure_time", dep_time)
				.setInteger("seat_type", seat_type)
				.setBoolean("round", round)
				.setString("unique_flag", unique_flag)
				.list();
		list.sort(comparator);
		list = cutListBySize(list, size);
		return list;
	}
	
	public List<Object[]> getFlightInfoByConditions(String departure_city, String arrival_city, Date start_date, Date end_date,
					int seat_type, List<String> stayDays,  List<String> stopovers){
		String stay_days = "";
		String stopoverReg = "";
		String sql="select airline_code,unique_flag as unique_flags from flight where departure_code=:departure_code and arrival_code=:arrival_code "+
				"and cast(departure_time as datetime) > :start_date and cast(departure_time as datetime) < :end_date and round=1";
		if(stayDays != null && stayDays.size()>0){
			for(String s:stayDays){
				stay_days +=s+",";
			}
			stay_days = stay_days.substring(0, stay_days.length()-1);
		}
		if(!stay_days.isEmpty()){
			sql+=" and stay_days in("+stay_days+")";
		}
		
		if(seat_type > 0){
			sql += " and seat_type=" + seat_type;
		}
		if(stopovers != null && stopovers.size()>0){
			for(String s:stopovers){
				stopoverReg += s+"|";
			}
			stopoverReg = stopoverReg.substring(0, stopoverReg.length()-1);
		}
		if(!stopoverReg.isEmpty()){
			sql+=" and stopover REGEXP \""+stopoverReg +"\"";
		}
		List<Object[]> list = this.getSession()
				.createSQLQuery(sql)
				.setString("departure_code", departure_city)
				.setString("arrival_code", arrival_city)
				.setDate("start_date", start_date)
				.setDate("end_date",end_date)
				.list();
		return list;
	}
	
	public Object[] getFlightInfoByAirlineAndUfs(String airline_code, List<String> ufs, List<String> stopovers){
		String unique_flag = "";
		String stopoverReg = "";
		String sql="select '"+airline_code+"' as airline_code, AVG(totalPrice) as price from flight where round=1 ";
		if(ufs != null && ufs.size()>0){
			for(String s:ufs){
				unique_flag +="'"+s+"',";
			}
			unique_flag = unique_flag.substring(0, unique_flag.length()-1);
		}
		if(!unique_flag.isEmpty()){
			sql+=" and unique_flag in("+unique_flag+")";
		}
		
		if(stopovers != null && stopovers.size()>0){
			for(String s:stopovers){
				stopoverReg += s+"|";
			}
			stopoverReg = stopoverReg.substring(0, stopoverReg.length()-1);
		}
		if(!stopoverReg.isEmpty()){
			sql+=" and stopover REGEXP \""+stopoverReg +"\"";
		}
		List<Object[]> list = this.getSession()
				.createSQLQuery(sql)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public List<Object[]> getFlightInfoByUfs(List<String> ufs, List<String> stopovers){
		String stopoverReg = "";
		String unique_flag = "";
		String sql="select airline_code, AVG(totalPrice) as price from flight where round=1 ";
		if(ufs != null && ufs.size()>0){
			for(String s:ufs){
				unique_flag +="'"+s+"',";
			}
			unique_flag = unique_flag.substring(0, unique_flag.length()-1);
		}
		if(!unique_flag.isEmpty()){
			sql+=" and unique_flag in("+unique_flag+")";
		}
		
		if(stopovers != null && stopovers.size()>0){
			for(String s:stopovers){
				stopoverReg += s+"|";
			}
			stopoverReg = stopoverReg.substring(0, stopoverReg.length()-1);
		}
		if(!stopoverReg.isEmpty()){
			sql+=" and stopover REGEXP \""+stopoverReg +"\"";
		}
		sql += " group by airline_code";
		List<Object[]> list = this.getSession()
				.createSQLQuery(sql)
				.list();
		return list;
	}
	
	List<flight> cutListBySize(List<flight> list, int size){
		if(list.size() > size){
			List<flight> newList = list.subList(0, size);
			List<flight> leftList = list.subList(size+1, list.size()-1);
			for(flight index: leftList){
				if(index.getAirline_code().equals("HU")){
					newList.add(index);
					break;
				}
			}
			return newList;
		}
		else{
			return list;
		}
	}
	
	
	
	public List<Object[]> getLegPricesTrend(List<String> departure_city, List<String> arrival_city, Date start_date, Date end_date, List<String> airlineCodes,
			int seat_type, List<String> stayDays, List<String> stopovers){
		String airline_codes = "";
		String stay_days = "";
		String dpartureCond = "";
		String arrivalCond = "";
		String stopoverReg = "";
		if(departure_city.size() == 1)
			dpartureCond = "departure_code=:departure_code ";
		else if(departure_city.size() > 1)
			dpartureCond = "departure_code in (:departure_codes) ";
		if(arrival_city.size() == 1)
			arrivalCond = " and arrival_code=:arrival_code ";
		else if(arrival_city.size() > 1)
			arrivalCond = " and arrival_code in (:arrival_codes) ";
		String sql="select airline_code, totalPrice, departure_time, unique_flag from flight where  "+dpartureCond +arrivalCond+
				"and cast(departure_time as datetime) > :start_date and cast(departure_time as datetime) < :end_date and round=1";
		if(airlineCodes != null && airlineCodes.size()>0){
			for(String s:airlineCodes){
				airline_codes += "'"+s+"',";
			}
			airline_codes = airline_codes.substring(0, airline_codes.length()-1);
		}
		System.out.println(airline_codes.isEmpty()+"===="+airline_codes.length());
		if(airline_codes.length() > 0){
			sql+=" and airline_code in  ("+airline_codes+")";
		}
		if(stayDays != null && stayDays.size()>0){
			for(String s:stayDays){
				stay_days +=s+",";
			}
			stay_days = stay_days.substring(0, stay_days.length()-1);
		}
		if(!stay_days.isEmpty()){
			sql+=" and stay_days in ("+stay_days+")";
		}
		
		if(stopovers != null && stopovers.size()>0){
			for(String s:stopovers){
				stopoverReg += s+"|";
			}
			stopoverReg = stopoverReg.substring(0, stopoverReg.length()-1);
		}
		if(!stopoverReg.isEmpty()){
			sql+=" and stopover REGEXP \""+stopoverReg +"\"";
		}
		
		if(seat_type > 0){
			sql += " and seat_type=" + seat_type;
		}
		SQLQuery query = this.getSession().createSQLQuery(sql);
		if(departure_city.size() == 1)
			query.setString("departure_code", departure_city.get(0));
		else if(departure_city.size() > 1)
			query.setParameterList("departure_codes", departure_city);
		if(arrival_city.size() == 1)
			query.setString("arrival_code", arrival_city.get(0));
		else if(arrival_city.size() > 1)
			query.setParameterList("arrival_codes", arrival_city);
		query.setDate("start_date", start_date).setDate("end_date",end_date);
		List<Object[]> list = query.list();
		return list;
	}
	
	
	public List<Object[]> getFlightPricesByUfs(List<String> ufs, List<String> stopovers){
		String unique_flag = "";
		String stopoverReg = "";
		String sql="select airline_code, totalPrice, departure_time, unique_flag from flight where round=1 ";
		if(ufs != null && ufs.size()>0){
			for(String s:ufs){
				unique_flag +="'"+s+"',";
			}
			unique_flag = unique_flag.substring(0, unique_flag.length()-1);
		}
		if(!unique_flag.isEmpty()){
			sql+=" and unique_flag in("+unique_flag+")";
		}
		
		
		if(stopovers != null && stopovers.size()>0){
			for(String s:stopovers){
				stopoverReg += s+"|";
			}
			stopoverReg = stopoverReg.substring(0, stopoverReg.length()-1);
		}
		if(!stopoverReg.isEmpty()){
			sql+=" and stopover REGEXP \""+stopoverReg +"\"";
		}
		List<Object[]> list = this.getSession()
				.createSQLQuery(sql)
				.list();
		return list;
	}
	
	public List<String> getFlightsByCityCode(String depart, String arrival){
		String sql="select distinct airline_code as airline_code from  flight where stay_days > 0 and round=1 ";
		List<String> departCodes = Arrays.asList(depart.split(","));
		List<String> arrivalCodes = Arrays.asList(arrival.split(","));
		String depart_codes = "";
		String arrival_codes = "";
		for(String s : departCodes){
			depart_codes += "'" + s + "',";
		}
		for(String s : arrivalCodes){
			arrival_codes += "'" + s + "',";
		}
		if(!depart_codes.isEmpty()){
			depart_codes = (String) depart_codes.subSequence(0, depart_codes.length()-1);
			sql+=" and departure_code in("+depart_codes+")";
		}
		if(!arrival_codes.isEmpty()){
			arrival_codes = (String) arrival_codes.subSequence(0, depart_codes.length()-1);
			sql+=" and arrival_code in("+arrival_codes+")";
		}
		System.out.println(sql+"--");
		List<String> list = this.getSession().createSQLQuery(sql).list();
		return list;
	}
	
}
