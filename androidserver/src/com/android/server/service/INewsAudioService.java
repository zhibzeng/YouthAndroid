package com.android.server.service;
import java.util.List;
import com.android.server.po.newsaudio;
public interface INewsAudioService {
	public void save(newsaudio newsaudio);
	public void update(newsaudio newsaudio);
	public List<newsaudio> listById(int id);
	public void delete(int id);

}
