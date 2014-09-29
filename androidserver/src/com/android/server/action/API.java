package com.android.server.action;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import sun.org.mozilla.javascript.internal.regexp.SubString;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.android.server.po.homeimage;
import com.android.server.po.newsCategory;
import com.android.server.po.newsCategoryImage;
import com.android.server.po.newsContent;
import com.android.server.po.newsaudio;
import com.android.server.po.newsimage;
import com.android.server.service.IHomeImageService;
import com.android.server.service.INewsAudioService;
import com.android.server.service.INewsCategoryImageService;
import com.android.server.service.INewsCategoryService;
import com.android.server.service.INewsContentService;
import com.android.server.service.INewsImageService;
import com.android.server.action.JsonActionSupport;
public class API extends JsonActionSupport{
	private int id;//文章ID
	private int category;
	private int length=0;
	private INewsCategoryService newsCategoryService;
	private INewsContentService newsContentService;
	private INewsImageService newsImageService;
	private INewsCategoryImageService categoryImageService;
	private IHomeImageService homeImageService;
	private INewsAudioService newsAudioService;
	
	//微信接口
	private String Content;
	private String fromUsername;
	private String toUsername;
	
	//微信运动计时接口
	public void getSportData() throws Exception{
		String[] reqContent = Content.trim().split(" ");
		if(reqContent.length!=2){
			write2Client("请求格式不正确哦");
			return ;
		}
		String result = new String();
		result="";
		//本学期运动时长
		String totalTime = getData("http://202.115.73.17:8000/match/api/total_time/"+reqContent[1]);
		//用户排名
		String rank = getData("http://202.115.73.17:8000/match/api/rank/"+reqContent[1]);
		//本周运动计时
		String week_amount_time =  getData("http://202.115.73.17:8000/match/api/week_amount_time/"+reqContent[1]);
		//运动时间分布 上午，中午，下午
		String day_distribute = getData("http://202.115.73.17:8000/match/api/day_distribute/"+reqContent[1]);
		if(totalTime!=null){
			result = result+"这学期您的总运动时长为："+totalTime+"h\n";
		}
		if(rank!=null){
			result = result+"您的排名为: 第"+rank+"名"+"\n";
		}
		if(week_amount_time!=null){
			JSONObject jsonObject = JSONObject.fromObject(week_amount_time);
			int mon = jsonObject.getInt("1");
			int tue = jsonObject.getInt("2");
			int wed = jsonObject.getInt("3");
			int thu = jsonObject.getInt("4");
			int fri = jsonObject.getInt("5");
			int sat = jsonObject.getInt("6");
			int sun = jsonObject.getInt("7");
			result = result +"按星期分布："+"\n"
					+"周一 :"+mon+"h\n"
					+"周二:"+tue+"h\n"
					+"周三:"+wed+"h\n"
					+"周四 :"+thu+"h\n"
					+"周五 :"+fri+"h\n"
					+"周六 :"+sat+"h\n"
					+"周日 :"+sun+"h\n";
		}
		if(day_distribute!=null){
			String[] day_d = day_distribute.split(",");
			String mor = day_d[0].substring(1,day_d[0].length()-1);
			String mid = day_d[1];
			String aft = day_d[2].substring(0,day_d[2].length()-2);
			result = result +"按时段分布："+"\n"
					+"上午"+mor+"h"+"\n"
					+"中午"+mid+"h"+"\n"
					+"下午"+aft+"h"+"\n";
		}
		System.out.println("Conetnt" + Content);
		System.out.println("fromUsername" + fromUsername);
		write2Client(result);
	}
	
	

	
	
	
	
	
	
	
	
