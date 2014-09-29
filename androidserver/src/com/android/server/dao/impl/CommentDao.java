package com.android.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.android.server.dao.ICommentDao;
import com.android.server.po.Comment;

public class CommentDao extends HibernateDaoSupport implements ICommentDao {

	public List<Comment> getCommentByNewsID(int newsid) {
		// TODO Auto-generated method stub
		List<Comment> commentList = null;
 		commentList = this.getHibernateTemplate().getSessionFactory()
		.openSession().createQuery("from Comment c where c.newsid=?")
		.setParameter(0,newsid).list();
		return commentList;
	}

	public List<Comment> getCommentByUserID(int userid) {
		// TODO Auto-generated method stub
		List<Comment> commentList = null;
 		commentList = this.getHibernateTemplate().getSessionFactory()
		.openSession().createQuery("from Comment c where c.userid=?")
		.setParameter(0,userid).list();
		return commentList;	}

	public Boolean save(Comment comment) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(comment);
		return true;
	}

	public List<Comment> queryByPage(int newsid, int start, int limit) {
		List<Comment> commentList = this.getSession().createQuery("from Comment t where t.newsid=?").setParameter(0,newsid)
		.setFirstResult(start)
		.setMaxResults(limit).list();
		return commentList;
	}
	
	/**
	 * get all comment Counts in specific news
	 * @return
	 */
	public int getCommentCounts(int newsid) {
		Number number = (Number)this.getSession()
				.createQuery("Select Count(t) from Comment t where t.newsid=?").setParameter(0,newsid)
				.uniqueResult();
		return number.intValue();
	}

	public void delete(int id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.createQuery("delete Comment c where c.id=?").setParameter(0,id);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
	}

}
