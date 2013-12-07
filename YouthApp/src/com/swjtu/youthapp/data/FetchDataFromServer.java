package com.swjtu.youthapp.data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.StringUtil;

import com.swjtu.youthapp.httpconnection.HttpConnect;
import com.swjtu.youthapp.po.CategoryEntity;
import com.swjtu.youthapp.po.Comment;
import com.swjtu.youthapp.po.HomeImage;
import com.swjtu.youthapp.po.NewsEntity;
import com.swjtu.youthapp.po.User;
public class FetchDataFromServer {
	private static final String BASE_PATH="http://192.168.1.101:8000/androidserver/";
	public static Map<Integer,NewsEntity> listNewsMap;
	public static Map<Integer,NewsEntity> listNewsTitleMap;
	//constructor
	public FetchDataFromServer(){
		
	}
	
	
	/**
	 * fetch category
	 * @return
	 */
	public List<CategoryEntity> GetCategories(){
		String result = HttpConnect.GetCategory();
		if(result==null){
			return null;
		}
		List<CategoryEntity>categoryList=new ArrayList<CategoryEntity>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			
			for(int i=0;i<jsonArray.length();i++)
			{	CategoryEntity categoryEntity = new CategoryEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				categoryEntity.setId(Integer.parseInt(temp.getString("id")));
				categoryEntity.setName(temp.getString("name"));
				categoryList.add(categoryEntity);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return categoryList;
		}
	}
	
	//fetch category image
	public String getCategoryImagePath(int category){
		String path="";
		String result=HttpConnect.getCategoryImage(category);
		if(result==null){
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			path=BASE_PATH+jsonObject.getString("path");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return path;	
		}
	}

	
	/**
	 * fetch homeimage
	 * @return
	 */
	public HomeImage getHomeImage(){
		HomeImage homeImage = new HomeImage();
		String path="";
		Integer id;
		String result=HttpConnect.GetHomeImage();
		if(result==null){
			return null;
		}
		try {
				JSONObject jsonObject = new JSONObject(result);
				path=jsonObject.getString("path");
				id=Integer.valueOf(jsonObject.getString("id"));
				homeImage.setId(id);
				homeImage.setRemotepath(BASE_PATH+path);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				return homeImage;	
			}	
	}
	
