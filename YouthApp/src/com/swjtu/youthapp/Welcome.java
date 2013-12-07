package com.swjtu.youthapp;

import java.io.IOException;

import com.swjtu.youthapp.common.IsNetWorkAlive;
import com.swjtu.youthapp.common.LoadBitmapFromLocal;
import com.swjtu.youthapp.data.SqliteControl;
import com.swjtu.youthapp.data.UpdateSqliteDataFromServer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Welcome extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//full screen and no title 
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.welcome);
		//Ê×Ò³Í¼Æ¬	
		ImageView iv=(ImageView)this.findViewById(R.id.wpic);
		SqliteControl sqliteControl = new SqliteControl(this);
		String sql= "select sdpath from homeimage";
		SQLiteDatabase db=sqliteControl.getDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if(cursor.getCount()==0){
			iv.setImageResource(R.drawable.prepic);
			//Log.d("homeimage count","homeimage count equals 0");
		}else{
		Bitmap map=null;
		try {
			LoadBitmapFromLocal bitmapFromLocal = new LoadBitmapFromLocal();
			map=bitmapFromLocal.LoadBitmap(cursor.getString(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
			if(map!=null){
				iv.setImageBitmap(map);
			}else{
				//Log.d("homeimage bitmap","homeimage bitmap == null");
				iv.setImageResource(R.drawable.prepic);
			}
		}
		cursor.close();
		sqliteControl.close();
		SharedPreferences sPreferences = getSharedPreferences("networktip",0 );
		Editor editor = sPreferences.edit();
		editor.putInt("networktips",0);
		editor.commit();
		welcome();
	}
	
	
	
	public void welcome() {
		new Thread(new Runnable() {
			public void run() {
				try {
					/*
					IsNetWorkAlive alive = new IsNetWorkAlive(Welcome.this);
					boolean flag=alive.isNetAlive();
					if(flag){
						loadhomeimage();
						Log.d("Welcome Activity network status", "Welcome Activity netAlive is false");
					}
					*/
					loadhomeimage();
					Thread.sleep(3000);
					Message m = new Message();
					logHandler.sendMessage(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	

	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			welcome1();
		}
	};

	
	public void welcome1() {		
		Intent it=new Intent();
		it.setClass(Welcome.this, ViewFlipperActivity.class);
    	startActivity(it);
    	overridePendingTransition(R.anim.fade, R.anim.fade);  
    	Welcome.this.finish();
	}
	
	public void loadhomeimage() {
		new Thread(new Runnable() {
			public void run() {
					UpdateSqliteDataFromServer dataFromServer = new UpdateSqliteDataFromServer(Welcome.this);
					dataFromServer.updateHomeImage();
			}
		}).start();
	}
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==4 ){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return super.onKeyDown(keyCode, event);
	}

}
