package com.swjtu.youthapp.data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swjtu.youthapp.CategoryOrderActivity;
import com.swjtu.youthapp.common.DeleteFile;
import com.swjtu.youthapp.common.GetInitPreference;
import com.swjtu.youthapp.po.CategoryEntity;
import com.swjtu.youthapp.po.HomeImage;
import com.swjtu.youthapp.po.NewsEntity;

import android.R.bool;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteControl {
	//public static Map<Integer,NewsEntity> listNewsMap;
	private Context context;
	private static final String DATABASE_NAME = "YouthApp.db" ;
	SQLiteDatabase db;
	//在SD上创建数据库
	public SqliteControl(Context context) {
		this.context=context;
		String filename = android.os.Environment.getExternalStorageDirectory()
				+ "/YouthAppData/"+DATABASE_NAME;
		db =context.openOrCreateDatabase(filename,0,null);
	}
	public void CreateTable_Category() {
		try {
			db.execSQL( "CREATE TABLE category (" //创建新闻类别表
					+ "id INTEGER PRIMARY KEY,"
					+ "name TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void CreateTable_News() {
		try {
			db.execSQL( "CREATE TABLE news (" //创建新闻内容表
					+ "id INTEGER PRIMARY KEY,"
					+ "title TEXT,"
					+ "content TEXT,"
					+ "category INTEGER,"
					+ "comefrom TEXT,"
					+ "time TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void CreateTable_NewsImage() {
		try {
			db.execSQL( "CREATE TABLE newsimage (" //创建新闻配图表
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "newsid INTEGER,"
					+ "sdpath TEXT,"
					+ "remotepath TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void CreateTable_categoryImage(){
		try {
			db.execSQL( "CREATE TABLE newscategoryimage (" //创建新闻类别图片表
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "categoryid INTEGER,"
					+ "sdpath TEXT,"
					+ "remotepath TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void CreateTable_CategoryOrder(){//用户订阅的新闻类别
		try {
			db.execSQL( "CREATE TABLE categoryorder (" //创建新闻配图表
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "category INTEGER"
					+ ");" );
		} catch (Exception e){
		}
	}
	public void CreateTable_NewsTitle(){//创建新闻标题表
		try {
			db.execSQL( "CREATE TABLE newstitle (" //创建新闻配图表
					+ "id INTEGER PRIMARY KEY ,"
					+ "title TEXT,"
					+ "time TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void CreateTable_HomeImage(){
		try {
			db.execSQL( "CREATE TABLE homeimage (" //创建新闻类别图片表
					+ "id INTEGER PRIMARY KEY ,"
					+ "sdpath TEXT,"
					+ "remotepath TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void CreateTable_User() {
		try {
			db.execSQL( "CREATE TABLE user(" //创建用户表
					+ "id INTEGER PRIMARY KEY ,"
					+ "name TEXT,"
					+ "password TEXT,"
					+ "sex TEXT,"
					+ "age INTEGER,"
					+ "address TEXT,"
					+ "registertime TEXT,"
					+ "question TEXT,"
					+ "answer TEXT,"
					+ "marry TEXT,"
					+ "hobby TEXT,"
					+ "email TEXT"
					+ ");" );
		} catch (Exception e){
		}
	}
	
	public void FirstStart(){
		//如果是第一次启动,就不存在category这张表.
		try {
		String col[] = { "type" , "name" };
		Cursor c =db.query( "sqlite_master" , col, "name='category'" , null , null , null , null );
		int n=c.getCount();
		if (c.getCount()==0){
			CreateTable_Category();
			CreateTable_News();
			CreateTable_NewsImage();
			CreateTable_CategoryOrder();
			CreateTable_NewsTitle();
			CreateTable_categoryImage();
			CreateTable_HomeImage();
			CreateTable_User();
			//Log.d("FirstStart", "FirstStart#######");
			}
		} catch (Exception e){
		}
	}
	
	//清楚新闻信息，图片信息
	public void clear(){
		try {
			db.execSQL( "DELETE FROM newstitle;" );
			db.execSQL( "DELETE FROM newsimage;" );
			db.execSQL( "DELETE FROM news;" );
		} catch (Exception e){
		}
	}
	
	
	public void InsertIntoCategory(int id,String name){//向category表插入数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", id);
		contentValues.put("name", name);
		db.insert("category", null, contentValues);
		 
	}
	
	public void InsertIntoNews(int id,String title,String comefrom,
			String time,String content,int category){			//向news表插入数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", id);
		contentValues.put("title", title);
		contentValues.put("comefrom", comefrom);
		contentValues.put("time", time);
		contentValues.put("content", content);
		contentValues.put("category",category);
		db.insert("news", null, contentValues);	 
	}
	
	public void InsertIntoNewsImage(Integer id,int newsid,String sdpath,String remotepath){//向newsimage表插入数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", id);
		contentValues.put("newsid", newsid);
		contentValues.put("sdpath", sdpath);
		contentValues.put("remotepath", remotepath);
		db.insert("newsimage", null, contentValues);
	}
	
	public void InsertIntoCategoryOrder(Integer id,int category){//向categoryOrder表插入数据
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", id);
		contentValues.put("category", category);
		db.insert("categoryorder", null, contentValues);
	}
	
	public void InsertIntoNewsTitle(Integer id,String title,String time){
		ContentValues contentValues = new ContentValues();
		contentValues.put("id",id);
		contentValues.put("title",title);
		contentValues.put("time", time);
		db.insert("newstitle", null, contentValues);
	}
	
	public void InsertIntoHomeImage(Integer id,String sdpath,String remotepath){
		ContentValues contentValues = new ContentValues();
		contentValues.put("id",id);
		contentValues.put("sdpath",sdpath);
		contentValues.put("remotepath", remotepath);
		db.insert("homeimage", null, contentValues);
	}
	
	public void InsertIntoCategoryImage(Integer id,Integer categoryid,String sdpath,String remotepath){
		ContentValues contentValues = new ContentValues();
		contentValues.put("id",id);
		contentValues.put("categoryid",categoryid);
		contentValues.put("sdpath",sdpath);
		contentValues.put("remotepath", remotepath);
		db.insert("newscategoryimage", null, contentValues);
	}
	
	public void InsertIntoUser(Integer id,String name,String password,String sex,Integer age,
			String address,String registertime,String question,String answer,String marry,
			String hobby,String email){
		ContentValues contentValues = new ContentValues();
		contentValues.put("id",id);
		contentValues.put("name", name);
		contentValues.put("password", password);
		contentValues.put("sex", sex);
		contentValues.put("age", age);
		contentValues.put("address", address);
		contentValues.put("registertime", registertime);
		contentValues.put("question", question);
		contentValues.put("answer", answer);
		contentValues.put("marry", marry);
		contentValues.put("hobby", hobby);
		contentValues.put("email", email);
		db.insert("user", null, contentValues);
	}
	
	//从数据库中获取首页图片
	public HomeImage getHomeImage(){
		HomeImage homeImage = new HomeImage();
		String sql="select id,sdpath,remotepath from homeimage";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		homeImage.setId(cursor.getInt(0));
		homeImage.setSdpath(cursor.getString(1));
		homeImage.setRemotepath(cursor.getString(2));
		return homeImage;
	}
	//从数据库中获取新闻分类信息
	public List<CategoryEntity> GetCategories(){
		List<CategoryEntity>categoryList=new ArrayList<CategoryEntity>();
		String sql="select id,name from category";
		Cursor cursor = db.rawQuery(sql, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			CategoryEntity categoryEntity = new CategoryEntity();
			categoryEntity.setId(cursor.getInt(0));
			categoryEntity.setName(cursor.getString(1));
			categoryList.add(categoryEntity);
		}
		return categoryList;
	}
	
	//从categoryOrder表中获取当前用户订阅的新闻分类
	public List<CategoryEntity> getCategoryOrder(){
		List<CategoryEntity>categoryList=new ArrayList<CategoryEntity>();
		String sql="select category from categoryorder";
		Cursor cursor = db.rawQuery(sql, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			CategoryEntity categoryEntity = new CategoryEntity();
			categoryEntity=getCategoryById(cursor.getInt(0));
			categoryList.add(categoryEntity);
		}
		cursor.close();
		return categoryList;
	}
	
	//活得用户尚未订阅的新闻类别
	public List<CategoryEntity> getUnOrderCategories(){
		List<CategoryEntity> unOrderCategories = new ArrayList<CategoryEntity>();
		String sql="select id,name from category";
		Cursor cursor = db.rawQuery(sql, null);
		String sql2="select category from categoryorder";
		Cursor c=db.rawQuery(sql2, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			boolean flag=false;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				if(cursor.getInt(0)==c.getInt(0)){
					flag=true;
					break;
				}
			}
			if(!flag){
				CategoryEntity categoryEntity = new CategoryEntity();
				categoryEntity.setId(cursor.getInt(0));
				categoryEntity.setName(cursor.getString(1));
				unOrderCategories.add(categoryEntity);
			}
		}
		cursor.close();
		c.close();
		return unOrderCategories;
	}
	
	//根据CategoryId获取当前类别下的新闻列表
	public List<NewsEntity> GetNews(int category,int length){
		//listNewsMap=new HashMap<Integer, NewsEntity>();
		List<NewsEntity> newsList=new ArrayList<NewsEntity>();
		String sql="select id,title,comefrom,time,content,category from news where category=? ORDER BY id DESC LIMIT "+length;
		Cursor cursor = db.rawQuery(sql,  new String[]{String.valueOf(category)});
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			NewsEntity newsEntity = new NewsEntity();
			newsEntity.setId(cursor.getInt(0));
			newsEntity.setTitle(cursor.getString(1));
			newsEntity.setComefrom(cursor.getString(2));
			newsEntity.setTime(cursor.getString(3));
			newsEntity.setContent(cursor.getString(4));
			newsEntity.setCategory(cursor.getInt(5));
			newsList.add(newsEntity);
			//listNewsMap.put(newsEntity.getId(),newsEntity);
		}
		cursor.close();
		return newsList;
	}
	
	//获取分类下新闻总数
	public int getNewsCount(int category){
		int num=0;
		String sql="select count(*) from news where category=?";
		Cursor cursor = db.rawQuery(sql,  new String[]{String.valueOf(category)});
		cursor.moveToFirst();
		num=cursor.getInt(0);
		cursor.close();
		return num;
	}
	
	//根据id查找category信息
	public CategoryEntity getCategoryById(int id){
		CategoryEntity categoryEntity = new CategoryEntity();
		String sql="select id,name from category where id=?";
		Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(id)});
		cursor.moveToFirst();
		categoryEntity.setId(cursor.getInt(0));
		categoryEntity.setName(cursor.getString(1));
		cursor.close();
		return categoryEntity;
	}
	
	//获取最近更新的num条新闻纪录
	public List<NewsEntity> GetLastNews(int category,int num){
		List<NewsEntity> newsList=new ArrayList<NewsEntity>();
		String sql="select id,title,comefrom,time,content,category from news where category=? ORDER BY id DESC LIMIT "+num;
		Cursor cursor = db.rawQuery(sql,  new String[]{String.valueOf(category)});
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			NewsEntity newsEntity = new NewsEntity();
			newsEntity.setId(cursor.getInt(0));
			newsEntity.setTitle(cursor.getString(1));
			newsEntity.setComefrom(cursor.getString(2));
			newsEntity.setTime(cursor.getString(3));
			newsEntity.setContent(cursor.getString(4));
			newsEntity.setCategory(cursor.getInt(5));
			newsList.add(newsEntity);
			//listNewsMap.put(newsEntity.getId(),newsEntity);
		}
		cursor.close();
		return newsList;
	}
	
	//根据指定类别，获取新闻标题的简单信息
	public List<NewsEntity> GetNewsTitle(int category,int length){
		List<NewsEntity> newsList=new ArrayList<NewsEntity>();
		String sql="select id,title,time from newstitle where category=? ORDER BY id DESC LIMIT "+length;
		Cursor cursor = db.rawQuery(sql,  new String[]{String.valueOf(category)});
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			NewsEntity newsEntity = new NewsEntity();
			newsEntity.setId(cursor.getInt(0));
			newsEntity.setTitle(cursor.getString(1));
			newsEntity.setTime(cursor.getString(2));
			newsList.add(newsEntity);
		}
		cursor.close();
		return newsList;
	}
	

	
	/**
	 * 将指定分类从用户订阅表中删除
	 * @param category
	 * @return
	 */
	public boolean deleteCategoryOrder(int category){
		//String sql="delete from categoryorder where category=?";
		db.delete("categoryorder","category=?",new String[]{String.valueOf(category)});
		return true;
	}
	
	/**
	 * 删除用户订阅表的信息
	 */
	public void deleteAllCategoryOrder(){
		db.delete("categoryorder",null,null);
	}
	
	/**
	 * 更新新闻分类
	 * @return
	 */
	public void updateCategory(int id,String name){
		 ContentValues cv = new ContentValues();
		 cv.put("id",id);
		 cv.put("name",name);
		 String[] args = {String.valueOf(id)};
		 db.update("category", cv, "id=?", args);
	}
	
	public SQLiteDatabase getDatabase(){
		return db;
	}
	
	public void close(){
		db.close();
		
		}

}
