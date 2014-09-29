package com.android.server.service;

import java.util.List;

import com.android.server.po.Comment;

public interface ICommentService {
	public boolean save(Comment comment);
	public List<Comment> getCommentByNewsID(int newsid);
	public List<Comment> getCommentByUserID(int userid);
	public List<Comment> queryByPage(int newsid, int start, int limit);
	public int getCommentCounts(int newsid);
	public void delete(int id);
}
