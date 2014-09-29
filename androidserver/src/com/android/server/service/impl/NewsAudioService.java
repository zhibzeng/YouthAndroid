package com.android.server.service.impl;
import java.util.List;
import com.android.server.dao.INewsAudioDao;
import com.android.server.po.newsaudio;
import com.android.server.service.INewsAudioService;
public class NewsAudioService implements INewsAudioService{
	
	private INewsAudioDao newsAudioDao;
	
	public void delete(int id) {
		newsAudioDao.delete(id);
	}

	public List<newsaudio> listById(int id) {
		return newsAudioDao.listById(id);
	}

	public void save(newsaudio newsaudio) {
		newsAudioDao.save(newsaudio);
	}

	public void update(newsaudio newsaudio) {
		newsAudioDao.update(newsaudio);
	}
	

	public INewsAudioDao getNewsAudioDao() {
		return newsAudioDao;
	}

	public void setNewsAudioDao(INewsAudioDao newsAudioDao) {
		this.newsAudioDao = newsAudioDao;
	}
	

}
