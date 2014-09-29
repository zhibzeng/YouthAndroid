package com.android.server.service.impl;

import java.util.List;

import com.android.server.dao.INewsCategoryDao;
import com.android.server.po.PageBean;
import com.android.server.po.newsCategory;
import com.android.server.po.newsContent;

public class NewsCategoryService implements
		com.android.server.service.INewsCategoryService {
	private INewsCategoryDao newsCategoryDao;

	public List  ListAll() {
		// TODO Auto-generated method stub
		return newsCategoryDao.ListAll();
	}

	public void delete(newsCategory newsCategory) {
		// TODO Auto-generated method stub
		newsCategoryDao.delete(newsCategory);
		
	}

	public void save(newsCategory newsCategory) {
		// TODO Auto-generated method stub
		newsCategoryDao.save(newsCategory);
	}

	public void update(newsCategory newsCategory) {
		// TODO Auto-generated method stub
	newsCategoryDao.update(newsCategory);	
	}

	public INewsCategoryDao getNewsCategoryDao() {
		return newsCategoryDao;
	}

	public void setNewsCategoryDao(INewsCategoryDao newsCategoryDao) {
		this.newsCategoryDao = newsCategoryDao;
	}

	public newsCategory queryById(int id) {
		// TODO Auto-generated method stub
		return newsCategoryDao.queryById(id);
		
	}

	public PageBean queryForPage(int pageSize, int page) {
		// TODO ��ҳ��ʾ������Ϣ
		int allRow = newsCategoryDao.ListAll().size();
		int totalPage = PageBean.countTotalPage(pageSize, allRow);
		final int offset = PageBean.countOffset(pageSize, page); //��ǰҳ��ʼ��¼
		final int length = pageSize; //ÿҳ��¼��
		final int currentPage = PageBean.countCurrentPage(page);
		List<newsCategory> list = newsCategoryDao.queryPerPage(offset, length);
		//�ѷ�ҳ��Ϣ���浽Bean��
		PageBean pageBean = new PageBean();
		pageBean.setPageSize(pageSize);
		pageBean.setCurrentPage(currentPage);
		pageBean.setAllRow(allRow);
		pageBean.setTotalPage(totalPage);
		pageBean.setList(list);
		pageBean.init();
		return pageBean;
	}

	public int getID(String name) {
		return newsCategoryDao.getID(name);
	}
	

}
