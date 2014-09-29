package com.android.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.android.server.dao.INewsCategoryImageDao;
import com.android.server.po.newsCategoryImage;
import com.android.server.po.newsContent;

public class NewsCategoryImageDao extends HibernateDaoSupport implements INewsCategoryImageDao {

	public newsCategoryImage getByCategoryID(int categoryid) {
		
		List list=this.getHibernateTemplate().getSessionFactory().openSession()
		.createQuery("from newsCategoryImage n where n.categoryid=?")
		.setParameter(0,categoryid).list();
		if(list.size()!=0){
			newsCategoryImage categoryImage = (newsCategoryImage)list.get(0);	
			return categoryImage;
		}else{
			return null;
		}
		
	}

	public void save(newsCategoryImage categoryImage) {
		this.getHibernateTemplate().save(categoryImage);
	}
	
	public void delete(int id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			
			Query query = session.createQuery("delete newsCategoryImage n where n.categoryid=?").setParameter(0, id);
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
