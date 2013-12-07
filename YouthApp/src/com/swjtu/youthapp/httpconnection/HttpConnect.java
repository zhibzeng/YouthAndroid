package com.swjtu.youthapp.httpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.swjtu.youthapp.po.User;

/**
 * @author zhibinZeng
 * @Date 2013-1-31
 * @version v1.0
 * @Description Connect to server to fetch data via HTTP 
 * 
 */
public class HttpConnect {
	
	private static final String BASE_PATH="http://192.168.1.101:8000/androidserver/";
	/*  构造函数
	 * */
	public HttpConnect(){}
	/*
	 * 获取所有新闻分类
	 * */
	public static String GetCategory()
	{
		String urlgetcategory = BASE_PATH+"getCategory"; 
		String result=null;
		HttpGet request = new HttpGet(urlgetcategory);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			//Log.d("homeimage result http",result);
			return result;	
		}
		
	}
	
	/*
	 *获取新闻类别的图片 
	 * */
	public static String getCategoryImage(int category){
		String urlgetcontent =BASE_PATH+"getCategoryImage?";
		urlgetcontent=urlgetcontent+"category="+category;
		String result=null;
		HttpGet request = new HttpGet(urlgetcontent);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
		
		
	}
	
	
	/*
	 *获取指定新闻分类下的所有新闻信息 
	 * */
	
	public static String GetNews(String category,int length)
	{
		String urlgetnews = BASE_PATH+"getNews?";  
		if("".equals(category)||category==null)
		{
			return null;
		}
		urlgetnews=urlgetnews+"category="+category;
		if(length != 0)
		{
			urlgetnews=urlgetnews+"&length="+Integer.toString(length);
		}
		
		HttpGet request = new HttpGet(urlgetnews);
		String result=null;
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}else{
				result=null;
			}

		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;
		}
	}
	
	
	
	/*
	 * 获取指定新闻的标题
	 * */
	public static String GetNewsTitle(String category,int length)
	{
		String urlgetnewstitle =BASE_PATH+"getNewsTitle?"; 
		if("".equals(category)||category==null)
		{
			return null;
		}
		urlgetnewstitle=urlgetnewstitle+"category="+category;
		if(length != 0)
		{
			urlgetnewstitle=urlgetnewstitle+"&length="+Integer.toString(length);
		}
		
		String result=null;
		HttpGet request = new HttpGet(urlgetnewstitle);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
		
	}
	
	
	/*
	 * 获取新闻内容
	 * */
	public static String GetContent(String id)
	{
		String urlgetcontent =BASE_PATH+"getContent?";
		if("".equals(id)||id==null)
		{
			return null;
		}
		urlgetcontent=urlgetcontent+"id="+id;
		String result="";
		HttpGet request = new HttpGet(urlgetcontent);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}
		finally{
			return result;			
		}
	}
	
	
	public static String executeHttpGet(String id) 
	{
		String urlgetcontent = BASE_PATH+"getContent?";
		String result = null; 
		URL url = null;        
		HttpURLConnection connection = null;         
		InputStreamReader in = null; 
		if("".equals(id)||id==null)
		{
			return null;
		}
		urlgetcontent=urlgetcontent+"id="+id;
		try
		{             
			url = new URL(urlgetcontent); 
			connection = (HttpURLConnection) url.openConnection();     
			in = new InputStreamReader(connection.getInputStream());         
			BufferedReader bufferedReader = new BufferedReader(in);         
			StringBuffer strBuffer = new StringBuffer();         
			String line = null;        
			while ((line = bufferedReader.readLine()) != null) 
			{              
				strBuffer.append(line);            
			}            
			result = strBuffer.toString();    
		}
		catch (Exception e) 
		{            
			e.printStackTrace();      
		} 
		finally 
		{            
			if (connection != null) 
			{                
				connection.disconnect();    
			}             
			if (in != null) 
			{                
				try 
				{         
					in.close();          
				} 
				catch (IOException e) 
				{                    
					e.printStackTrace();           
				}          
			}           
		}        
		return result;   
	}
	
	
	/*
	 * 获取指定新闻图片
	 * */
	public static String GetImage(String id)
	{
		String urlgetimage = BASE_PATH+"getImage?";
		if("".equals(id)||id==null)
		{
			return null;
		}
		urlgetimage=urlgetimage+"id="+id;
		String result="";
		HttpGet request = new HttpGet(urlgetimage);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;
			}
	}
	
	
	/**
	 * 获取指定新闻的音频文件
	 * @return id NEWSID
	 */
	public static String getAudio(String id){
		String urlgetaudio =BASE_PATH+"getAudio?";
		if("".equals(id)||id==null)
		{
			return null;
		}
		urlgetaudio=urlgetaudio+"id="+id;
		String result=null;
		HttpGet request = new HttpGet(urlgetaudio);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;
			}
	}
	

	/*
	 * 获取应用启动图片
	 * */
	public static String GetHomeImage()
	{
		String urlgetimage = BASE_PATH+"getHomeImage";
		String result=null;
		HttpGet request = new HttpGet(urlgetimage);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;
		}
	}
	
	
	/**
	 * 用户注册请求
	 * @Description 通过HTTP POST提交数据
	 * 
	 */
	public static String userRegister(User user){
		String url =  BASE_PATH+"register";
		String result=null;
		 HttpParams parms = new BasicHttpParams();
		 parms.setParameter("charset", HTTP.UTF_8);
		 HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		 HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		 HttpClient httpclient = new DefaultHttpClient(parms);
		 HttpPost httppost = new HttpPost(url);
		 httppost.addHeader("charset", HTTP.UTF_8);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name",user.getName()));
			nameValuePairs.add(new BasicNameValuePair("password",user.getPassword()));
			nameValuePairs.add(new BasicNameValuePair("sex",user.getSex()));
			nameValuePairs.add(new BasicNameValuePair("address",user.getAddress()));
			nameValuePairs.add(new BasicNameValuePair("answer",user.getAnswer()));
			nameValuePairs.add(new BasicNameValuePair("question",user.getQuestion()));
			nameValuePairs.add(new BasicNameValuePair("age",user.getAge()+""));
			nameValuePairs.add(new BasicNameValuePair("registertime",user.getRegistertime()));
			nameValuePairs.add(new BasicNameValuePair("marry",user.getMarry()));
			nameValuePairs.add(new BasicNameValuePair("hobby",user.getHobby()));
			nameValuePairs.add(new BasicNameValuePair("email",user.getEmail()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			HttpResponse response; 
			response=httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				
			}else{
				return null;
			}
		} catch (Exception e) {
			result=null;
			}finally{
				return result;				
			}
	}
	
	/**
	 * 用户登录
	 */
	public static String  userLogin(String name,String password){
		String url =  BASE_PATH+"login";
		String result=null;
		 HttpParams parms = new BasicHttpParams();
		 parms.setParameter("charset", HTTP.UTF_8);
		 HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		 HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		 HttpClient httpclient = new DefaultHttpClient(parms);
		 HttpPost httppost = new HttpPost(url);
		 httppost.addHeader("charset", HTTP.UTF_8);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name",name));
			nameValuePairs.add(new BasicNameValuePair("password",password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			HttpResponse response; 
			response=httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				
			}else{
				return null;
			}
		} catch (Exception e) {
			result=null;
			}finally{
				return result;	
			}
		
	}
	
	/**
	 *提交新闻评论
	 *@param newsid 新闻编号
	 *@param userid 用户编号
	 *@param username 用户名称
	 *@param content 评论内容
	 */
	public static String commentSave(int newsid,int userid,String username,String content){
		String url = BASE_PATH+"commentSave";
		String result=null;
		 HttpParams parms = new BasicHttpParams();
		 parms.setParameter("charset", HTTP.UTF_8);
		 HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		 HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		 HttpClient httpclient = new DefaultHttpClient(parms);
		 HttpPost httppost = new HttpPost(url);
		 httppost.addHeader("charset", HTTP.UTF_8);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("newsid",newsid+""));
			nameValuePairs.add(new BasicNameValuePair("userid",userid+""));
			nameValuePairs.add(new BasicNameValuePair("username",username));
			nameValuePairs.add(new BasicNameValuePair("content",content));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			HttpResponse response; 
			response=httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				
			}else{
				return null;
			}
		} catch (Exception e) {
			result=null;
			}finally{
				return result;			
			}
	}
	
	
	
	/**
	 * 根据新闻ID获取新闻评论列表
	 * @param newsid
	 */
	public static String getCommentByNewsID(int newsid){
		String url = BASE_PATH+"getCommentByNewsID";
		String result=null;
		 HttpParams parms = new BasicHttpParams();
		 parms.setParameter("charset", HTTP.UTF_8);
		 HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		 HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		 HttpClient httpclient = new DefaultHttpClient(parms);
		 HttpPost httppost = new HttpPost(url);
		 httppost.addHeader("charset", HTTP.UTF_8);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("newsid",newsid+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			HttpResponse response; 
			response=httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				
			}else{
				return null;
			}
		} catch (Exception e) {
			result=null;
			}finally{
				return result;				
			}
		
	}
	
	
	/**
	 * 根据新闻ID获取新闻评论数目
	 * @param newsid
	 */
	public static String getCommentNumByNewsID(int newsid){
		String url = BASE_PATH+"getCommentNumByNewsID";
		String result=null;
		 HttpParams parms = new BasicHttpParams();
		 parms.setParameter("charset", HTTP.UTF_8);
		 HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		 HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		 HttpClient httpclient = new DefaultHttpClient(parms);
		 HttpPost httppost = new HttpPost(url);
		 httppost.addHeader("charset", HTTP.UTF_8);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("newsid",newsid+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			HttpResponse response; 
			response=httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				
			}else{
				return null;
			}
		} catch (Exception e) {
			result=null;
			}finally{
				return result;				
			}
	}
	
	
	
	/**
	 * 根据用户ID获取新闻评论列表
	 * @param userid
	 */
	public static String getCommentByUserID(int userid){
		String url = BASE_PATH+"getCommentByUserID";
		String result=null;
		 HttpParams parms = new BasicHttpParams();
		 parms.setParameter("charset", HTTP.UTF_8);
		 HttpConnectionParams.setConnectionTimeout(parms, 8 * 1000);
		 HttpConnectionParams.setSoTimeout(parms, 8 * 1000);
		 HttpClient httpclient = new DefaultHttpClient(parms);
		 HttpPost httppost = new HttpPost(url);
		 httppost.addHeader("charset", HTTP.UTF_8);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("userid",userid+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			HttpResponse response; 
			response=httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				
			}else{
				return null;
			}
		} catch (Exception e) {
			result=null;
			}finally{
				return result;
			}
	}
	
	
	
	
	/**WordPresss**/
	
	/**
	 * 获取视觉栏目图片
	 * id:分类(category)id；
	 * count：每一页返回文章的数量；
	 * page：返回页总数目；
	 */
	public static String getVisionPicture(int id,int count,int page){
		String url = "http://192.168.1.101/wordpress/";
		//String url = "http://192.168.1.100/wordpress/";
		
		String result=null;
		HttpGet request = new HttpGet(url+"?json=get_category_posts&id="+id+"&count="+count+"&page="+page);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
	}
	
	

}
