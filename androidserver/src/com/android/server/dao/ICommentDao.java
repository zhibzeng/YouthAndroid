package com.android.server.dao;

import java.util.List;

import com.android.server.po.Comment;

public interface ICommentDao {
	public Boolean save(Comment comment);
	public List<Comment> getCommentByNewsID(int newsid);
	public List<Comment> getCommentByUserID(int userid);
	//query order by page
	public List<Comment> queryByPage(int newsid,int start,int limit);
	public int getCommentCounts(int newsid);
	public void delete(int id);
}
