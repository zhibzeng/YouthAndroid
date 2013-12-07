package com.swjtu.youthapp.guide;
import com.swjtu.youthapp.R;
import com.swjtu.youthapp.ViewFlipperActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
/**
 * 
 * @author zhibinzeng
 *
 */
public class Appstart extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.appstart);
	new Handler().postDelayed(new Runnable(){
		public void run(){
			Intent intent = new Intent (Appstart.this,Viewpager.class);			
			startActivity(intent);			
			finish();
		}
	}, 1000);
   }
}