package com.android.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.android.server.dao.INewsImageDao;
import com.android.server.po.newsimage;

public class NewsImageDao extends HibernateDaoSupport implements INewsImageDao {


	public void save(newsimage newsimage) {
		this.getHibernateTemplate().save(newsimage);

	}

	public void update(newsimage newsimage) {
		this.getHibernateTemplate().update(newsimage);
	}

	public List<newsimage> listById(int id) {
		List<newsimage> list=null;
		list=this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from newsimage i where i.newsid=?").setParameter(0,id).list();
		if(list==null||list.size()==0){
			return null;
		}else{
			return list;
		}
	}

	public void delete(int id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			
			Query query = session.createQuery("delete newsimage n where n.newsid=?").setParameter(0, id);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		
	}

}
