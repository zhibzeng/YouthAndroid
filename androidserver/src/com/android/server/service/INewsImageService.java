package com.android.server.service;

import java.util.List;

import com.android.server.po.newsimage;

public interface INewsImageService {
	public void save(newsimage newsimage);
	public void update(newsimage newsimage);
	public List<newsimage> listById(int id);
	public void delete(int id);

}
