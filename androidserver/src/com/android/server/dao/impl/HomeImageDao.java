package com.android.server.dao.impl;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.android.server.dao.IHomeImageDao;
import com.android.server.po.homeimage;

public class HomeImageDao  extends HibernateDaoSupport implements IHomeImageDao{

	public void delete(int id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			
			Query query = session.createQuery("delete homeimage n where n.id=?").setParameter(0, id);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally{
			session.close();
		}
		
	}

	public homeimage getImage() {
		List list=this.getHibernateTemplate().getSessionFactory().openSession()
		.createQuery("from homeimage").list();
		if(list.size()!=0){
			homeimage homeimage = (homeimage)list.get(0);	
			return homeimage;
		}else{
			return null;
		}
	}
	


	public void save(homeimage homeimage) {
		this.getHibernateTemplate().save(homeimage);
	}

}
