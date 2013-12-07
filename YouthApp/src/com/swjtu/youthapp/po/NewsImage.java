package com.swjtu.youthapp.po;

import java.util.List;

public class NewsImage {
	private int id;
	private int newsid;
	private String sdPath;
	private String remotePath;
	private List<String> sdpathList;
	private List<String> remotepathList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}
	public String getSdPath() {
		return sdPath;
	}
	public void setSdPath(String sdPath) {
		this.sdPath = sdPath;
	}
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public List<String> getSdpathList() {
		return sdpathList;
	}
	public void setSdpathList(List<String> sdpathList) {
		this.sdpathList = sdpathList;
	}
	public List<String> getRemotepathList() {
		return remotepathList;
	}
	public void setRemotepathList(List<String> remotepathList) {
		this.remotepathList = remotepathList;
	}
	
	

}