	/**
	 * 访问网络
	 * @param path
	 * @return
	 * @throws Exception
	 */
	 private String getData(String path) throws Exception {  
		 	URL url = new URL(path);  
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
	        connection.connect();  
	        InputStream inputStream = connection.getInputStream();  
	        //对应的字符编码转换  
	        Reader reader = new InputStreamReader(inputStream, "UTF-8");  
	        BufferedReader bufferedReader = new BufferedReader(reader);  
	        String str = null;  
	        StringBuffer sb = new StringBuffer();  
	        while ((str = bufferedReader.readLine()) != null) {  
	            sb.append(str);  
	        }  
	        reader.close();  
	        connection.disconnect();  
	        return sb.toString(); 
	    }  
	 

	
	
	
	//获得所有的新闻分类
	public void getAllCategory(){
		List<newsCategory> newsCategories = null;
		newsCategories = newsCategoryService.ListAll();
		JSONArray array = new JSONArray();
		JSONObject o = new JSONObject();
		for(newsCategory category:newsCategories){
			JSONObject jobject = new JSONObject();
			jobject.put("id", category.getId());
			jobject.put("name", category.getName()+"");
			array.add(jobject);
		}
		o.put("total", newsCategories.size());
		o.put("rows", array);
		write2Client(o.toString());
	}
	//获取某一类别下的给定数量的文章
	public void getArticle(){
		//length!=0则按指定返回数量，否则，返回该分类下的最近10篇文章，DAO层按时间排序返回
		if(length==0){length=10;}
		List<newsContent> newsContents=null;//该文章相关的图片链接
		newsContents = newsContentService.getByCategory(category, length);
			JSONArray array = new JSONArray();
			JSONObject o = new JSONObject();
			if(newsContents==null||newsContents.size()==0){
				JSONObject jobject = new JSONObject();
				array.add(jobject);
				o.put("total", 0);
				o.put("rows", array);
			}else{
				List<newsimage> newsimages=null;
				for(newsContent content:newsContents){
					JSONObject jobject = new JSONObject();
					jobject.put("id",content.getId());
					jobject.put("title",content.getTitle()+"");
					jobject.put("time", content.getTime()+"");
					jobject.put("category",content.getCategory());
					jobject.put("comefrom",content.getComefrom());
					jobject.put("content", content.getContent()+"");
					newsimages=newsImageService.listById(content.getId());
					if(newsimages!=null){//如果该文章具有新闻配图
						JSONArray imagearr = new JSONArray();
						for(newsimage newsimage:newsimages){
							JSONObject imageobj=new JSONObject();
							imageobj.put("path",newsimage.getImagepath()+"");
							imagearr.add(imageobj);
						}
						jobject.put("image",imagearr.toString());
					}else{//如果该文章没有新闻 配图
						JSONArray imagearr = new JSONArray();
						JSONObject imageobj=new JSONObject();
						imagearr.add(imageobj);
						jobject.put("image",imagearr.toString());
					}
					array.add(jobject);
				}
				o.put("total", newsContents.size());
				o.put("rows", array);
			}
			write2Client(o.toString());
	}
	
	
	//获取某分类下的新闻标题列表及发布时间列表
	public void getArticleTitle(){
		if(length==0){length=10;}
		List<newsContent> newsContents=null;//该文章相关的图片链接
		newsContents = newsContentService.getByCategory(category, length);
			JSONArray array = new JSONArray();
			JSONObject o = new JSONObject();
			if(newsContents==null||newsContents.size()==0){
				JSONObject jobject = new JSONObject();
				array.add(jobject);
				o.put("total", 0);
				o.put("rows", array);
			}else{
				for(newsContent content:newsContents){
					JSONObject jobject = new JSONObject();
					jobject.put("id",content.getId());
					jobject.put("title",content.getTitle()+"");
					jobject.put("time", content.getTime()+"");
					array.add(jobject);
				}
				o.put("total", newsContents.size());
				o.put("rows", array);
			}
			write2Client(o.toString());
	}
	
