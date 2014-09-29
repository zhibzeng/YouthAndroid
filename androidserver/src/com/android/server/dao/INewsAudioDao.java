package com.android.server.dao;
import java.util.List;
import com.android.server.po.newsaudio;
public interface INewsAudioDao {
	public void save(newsaudio newsaudio);
	public List<newsaudio> listById(int id);
	public void update(newsaudio newsaudio);
	public void delete(int id);

}
