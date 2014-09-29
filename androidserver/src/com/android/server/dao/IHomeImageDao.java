package com.android.server.dao;

import com.android.server.po.homeimage;

public interface IHomeImageDao {
	public void save(homeimage homeimage);
	public void delete(int id);
	public homeimage getImage();

}
