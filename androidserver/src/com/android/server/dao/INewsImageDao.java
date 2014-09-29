package com.android.server.dao;

import java.util.List;

import com.android.server.po.newsimage;

public interface INewsImageDao {
	public void save(newsimage newsimage);
	public List<newsimage> listById(int id);
	public void update(newsimage newsimage);
	public void delete(int id);

}
