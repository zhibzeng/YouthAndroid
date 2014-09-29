package com.android.server.service;

import com.android.server.po.homeimage;

public interface IHomeImageService {
	public void save(homeimage homeimage);
	public void delete(int id);
	public homeimage getImage();
}
