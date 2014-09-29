package com.android.server.service.impl;

import java.util.List;

import com.android.server.dao.INewsImageDao;
import com.android.server.po.newsimage;
import com.android.server.service.INewsImageService;

public class NewsImageService implements INewsImageService {
	private INewsImageDao newsImageDao;

	public INewsImageDao getNewsImageDao() {
		return newsImageDao;
	}

	public void setNewsImageDao(INewsImageDao newsImageDao) {
		this.newsImageDao = newsImageDao;
	}

	public List<newsimage> listById(int id) {
		return newsImageDao.listById(id);
	}

	public void save(newsimage newsimage) {
		newsImageDao.save(newsimage);
	}

	public void update(newsimage newsimage) {
		newsImageDao.update(newsimage);

	}

	public void delete(int id) {
		newsImageDao.delete(id);
		
	}

}
