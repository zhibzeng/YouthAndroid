package com.android.server.service;

import java.util.List;

import com.android.server.po.pushmessage;

public interface IPushMessageService {
	public void save(pushmessage pushmessage);
	public List listAll();

}
