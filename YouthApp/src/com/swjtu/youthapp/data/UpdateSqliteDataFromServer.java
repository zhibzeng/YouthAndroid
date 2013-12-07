package com.swjtu.youthapp.data;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.swjtu.youthapp.common.IsNetWorkAlive;
import com.swjtu.youthapp.common.LoadFileToLocal;
import com.swjtu.youthapp.po.CategoryEntity;
import com.swjtu.youthapp.po.HomeImage;
import com.swjtu.youthapp.po.NewsEntity;
public class UpdateSqliteDataFromServer {
	
	//新闻配图路径
	private final static String NewsImagePath = "/YouthAppData/images/newsimg/";
	//新闻类别图片路径
	private final static String NewsCategoryImagePath = "/YouthAppData/images/categoryimg/";
	//首页图片路径
	private final static String  HomeImagePath = "/YouthAppData/images/homeimg/";
	//每次更新新闻数量 
	private final static int updateNewsNum=10;
	
	private Context context;
	/**
	 * constructor
	 * @param context
	 */
	public UpdateSqliteDataFromServer(Context context){
		this.context=context;
	}
	
	/**
	 * 获取新闻分类，更新数据库
	 */
	public void UpdateCategory(){
		List<CategoryEntity>categoryList=new ArrayList<CategoryEntity>();
		FetchDataFromServer dataFromServer= new FetchDataFromServer();
		categoryList=dataFromServer.GetCategories();
		SqliteControl sqliteControl =new SqliteControl(context);
		SQLiteDatabase db = sqliteControl.getDatabase();
		for (CategoryEntity categoryEntity : categoryList) {
			String selectSQL = "select id, name from category where id=?";
			Cursor cursor = db.rawQuery(selectSQL, new String[]{String.valueOf(categoryEntity.getId())});
			cursor.moveToFirst();
			if(cursor.getCount()==0){
				sqliteControl.InsertIntoCategory(categoryEntity.getId(),categoryEntity.getName());	
			}else if(!cursor.getString(1).equals(categoryEntity.getName())){
				sqliteControl.updateCategory(categoryEntity.getId(),categoryEntity.getName());
			}
			cursor.close();
		}
		sqliteControl.close();
	}
	
	/**
	 * 初始化CategoryOrder表
	 * @param num
	 */
	public void initCategoryOrder(int num){//表示初始时推荐前num个新闻类别
		SqliteControl sqliteControl =new SqliteControl(context);
		sqliteControl.deleteAllCategoryOrder();
		SQLiteDatabase db = sqliteControl.getDatabase();
		String sql="select id from category ORDER BY id ASC LIMIT "+num;
		Cursor cursor = db.rawQuery(sql, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			SqliteControl sqliteControl2 = new SqliteControl(context);
			sqliteControl2.InsertIntoCategoryOrder(null, cursor.getInt(0));
			sqliteControl2.close();
		}
		cursor.close();
		sqliteControl.close();
	  }
	
