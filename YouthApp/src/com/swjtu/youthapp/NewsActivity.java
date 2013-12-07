package com.swjtu.youthapp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import util.StringUtil;
import com.swjtu.youthapp.common.LoadBitmapFromLocal;
import com.swjtu.youthapp.common.LoadFileToLocal;
import com.swjtu.youthapp.data.FetchDataFromServer;
import com.swjtu.youthapp.data.SqliteControl;
import com.swjtu.youthapp.data.UpdateSqliteDataFromServer;
import com.swjtu.youthapp.po.NewsEntity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class NewsActivity extends Activity {
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	private Map<String, Bitmap> cache=new HashMap<String, Bitmap>();;//加载完毕的图片缓存到这里   
    private Gallery gallery;   
    private List<String> imgUrl;//加载到gallery中的所有图片路径  
    private int newsid;
    private int categoryid; 
    private TextView tv_newstitle;
    private TextView tv_newscontents;
    private TextView tv_comefrom;
    private TextView tv_time;
	private NewsEntity newsEntity;
	private String categoryName;
	private EditText commentContent;//评论内容
	private Button commentNumButton,commentPub,commentSubmit,commentCancel;
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,
			main_bottom_layout4,commentPubLayout,footbar_layout_ly;
	private Button newsActivityBtn; //返回按钮
	private int type = 0;//提交评论的返回值
	private ProgressDialog commentSaveProgressDialog;// 提交评论对话框
	private MediaPlayer mediaPlayer=null;//音频播放器
	private static int audioPlayNum=0;//音频播放次数
	private ImageButton audioPlayBtn;
	private ImageButton audioStopBtn;
	private ImageButton audioDownloadBtn;
	private TextView preNews;
	private TextView nextNews;
	private LinearLayout audioLayout;
	private static String AUDIOPATH="/YouthAppData/Audio/";
	private String audioPath=null;//音频播放地址
	private static int AUDIOSTOP=0;//判断是否点击了stop audio按钮
	private static Uri AUDIOURI=null;
	private List<Integer> newsIdList = new ArrayList<Integer>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//全屏   
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式   
        //横屏   
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏   
    	newsid = getIntent().getIntExtra("newsid",1);
    	categoryid = getIntent().getIntExtra("categoryid", 1);
    	categoryName = getIntent().getStringExtra("categoryName"); 
    	newsIdList = getIntent().getIntegerArrayListExtra("newsidlist");
    	Log.d("categoryid 1 ", categoryid+"");
    	Log.d("categoryName 1 ", categoryName+"");
    	
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.news); 
        //底部导航条
		main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
		main_bottom_layout1.setOnClickListener(clickListener_layout1);
		main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
		main_bottom_layout2.setOnClickListener(clickListener_layout2);
		main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
		main_bottom_layout3.setOnClickListener(clickListener_layout3);
		main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
		main_bottom_layout4.setOnClickListener(clickListener_layout4);
		audioLayout = (LinearLayout) findViewById(R.id.audioLayout);
		
		newsActivityBtn  = (Button) findViewById(R.id.newsActivityBtn);
		newsActivityBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO go back to news list Activity
				Intent intent  =  new Intent();
				intent.setClass(NewsActivity.this,NewsTitleActivity.class);
				intent.putExtra("categoryid", categoryid);
				intent.putExtra("categoryName",categoryName);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				NewsActivity.this.finish();
			}
		});
		
        tv_newstitle=(TextView)findViewById(R.id.news_displaytitle);
        tv_newscontents=(TextView)findViewById(R.id.news_displaycontents);
        tv_comefrom=(TextView)findViewById(R.id.comefrom);
        tv_time=(TextView)findViewById(R.id.time);
        gallery = (Gallery) findViewById(R.id.newsphoto);   
        commentNumButton = (Button) findViewById(R.id.commentNum);
        commentPub = (Button) findViewById(R.id.commentPub);
        commentPub.setOnClickListener(commentPubOnClickListener);
        commentPubLayout = (LinearLayout) findViewById(R.id.commentPubLayout);//评论布局
        footbar_layout_ly = (LinearLayout) findViewById(R.id.footbar_layout_ly);//底部布局
        commentSubmit = (Button) findViewById(R.id.commentSubmit);//提交评论
        commentSubmit.setOnClickListener(commentSubmitOnClickListener);
        commentCancel = (Button) findViewById(R.id.commentCancel);
        commentCancel.setOnClickListener(commentCancelOnClickListener);
        if(newsIdList!=null){
        int current = newsIdList.indexOf(newsid);
        if(current>0){
        	 preNews = (TextView) findViewById(R.id.preNews);
        	 final int preNewsId = newsIdList.get(current-1);
        	 SqliteControl sqliteControl = new SqliteControl(NewsActivity.this);
         	 SQLiteDatabase db = sqliteControl.getDatabase();
         	 String sql="select title from news where id=?";
     		 Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(preNewsId)});
     		 cursor.moveToFirst();
        	 preNews.setText("上一篇: "+cursor.getString(0));
        	 cursor.close();
        	 sqliteControl.close();
        	 preNews.setVisibility(View.VISIBLE);
        	 preNews.setOnClickListener(new OnClickListener() {
 				public void onClick(View v) {
 					Intent intent=new Intent();
 					intent.setClass(NewsActivity.this, NewsActivity.class);
 					intent.putExtra("categoryid",categoryid);
 					intent.putExtra("newsid",preNewsId);
 					intent.putExtra("categoryName", categoryName);
 					intent.putIntegerArrayListExtra("newsidlist", (ArrayList<Integer>) newsIdList);
 					startActivity(intent);
 					NewsActivity.this.finish();
 				}
 			});
        	
        }
        if(current!=(newsIdList.size()-1)){
             nextNews = (TextView) findViewById(R.id.nextNews);
        	 final int nextNewsId = newsIdList.get(current+1);
        	 SqliteControl sqliteControl = new SqliteControl(NewsActivity.this);
         	 SQLiteDatabase db = sqliteControl.getDatabase();
         	 String sql="select title from news where id=?";
     		 Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(nextNewsId)});
     		 cursor.moveToFirst();
     		 nextNews.setText("下一篇: "+cursor.getString(0));
        	 cursor.close();
        	 sqliteControl.close();
        	 nextNews.setVisibility(View.VISIBLE);
        	 nextNews.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(NewsActivity.this, NewsActivity.class);
					intent.putExtra("categoryid",categoryid);
					intent.putExtra("newsid",nextNewsId);
					intent.putExtra("categoryName", categoryName);
					intent.putIntegerArrayListExtra("newsidlist", (ArrayList<Integer>) newsIdList);
					startActivity(intent);
					NewsActivity.this.finish();
				}
			});
        }
        }
        InitNewsInfo();   //初始化操作   
        getNewsImages(); //调用函数获取图片，开启新线程加载图片
        getCommetNum();//获取新闻评论数目
        getAudio();//获取新闻音频
    }
    
    final Handler Handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(NewsActivity.this,"音频已下载至"+AUDIOPATH,Toast.LENGTH_LONG).show();
				break;
			case 1:
				Bitmap bmp = BitmapFactory.decodeResource(NewsActivity.this.getResources()
						,R.drawable.pause_button_pressed);
				audioPlayBtn.setImageBitmap(bmp);
				break;
			case 2:
				Bitmap bmp2 = BitmapFactory.decodeResource(NewsActivity.this.getResources()
						,R.drawable.play_button_pressed);
				audioPlayBtn.setImageBitmap(bmp2);
				break;
			default:
				break;
			}
		}
	};
    
    /*
     * 获取该文章的图片
     * */
    public void getNewsImages(){
    	myDialog = ProgressDialog.show(NewsActivity.this, "亲，请稍等一会哦...", "正在努力加载图片...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
		myDialog.setCancelable(true);
    	//开启新线程
    new Thread(){
    	public void run(){
    		UpdateSqliteDataFromServer updateSqliteDataFromServer = new UpdateSqliteDataFromServer(NewsActivity.this);
    	    updateSqliteDataFromServer.UpdateImageByNewsId(newsid);
    		Message m = new Message();
			GalleryHandler.sendMessage(m);
    	}
    	}.start();
    }
    //更新界面UI
    final Handler GalleryHandler = new Handler() {
		public void handleMessage(Message msg) {
			setGallery();
			myDialog.dismiss();
				
		}
	};
	
    //gallery显示图片
   public void setGallery(){
		InitImageInfo(newsid);
    	if(imgUrl!=null){
    		for(int i=0;i<imgUrl.size();i++){
    			try {
    				loadImage(imgUrl.get(i));
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		ImageAdapter adapter = new ImageAdapter(this, imgUrl,cache);   
    		gallery.setAdapter(adapter);
    		gallery.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
						String url= android.os.Environment.getExternalStorageDirectory()
			     				+ imgUrl.get(position);
						File file = new File(url);
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(file), "image/*");
						startActivity(intent);
					}
				});
    	} 
  }
    
    /**  
     * 初始化新闻信息  
     * @param url  
     */  
    public void InitNewsInfo()
    {   
		String newstitle;
		String newstime;
		String newscomefrom;
		String newscontents;
		newsEntity=getNewsFromDB(newsid);
		newstitle=newsEntity.getTitle();
    	newstime=newsEntity.getTime();
    	newscomefrom=newsEntity.getComefrom();
    	newstime=newsEntity.getTime();
    	newscontents=newsEntity.getContent();
    	tv_newstitle.setText(newstitle);
    	String contentTotal="";
    	String[] contenStrings;
    	contenStrings=newscontents.split(System.getProperty("line.separator"));
    	for(int i=0;i<contenStrings.length;i++){
    		if((!contenStrings[i].trim().equals(""))
    				||(!contenStrings[i].equals(System.getProperty("line.separator")))){
    			contenStrings[i]=contenStrings[i].trim();
    			/*
    			Pattern p = Pattern.compile("[ | ]*");
    			Matcher matcher=p.matcher(contenStrings[i]);
    			contenStrings[i]=matcher.replaceAll("");
        		*/
    			StringUtil stringUtil = new StringUtil();
    			contenStrings[i]=stringUtil.getStringNoBlank(contenStrings[i]);
        		contentTotal=contentTotal+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+contenStrings[i]+"<br>";	
    		}
    	}
        tv_newscontents.setText(Html.fromHtml(contentTotal));
        tv_comefrom.setText(newscomefrom);
        tv_time.setText(newstime);
    }  
    
    /**
     * 根据新闻ID从数据库中获取新闻实体
     */
    
    private NewsEntity getNewsFromDB(int newsid){
    	NewsEntity newsEntity = new NewsEntity();
    	SqliteControl sqliteControl = new SqliteControl(NewsActivity.this);
    	SQLiteDatabase db = sqliteControl.getDatabase();
    	String sql="select id,title,comefrom,time,content,category from news where id=?";
		Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(newsid)});
		cursor.moveToFirst();
		newsEntity.setId(cursor.getInt(0));
		newsEntity.setTitle(cursor.getString(1));
		newsEntity.setComefrom(cursor.getString(2));
		newsEntity.setTime(cursor.getString(3));
		newsEntity.setContent(cursor.getString(4));
		newsEntity.setCategory(cursor.getInt(5));
		cursor.close();
		sqliteControl.close();
		return newsEntity;
    }
    
    /**  
     * 启动线程加载指定路径的图片到cache  
     * @param url  
     * @throws IOException 
     */  
    public void loadImage(String url) throws IOException{   
      // new Thread(new LoadImageThread(url,cache)).start();   
    	      //将指定的路径下的图片加载到定义的cache当中去   
             // InputStream imageStream = new URL(url).openStream();
        /*      
    	String filename = android.os.Environment.getExternalStorageDirectory()
      				+ url;
      			FileInputStream fis = null;
				try {
					fis = new FileInputStream(filename);
				} catch (Exception e) {
					e.printStackTrace();
				}
              Bitmap map =BitmapFactory.decodeStream(fis);   
              fis.close();
          */
    	if(cache.get(url)==null){
    		LoadBitmapFromLocal bitmapFromLocal = new LoadBitmapFromLocal();
        	Bitmap map = bitmapFromLocal.LoadBitmap(url);
        	cache.put(url, map);
    	}
    }   
    
    
    /**  
     * 初始化下缓存cache  
     * 要加载到gallery中的图片路径  
     * 初始Gallery并设置其adapter  
     */  
    public void InitImageInfo(int newsid)
    {
    imgUrl = new ArrayList<String>(); 
    SqliteControl sqliteControl2 = new SqliteControl(this);
    SQLiteDatabase db=sqliteControl2.getDatabase();
    String sql="select sdpath from newsimage where newsid=?";
    Cursor cursor = db.rawQuery(sql,  new String[]{String.valueOf(newsid)});
    for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
    	imgUrl.add(cursor.getString(0));
    }
    cursor.close();
    sqliteControl2.close();
    } 
  
    
    /**
     *我要评论按钮监听器
     */
    private OnClickListener commentPubOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			footbar_layout_ly.setVisibility(View.GONE);
			commentPubLayout.setVisibility(View.VISIBLE);
			commentContent = (EditText) findViewById(R.id.commentContent);
			commentContent.requestFocus();
			InputMethodManager imm = (InputMethodManager)NewsActivity.this.getSystemService(INPUT_METHOD_SERVICE); 
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
             
		}
	};
	
    
    
    
    /**
     *查看新闻评论 Button 单击事件监听
     */
    private OnClickListener commentNumOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(NewsActivity.this,CommentList.class);
			intent.putExtra("newsid",newsid);
			intent.putExtra("categoryid", categoryid);
			intent.putExtra("categoryName", categoryName);
			intent.putIntegerArrayListExtra("newsidlist", (ArrayList<Integer>) newsIdList);
			startActivity(intent);
			NewsActivity.this.finish();
		}
	};
    

	
	final Handler commentNumHandler = new Handler() {
		public void handleMessage(Message msg) {
			int num = msg.getData().getInt("commentNum");
			if(num==0){
				commentNumButton.setText("暂无相关评论");
				commentNumButton.setOnClickListener(null);
			}else{
				commentNumButton.setText("点击查看"+num+"条评论");
				commentNumButton.setOnClickListener(commentNumOnClickListener);
			}
		}
	};
	
	
	/**
	 * 获取新闻评论数目
	 * @param newsid
	 */
	private void getCommetNum(){
		new Thread(){
			public void run(){
				FetchDataFromServer fetchDataFromServer = new FetchDataFromServer();
				int commentNum = 0;
				try {
					commentNum = fetchDataFromServer.getCommentNumByNewsID(newsid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Message m = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("commentNum",commentNum);
				m.setData(bundle);
				commentNumHandler.sendMessage(m);
			}
		}.start();
	}
	
	
	/**
	 * 评论校验
	 */
	private Boolean validateData(){
		if(commentContent.getText().toString().trim().length()==0){
			commentContent.setError("请输入评论内容");
			return false;
		}
		return true;
	}
	
	/**
	 * Hander
	 */
	final Handler commentSubmitHandler = new Handler() {
	public void handleMessage(Message msg) {
		Integer type=msg.getData().getInt("type");
		if(type==1){//提交评论成功，跳转到其他页面
			commentNumButton.performClick();//自动触发Button事件
			Toast.makeText(NewsActivity.this, "成功提交评论",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(NewsActivity.this,CommentList.class);
			intent.putExtra("newsid",newsid);
			intent.putExtra("categoryid", categoryid);
			intent.putExtra("categoryName", categoryName);
			startActivity(intent);
			NewsActivity.this.finish();
		}else{//评论失败
			Toast.makeText(NewsActivity.this, "网络故障,提交评论失败。",
					Toast.LENGTH_SHORT).show();
		}
	}
};

	
	
	/**
	 * commentSubmit click监听器
	 */
	private OnClickListener commentSubmitOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if(validateData()){
			SqliteControl sqliteControl = new SqliteControl(NewsActivity.this);
			SQLiteDatabase db = sqliteControl.getDatabase();
			String sqlString = "select id,name from user";
			Cursor cursor = db.rawQuery(sqlString, null);
			if(cursor.getCount()==0){
				//用户尚未登录，跳转到登录界面
				cursor.close();
				sqliteControl.close();
				Intent intent = new Intent();
				intent.setClass(NewsActivity.this, UserLogin.class);
				intent.putExtra("from","NewsActivity");//来源
				intent.putExtra("newsid",newsid);
				intent.putExtra("categoryid", categoryid);
				intent.putExtra("categoryName", categoryName);
				startActivity(intent);
				finish();
			}else{
				//用户已经登录，则直接提交评论
				cursor.moveToFirst();
				final int userid = cursor.getShort(0);
				final String username = cursor.getString(1);
				cursor.close();
				sqliteControl.close();
				commentSaveProgressDialog = ProgressDialog.show(NewsActivity.this,"发表评论","正在提交评论...",true);
				commentSaveProgressDialog.setCancelable(true);//按返回键取消显示
				new Thread(){
					public void run(){
						FetchDataFromServer submitComment = new FetchDataFromServer();
						type=submitComment.commentSave(newsid, userid, username, 
								commentContent.getText().toString().trim());
						commentSaveProgressDialog.dismiss();
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putInt("type", type);
						message.setData(bundle);
						commentSubmitHandler.sendMessage(message);
					}
				}.start();
				
			}
			
			}	
		}
	};
	
	
	/**
	 * commentCancelOnClickListener
	 */
	private OnClickListener commentCancelOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			footbar_layout_ly.setVisibility(View.VISIBLE);
			commentPubLayout.setVisibility(View.GONE);
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.hideSoftInputFromWindow(commentContent.getWindowToken(), 0);
		}
	};
	
	/**
	 * Audio Handle
	 */
	final Handler audioHandler = new Handler() {
		public void handleMessage(Message msg) {
			audioLayout.setVisibility(View.VISIBLE);
		}
	};
	private void getAudio() {
		new Thread(){
			public void run(){
				FetchDataFromServer fetchAudio = new FetchDataFromServer();
				audioPath=fetchAudio.getNewsAudioPath(newsid);
				if(audioPath!=null){
					//播放来自网络的音频文件
					AUDIOURI = Uri.parse(audioPath);
					mediaPlayer = new MediaPlayer();
					try {
						mediaPlayer.setDataSource(NewsActivity.this, AUDIOURI);
						mediaPlayer.prepare();
						audioPlayBtn = (ImageButton)findViewById(R.id.audioPlayBtn);
						audioStopBtn = (ImageButton)findViewById(R.id.audioStopBtn);
						audioDownloadBtn = (ImageButton)findViewById(R.id.audioDownloadBtn);
						audioPlayBtn.setOnClickListener(audioPlayListener);
						audioStopBtn.setOnClickListener(audioStopListener);
						audioDownloadBtn.setOnClickListener(audioDownloadListener);
						Message m = new Message();
						audioHandler.sendMessage(m);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}
	
	/**
	 * 音频播放Button监听器
	 */
	private OnClickListener audioPlayListener = new OnClickListener() {
		public void onClick(View v) {
			if(mediaPlayer!=null){
				if(audioPlayNum%2==0){
					Bitmap bmp = BitmapFactory.decodeResource(NewsActivity.this.getResources()
							,R.drawable.pause_button_pressed);
					audioPlayBtn.setImageBitmap(bmp);
					new Thread(){
						public void run(){
									if(AUDIOSTOP!=0){//之前点击了STOP按钮
										try {
											mediaPlayer.setDataSource(NewsActivity.this, AUDIOURI);
											mediaPlayer.prepare();
										} catch (IllegalArgumentException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (SecurityException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IllegalStateException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										AUDIOSTOP=0;
									}
									mediaPlayer.start();
							}
						}.start();
				}else{
					mediaPlayer.pause();
					Bitmap bmp2 = BitmapFactory.decodeResource(NewsActivity.this.getResources()
							,R.drawable.play_button_pressed);
					audioPlayBtn.setImageBitmap(bmp2);
				}
				audioPlayNum++;
			}
		}
	};
	
	
	/**
	 * 音频停止播放Button监听器
	 */
	private OnClickListener audioStopListener = new OnClickListener() {
		public void onClick(View v) {
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				Bitmap bmp = BitmapFactory.decodeResource(NewsActivity.this.getResources()
						,R.drawable.play_button_pressed);
				audioPlayBtn.setImageBitmap(bmp);
				AUDIOSTOP=1;
			}
		}
	};
	
	/**
	 * 音频下载Button监听器
	 */
	private OnClickListener audioDownloadListener = new OnClickListener() {
		public void onClick(View v) {
			if(mediaPlayer!=null&&audioPath!=null){
				new Thread(){
				public void run(){
					LoadFileToLocal loadFileToLocal = new LoadFileToLocal();
					loadFileToLocal.LoadFile(audioPath, AUDIOPATH);
					Handler.obtainMessage(0).sendToTarget();
				}
			}.start();
			}
		}
	};
	
	
	
	
	
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
				intent.setClass(NewsActivity.this, ViewFlipperActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				overridePendingTransition(R.anim.fade,R.anim.fade);
				main_bottom_layout1.setSelected(false);
				NewsActivity.this.finish();
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
			intent.setClass(NewsActivity.this, UserInfo.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout2.setSelected(false);
			NewsActivity.this.finish();
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
			intent.setClass(NewsActivity.this, VisionActivity.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout3.setSelected(false);
			NewsActivity.this.finish();
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
			intent.setClass(NewsActivity.this, SinaWeiboWebView.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			overridePendingTransition(R.anim.fade,R.anim.fade);
			main_bottom_layout4.setSelected(false);
			NewsActivity.this.finish();
		}
	};
	
	
	

	/**
	 * 按下返回键	
	 */
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	 Intent intent = new Intent();
	    	 intent.setClass(NewsActivity.this,NewsTitleActivity.class);
	    	 Log.d("categoryid", categoryid+"");
	    	 Log.d("categoryName", categoryName+"");
	    	 
	    	 intent.putExtra("categoryid", categoryid);
	    	 intent.putExtra("categoryName",categoryName);
	    	 startActivity(intent);
	    	 overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	    	 NewsActivity.this.finish();
	         return true;   
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	 
	 @Override  
	    protected void onStop() {
		 Log.d("onstop","stop");
		 if(mediaPlayer!=null){
			 mediaPlayer.stop();
			 mediaPlayer.release();//回收所占的内存 
	         System.gc();//提醒系统及时回收 
		 }
		 //释放图片缓存
    	 if(cache!=null){
    		 for(Bitmap bitmap:cache.values()){
    			 if(!bitmap.isRecycled() && bitmap != null){
    				    bitmap.recycle();
    			 	}
    		 }
    	 }
	        super.onStop();  
	    }  
	 

}
