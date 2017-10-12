package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class currencyDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public currencyDAO() {
		super();
	}
	
	public currency saveCurrency(currency currencyInfo){
		this.save(currencyInfo);
        return currencyInfo;
	}
	
	public void updateCurrency(currency currencyInfo){
		this.update(currencyInfo);
	}
	
	public void updateRateByCode(String code, String rate){
		this.getSession().createSQLQuery("update currency set rate=:rate,update_at=now() where code=:code")
		.setString("code", code)
		.setString("rate", rate).executeUpdate();
	}
	
	public List<currency> getCurrencys(){
		List<currency> list = this.getSession()
				.createQuery("from currency order by update_at")
				.list();
		return list;
	}
	
	public currency getCurrencyByCode(String code){
		List<currency> list = this.getSession()
				.createQuery("from currency where code =:code")
				.setString("code", code)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public void deleteCurrencyByCode(String code){
		this.getSession()
		.createSQLQuery("delete from currency where code =:code")
		.setString("code", code).executeUpdate();
	}
	
	
}