	//更具文章id获取图片路径
	public void getImage(){
		List<newsimage> newsimages=null;
		JSONArray imagearr = new JSONArray();
		JSONObject jobject = new JSONObject();
		newsimages=newsImageService.listById(id);
		if(newsimages!=null){//如果该文章具有新闻配图
			for(newsimage newsimage:newsimages){
					JSONObject imageobj=new JSONObject();
					imageobj.put("path",newsimage.getImagepath()+"");
					imagearr.add(imageobj);
				}
				jobject.put("total",newsimages.size());
				jobject.put("image",imagearr.toString());
			}else{//如果该文章没有新闻 配图
				JSONObject imageobj=new JSONObject();
				imagearr.add(imageobj);
				jobject.put("total",0);
				jobject.put("image",imagearr.toString());
			}
		write2Client(jobject.toString());
	}
	
	//根据文章ID获取音频路径
	public void getAudio(){
		List<newsaudio> newsaudios=null;
		JSONObject jobject = new JSONObject();
		newsaudios=newsAudioService.listById(id);
		if(newsaudios!=null){//如果该文章具有新闻配图
				jobject.put("total",1);
				jobject.put("audio",newsaudios.get(0).getAudiopath());
			}else{//如果该文章没有新闻 配图
				jobject.put("total",0);
				jobject.put("audio","");
			}
		write2Client(jobject.toString());
	}
	
	//根据新闻ID获得出照片外的所有信息
	public void getContent(){
		newsContent content = new newsContent();
		content=newsContentService.queryById(id);
		JSONObject jobject = new JSONObject();
		jobject.put("id",content.getId());
		jobject.put("title",content.getTitle()+"");
		jobject.put("time", content.getTime()+"");
		jobject.put("category",content.getCategory());
		jobject.put("comefrom",content.getComefrom());
		jobject.put("content", content.getContent()+"");
		write2Client(jobject.toString());
	}
	
	//根据新闻类别获得类别图片
	public void getCategoryImage(){
		newsCategoryImage categoryImage = new newsCategoryImage();
		categoryImage=categoryImageService.getByCategoryID(category);
		JSONObject jobject = new JSONObject();
		if(categoryImage!=null){
			jobject.put("path",categoryImage.getImagepath()+"");
			
		}else{
			jobject.put("path","");
		}
		write2Client(jobject.toString());	
	}

	//获取首页图片
	public void getHomeImage(){
		homeimage homeimage = new homeimage();
		homeimage=homeImageService.getImage();
		JSONObject jobject = new JSONObject();
		if(homeimage!=null){
			jobject.put("path",homeimage.getPath()+"");
			jobject.put("id", homeimage.getId()+"");
			
		}else{
			jobject.put("path","");
			jobject.put("id","");
		}
		write2Client(jobject.toString());	
	}
	public INewsCategoryService getNewsCategoryService() {
		return newsCategoryService;
	}

	public void setNewsCategoryService(INewsCategoryService newsCategoryService) {
		this.newsCategoryService = newsCategoryService;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public INewsContentService getNewsContentService() {
		return newsContentService;
	}
	public void setNewsContentService(INewsContentService newsContentService) {
		this.newsContentService = newsContentService;
	}
	public INewsImageService getNewsImageService() {
		return newsImageService;
	}
	public void setNewsImageService(INewsImageService newsImageService) {
		this.newsImageService = newsImageService;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public INewsCategoryImageService getCategoryImageService() {
		return categoryImageService;
	}
	public void setCategoryImageService(
			INewsCategoryImageService categoryImageService) {
		this.categoryImageService = categoryImageService;
	}
	public IHomeImageService getHomeImageService() {
		return homeImageService;
	}
	public void setHomeImageService(IHomeImageService homeImageService) {
		this.homeImageService = homeImageService;
	}
	public INewsAudioService getNewsAudioService() {
		return newsAudioService;
	}
	public void setNewsAudioService(INewsAudioService newsAudioService) {
		this.newsAudioService = newsAudioService;
	}
	public String getFromUsername() {
		return fromUsername;
	}
	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}
	public String getToUsername() {
		return toUsername;
	}
	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
	
	

}
