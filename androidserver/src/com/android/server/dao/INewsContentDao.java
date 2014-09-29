package com.android.server.dao;
import java.util.Date;
import java.util.List;

import com.android.server.po.newsCategory;
import com.android.server.po.newsContent;

public interface INewsContentDao{
	public void save(newsContent newsContent);
	public void delete(newsContent newsContent);
	public List<newsContent> ListAll();
	public void update(newsContent newsContent);
	public List queryPerPage(int offset,int length);
	public newsContent queryById(int id);
	public boolean articleDelete(int id);
	public newsContent getId(String title,Date time,int category);
	//查询某一子类下的给定数量的新闻
	public List<newsContent> getByCategory(int category,int length);//按时间顺序
	//按ID顺序
	public List<newsContent> getByCategoryOrderById(int category,int length);

}
