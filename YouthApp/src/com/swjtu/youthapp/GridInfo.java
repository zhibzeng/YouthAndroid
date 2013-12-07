package com.swjtu.youthapp;
import android.graphics.Bitmap;

/**
 * Copyright (C) 2010,Under the supervision of China Telecom Corporation
 * Limited Guangdong Research Institute
 * The New Vphone Project
 * @Author fonter.yang
 * @Create date£º2010-10-11
 * 
 */
public class GridInfo {
	private int categoryid;
	private String name;
	private Bitmap image;
	public GridInfo(String name,int category,Bitmap image) {
		super();
		this.name = name;
		this.categoryid=category;
		this.image=image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	
	
	
}
