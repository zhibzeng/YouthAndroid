package com.android.server.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.android.server.dao.IUserDao;
import com.android.server.po.user;

public class UserDao extends HibernateDaoSupport implements IUserDao {

	public user getByName(String name) {
		List list = this.getHibernateTemplate().getSessionFactory().openSession().createQuery("from user u where u.name=?").setParameter(0,name).list();
		if(list!=null){
			if(list.size()!=0){
				return (user)list.get(0);
			}else{
				return null;
			}
		
		}else{
			return null;
		}
	}

	public boolean save(user user) {
		this.getHibernateTemplate().save(user);
		return true;
	}

	public boolean update(user user) {
		this.getHibernateTemplate().update(user);
		return true;
	}

}
