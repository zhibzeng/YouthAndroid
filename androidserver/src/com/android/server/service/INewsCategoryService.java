package com.android.server.service;

import java.util.List;

import com.android.server.po.PageBean;
import com.android.server.po.newsCategory;

public interface INewsCategoryService {
	public void save(newsCategory newsCategory);
	public void delete(newsCategory newsCategory);
	public List ListAll();
	public void update(newsCategory newsCategory);
	public newsCategory queryById(int id);
	//��ҳ��ʾ
	public PageBean queryForPage(int pageSize,int page);
	public int getID(String name);

}
