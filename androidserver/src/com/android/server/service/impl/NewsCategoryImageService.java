package com.android.server.service.impl;

import com.android.server.dao.impl.NewsCategoryImageDao;
import com.android.server.po.newsCategoryImage;
import com.android.server.service.INewsCategoryImageService;

public class NewsCategoryImageService implements INewsCategoryImageService{
	private NewsCategoryImageDao categoryImageDao;

	public newsCategoryImage getByCategoryID(int categoryid) {
		
		return categoryImageDao.getByCategoryID(categoryid);
	}

	public void save(newsCategoryImage categoryImage) {
		categoryImageDao.save(categoryImage);
		
	}

	public NewsCategoryImageDao getCategoryImageDao() {
		return categoryImageDao;
	}

	public void setCategoryImageDao(NewsCategoryImageDao categoryImageDao) {
		this.categoryImageDao = categoryImageDao;
	}

	public void delete(int categoryid) {
		categoryImageDao.delete(categoryid);
		
	}
	

}
