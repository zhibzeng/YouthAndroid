package com.swjtu.youthapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
public class PushMesgActivity extends Activity{
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,
	main_bottom_layout4,commentPubLayout,footbar_layout_ly;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE); 
	        setContentView(R.layout.pushinfo);
	        TextView tv =(TextView)findViewById(R.id.pushtext);
	        TextView titleView=(TextView)findViewById(R.id.pushtitle);
	        String text=getIntent().getExtras().getString("pushtext");
	        String title=getIntent().getExtras().getString("title");
	        titleView.setText(title);
	        tv.setText(text);
	        //底部导航条
			main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
			main_bottom_layout1.setOnClickListener(clickListener_layout1);
			main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
			main_bottom_layout2.setOnClickListener(clickListener_layout2);
			main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
			main_bottom_layout3.setOnClickListener(clickListener_layout3);
			main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
			main_bottom_layout4.setOnClickListener(clickListener_layout4);
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
					intent.setClass(PushMesgActivity.this, ViewFlipperActivity.class);
					intent.putExtra("clickble", true);
					startActivity(intent);
					overridePendingTransition(R.anim.fade,R.anim.fade);
					main_bottom_layout1.setSelected(false);
					PushMesgActivity.this.finish();
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
				intent.setClass(PushMesgActivity.this, UserInfo.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout2.setSelected(false);
				PushMesgActivity.this.finish();
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
				intent.setClass(PushMesgActivity.this, VisionActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				main_bottom_layout3.setSelected(false);
				PushMesgActivity.this.finish();
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
				intent.setClass(PushMesgActivity.this, SinaWeiboWebView.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				overridePendingTransition(R.anim.fade,R.anim.fade);
				main_bottom_layout4.setSelected(false);
				PushMesgActivity.this.finish();
			}
		};
}
