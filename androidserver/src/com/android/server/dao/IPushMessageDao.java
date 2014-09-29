package com.android.server.dao;

import java.util.List;

import com.android.server.po.pushmessage;

public interface IPushMessageDao {
	public void save(pushmessage pushmessage);
	public List listAll();

}
