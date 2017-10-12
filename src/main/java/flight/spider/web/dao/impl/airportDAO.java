package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class airportDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public airportDAO() {
		super();
	}
	
	public airport saveAirport(airport airportInfo){
		this.save(airportInfo);
        return airportInfo;
	}
	
	public void updateAirport(airport airportInfo){
		this.update(airportInfo);
	}
	
	// 获取机场列表
	public List<airport> getAirports(){
		List<airport> list = this.getSession()
				.createQuery("from airport order by create_at desc")
				.list();
		return list;
	}
	
	// 根据cityId获取机场列表
	public List<airport> getAirportByCityId(int cityId){
		List<airport> list = this.getSession()
				.createQuery("from airport where cityId=:cityId order by create_at desc")
				.setInteger("cityId", cityId)
				.list();
		return list;
	}
	
	// 根据id获取机场信息
	public airport getAirportById(int id){
		List<airport> list = this.getSession()
				.createQuery("from airport where id=:id ")
				.setInteger("id", id)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	// 根据id获取机场信息
	public airport getAirportByCode(String code){
		List<airport> list = this.getSession()
				.createQuery("from airport where code=:code ")
				.setString("code", code)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
		
	// 根据id删除机场信息
	public void deleteAirportById(int id){
		this.getSession()
		.createSQLQuery("delete from airport where id=:id")
		.setInteger("id", id)
		.executeUpdate();
	}
}
