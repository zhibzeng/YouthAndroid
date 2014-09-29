package com.android.server.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.android.server.dao.IPushMessageDao;
import com.android.server.po.pushmessage;

public class PushMessageDao extends HibernateDaoSupport implements IPushMessageDao {

	public List listAll() {
		return this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from pushmessage").list();
		
	}
	public void save(pushmessage pushmessage) {
		this.getHibernateTemplate().save(pushmessage);
	}

}
