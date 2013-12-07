package com.swjtu.youthapp.po;

import java.io.Serializable;

/**
 * ĞÂÎÅÆÀÂÛBean
 * @author zhibinZeng
 * @Date 2013-2-3
 */
public class Comment implements Serializable{
	private int id;
	private int newsid;
	private int userid;
	private String content;
	private String username;
	private String createtime;
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
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
}