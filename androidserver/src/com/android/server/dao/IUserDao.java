package com.android.server.dao;

import com.android.server.po.user;

public interface IUserDao {
	public boolean save(user user);
	public user getByName(String name);
	public boolean update(user user);
}
