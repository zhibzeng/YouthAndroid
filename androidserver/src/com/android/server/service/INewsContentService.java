package com.android.server.service;

import java.util.Date;
import java.util.List;

import com.android.server.po.PageBean;
import com.android.server.po.newsContent;

public interface INewsContentService {
	public void save(newsContent newsContent);
	public void delete(newsContent newsContent);
	public List<newsContent> ListAll();
	public void update(newsContent newsContent);
	public PageBean queryForPage(int pageSize,int page);
	public newsContent queryById(int id);
	public boolean articleDelete(int id);
	public newsContent getId(String title,Date time,int category);
	//获取某一新闻类别的指定数量的新闻
	public List<newsContent> getByCategory(int category,int length);
	//按ID顺序
	public List<newsContent> getByCategoryOrderById(int category,int length);
}