	/**
	 * 获取所有新闻内容
	 */
	public void UpdateNews(){
		SqliteControl sqliteControl =new SqliteControl(context);
		SQLiteDatabase db = sqliteControl.getDatabase();
		String sql="select id from category";
		Cursor cursor = db.rawQuery(sql, null);
		FetchDataFromServer dataFromServer = new FetchDataFromServer();
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			List<NewsEntity> newsList=new ArrayList<NewsEntity>();
			newsList=dataFromServer.GetNews(cursor.getInt(0), updateNewsNum);
			if(newsList==null)return;//更新出现问题，则自动终止执行
			for (NewsEntity newsEntity : newsList) {
				SqliteControl sqliteControl2 =new SqliteControl(context);
				SQLiteDatabase db2 = sqliteControl2.getDatabase();
				String selectSQL = "select id from news where id=?";
				Cursor c = db2.rawQuery(selectSQL, new String[]{String.valueOf(newsEntity.getId())});
				if(c.getCount()==0){
					SqliteControl sqliteControl3 =new SqliteControl(context);
					sqliteControl3.InsertIntoNews(newsEntity.getId(), newsEntity.getTitle(),
							newsEntity.getComefrom(), newsEntity.getTime(), 
							newsEntity.getContent(), newsEntity.getCategory());
					//Log.d("newsTitle",newsEntity.getTitle()+newsEntity.getId());//commont
					sqliteControl3.close();
				}else{
					c.close();
					sqliteControl2.close();
					continue;
				}
				c.close();
				sqliteControl2.close();
			}
			
		 }
		cursor.close();
		sqliteControl.close();
	}

	
	/**获取指定新闻类别的新闻内容
	 * 返回更新数目
	 */
	public int UpdateNewsByCategory(int category)
	{	int num=0;
		FetchDataFromServer dataFromServer = new FetchDataFromServer();
		//List<NewsEntity> newsList=new ArrayList<NewsEntity>();
		List<NewsEntity> newsList=null;
		newsList=dataFromServer.GetNews(category, updateNewsNum);
		if(newsList==null){
			return num;
		}
		for (NewsEntity newsEntity : newsList) {
			SqliteControl sqliteControl2 =new SqliteControl(context);
			SQLiteDatabase db2 = sqliteControl2.getDatabase();
			String selectSQL = "select id from news where id=?";
			Cursor c = db2.rawQuery(selectSQL, new String[]{String.valueOf(newsEntity.getId())});
			if(c.getCount()==0){
				num++;
				SqliteControl sqliteControl3 =new SqliteControl(context);
				sqliteControl3.InsertIntoNews(newsEntity.getId(), newsEntity.getTitle(),
						newsEntity.getComefrom(), newsEntity.getTime(), 
						newsEntity.getContent(), newsEntity.getCategory());
				//Log.d("newsTitle",newsEntity.getTitle()+newsEntity.getId());//commont
				sqliteControl3.close();
			}else{
				continue;
			}
			c.close();
			sqliteControl2.close();
		}	
		return num;
	}
	
	
	/**
	 * 从网络中读取图片并保存
	 */
	public void UpdateImage(){
		SqliteControl sqliteControl = new SqliteControl(context);
		SQLiteDatabase db = sqliteControl.getDatabase();
		SqliteControl sqliteControl2 = new SqliteControl(context);
		SQLiteDatabase db2 = sqliteControl.getDatabase();
		String sqlNews="select id from news";
		String sqlNewsImage="select newsid from newsimage";
		Cursor cursor = db.rawQuery(sqlNews, null);
		Cursor c = db2.rawQuery(sqlNewsImage, null);
		FetchDataFromServer dataFromServer = new FetchDataFromServer();
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){//遍历Cursor
			//Log.d("newsid","读取图片新闻编号"+cursor.getInt(0));
			boolean flag = false;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				//Log.d("imageNewsid","新闻配图表中"+c.getInt(0));
				if(c.getInt(0)==cursor.getInt(0)){
					flag=true;
					break;
					}
				}
			if(!flag){
			//uodate
				List<String> imgUrl = new ArrayList<String>();   
				imgUrl=dataFromServer.getNewsImageUrl(cursor.getInt(0));
				if(imgUrl==null)return;//更新失败，则自动终止
				if(imgUrl!=null&&imgUrl.size()>0){
					for(int i=0;i<imgUrl.size();i++){
					//	Log.d("url","新闻配图路径"+imgUrl.get(i));
						LoadFileToLocal LoadFileToLocal = new LoadFileToLocal();
						String sdpath=LoadFileToLocal.LoadFile(imgUrl.get(i), NewsImagePath);
						SqliteControl sqliteControl3 = new SqliteControl(context);
						sqliteControl3.InsertIntoNewsImage(null, cursor.getInt(0),sdpath, imgUrl.get(i));
						sqliteControl3.close();
					}
				}
			}
		}
		cursor.close();
		c.close();
		sqliteControl.close();
		sqliteControl2.close();
	}
	
	
	
	/**
	 * 根据新闻id获取指定新闻的图片
	 * @param newsid
	 */
	public void UpdateImageByNewsId(int newsid){
		FetchDataFromServer dataFromServer = new FetchDataFromServer();
		List<String> imgUrl = new ArrayList<String>();   
		imgUrl=dataFromServer.getNewsImageUrl(newsid);
		if(imgUrl==null){
			return;
		}
		if(imgUrl!=null&&imgUrl.size()>0){
			for(int i=0;i<imgUrl.size();i++){
			//	Log.d("url","新闻配图路径"+imgUrl.get(i));
				SqliteControl sqliteControl = new SqliteControl(context);
				SQLiteDatabase db = sqliteControl.getDatabase();
				String sqlNewsImage="select remotepath from newsimage where remotepath=?";
				Cursor cursor = db.rawQuery(sqlNewsImage, new String[]{imgUrl.get(i)});
				if(cursor.getCount()==0){
					LoadFileToLocal LoadFileToLocal = new LoadFileToLocal();
					String sdpath=LoadFileToLocal.LoadFile(imgUrl.get(i), NewsImagePath);
					SqliteControl sqliteControl3 = new SqliteControl(context);
					sqliteControl3.InsertIntoNewsImage(null, newsid,sdpath, imgUrl.get(i));
					sqliteControl3.close();
				}
				cursor.close();
				sqliteControl.close();		
			}
		}
	}
	
	
	
	
	/**
	 * 获取首页图片
	 */
	public void updateHomeImage(){
		FetchDataFromServer dataFromServer = new FetchDataFromServer();
		  //获得网络连接服务
		HomeImage homeImage = dataFromServer.getHomeImage();
		if(homeImage!=null){
				SqliteControl sqliteControl = new SqliteControl(context);
				SQLiteDatabase db = sqliteControl.getDatabase();
				String sqlNewsImage="select id from homeimage";
				Cursor cursor = db.rawQuery(sqlNewsImage, null);
				cursor.moveToFirst();
				sqliteControl.close();	
				if(cursor.getCount()==0){//如果首页图片已经更改
					SqliteControl sqliteControl2 = new SqliteControl(context);
					SQLiteDatabase db2 = sqliteControl2.getDatabase();
					db2.execSQL( "DELETE FROM homeimage;" );
					sqliteControl2.close();
					LoadFileToLocal LoadFileToLocal = new LoadFileToLocal();
					String sdpath=LoadFileToLocal.LoadFile(homeImage.getRemotepath(), HomeImagePath);
					SqliteControl sqliteControl3 = new SqliteControl(context);
					sqliteControl3.InsertIntoHomeImage(homeImage.getId(), sdpath, homeImage.getRemotepath());
					sqliteControl3.close();
				}else{
					if(cursor.getInt(0)!=homeImage.getId()){
						SqliteControl sqliteControl4 = new SqliteControl(context);
						SQLiteDatabase db4 = sqliteControl4.getDatabase();
						db4.execSQL( "DELETE FROM homeimage;" );
						sqliteControl4.close();
						LoadFileToLocal LoadFileToLocal = new LoadFileToLocal();
						String sdpath=LoadFileToLocal.LoadFile(homeImage.getRemotepath(), HomeImagePath);
						SqliteControl sqliteControl3 = new SqliteControl(context);
						sqliteControl3.InsertIntoHomeImage(homeImage.getId(), sdpath, homeImage.getRemotepath());
						sqliteControl3.close();
						}
					}
			}
		}
	
	/**
	 * 获取新闻类别图片
	 */
	
	public void updateCategoryImage(int category){
		//判断网络连接是否通畅，若无网络则推出
		IsNetWorkAlive isNetWorkAlive = new IsNetWorkAlive(context);
		boolean flag=isNetWorkAlive.isNetAlive();
		if(!flag){return;}//如果无网络则退出
		FetchDataFromServer dataFromServer = new FetchDataFromServer();
		  //获得网络连接服务
		String CategoryImgPath = dataFromServer.getCategoryImagePath(category);
		if(CategoryImgPath!=null&&!CategoryImgPath.equals("")){
				SqliteControl sqliteControl = new SqliteControl(context);
				SQLiteDatabase db = sqliteControl.getDatabase();
				String sqlNewsImage="select remotepath from newscategoryimage where categoryid=?";
				Cursor cursor = db.rawQuery(sqlNewsImage, new String[]{String.valueOf(category)});
				cursor.moveToFirst();
				if(cursor.getCount()==0){
					LoadFileToLocal LoadFileToLocal = new LoadFileToLocal();
					String sdpath=LoadFileToLocal.LoadFile(CategoryImgPath, NewsCategoryImagePath);
					SqliteControl sqliteControl3 = new SqliteControl(context);
					sqliteControl3.InsertIntoCategoryImage(null, category, sdpath, CategoryImgPath);
					sqliteControl3.close();
				}else{
					if(!(cursor.getString(0).trim()).equals(CategoryImgPath.trim())){
							SqliteControl sqliteControl2 = new SqliteControl(context);
							SQLiteDatabase db2 = sqliteControl2.getDatabase();
							db2.execSQL( "DELETE FROM newscategoryimage;" );
							sqliteControl2.close();
							LoadFileToLocal LoadFileToLocal = new LoadFileToLocal();
							String sdpath=LoadFileToLocal.LoadFile(CategoryImgPath, NewsCategoryImagePath);
							SqliteControl sqliteControl3 = new SqliteControl(context);
							sqliteControl3.InsertIntoCategoryImage(null, category, sdpath, CategoryImgPath);
							sqliteControl3.close();
					}
				}
				cursor.close();
				sqliteControl.close();	
			}
		
		}	
		 
}