	/**
	 * fetch news in a category
	 * @param category
	 * @param length
	 * @return
	 */
	public List<NewsEntity> GetNews(int category,int length){
		String result = HttpConnect.GetNews(String.valueOf(category), length);
		if(result==null){
			return null;
		}
		if("".equals(result)||result==null){
			return null;
		}
		listNewsMap=new HashMap<Integer, NewsEntity>();
		List<NewsEntity> newsList=new ArrayList<NewsEntity>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for(int i=0;i<jsonArray.length();i++){
				NewsEntity newsEntity = new NewsEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				newsEntity.setId(Integer.parseInt(temp.getString("id")));
				newsEntity.setTitle(temp.getString("title"));
				newsEntity.setTime(temp.getString("time"));
				newsEntity.setCategory(Integer.parseInt(temp.getString("category")));
				newsEntity.setContent(temp.getString("content"));
				newsEntity.setComefrom(temp.getString("comefrom"));
				
				/*
				 * 
				 JSONArray imageArray=temp.getJSONArray("image");
				List<String> imageList=new ArrayList<String>();
				for(int j=0;j<imageArray.length();j++){
					JSONObject image=new JSONObject();
					image=imageArray.getJSONObject(j);
					imageList.add(BASE_PATH+image.getString("path"));					
				}
				newsEntity.setImagePathList(imageList);
				*/
				newsList.add(newsEntity);
				listNewsMap.put(newsEntity.getId(),newsEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			return newsList;
		}
}
	
	
	/**
	 * 根据新闻ID获取新闻标题
	 * @param category
	 * @param length
	 * @return
	 */
	public  List<NewsEntity> GetNewsTitle(String category,int length){
		listNewsTitleMap=new HashMap<Integer, NewsEntity>();
		String result = HttpConnect.GetNewsTitle(String.valueOf(category), length);
		if(result==null){
			return null;
		}
		List<NewsEntity> newsList=new ArrayList<NewsEntity>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for(int i=0;i<jsonArray.length();i++){
				NewsEntity newsEntity = new NewsEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				newsEntity.setId(Integer.parseInt(temp.getString("id")));
				newsEntity.setTitle(temp.getString("title"));
				newsEntity.setTime(temp.getString("time"));
				newsList.add(newsEntity);
				listNewsTitleMap.put(newsEntity.getId(),newsEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			return newsList;
		}
		
	}
	
	
	/**
	 * 根据新闻ID获取新闻配图路径
	 * @param newsid
	 * @return
	 */
	public List<String> getNewsImageUrl(int newsid){
		    String imageaddr=""; 
		    List<String> imgUrl = new ArrayList<String>();   
		    String result=HttpConnect.GetImage(String.valueOf(newsid));
			if(result==null){
				return null;
			}
		    int imageqty;
		    int index0;
		    try 
			{
		    	JSONObject jsonObject = new JSONObject(result);
		    	imageqty=Integer.parseInt(jsonObject.getString("total"));
				JSONArray jsonArray = jsonObject.getJSONArray("image");
				for(index0=0;index0<imageqty;index0++)
				{
					JSONObject temp=jsonArray.getJSONObject(index0);
					imageaddr=BASE_PATH+temp.getString("path");
					imgUrl.add(imageaddr);
				}
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		    return imgUrl;
	}
	
	
	/**
	 * 根据新闻ID获取新闻音频路径
	 */
	public String getNewsAudioPath(int newsid){
		 	String path=null; 
		    String result=HttpConnect.getAudio(String.valueOf(newsid));
			if(result==null){
				return null;
			}
		    try 
			{
		    	JSONObject jsonObject = new JSONObject(result);
		    	int total=Integer.parseInt(jsonObject.getString("total"));
		    	if(total==0)return null;
		    	//path=BASE_PATH+jsonObject.getString("audio");
		    	path=BASE_PATH+jsonObject.getString("audio");
				
			} 
			catch (JSONException e) 
			{
				return null;
			}  
		    return path;
	}
	
	
	
	/**
	 * 用户注册
	 */
	public Integer userRegister(User user){
		String result = HttpConnect.userRegister(user);
		/* type=1 用户已经存在
		 * type=2 注册成功
		 * type=3 注册失败
		 */
		Integer type=3;
		if(result==null){
			return type;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			type = jsonObject.getInt("type");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return type;
	}
	
	
	
	/**
	 *用户登录
	 *type = 0 登陆成功
	 *type = 1 密码错误
	 *type = 2 尚未注册
	 */
	public User userLogin(String name,String password){
		String result = HttpConnect.userLogin(name, password);
		if(result==null){
			return null;
		}
		User user = new User();
		int type = 0;
		try {
			JSONObject jsonObject  = new JSONObject(result);
			type = jsonObject.getInt("type");
			if(type==0){
				user.setId(Integer.parseInt(jsonObject.getString("id")));
				user.setAge(Integer.parseInt(jsonObject.getString("age")));
				user.setName(jsonObject.getString("name"));
				user.setPassword(jsonObject.getString("password"));
				user.setAddress(jsonObject.getString("address"));
				user.setQuestion(jsonObject.getString("question"));
				user.setAnswer(jsonObject.getString("answer"));
				user.setRegistertime(jsonObject.getString("registertime"));
				user.setSex(jsonObject.getString("sex"));
				user.setEmail(jsonObject.getString("email"));
				user.setHobby(jsonObject.getString("hobby"));
				user.setMarry(jsonObject.getString("marry"));
				return user;
			}else{
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception 
			e.printStackTrace();
			return null;
		}
		
	}

	
	/**
	 *提交新闻评论
	 *@param newsid 新闻编号
	 *@param userid 用户编号
	 *@param username 用户名称
	 *@param content 评论内容
	 *type==1 评论提交成功
	 *type==0 评论提交失败
	 */
	public Integer commentSave(int newsid,int userid,String username,String content){
		String result = HttpConnect.commentSave(newsid, userid, username, content);
		int type=0;
		if(result==null){
			return type;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			type = Integer.parseInt(jsonObject.getString("type"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return type;
	}
	
	
	/**
	 * 根据新闻ID获取新闻评论
	 * @param newsid
	 */
	public List<Comment> getCommentByNewsID(int newsid){
		List<Comment> commentList = new ArrayList<Comment>();
		String result = HttpConnect.getCommentByNewsID(newsid);
		if(result==null){
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			int size = Integer.parseInt(jsonObject.getString("size"));
			if(size==0){
				return null;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for(int i=0;i<jsonArray.length();i++){
				JSONObject o = jsonArray.getJSONObject(i);
				Comment comment = new Comment();
				comment.setId(Integer.parseInt(o.getString("id")));
				comment.setNewsid(newsid);
				comment.setUserid(Integer.parseInt(o.getString("userid")));
				comment.setUsername(o.getString("username"));
				comment.setContent(o.getString("content"));
				comment.setCreatetime(o.getString("createtime"));
				commentList.add(comment);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return commentList;
		}
		
	}
	
	
	
	/**
	 * 根据新闻ID获取评论数目
	 * @param newsid
	 * @return
	 * @throws JSONException 
	 */
	public int getCommentNumByNewsID(int newsid) throws JSONException{
		String result = HttpConnect.getCommentByNewsID(newsid);
		int size=0;
		if(result==null){
			return size;
		}
		if(result!=null){
			JSONObject jsonObject = new JSONObject(result);
			size= Integer.parseInt(jsonObject.getString("size"));
		}
		return size;
	}
	
	
	/**
	 * 根据用户ID获取新闻评论列表
	 * @param userid
	 */
	public List<Comment> getCommentByUserID(int userid){
		List<Comment> commentList = new ArrayList<Comment>();
		String result = HttpConnect.getCommentByUserID(userid);
		if(result==null){
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			int size = Integer.parseInt(jsonObject.getString("size"));
			if(size==0){
				return null;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for(int i=0;i<jsonArray.length();i++){
				JSONObject o = jsonArray.getJSONObject(i);
				Comment comment = new Comment();
				comment.setId(Integer.parseInt(o.getString("id")));
				comment.setNewsid(Integer.parseInt(o.getString("newsid")));
				comment.setUserid(userid);
				comment.setUsername(o.getString("username"));
				comment.setContent(o.getString("content"));
				comment.setCreatetime(o.getString("createtime"));
				commentList.add(comment);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commentList;
	}
	

	/**
	 * 获取视觉栏目图片
	 * id:分类(category)id；
	 * count：每一页返回文章的数量；
	 * page：返回页总数目；
	 */
	public List<String> getVisionPicture(int id,int count,int page){
		List<String> imageUrl = new ArrayList<String>();
		String result = HttpConnect.getVisionPicture(id, count, page);
		if(result==null){
			return null;
		}else{
			//Log.d("fetchdata", result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("posts");
				for(int i=0;i<jsonArray.length();i++){
					JSONObject o = jsonArray.getJSONObject(i);
					String content = o.getString("content");
					List<String> tempList=StringUtil.paserPicturePath(content);
					if(tempList!=null){
						for (String string : tempList) {
							imageUrl.add(string);
						}
					}
				}
					
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				return null;
			}
			return imageUrl;
		}
	}	
	
	
}
