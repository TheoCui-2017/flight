package flight.spider.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.user;

@Repository
public class userDAO extends baseDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public user saveUser(user userInfo){ 
        this.save(userInfo);
        return userInfo;
	}
	
	public user getUserByUsername(String username){
		List<user> list = this.getSession()
				.createQuery("from  user where username =:username")
				.setString("username", username)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public void updateUser(user userInfo){
		this.update(userInfo);
	}
}
