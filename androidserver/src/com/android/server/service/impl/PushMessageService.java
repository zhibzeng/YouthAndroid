package com.android.server.service.impl;

import java.util.List;

import com.android.server.dao.IPushMessageDao;
import com.android.server.po.pushmessage;
import com.android.server.service.IPushMessageService;

public class PushMessageService implements IPushMessageService {
	private IPushMessageDao iPushMessageDao;

	public List listAll() {
		// TODO Auto-generated method stub
		return iPushMessageDao.listAll();
	}

	public void save(pushmessage pushmessage) {
		iPushMessageDao.save(pushmessage);

	}

	public IPushMessageDao getiPushMessageDao() {
		return iPushMessageDao;
	}

	public void setiPushMessageDao(IPushMessageDao iPushMessageDao) {
		this.iPushMessageDao = iPushMessageDao;
	}

}
