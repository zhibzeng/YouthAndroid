package com.android.server.dao.impl;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.android.server.dao.INewsCategoryDao;
import com.android.server.po.newsCategory;
import com.android.server.po.newsContent;
public class NewsCategoryDao extends HibernateDaoSupport implements INewsCategoryDao {
	public List ListAll() {
			return this.getHibernateTemplate().find("from newsCategory");
	}

	public void delete(newsCategory newsCategory) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query= session.createQuery("delete  newscategory n where n.id=?").setParameter(0, newsCategory.getId());
		query.executeUpdate();
		transaction.commit();
		session.close();
		
	}

	public void save(newsCategory newsCategory) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(newsCategory);
		
	}

	public void update(newsCategory newsCategory) {
		// TODO Auto-generated method stub
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query= session.createQuery("update newsCategory n set n.id=?,n.name=? where n.id=?").setParameter(0, newsCategory.getId()).setParameter(1,newsCategory.getName()).setParameter(2,newsCategory.getId());
		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	public newsCategory queryById(int id) {
		// TODO Auto-generated method stub
		newsCategory newsCategory = (newsCategory) this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from newsCategory n where n.id=?").setParameter(0,id).list().get(0);
		return newsCategory;
	}

	public List queryPerPage(int offset, int length) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from newsCategory");
		query.setFirstResult(offset);
		query.setMaxResults(length);
		transaction.commit();
		List list = query.list();
		session.close();
		return list;
	}

	public int getID(String name) {
		newsCategory newsCategory=(newsCategory)this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from newsCategory n where n.name=?").setParameter(0,name).list().get(0);
		return newsCategory.getId();
	}
}
