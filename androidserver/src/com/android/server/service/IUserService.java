package com.android.server.service;

import com.android.server.po.user;

public interface IUserService {
	public boolean save(user user);
	public user getByName(String name);
	public boolean update(user user);

}
