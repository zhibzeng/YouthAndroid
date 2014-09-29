package com.android.server.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.android.server.dao.INewsContentDao;
import com.android.server.po.newsContent;
import com.opensymphony.xwork2.ActionContext;
public class NewsContentDao extends HibernateDaoSupport implements INewsContentDao {

	public List<newsContent> ListAll() {
		// TODO Auto-generated method stub
		List<newsContent> listAll = this.getHibernateTemplate().find("from newsContent");
		if(listAll.size()!=0){
			return listAll;	
		}else{
			return null;
		}
		
	}

	public void delete(newsContent newsContent) {
		this.getHibernateTemplate().delete(newsContent);
		
	}

	public void save(newsContent newsContent) {
		this.getHibernateTemplate().save(newsContent);
		
	}

	public void update(newsContent newsContent) {
		this.getHibernateTemplate().update(newsContent);
		
	}

	public List queryPerPage(int offset, int length) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from newsContent");
		query.setFirstResult(offset);
		query.setMaxResults(length);
		transaction.commit();
		List list = query.list();
		session.close();
		return list;
	}

	public newsContent queryById(int id) {
		return (newsContent) this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from newsContent n where n.id=?").setParameter(0,id).list().get(0);
	}

	public boolean articleDelete(int id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			
			Query query = session.createQuery("delete newsContent n where n.id=?").setParameter(0, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			return false;
		}finally{
			session.close();
		}
		
	}

	public newsContent getId(String title, Date time, int category) {
		
		return (newsContent) this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from newsContent n where n.title=? and n.time=? and n.category=?").setParameter(0, title).setParameter(1, time).setParameter(2, category).list().get(0);
	}
	//获取某一子类下给定数量的新闻
	public List<newsContent> getByCategory(int category, int length) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from newsContent n where n.category=? order by time DESC").setParameter(0, category);
		if(length!=0){
			query.setMaxResults(length);	
		}
		transaction.commit();
		List list = null;
		list=query.list();
		session.close();
		if(list==null||list.size()==0){
			return null;
		}else{
			return list;	
		}
		
	}

	public List<newsContent> getByCategoryOrderById(int category, int length) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from newsContent n where n.category=? order by id DESC").setParameter(0, category);
		if(length!=0){
			query.setMaxResults(length);	
		}
		transaction.commit();
		List list = null;
		list=query.list();
		session.close();
		if(list==null||list.size()==0){
			return null;
		}else{
			return list;	
		}
	}

}
