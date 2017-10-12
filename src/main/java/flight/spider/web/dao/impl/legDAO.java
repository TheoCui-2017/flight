package flight.spider.web.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.*;

@Repository
public class legDAO extends baseDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public legDAO() {
		super();
	}
	
	public leg saveLeg(leg legInfo){
		this.save(legInfo);
        return legInfo;
	}
	
	public void updateLeg(leg legInfo){
		this.update(legInfo);
	}
	
	// 获取航段
	public List<leg> getLegs(){
		List<leg> list = this.getSession()
				.createQuery("from leg order by update_at")
				.list();
		return list;
	}
	
	// 根据城市代码获取航段
	public leg getLegByCodes(String depart_city_code, String arrival_city_code){
		List<leg> list = this.getSession()
				.createQuery("from leg where depart_city_code =:depart_city_code and arrival_city_code =:arrival_city_code")
				.setString("depart_city_code", depart_city_code)
				.setString("arrival_city_code", arrival_city_code)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	// 根据城市代码删除航段
	public void deleteLegByCodes(String depart_city_code, String arrival_city_code){
		this.getSession()
		.createSQLQuery("delete from leg where depart_city_code =:depart_city_code and arrival_city_code =:arrival_city_code")
		.setString("depart_city_code", depart_city_code)
		.setString("arrival_city_code", arrival_city_code)
		.executeUpdate();
	}
	
	public List<leg> getLegsIn(List<Integer> legsids) {
		List<leg> list = this.getSession()
				.createQuery("from leg where id in(:ids)")
				.setParameterList("ids", legsids)
				.list();
		return list;
	}
}
