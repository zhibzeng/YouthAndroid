package com.swjtu.youthapp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.swjtu.youthapp.data.SqliteControl;
import com.swjtu.youthapp.data.UpdateSqliteDataFromServer;
import com.swjtu.youthapp.po.NewsEntity;
import com.swjtu.youthapp.widget.MyAdapter;
import com.swjtu.youthapp.widget.PullDownListView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class NewsTitleActivity extends Activity implements PullDownListView.OnRefreshListioner {
	private static int updateNum=0;
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	private static int refreshNoNews=1;
	private static int newsnum=0;//新闻总数
	private static int offset=1;
	private final static int PERPAGE=10;//每页默认显示行数
	private static int usertotal=PERPAGE;//用户确定一屏显示几条数据
	private Handler mHandler;
	private ArrayList<HashMap<String, Object>> listItem;
	private PullDownListView mPullDownView;
	private ListView mListView;
	private MyAdapter adapter;
	private TextView categoryNameTV;//栏目名称
	private Button newsTitleActivityBtn;//头部返回按钮
	private int selectedcategoryid;
	private String categoryName;
	//用户存储该分类下的所有新闻内容，传递给NewsActivity,用户上一篇，下一篇文章
	private List<Integer> newsIdList;
	//底部导航条
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,main_bottom_layout4;
	//int index;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.newstitles);
		selectedcategoryid=getIntent().getIntExtra("categoryid", 1);
		categoryName = getIntent().getStringExtra("categoryName");
		newsIdList = new ArrayList<Integer>();
		mHandler = new Handler();
		mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
		mPullDownView.setRefreshListioner(this);
		mListView = mPullDownView.mListView;
		categoryNameTV = (TextView) findViewById(R.id.newsCategoryName);
		categoryNameTV.setText(categoryName);

		newsTitleActivityBtn = (Button) findViewById(R.id.newsTitleActivityBtn);
		newsTitleActivityBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO 单击返回按钮
				Intent intent = new Intent();
				intent.setClass(NewsTitleActivity.this,ViewFlipperActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				NewsTitleActivity.this.finish();//关闭当前Activity
			}
		});
		
		//底部导航条
		main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
		main_bottom_layout1.setOnClickListener(clickListener_layout1);
		
		main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
		main_bottom_layout2.setOnClickListener(clickListener_layout2);
		
		main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
		main_bottom_layout3.setOnClickListener(clickListener_layout3);
		
		main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
		main_bottom_layout4.setOnClickListener(clickListener_layout4);
		
		

		InitNewsTitles(selectedcategoryid);
		if(listItem.size()==0&&updateNum==0){
			refreshData();
		}
			//判断是否在底部显示"更多"按钮
			if(newsnum>listItem.size()){
				mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
			}else{
				mPullDownView.setMore(false);
			}
			adapter = new MyAdapter(this,listItem);	
			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					int newsid;
					int preNewsid=-1;//上一篇文章id
					int nextNewsid=-1;//下一篇文章id
					int categoryid;
						newsid=(Integer)listItem.get(position-1).get("newsid");
						if(position!=1){
							preNewsid = (Integer)listItem.get(position-2).get("newsid"); 
						}
						if(position!=listItem.size()){
							nextNewsid = (Integer)listItem.get(position).get("newsid"); 
						}
						 
						categoryid = (Integer)listItem.get(position-1).get("categoryid");
						Intent intent=new Intent();
						intent.setClass(NewsTitleActivity.this, NewsActivity.class);
						intent.putExtra("categoryid",categoryid);
						intent.putExtra("newsid",newsid);
						intent.putExtra("categoryName", categoryName);
						intent.putIntegerArrayListExtra("newsidlist", (ArrayList<Integer>) newsIdList);
						startActivity(intent);	
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						NewsTitleActivity.this.finish();
				}
			});
		}
	

	//刷新数据Hander
		final Handler listHandler = new Handler() {
			public void handleMessage(Message msg) {
			Intent intent = new Intent();
			intent.setClass(NewsTitleActivity.this,NewsTitleActivity.class);
			intent.putExtra("categoryid", selectedcategoryid);
			intent.putExtra("categoryName", categoryName);
			startActivity(intent);
			NewsTitleActivity.this.finish();
			}
		};
		
		//刷新数据
		public void refreshData(){
			myDialog = ProgressDialog.show(NewsTitleActivity.this, "亲，请稍等一会哦...", "正在努力加载数据...",
					true);
			myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
			myDialog.setCancelable(true);
			new Thread() {
				public void run() {
					try {
						updateNum=1;//更新次数
						//更新数据库
						UpdateSqliteDataFromServer updateSqliteDataFromServer = new UpdateSqliteDataFromServer(NewsTitleActivity.this);
						updateSqliteDataFromServer.UpdateNewsByCategory(selectedcategoryid);
						//updateSqliteDataFromServer.updateCategoryImage(selectedcategoryid);
						Thread.sleep(1500);//模拟加载，停留1.5秒
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
	public void InitNewsTitles(int selectedcategoryid)
	{	listItem=new ArrayList<HashMap<String, Object>>();
		// FetchDataFromServer dataFromServer = new FetchDataFromServer();
		 SqliteControl sqliteControl= new SqliteControl(NewsTitleActivity.this);
		 List<NewsEntity> newsList =new ArrayList<NewsEntity>();
		 newsList = sqliteControl.GetNews(selectedcategoryid, usertotal);
		 sqliteControl.close();
		 SqliteControl sqliteControl2 = new SqliteControl(NewsTitleActivity.this);
		 newsnum=sqliteControl2.getNewsCount(selectedcategoryid);
		 sqliteControl2.close();
		 if(newsList!=null){
	     for(int i=0;i<newsList.size();i++)
         {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.viewnewsdetail);//图像资源的ID,无图片则设为null
            map.put("ItemTitle", newsList.get(i).getTitle());
            map.put("newsid", newsList.get(i).getId());
            map.put("categoryid",selectedcategoryid);
            map.put("time",newsList.get(i).getTime());
            map.put("comefrom", newsList.get(i).getComefrom());
            listItem.add(map);
            newsIdList.add(newsList.get(i).getId());
         }
		 }
	  
	 }
	private int addLists(){
		UpdateSqliteDataFromServer dataFromServer =new UpdateSqliteDataFromServer(this);
		int num=dataFromServer.UpdateNewsByCategory(selectedcategoryid);
		if(num>0){
			refreshNoNews=1;
			listItem.clear();
			newsIdList.clear();
			 SqliteControl sqliteControl= new SqliteControl(NewsTitleActivity.this);
			 List<NewsEntity> newsList =new ArrayList<NewsEntity>();
			 newsList = sqliteControl.GetNews(selectedcategoryid, PERPAGE*offset);
			 sqliteControl.close();
			 SqliteControl sqliteControl2 = new SqliteControl(NewsTitleActivity.this);
			 newsnum=sqliteControl2.getNewsCount(selectedcategoryid);
			 sqliteControl2.close();
			 if(newsList!=null){
		     for(int i=0;i<newsList.size();i++)
	         {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            map.put("ItemImage", R.drawable.viewnewsdetail);//图像资源的ID,无图片则设为null
	            map.put("ItemTitle", newsList.get(i).getTitle());
	            map.put("newsid", newsList.get(i).getId());
	            map.put("categoryid",selectedcategoryid);
	            map.put("time",newsList.get(i).getTime());
	            map.put("comefrom", newsList.get(i).getComefrom());
	            listItem.add(map);
	            newsIdList.add(newsList.get(i).getId());
	         }
			 }
		}else{
			refreshNoNews++;
		}
		return num;
    }
	
	
	
	/**
	 * 刷新数据Hander
	 */
	final Handler pullRefreshHadle = new Handler(){
		public void handleMessage(Message msg) {
			int num = msg.getData().getInt("num");
			mPullDownView.onRefreshComplete();//这里表示刷新处理完成后把上面的加载刷新界面隐藏
			if(newsnum>listItem.size()){
				mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
			}else{
				mPullDownView.setMore(false);
			}
			adapter.notifyDataSetChanged();	
			if(num>0){
				Toast.makeText(NewsTitleActivity.this,"本次刷新有"+num+"条更新",Toast.LENGTH_LONG).show();
			}else{
				switch (refreshNoNews) {
				case 1:
					Toast.makeText(NewsTitleActivity.this,"亲，没有新内容哦。",1500).show();
					break;
				case 2:
					Toast.makeText(NewsTitleActivity.this,"亲，真的没有新内容...",1500).show();
					break;
				case 3:
					Toast.makeText(NewsTitleActivity.this,"求求你不要再刷了，真心没有新内容...",1500).show();
					break;
				case 4:
					Toast.makeText(NewsTitleActivity.this,"看来你闲得蛋疼，玩DOTA吧...",1500).show();
					break;
				default:
					Toast.makeText(NewsTitleActivity.this,"好好学习，不要再刷了...",1500).show();
					break;
				}
			}
		}
	};
	
	//下拉刷新
	public void onRefresh() {
		new Thread() {
			public void run() {
				int num = addLists();
				Message m = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("num",num);
				m.setData(bundle);
				pullRefreshHadle.sendMessage(m);
			}
		}.start();
	}
	
	
	//底部更多按钮
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				offset++;
				usertotal=PERPAGE*offset;
				listItem.clear();
				newsIdList.clear();
				 SqliteControl sqliteControl= new SqliteControl(NewsTitleActivity.this);
				 List<NewsEntity> newsList =new ArrayList<NewsEntity>();
				 newsList = sqliteControl.GetNews(selectedcategoryid, PERPAGE*offset);
				 sqliteControl.close();
				 if(newsList!=null){
			     for(int i=0;i<newsList.size();i++)
		         {
		            HashMap<String, Object> map = new HashMap<String, Object>();
		            map.put("ItemImage", R.drawable.viewnewsdetail);//图像资源的ID,无图片则设为null
		            map.put("ItemTitle", newsList.get(i).getTitle());
		            map.put("newsid", newsList.get(i).getId());
		            map.put("categoryid",selectedcategoryid);
		            map.put("time",newsList.get(i).getTime());
		            map.put("comefrom", newsList.get(i).getComefrom());
		            listItem.add(map);
		            newsIdList.add(newsList.get(i).getId());
		            
		         }
				 }
				 //判断是否还有更多新闻
				 mPullDownView.onLoadMoreComplete();//这里表示加载更多处理完成后把下面的加载更多界面（隐藏或者设置字样更多）
				 if(newsnum>PERPAGE*offset){
					 mPullDownView.setMore(true);
				 }else{
					 mPullDownView.setMore(false);
				 }
				 
				adapter.notifyDataSetChanged();	
			}
		}, 1500);
		
	}
	
	

	
	/**
	 * 底部导航条 导航1 按钮单击事件
	 */
    private OnClickListener clickListener_layout1 = new OnClickListener() {
		public void onClick(View v) {
				main_bottom_layout1.setSelected(true);
				main_bottom_layout2.setSelected(false);
				main_bottom_layout3.setSelected(false);
				main_bottom_layout4.setSelected(false);
				Intent intent = new Intent();
				intent.setClass(NewsTitleActivity.this, ViewFlipperActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				main_bottom_layout1.setSelected(false);
				NewsTitleActivity.this.finish();
		}
	};
	
	/**
	 * 底部导航条 导航2 按钮单击事件
	 */
    private OnClickListener clickListener_layout2 = new OnClickListener() {
		public void onClick(View v) {
			main_bottom_layout1.setSelected(false);
			main_bottom_layout2.setSelected(true);
			main_bottom_layout3.setSelected(false);
			main_bottom_layout4.setSelected(false);
			Intent intent = new Intent();
			intent.setClass(NewsTitleActivity.this, UserInfo.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout2.setSelected(false);
			NewsTitleActivity.this.finish();
		}
	};
	
	/**
	 * 底部导航条 导航3 按钮单击事件
	 */
    private OnClickListener clickListener_layout3 = new OnClickListener() {
		public void onClick(View v) {
			main_bottom_layout1.setSelected(false);
			main_bottom_layout2.setSelected(false);
			main_bottom_layout3.setSelected(true);
			main_bottom_layout4.setSelected(false);
			Intent intent = new Intent();
			intent.setClass(NewsTitleActivity.this, VisionActivity.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout3.setSelected(false);
			NewsTitleActivity.this.finish();
		}
	};
	
	
	
	/**
	 * 底部导航条 导航4 按钮单击事件
	 */
    private OnClickListener clickListener_layout4 = new OnClickListener() {
		public void onClick(View v) {
			main_bottom_layout1.setSelected(false);
			main_bottom_layout2.setSelected(false);
			main_bottom_layout3.setSelected(false);
			main_bottom_layout4.setSelected(true);
			Intent intent = new Intent();
			intent.setClass(NewsTitleActivity.this, SinaWeiboWebView.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			overridePendingTransition(R.anim.fade,R.anim.fade);
			main_bottom_layout4.setSelected(false);
			NewsTitleActivity.this.finish();
		}
	};
	
	
	
	/**
	 * 按下返回键	
	 */
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	 Intent intent = new Intent();
	    	 intent.setClass(NewsTitleActivity.this,ViewFlipperActivity.class);
	    	 startActivity(intent);
	    	 overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	    	 NewsTitleActivity.this.finish();
	         return false;   
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	
	
	
	
	 @Override  
	    protected void onRestart() {  
	        super.onRestart();  
	        refreshNoNews=1;
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();   
	        refreshNoNews=1;
	    }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();    
	        refreshNoNews=1;
	        System.gc();
	    }  
	    @Override  
	    protected void onStop() {  
	        super.onStop();  
	        refreshNoNews=1;
	        System.gc();
	    }  
	    @Override  
	    protected void onDestroy() {  
	        super.onDestroy();
	        refreshNoNews=1;	
	        System.gc();
	    }  
	
		
}
