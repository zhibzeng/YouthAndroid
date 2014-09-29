package com.android.server.dao;

import java.util.List;

import com.android.server.po.newsCategory;

public interface INewsCategoryDao {
	public void save(newsCategory newsCategory);
	public void delete(newsCategory newsCategory);
	public List ListAll();
	public void update(newsCategory newsCategory);
	public newsCategory queryById(int id);
	public List queryPerPage(int offset, int length);
	public int getID(String name);

}
