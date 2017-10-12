package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class cityDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public cityDAO() {
		super();
	}
	
	public city saveCity(city cityInfo){
		this.save(cityInfo);
        return cityInfo;
	}
	
	public void updateCity(city cityInfo){
		this.update(cityInfo);
	}
	
	// 获取城市列表
	public List<city> getCitys(){
		List<city> list = this.getSession()
				.createQuery("from city order by create_at")
				.list();
		return list;
	}
	
	// 获取城市列表
		public List<city> getCitysByAreaOrCountry(String areaOrCountry){
			List<city> list = this.getSession()
					.createQuery("from city where area=:area or country=:country")
					.setString("area", areaOrCountry)
					.setString("country", areaOrCountry)
					.list();
			return list;
		}
	
	// 根据id获取城市信息
	public city getCityById(int id){
		List<city> list = this.getSession()
				.createQuery("from  city where id=:id ")
				.setInteger("id", id)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	// 根据id删除城市信息
	public void deleteCityById(int id){
		this.getSession()
		.createSQLQuery("delete from city where id=:id")
		.setInteger("id", id)
		.executeUpdate();
	}
	
	// 根据code获取城市信息
		public city getCityByCode(String code){
			List<city> list = this.getSession()
					.createQuery("from  city where code=:code ")
					.setString("code", code)
					.list();
			return list==null || list.size()<1 ? null : list.get(0);
		}
}
