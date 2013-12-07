package com.swjtu.youthapp;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SinaWeiboWebView extends Activity{
	private static int num=0;//判断提示对话框启动次数
	 /** Called when the activity is first created. */
	private WebView wv;
	private ProgressDialog pd;
	private Handler handler;
	private long exitTime = 0;
	//底部导航条
	private LinearLayout main_bottom_layout1,main_bottom_layout2,main_bottom_layout3,main_bottom_layout4;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sina_weibo);
        init();//执行初始化函数
        String url = "http://m.weibo.com/swjtuyouthmedia";
        handler=new Handler(){
        	public void handleMessage(Message msg)
    	    {//定义一个Handler，用于处理下载线程与UI间通讯
    	      if (!Thread.currentThread().isInterrupted())
    	      {
    	        switch (msg.what)
    	        {
    	        case 0:
    	        	if(num==0){
    	        		pd.show();//显示进度对话框 
        	        	pd.getWindow().setGravity(Gravity.CENTER);
        	        	num++;
    	        	}
    	        	break;
    	        case 1:
    	        	pd.hide();//隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
    	        	break;
    	        }
    	      }
    	      super.handleMessage(msg);
    	    }
        };
        loadurl(wv,url);
        
        //判断底部导航条是否被触按
        boolean clickble = getIntent().getBooleanExtra("clickble", true);
    	// 底部导航条
		main_bottom_layout1 = (LinearLayout) findViewById(R.id.main_bottom_layout1_ly);
		main_bottom_layout1.setOnClickListener(clickListener_layout1);

		main_bottom_layout2 = (LinearLayout) findViewById(R.id.main_bottom_layout2_ly);
		main_bottom_layout2.setOnClickListener(clickListener_layout2);

		main_bottom_layout3 = (LinearLayout) findViewById(R.id.main_bottom_layout3_ly);
		main_bottom_layout3.setOnClickListener(clickListener_layout3);

		main_bottom_layout4 = (LinearLayout) findViewById(R.id.main_bottom_layout4_ly);
		main_bottom_layout4.setOnClickListener(clickListener_layout4);
		main_bottom_layout4.setSelected(clickble);
        
    }
	
	
	/*
	 * 初始化
	 */
    public void init(){
    	wv=(WebView)findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);//可用JS
        wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	loadurl(view,url);//载入网页
            	
                return true;   
            }//重写点击动作,用webview载入
 
        });
        wv.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){//载入进度改变而触发 
             	if(progress==100){
            		handler.sendEmptyMessage(1);//如果全部载入,隐藏进度对话框
            	}else{
            		pd.setMessage("页面加载中，请稍候..."+progress + "%");
            	}   
                super.onProgressChanged(view, progress);   
            }   
        });
    	pd=new ProgressDialog(SinaWeiboWebView.this);
    	pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       
        
    }
    
    
    
    
    
    public void loadurl(final WebView view,final String url){
    	new Thread(){
        	public void run(){
        		handler.sendEmptyMessage(0);
        		view.loadUrl(url);//载入网页
        	}
        }.start();
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
				intent.setClass(SinaWeiboWebView.this, ViewFlipperActivity.class);
				intent.putExtra("clickble", true);
				startActivity(intent);
				overridePendingTransition(R.anim.fade,R.anim.fade);
				main_bottom_layout1.setSelected(false);
				SinaWeiboWebView.this.finish();
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
			intent.setClass(SinaWeiboWebView.this, UserInfo.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout2.setSelected(false);
			SinaWeiboWebView.this.finish();
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
			intent.setClass(SinaWeiboWebView.this, VisionActivity.class);
			intent.putExtra("clickble", true);
			startActivity(intent);
			main_bottom_layout3.setSelected(false);
			SinaWeiboWebView.this.finish();
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
		}
	};
    
    
    
		/**
	    * 退出确认
	    */
	public void ConfirmExit(){
	    	AlertDialog.Builder ad=new AlertDialog.Builder(SinaWeiboWebView.this);
	    	ad.setTitle("退出");
	    	ad.setMessage("真的要离开?多待会儿嘛...");
	    	ad.setPositiveButton("确定离开", new DialogInterface.OnClickListener() {//退出按钮
				public void onClick(DialogInterface dialog, int i) {
					// TODO Auto-generated method stub
					SinaWeiboWebView.this.finish();//关闭activity
	 
				}
			});
	    	ad.setNegativeButton("多留一会儿",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int i) {
					//不退出不用执行任何操作
				}
			});
	    	ad.show();//显示对话框
	    }
	
    /*
     * 捕捉返回键(non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {   
            wv.goBack();   
            return true;   
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
        	ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
        	return true; 
        }   
        return super.onKeyDown(keyCode, event);   
    }

}
