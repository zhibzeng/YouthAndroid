package com.swjtu.youthapp.po;

import java.util.List;

public class NewsEntity {
	private int id;
	private int category;
	private String title;
	private String time;
	private String content;
	private String comefrom;
	private List<String> imagePathList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImagePathList() {
		return imagePathList;
	}
	public void setImagePathList(List<String> imagePathList) {
		this.imagePathList = imagePathList;
	}
	public String getComefrom() {
		return comefrom;
	}
	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}
	
	
	

}
