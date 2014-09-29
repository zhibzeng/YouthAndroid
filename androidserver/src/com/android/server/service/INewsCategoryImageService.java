package com.android.server.service;
import com.android.server.po.newsCategoryImage;
public interface INewsCategoryImageService {
	public void save(newsCategoryImage categoryImage);
	public newsCategoryImage getByCategoryID(int categoryid);
	public void delete(int categoryid);
}
