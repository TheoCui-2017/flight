package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class segmentDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public segmentDAO() {
		super();
	}
	
	public segment saveSegment(segment segmentInfo){ 
        this.save(segmentInfo);
        return segmentInfo;
	}
	
	public List<segment> getSegmentsByFlag(String unique_flag){
		List<segment> list = this.getSession()
				.createQuery("from  segment where unique_flag =:unique_flag")
				.setString("unique_flag", unique_flag)
				.list();
		return list;
	}
	
	public segment getSegmentByFlagAndFlightNum(String unique_flag, int flight_num){
		List<segment> list = this.getSession()
				.createQuery("from  segment where unique_flag =:unique_flag and flight_num=:flight_num")
				.setString("unique_flag", unique_flag)
				.setInteger("flight_num",flight_num)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public void updateSegment(segment segmentInfo){
		this.update(segmentInfo);
	}
	
	public void updateSegmentMeal(String unique_flag, int flight_num, String meal){
		String sql = "update segment set meal_type=:meal_type where flight_num=:flight_num and unique_flag in (" +"'1-"+unique_flag+"','2-"+unique_flag+ "')";
		this.getSession().createSQLQuery(sql)
		.setString("meal_type", meal)
		.setInteger("flight_num", flight_num).executeUpdate();
	}
	
	public void deleteSegmentsByFlags(List<String> unique_flags){
		this.getSession()
				.createSQLQuery("delete from  segment where unique_flag in (:unique_flags)")
				.setParameterList("unique_flags", unique_flags).executeUpdate();
	}
	
	public void deleteSegmentsByFlag(String unique_flag){
		String sql = "delete from  segment where unique_flag in (" +"'1-"+unique_flag+"','2-"+unique_flag+ "')";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public List<segment> getSegmentsByFlags(List<String> unique_flags){
		List<segment> list = this.getSession()
				.createQuery("from  segment where unique_flag in (:unique_flags)")
				.setParameterList("unique_flags", unique_flags)
				.list();
		return list;
	}
}
