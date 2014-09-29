package com.android.server.service.impl;

import java.util.List;

import com.android.server.dao.ICommentDao;
import com.android.server.po.Comment;
import com.android.server.service.ICommentService;

public class CommentService implements ICommentService {
	private ICommentDao commentDao;

	public List<Comment> getCommentByNewsID(int newsid) {
		// TODO Auto-generated method stub
		return commentDao.getCommentByNewsID(newsid);
	}

	public List<Comment> getCommentByUserID(int userid) {
		// TODO Auto-generated method stub
		return commentDao.getCommentByUserID(userid);
	}

	public boolean save(Comment comment) {
		// TODO Auto-generated method stub
		return commentDao.save(comment);
		
	}
	public ICommentDao getCommentDao() {
		return commentDao;
	}

	public void setCommentDao(ICommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public List<Comment> queryByPage(int newsid, int start, int limit) {
		return commentDao.queryByPage(newsid, start, limit);
	}

	public int getCommentCounts(int newsid) {
		return commentDao.getCommentCounts(newsid);
	}

	public void delete(int id) {
		commentDao.delete(id);
		
	}

	
	

}
