package com.android.server.dao;

import com.android.server.po.newsCategoryImage;

public interface INewsCategoryImageDao {
	public void save(newsCategoryImage categoryImage);
	public newsCategoryImage getByCategoryID(int categoryid);
	public void delete(int id);
}
