package com.android.server.service.impl;

import com.android.server.dao.IHomeImageDao;
import com.android.server.po.homeimage;
import com.android.server.service.IHomeImageService;

public class HomeImageService implements IHomeImageService {
	private IHomeImageDao homeImageDao;
	
	public void delete(int id) {
		homeImageDao.delete(id);
	}

	public homeimage getImage() {
		return homeImageDao.getImage();
	}

	public void save(homeimage homeimage) {
		homeImageDao.save(homeimage);
	}

	public IHomeImageDao getHomeImageDao() {
		return homeImageDao;
	}

	public void setHomeImageDao(IHomeImageDao homeImageDao) {
		this.homeImageDao = homeImageDao;
	}
	

}
