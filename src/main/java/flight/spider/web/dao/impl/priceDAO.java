package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class priceDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public priceDAO() {
		super();
	}

	public price savePrice(price priceInfo){ 
        this.save(priceInfo);
        return priceInfo;
	}
	
	public price getPriceByFlag(String unique_flag){
		List<price> list = this.getSession()
				.createQuery("from  price where unique_flag =:unique_flag")
				.setString("unique_flag", unique_flag)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public void updatePrice(price priceInfo){
		this.update(priceInfo);
	}
	
	public void updatePriceBaggage(String unique_flag,  String carryOn, String firstBaggage, String secondBaggage){
		this.getSession().createSQLQuery("update price set carryOn=:carryOn, firstBaggage=:firstBaggage, secondBaggage=:secondBaggage where unique_flag =:unique_flag")
		.setString("carryOn", carryOn)
		.setString("firstBaggage", firstBaggage)
		.setString("secondBaggage", secondBaggage)
		.setString("unique_flag", unique_flag).executeUpdate();
	}
	
	public void deletePricesByFlags(List<String> unique_flags){
		this.getSession().createSQLQuery("delete from  price where unique_flag in(:unique_flags)")
				.setParameterList("unique_flags", unique_flags).executeUpdate();
	}
	
	public List<price> getPriceByFlags(List<String> unique_flags){
		List<price> list = this.getSession()
				.createQuery("from  price where unique_flag in(:unique_flags)")
				.setParameterList("unique_flags", unique_flags)
				.list();
		return list;
	}
}
