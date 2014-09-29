package com.android.server.service.impl;

import java.util.Date;
import java.util.List;

import com.android.server.dao.INewsContentDao;
import com.android.server.po.PageBean;
import com.android.server.po.newsContent;
import com.android.server.service.INewsContentService;

public class NewsContentService implements INewsContentService {

	private INewsContentDao newsContentDao;
	
	public List<newsContent> ListAll() {
		// TODO Auto-generated method stub
		return newsContentDao.ListAll();
	}

	public void delete(newsContent newsContent) {
		// TODO Auto-generated method stub
		newsContentDao.delete(newsContent);
		
	}

	public void save(newsContent newsContent) {
		// TODO Auto-generated method stub
		newsContentDao.save(newsContent);
	}

	public void update(newsContent newsContent) {
		// TODO Auto-generated method stub
		newsContentDao.update(newsContent);
	}

	public INewsContentDao getNewsContentDao() {
		return newsContentDao;
	}

	public void setNewsContentDao(INewsContentDao newsContentDao) {
		this.newsContentDao = newsContentDao;
	}

	public PageBean queryForPage(int pageSize, int page) {
		// TODO Auto-generated method stub
		int allRow = newsContentDao.ListAll().size();
		int totalPage = PageBean.countTotalPage(pageSize, allRow);
		final int offset = PageBean.countOffset(pageSize, page); //��ǰҳ��ʼ��¼
		final int length = pageSize; //ÿҳ��¼��
		final int currentPage = PageBean.countCurrentPage(page);
		List<newsContent> list = newsContentDao.queryPerPage(offset, length);
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

	public newsContent queryById(int id) {
		// TODO Auto-generated method stub
		return newsContentDao.queryById(id);
	}

	public boolean articleDelete(int id) {
		// TODO Auto-generated method stub
		return newsContentDao.articleDelete(id);
	}

	public newsContent getId(String title, Date time, int category) {
		// TODO Auto-generated method stub
		return newsContentDao.getId(title, time, category);
	}

	public List<newsContent> getByCategory(int category, int length) {
		return newsContentDao.getByCategory(category, length);
	}

	public List<newsContent> getByCategoryOrderById(int category, int length) {
		return newsContentDao.getByCategoryOrderById(category, length);
	}

}
