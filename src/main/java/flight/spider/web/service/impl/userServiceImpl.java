package flight.spider.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flight.spider.web.bean.user;
import flight.spider.web.dao.impl.userDAO;
import flight.spider.web.service.userService;

@Service("userService")
public class userServiceImpl implements userService{
	@Autowired
	private userDAO userDao;
	
	@Override
	public user saveUser(user userInfo) {
		return userDao.saveUser(userInfo);
	}

	@Override
	public user getUserByUsername(String username) {
		return userDao.getUserByUsername(username);
	}

	@Override
	public void updateUser(user userInfo) {
		userDao.updateUser(userInfo);
		
	}
}
