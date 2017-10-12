package flight.spider.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import flight.spider.web.bean.privilege;

@Repository
public class privilegeDAO extends baseDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public privilege savePrivilege(privilege privilegeInfo){ 
        this.save(privilegeInfo);
        return privilegeInfo;
	}
	
	public privilege getPrivilegeByUsername(String username){
		List<privilege> list = this.getSession()
				.createQuery("from privilege where username=:username")
				.setString("username", username)
				.list();
		return list==null || list.size()<1 ? null : list.get(0);
	}
	
	public void updatePrivilege(privilege privilegeInfo){
		this.update(privilegeInfo);
	}
	
	public void deletePrivilegByUsername(String username){
		this.getSession()
		.createSQLQuery("delete from privilege where username=:username")
		.setString("username", username).executeUpdate();
	}
}
