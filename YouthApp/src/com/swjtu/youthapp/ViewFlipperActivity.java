package com.swjtu.youthapp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swjtu.youthapp.common.DeleteFile;
import com.swjtu.youthapp.common.LoadBitmapFromLocal;
import com.swjtu.youthapp.data.SqliteControl;
import com.swjtu.youthapp.data.UpdateSqliteDataFromServer;
import com.swjtu.youthapp.po.CategoryEntity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
/**
 * Android实现左右滑动效果
 * @Description: Android实现左右滑动效果
 * @Author zhibinzeng
 * @Date 2012-11-22 上午10:44:04
 * @Version V1.0
 */
public class ViewFlipperActivity extends Activity
{	
	//新闻配图路径
	private final static String NewsImagePath = "/YouthAppData/images/newsimg/";
	//视觉图片路径
	private final static String VisionImagePath = "/YouthAppData/images/vision/";
	private  int dialogTip;
	private long exitTime = 0;
	public static final String TAG="ViewFlipperActivity";
	private ViewFlipper flipper;//首页栏目展示Flipper
	private ViewFlipper main_pic_Fliper;//首页滚动图片展示Flipper
	private GestureDetector detector;
	private GestureDetector main_pic_Detector;
	private LayoutInflater mInflater;
	private List<GridInfo> list=null;
	private List<CategoryEntity> categorylist=null;
	private final int SHOW_NEXT = 0011;//首页滚动图片相关参数
	private boolean showNext = true;//首页滚动图片相关参数
	private boolean isRun = true;//首页滚动图片相关参数
	private int main_pic_currentPage = 0;
	private int mCurrentPage=0;
	private int mTotlePage;
	private boolean isDeleteMode=false;
	private ImageButton btnAdd;//增加首页新闻分类按钮
	private ImageButton btnclear;//清除新闻信息
	private ImageButton btnRefresh;
	private TextView date_TextView;//显示日期
	int mCurrentX;
	int mCurrentY;
	private String InitConfig="initConfig";//初始配置文件名
	private String count="";//标志是否第一次启动
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	private ProgressDialog fetchFirstDataDialog;// 声明ProgressDialog类型变量
	//首页底部导航条
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,main_bottom_layout4;
	//导航栏图片缓存
	public static Map<String, Bitmap> filpperPictureCache = new HashMap<String, Bitmap>();
	//栏目图片缓存
	public static Map<String, Bitmap> categoryBitmapCache = new HashMap<String, Bitmap>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.main);
		
		//注册网络连接监听
		IntentFilter filter = new IntentFilter();        
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateReceiver, filter);
		
		//获取网络提示的次数
		SharedPreferences sPreferences = getSharedPreferences("networktip",0 );
		dialogTip=sPreferences.getInt("networktips", 1);

		String string=getInitData();
		if(string.equals("first")){//第一次启动
			firstDataFetch();//第一次启动时更新系统数据库
		}
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipperMain);
		detector = new GestureDetector(categoryGestureListener);
		flipper.setOnTouchListener(CategoryonTouchListener);
		flipper.setLongClickable(true);
		date_TextView = (TextView) findViewById(R.id.home_date_tv);
	    date_TextView.setText(getDate());//设置首页显示的日期
		btnAdd=(ImageButton)findViewById(R.id.imageButtonAdd);
		btnAdd.setOnClickListener(new btbAddListener());
		btnRefresh=(ImageButton)findViewById(R.id.imageButtonRefresh);
		btnRefresh.setOnClickListener(new ImageButton.OnClickListener(){
			public void onClick(View v) {
				refreshData();//刷新Category数据
				//updateCategoryImage();//更新新闻类别图片
			}
		});
		
		//清除缓存
		btnclear=(ImageButton)findViewById(R.id.imageButtonClear);
		btnclear.setOnClickListener(btnClearClickListener);
		
		SqliteControl sqliteControl2 = new SqliteControl(this);
		categorylist=sqliteControl2.getCategoryOrder();//从用户订阅表中获取数据
		sqliteControl2.close();
		
		if(list!=null){
			list.clear();
		}
		list = new ArrayList<GridInfo>();
		
		
		if(categorylist!=null){
			for(int i=0;i<categorylist.size();i++)
			{
				Bitmap bitmap = null;
				try {
					bitmap = setCategoryImage(categorylist.get(i).getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(bitmap!=null){
					list.add(new GridInfo(categorylist.get(i).getName(),categorylist.get(i).getId(),bitmap));
				}
			
			}
		
		}
		
		refreshFlipper();
		
		//首页图片滚动Flipper
		main_pic_Fliper = (ViewFlipper) findViewById(R.id.main_pic_Fliper);
		main_pic_Detector = new GestureDetector(main_pic_OnGestureListener);
		main_pic_Fliper.setOnTouchListener(onTouchListener);
		//main_pic_Fliper.setOnClickListener(onClickListener);
		main_pic_Fliper.setLongClickable(true);
		displayRatio_selelct(main_pic_currentPage);
		thread.start();//首页图片滚动线程
		
		//判断底部导航条是否被触按
        boolean clickble = getIntent().getBooleanExtra("clickble", true);
		//首页底部导航条
		main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
		main_bottom_layout1.setOnClickListener(clickListener_layout1);
		main_bottom_layout1.setSelected(clickble);
		
		main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
		main_bottom_layout2.setOnClickListener(clickListener_layout2);
		
		main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
		main_bottom_layout3.setOnClickListener(clickListener_layout3);
		
		main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
		main_bottom_layout4.setOnClickListener(clickListener_layout4);
		
		//滚动图片初始化
		try {
			initFlipperPic(4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//onCreate()函数结束
	
	
	/**
	 * 获取配置信息
	 * @return
	 */
	public String getInitData(){
		SharedPreferences sPreferences = getSharedPreferences(InitConfig,0 );
		count=sPreferences.getString("count","first");
		Editor editor = sPreferences.edit();
		editor.putString("count","other");
		editor.commit();
		return count;
	}
	
	
	/**
	 * 获取日期
	 * @return
	 */
    private String getDate(){
    	Date date = new Date();
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	String[] weekDays = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    	int w = c.get(Calendar.DAY_OF_WEEK) - 1 ;
    	if (w < 0) {
			w = 0;
		}
    	String mDate = c.get(Calendar.YEAR)+"年" + (c.get(Calendar.MONTH)+1)+ "月" + c.get(Calendar.DATE) + "日" + weekDays[w];
    	return mDate;
    }
    
	/**
	 *设置新闻类别图片，如果本地有更新则显示，若没有则显示默认的 
	 * @throws IOException 
	 */
	public Bitmap setCategoryImage(int category) throws IOException{
		Bitmap mBitmap=null;
		SqliteControl sqliteControl = new SqliteControl(this);
		String sqlNewsImage="select sdpath from newscategoryimage where categoryid=?";
		SQLiteDatabase db=sqliteControl.getDatabase();
		Cursor cursor = db.rawQuery(sqlNewsImage, new String[]{String.valueOf(category)});
		cursor.moveToFirst();
		sqliteControl.close();
		if(cursor.getCount()==0){
			//若本地无图片则返回默认图片
			 mBitmap = categoryBitmapCache.get("column_default");
			 if(mBitmap==null){
				 InputStream is = getResources().openRawResource(R.drawable.column_default);  
		         mBitmap = BitmapFactory.decodeStream(is);
		         categoryBitmapCache.put("column_default",mBitmap);
			 }
		}else{
			mBitmap = categoryBitmapCache.get(cursor.getString(0));
			if(mBitmap==null){
				LoadBitmapFromLocal bitmapFromLocal = new LoadBitmapFromLocal();
				mBitmap=bitmapFromLocal.LoadBitmap(cursor.getString(0));
				categoryBitmapCache.put(cursor.getString(0),mBitmap);
				//Log.d(cursor.getString(0),cursor.getString(0));
			}
		}
		cursor.close();
		if(mBitmap==null){
			//若本地无图片则返回默认图片
			 mBitmap = categoryBitmapCache.get("column_default");
			 if(mBitmap==null){
				 InputStream is = getResources().openRawResource(R.drawable.column_default);  
		         mBitmap = BitmapFactory.decodeStream(is);
		         categoryBitmapCache.put("column_default",mBitmap);
			 }
		}
		return mBitmap;
	}
	
	/**
	 * btnAdd的点击事件
	 * @author Administrator
	 */
	class btbAddListener implements OnClickListener{
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(ViewFlipperActivity.this,CategoryOrderActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.fade);  
		ViewFlipperActivity.this.finish();
		}
	}
	
	
	/**
	 * 更新新闻类别图片线程
	 */
	public void updateCategoryImage(){
		new Thread(){
			public void run() {
				//获取数据库中新闻类别
				SqliteControl sqliteControl = new SqliteControl(ViewFlipperActivity.this);
				List<CategoryEntity>categoryList=sqliteControl.GetCategories();
				sqliteControl.close();
				for (CategoryEntity categoryEntity : categoryList) {
					UpdateSqliteDataFromServer dataFromServer = new UpdateSqliteDataFromServer(ViewFlipperActivity.this);
					dataFromServer.updateCategoryImage(categoryEntity.getId());
				}
			}
		}.start();
	}
	
	
	
	/**
	 * 第一次启动时获取数据Hander
	 */
	final Handler firstDataHandler = new Handler() {
		public void handleMessage(Message msg) {
			refreshFlipper();//更新主界面UI
		}
	};
	
	
	/**
	 * 第一次启动时获取数据
	 */
		public void firstDataFetch(){
			fetchFirstDataDialog = ProgressDialog.show(ViewFlipperActivity.this, "欢迎使用有思", "正在初始化数据...",
					true);
			fetchFirstDataDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
			fetchFirstDataDialog.setCancelable(true);//按返回键取消显示
			//开启线程加载数据
			new Thread(){
				public void run() {
					//更新数据库
					UpdateSqliteDataFromServer updateSqliteDataFromServer = new UpdateSqliteDataFromServer(ViewFlipperActivity.this);
					updateSqliteDataFromServer.UpdateCategory();
					updateCategoryImage();//更新新闻类别图片
					//初始用户订阅表
					updateSqliteDataFromServer.initCategoryOrder(6);
					list.clear();
					categorylist.clear();
					SqliteControl sqliteControl3 = new SqliteControl(ViewFlipperActivity.this);
					categorylist=sqliteControl3.getCategoryOrder();//从用户订阅表中获取数据
					sqliteControl3.close();
					if(categorylist!=null){
						
						for(int i=0;i<categorylist.size();i++)
						{
							Bitmap bitmap = null;
							try {
								bitmap = setCategoryImage(categorylist.get(i).getId());
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(bitmap!=null){
								list.add(new GridInfo(categorylist.get(i).getName(),categorylist.get(i).getId(),bitmap));
							}
							
						}
						
					}
					updateNewsFromServer();//更新所有新闻
					Message m = new Message();
					firstDataHandler.sendMessage(m);
					fetchFirstDataDialog.dismiss();
				}
			}.start();
			
		}
	
	
	
		
		
		
	/**
	 * 刷新数据Hander
	 */
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (list.size() == 0) {
				return;
			}
			refreshFlipper();
			updateCategoryImage();//更新新闻类别图片
		}
	};
	
	
	
	
	
	/**
	 * 刷新数据
	 */
	public void refreshData(){
		myDialog = ProgressDialog.show(ViewFlipperActivity.this, "亲，请稍等一会哦...", "正在努力加载数据...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
		myDialog.setCancelable(true);
		new Thread() {
			public void run() {
				try {
					//更新数据库
					UpdateSqliteDataFromServer updateSqliteDataFromServer = new UpdateSqliteDataFromServer(ViewFlipperActivity.this);
					updateSqliteDataFromServer.UpdateCategory();
					//Thread.sleep(2000);//停止两秒，模拟加载
					list.clear();
					categorylist.clear();
					SqliteControl sqliteControl3 = new SqliteControl(ViewFlipperActivity.this);
					categorylist=sqliteControl3.getCategoryOrder();//从用户订阅表中获取数据
					sqliteControl3.close();
					if(categorylist!=null){
						for(int i=0;i<categorylist.size();i++)
						{
						Bitmap bitmap = null;
						try {
							bitmap = setCategoryImage(categorylist.get(i).getId());
							} catch (IOException e) {
							e.printStackTrace();
							}
							list.add(new GridInfo(categorylist.get(i).getName(),categorylist.get(i).getId(),bitmap));
							}
					
		
					}
					
					Message m = new Message();
					listHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					myDialog.dismiss();
				}
			}
		}.start();
	}

	
	
	
	
	/**
	 * 刷新主界面
	 */
	public void refreshFlipper()
	{
		flipper.removeAllViews();
		refreshAdapterAll();
		//flipper.setDisplayedChild(0);当前显示第几个子View
		for (int i = 0; i <flipper.getChildCount(); i++) {
			final GridView gv = (GridView) flipper.getChildAt(i);
			gv.setOnItemLongClickListener(GridViewLongClickListener);
			gv.setOnItemClickListener(GridViewOnItemClickListener);
			gv.setOnTouchListener(CategoryonTouchListener);
			gv.setTag(i);
		}
	}

	
	
	
	
	/**
	 * 刷新首页栏目FlipperAdapter所有数据
	 */
	
	private void refreshAdapterAll(){
		 mTotlePage=list.size();
	        for(int i=0;i<Math.ceil(list.size()/4.0);i++)
	        {
	    		GridView gridview=(GridView) mInflater.inflate(R.layout.gridview, null);
	    		flipper.addView(gridview);
	    		GridAdapter adapter = new GridAdapter(this);
	            int end=4*i+4;
	            if(end>list.size()) end=list.size();
	            adapter.setList(list.subList(4*i, end));
	            gridview.setAdapter(adapter);
	        }
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//return this.detector.onTouchEvent(event);
		return false;
	}

    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
//    	detector.onTouchEvent(ev);  
        return super.dispatchTouchEvent(ev);  
    } 
    

    


	/**
	 * 用户长按首页 显示删除按钮
	 */
    private OnItemLongClickListener GridViewLongClickListener = new OnItemLongClickListener() {
    	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
    			long id) {
    		if (isDeleteMode==true)
    			return false;
    		for (int i = 0; i < flipper.getChildCount(); i++) {
    			GridView gv = (GridView) flipper.getChildAt(i);
    			gv.clearFocus();
    			for (int j = 0; j < gv.getChildCount(); j++) {
    				ImageButton itemButtonCancel = (ImageButton) gv
    						.getChildAt(j).findViewById(R.id.itemCancel);
    				itemButtonCancel.setVisibility(View.VISIBLE);
    			}
    		}
    		isDeleteMode=true;
    		return false;
    	}
	};
	
	
	
	
	
	/**
	 * 进入指定新闻分类
	 */
	private OnItemClickListener GridViewOnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			int categoryid;
			categoryid=list.get((Integer)flipper.getCurrentView().getTag()*4+arg2).getCategoryid();
			Intent intent=new Intent();
			intent.setClass(ViewFlipperActivity.this, NewsTitleActivity.class);
			String categoryName = list.get((Integer)flipper.getCurrentView().getTag()*4+arg2).getName();//新闻类别名称
			intent.putExtra("categoryid", categoryid);
			intent.putExtra("categoryName",categoryName);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			ViewFlipperActivity.this.finish();
		}
		
	};
	


	
	
	
	/**
	 * 首页滚动图片FlipperTouch监听器
	 * 
	 */
    private OnTouchListener onTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			//Log.d("首页滚动图片点击事件", "首页滚动图片点击事件");
			return main_pic_Detector.onTouchEvent(event);
		}
	};
	

	
	
	
	
	/**
	 * 首页滚动图片Filpper OnGestureListener
	 */
	private OnGestureListener main_pic_OnGestureListener = new OnGestureListener() {
		
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
		
		public void onShowPress(MotionEvent e) {
			
		}
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			return false;
		}
		
		public void onLongPress(MotionEvent e) {
		}
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			//Log.d("滚动图片Flipper 触发事件", "滚动图片Flipper 触发事件");
			if (e1.getX() - e2.getX()> 50  
	                && Math.abs(velocityX) > 0) {
				showNextView();
				showNext = true;
				return true;
			} else if (e2.getX() - e1.getX() > 50  
	                && Math.abs(velocityX) > 0){
				showPreviousView();
				showNext = false;
				return true;
			}
			return false;
		}
		
		public boolean onDown(MotionEvent e) {
			return false;
		}
	}; 
	
	
	
	
	
	/**
	 *首页滚动图片，显示下一张图片
	 */
	private void showNextView(){
		main_pic_Fliper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
		main_pic_Fliper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));		
		main_pic_Fliper.showNext();
		main_pic_currentPage ++;
		if (main_pic_currentPage == main_pic_Fliper.getChildCount()) {
			displayRatio_normal(main_pic_currentPage - 1);
			main_pic_currentPage = 0;
			displayRatio_selelct(main_pic_currentPage);
		} else {
			displayRatio_selelct(main_pic_currentPage);
			displayRatio_normal(main_pic_currentPage - 1);
		}
		//Log.e("currentPage", main_pic_currentPage + "");		
		
	}
	
	
	/**
	 *首页滚动图片，显示前一张图片
	 */
	private void showPreviousView(){
		displayRatio_selelct(main_pic_currentPage);
		main_pic_Fliper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
		main_pic_Fliper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
		main_pic_Fliper.showPrevious();
		main_pic_currentPage --;
		if (main_pic_currentPage == -1) {
			displayRatio_normal(main_pic_currentPage + 1);
			main_pic_currentPage = main_pic_Fliper.getChildCount() - 1;
			displayRatio_selelct(main_pic_currentPage);
		} else {
			displayRatio_selelct(main_pic_currentPage);
			displayRatio_normal(main_pic_currentPage + 1);
		}
		//Log.e("currentPage", currentPage + "");		
	}
	
	
	
	
	/**
	 * 滚动图片Flipper导航小圆点显示控制
	 * @param id
	 */
	private void displayRatio_selelct(int id){
		int[] ratioId = {R.id.home_ratio_img_04, R.id.home_ratio_img_03, R.id.home_ratio_img_02, R.id.home_ratio_img_01};
		ImageView img = (ImageView)findViewById(ratioId[id]);
		img.setSelected(true);
	}
	
	
	
	
	
	/**
	 * 滚动图片Flipper导航小圆点显示控制
	 * @param id
	 */
	private void displayRatio_normal(int id){
		int[] ratioId = {R.id.home_ratio_img_04, R.id.home_ratio_img_03, R.id.home_ratio_img_02, R.id.home_ratio_img_01};
		ImageView img = (ImageView)findViewById(ratioId[id]);
		img.setSelected(false);
	}
	
	
	
	
	/**
	 * 首页图片滚动线程
	 */
	Thread thread = new Thread(){
		public void run() {
			// TODO 图片自动滚动
			while(isRun){
				try {
					Thread.sleep(1000 * 5);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
	
	
	
    Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;

			default:
				break;
			}
		}
    	
    };
	
	
	
    
	
    /**
	 * 首页栏目显示Filpper OnGestureListener
	 */
    private OnGestureListener categoryGestureListener = new OnGestureListener() {
		
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
		
		public void onShowPress(MotionEvent e) {
		}
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			return false;
		}
		
		public void onLongPress(MotionEvent e) {
			
		}
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX()>120) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
						R.anim.push_left_in_main));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
						R.anim.push_left_out_main));
				flipper.showNext();
				mCurrentPage = (mCurrentPage+1)%mTotlePage;      
				return true;
			} else if (e1.getX() - e2.getX() < -120) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
						R.anim.push_right_out));
				flipper.showPrevious();
				mCurrentPage = (mCurrentPage-1)%mTotlePage;
				return true;
			}
			return false;
		}
		
		public boolean onDown(MotionEvent ev) {
			mCurrentX=(int) ev.getX();
			mCurrentY=(int) ev.getY();
			if (isDeleteMode==false)
				return false;
			for (int i = 0; i < flipper.getChildCount(); i++) {
				GridView gv = (GridView) flipper.getChildAt(i);
				for (int j = 0; j < gv.getChildCount(); j++) {
					ImageButton itemButtonCancel = (ImageButton) gv
							.getChildAt(j).findViewById(R.id.itemCancel);
					itemButtonCancel.setVisibility(View.INVISIBLE);
				}
			}
			isDeleteMode = false;
			return false;
		}
	}; 
	
	
	
	

	/**
	 * 首页栏目FlipperTouch监听器
	 * 
	 */
    private OnTouchListener CategoryonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			return detector.onTouchEvent(event);
		}
	};
	
	
	
	
	/**
	 * 首页底部导航条 导航1 按钮单击事件
	 */
    private OnClickListener clickListener_layout1 = new OnClickListener() {
		public void onClick(View v) {
				main_bottom_layout1.setSelected(true);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(false);
				main_bottom_layout4.setSelected(false);
				/*
				Intent intent = new Intent();
				intent.setClass(ViewFlipperActivity.this, MyActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout1.setSelected(false);
				*/
		}
	};

	/**
	 * 首页底部导航条 导航2 按钮单击事件
	 */
    private OnClickListener clickListener_layout2 = new OnClickListener() {
		public void onClick(View v) {
				main_bottom_layout1.setSelected(false);
				main_bottom_layout2.setSelected(true);
				main_bottom_layout3.setSelected(false);
				main_bottom_layout4.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(ViewFlipperActivity.this, UserInfo.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout2.setSelected(false);
		}
	};
	
	/**
	 * 首页底部导航条 导航3 按钮单击事件
	 */
    private OnClickListener clickListener_layout3 = new OnClickListener() {
		public void onClick(View v) {
				main_bottom_layout1.setSelected(false);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(true);
				main_bottom_layout4.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(ViewFlipperActivity.this, VisionActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout3.setSelected(false);
		}
	};

	/**
	 * 首页底部导航条 导航4 按钮单击事件
	 */
    private OnClickListener clickListener_layout4 = new OnClickListener() {
		public void onClick(View v) {
				main_bottom_layout1.setSelected(false);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(false);
				main_bottom_layout4.setSelected(true);
				Intent intent = new Intent();
				intent.setClass(ViewFlipperActivity.this, SinaWeiboWebView.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				overridePendingTransition(R.anim.fade,R.anim.fade);
				main_bottom_layout4.setSelected(false);
				ViewFlipperActivity.this.finish();
		}
	};
	
	
	
	
	
	/**
	 *设置首页滚动图片，如果本地有更新则显示，若没有则显示默认的 
	 * @throws IOException 
	 */
	public void initFlipperPic(int length) throws IOException{
		Bitmap mBitmap=null;
		SqliteControl sqliteControl = new SqliteControl(this);
		String sqlNewsImage="select sdpath,newsid from newsimage ORDER BY id DESC LIMIT "+length;
		SQLiteDatabase db=sqliteControl.getDatabase();
		Cursor cursor = db.rawQuery(sqlNewsImage, null);
		cursor.moveToFirst();
		sqliteControl.close();
		if(cursor.getCount()==0){
			//若本地无图片则返回默认图片
			InputStream is = getResources().openRawResource(R.drawable.default_pic);  
	        mBitmap = BitmapFactory.decodeStream(is);
	        for(int i=0;i<main_pic_Fliper.getChildCount();i++){
	        	LinearLayout flipperChildrenLayout = (LinearLayout) main_pic_Fliper.getChildAt(i);
	        	ImageView imageView = (ImageView) flipperChildrenLayout.getChildAt(0);
	        	imageView.setImageBitmap(mBitmap);
	        	imageView.setTag(null);
	        }
		}else{
			int i=0;
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
				mBitmap=filpperPictureCache.get(cursor.getString(0));
				if(mBitmap==null){
					LoadBitmapFromLocal bitmapFromLocal = new LoadBitmapFromLocal();
					mBitmap=bitmapFromLocal.LoadBitmap(cursor.getString(0));
					filpperPictureCache.put(cursor.getString(0), mBitmap);
					//Log.d("mbitmap null", "mbitmap null");
				}
				LinearLayout flipperChildrenLayout = (LinearLayout) main_pic_Fliper.getChildAt(i);
	        	ImageView imageView = (ImageView) flipperChildrenLayout.getChildAt(0);
	        	imageView.setImageBitmap(mBitmap);
	        	imageView.setTag(cursor.getInt(1));
	        	imageView.setOnClickListener(Flipper_pic_clickListener);
	        	imageView.setOnTouchListener(onTouchListener);
	        	i++;
			}
		}
		
	}
	
	
	
	/**
	 * 更新新闻内容，并获取新闻图片
	 */
	private void updateNewsFromServer(){
		UpdateSqliteDataFromServer updateNews = new UpdateSqliteDataFromServer(this);
		updateNews.UpdateNews();//更新所有栏目的新闻内容
		updateNews.UpdateImage();//更新新闻图片
		/**此处更新所有新闻，并且获取这些新闻的图片，采用多重循环，性能很差，需优化**/
	}
	
	
	
	/**
	 * 首页滚动图片单击事件监听器
	 */
	
	private OnClickListener Flipper_pic_clickListener  = new OnClickListener() {
		public void onClick(View v) {
			// TODO 点击进入该图片所对应新闻的详细信息
			Integer newsid = (Integer)v.getTag();
			SqliteControl sqliteControl = new SqliteControl(ViewFlipperActivity.this);
			SQLiteDatabase database = sqliteControl.getDatabase();
			String queryCategoryIDSQL = "select category from news where id=?";
			Cursor cursor = database.rawQuery(queryCategoryIDSQL,new String[]{String.valueOf(newsid)});
			cursor.moveToFirst();
			Integer categoryid = cursor.getInt(0); 
			cursor.close();
			String queryCategoryNameSQL = "select name from category where id=?";
			Cursor cursor2 = database.rawQuery(queryCategoryNameSQL,new String[]{String.valueOf(categoryid)});
			cursor2.moveToFirst();
			String categoryName = cursor2.getString(0);
			cursor2.close();
			sqliteControl.close();
			
			Intent intent=new Intent();
			intent.setClass(ViewFlipperActivity.this, NewsActivity.class);
			intent.putExtra("categoryid",categoryid);
			intent.putExtra("newsid",newsid);
			intent.putExtra("categoryName", categoryName);
			intent.putIntegerArrayListExtra("newsidlist",null);
			startActivity(intent);	
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			//释放变量 newsid=null;categoryid=null;categoryName=null;
			ViewFlipperActivity.this.finish();
			
		}
	};
	
	
	
	/**
	 *清除缓存按钮对话框的确定选择项监听器 
	 */
	private DialogInterface.OnClickListener btnClearDialogPositiveClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        	fetchFirstDataDialog = ProgressDialog.show(ViewFlipperActivity.this, "正在清理缓存数据...", "请稍等片刻...",
					true);
			fetchFirstDataDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
			//开启线程清除数据
			new Thread(){
				public void run() {
					//清除数据库中新闻标题、内容以及图片记录
					SqliteControl sqliteControl = new SqliteControl(ViewFlipperActivity.this);
    				sqliteControl.clear();
    				sqliteControl.close();
    				//清除SD卡中新闻配图
    				String filename = android.os.Environment
    						.getExternalStorageDirectory() +NewsImagePath;
    				File databaseFile = new File(filename);
    				if (databaseFile.exists()) {
    					DeleteFile.delete(databaseFile);
    				}
    				//清除视觉栏目图片
    				String visionFileName = android.os.Environment
    						.getExternalStorageDirectory() +VisionImagePath;
    				File visionImageFile = new File(visionFileName);
    				if (visionImageFile.exists()) {
    					DeleteFile.delete(visionImageFile);
    				}
    				
    				
    				try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				fetchFirstDataDialog.dismiss();
				}
			}.start();
        }
    };
	
    
    
    /**
     *清除缓存按钮对话框的取消选择项监听器 
     */
	private DialogInterface.OnClickListener btnClearDialogNeutralClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            dialog.cancel();
        }
    };
    
    
	
	/**
	 * 清除缓存按钮监听器
	 */
	private OnClickListener btnClearClickListener = new ImageButton.OnClickListener(){
		public void onClick(View v) {
			AlertDialog dialog = new AlertDialog.Builder(ViewFlipperActivity.this).setTitle("你确定清除缓存数据?")
        			.setPositiveButton("确定", btnClearDialogPositiveClickListener)
        			.setNeutralButton("取消", btnClearDialogNeutralClickListener).create();
        	 Window window = dialog.getWindow();     
        	 WindowManager.LayoutParams lp = window.getAttributes(); // 设置透明度为0.3      
        	 WindowManager windowManager = getWindowManager();
        	 Display d = windowManager.getDefaultDisplay();  //为获取屏幕宽、高   
        	 lp.alpha = 0.9f;  
        	 lp.x=0; 
        	 lp.y=0; //居中显示，中心为（0,0）
        	 lp.width=(int) (d.getWidth() * 0.9);
        	 lp.height=(int)(d.getHeight()*0.4);
        	 window.setAttributes(lp);   
        	dialog.show();
		}
		
	};
	
	
	
	/**
	 * 菜单
	 */
	/*
	 public boolean onCreateOptionsMenu(Menu menu)  
	    {  
	        super.onCreateOptionsMenu(menu);  
	        MenuItem item = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Exit");  
	        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()  
	        {  
	            public boolean onMenuItemClick(MenuItem item)  
	            {  
	                System.exit(0);  
	                return true;  
	            }  
	        }); 
	        MenuItem refresh = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "refresh");
	        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					refreshData();//刷新数据
				return true;
				}
			});
	        return true;  
	        
	    } 
	 
	 */
	 
	 /**
	  * 网络连接监听
	  */
	 BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() 
	 {
	    @Override
	    public void onReceive(Context context, Intent intent)
	    {	boolean success;//网路连通检查；
	        //Log.e(TAG, "网络状态改变");
	    	 success = false;
	        //获得网络连接服务
	        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	        // State state = connManager.getActiveNetworkInfo().getState();
	        android.net.NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
	        if (android.net.NetworkInfo.State.CONNECTED == state)
	        { // 判断是否正在使用WIFI网络
	        	success = true;
	        }
	        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
	        if (android.net.NetworkInfo.State.CONNECTED == state) 
	        { // 判断是否正在使用GPRS网络
	            success = true;
	        }
	        
	        if ((!success)&&(dialogTip==0)) 
	        {
	        	AlertDialog dialog = new AlertDialog.Builder(ViewFlipperActivity.this).setTitle("哥们，还没联网呢。")
	        			.setPositiveButton("马上联网", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    Intent mIntent = new Intent("/");
	                    ComponentName comp = new ComponentName(
	                            "com.android.settings",
	                            "com.android.settings.WirelessSettings");
	                    mIntent.setComponent(comp);
	                    mIntent.setAction("android.intent.action.VIEW");
	                    startActivityForResult(mIntent,0);  // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
	                }
	            }).setNeutralButton("没流量不联网", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    dialog.cancel();
	                }
	            }).create();
	        	 Window window = dialog.getWindow();     
	        	 WindowManager.LayoutParams lp = window.getAttributes(); // 设置透明度为0.3      
	        	 WindowManager windowManager = getWindowManager();
	        	 Display d = windowManager.getDefaultDisplay();  //为获取屏幕宽、高   
	        	 lp.alpha = 0.9f;  
	        	 lp.x=0; 
	        	 lp.y=0; //居中显示，中心为（0,0）
	        	 lp.width=(int) (d.getWidth() * 0.9);
	        	 lp.height=(int)(d.getHeight()*0.4);
	        	 window.setAttributes(lp);   
	        	dialog.show();
	        	SharedPreferences sPreferences = getSharedPreferences("networktip",0 );
	    		Editor editor = sPreferences.edit();
	    		editor.putInt("networktips",1);
	    		editor.commit();//只提醒一次
	        } else if((dialogTip==0)&&(success)){
	        	
	        	/**每次启动后台更新新闻**/
	    		new Thread(){
	    			public void run(){
	    				updateNewsFromServer();
	    			}
	    		}.start();
	    		
	        	State mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	 	        State wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	 	       if(mobile.toString().equals("CONNECTED")){
	 	    	  Toast.makeText(ViewFlipperActivity.this,"您现在正使用GPRS网络",1500).show();
		        }
		        if(wifi.toString().equals("CONNECTED")){
		        	 Toast.makeText(ViewFlipperActivity.this,"您现在正使用WIFI网络",1500).show();
		        }
	        	SharedPreferences sPreferences = getSharedPreferences("networktip",0 );
	    		Editor editor = sPreferences.edit();
	    		editor.putInt("networktips",1);
	    		editor.commit();//只提醒一次
	        }           
	                
	    }
	 };
	 
	/**
	 * 按下返回键	
	 */
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	         if((System.currentTimeMillis()-exitTime) > 2000){  
	             Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	             exitTime = System.currentTimeMillis();   
	         } else {
	        	 //释放首页滚动图片缓存
	        	 if(filpperPictureCache!=null){
	        		 for(Bitmap bitmap:filpperPictureCache.values()){
	        			 if(!bitmap.isRecycled() && bitmap != null){
	        				    bitmap.recycle();
	        			 	}
	        		 }
	        	 }
	        	 //释放栏目图片缓存
	        	 if(categoryBitmapCache!=null){
	        		 for(Bitmap bitmap:categoryBitmapCache.values()){
	        			 if(!bitmap.isRecycled() && bitmap != null){
	        				    bitmap.recycle();
	        			 	}
	        		 }
	        	 }
	        	 //释放视觉图片缓存
	        	 if(VisionActivity.visionCacheMap!=null){
	        		 for(Bitmap bitmap:VisionActivity.visionCacheMap.values()){
	        			 if(!bitmap.isRecycled() && bitmap != null){
	        				    bitmap.recycle();
	        			 	}
	        		 }
	        	 }
	        	 
	        	 
	        	 android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
	        	 System.exit(0); 
	         }
	         return false;   
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	 
	 @Override
     protected void onDestroy() 
     {
		 // TODO Auto-generated method stub
         unregisterReceiver(mNetworkStateReceiver); //取消监听
    	 super.onDestroy();
     }



	}