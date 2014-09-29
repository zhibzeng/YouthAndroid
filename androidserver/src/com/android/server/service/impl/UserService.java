package com.android.server.service.impl;

import com.android.server.dao.IUserDao;
import com.android.server.po.user;
import com.android.server.service.IUserService;

public class UserService implements IUserService {
	private IUserDao userDao;

	public user getByName(String name) {
		return userDao.getByName(name);
	}

	public boolean save(user user) {
		return userDao.save(user);
	}

	public boolean update(user user) {
		return userDao.update(user);
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
	

}
