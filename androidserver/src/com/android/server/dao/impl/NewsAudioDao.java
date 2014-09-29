package com.android.server.dao.impl;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.android.server.dao.INewsAudioDao;
import com.android.server.po.newsaudio;
public class NewsAudioDao extends HibernateDaoSupport implements INewsAudioDao{
	public void delete(int id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			
			Query query = session.createQuery("delete newsaudio n where n.newsid=?").setParameter(0, id);
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

	public List<newsaudio> listById(int id) {
		List<newsaudio> list=null;
		list=this.getHibernateTemplate().getSessionFactory().openSession()
		.createQuery("from newsaudio i where i.newsid=?").setParameter(0,id).list();
		if(list==null||list.size()==0){
			return null;
		}else{
			return list;
		}
	}

	public void save(newsaudio newsaudio) {
		this.getHibernateTemplate().save(newsaudio);
	}

	public void update(newsaudio newsaudio) {
		this.getHibernateTemplate().update(newsaudio);
	}

}
