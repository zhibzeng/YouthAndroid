package com.swjtu.youthapp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.swjtu.youthapp.common.LoadFileToLocal;
import com.swjtu.youthapp.data.FetchDataFromServer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
public class VisionActivity  extends Activity{
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	private ViewFlipper visionMainFlipper;
	private GestureDetector detector;
	private int mCurrentPage=0;
	private int mTotlePage=1;
	private LayoutInflater mInflater;
	public static List<String> imageUrlList=null;
	//保存每个图片所对应的bitmap
	public static Map<String, Bitmap> visionCacheMap = null;
	private boolean flag  = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.vision_main);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		visionMainFlipper = (ViewFlipper) findViewById(R.id.VisionMainFlipper);
		detector = new GestureDetector(GestureListener);
		visionMainFlipper.setOnTouchListener(FlipperTouchListener);
		//设置一个定时任务，当获取图片时长超过10秒后，自动退出
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if(!flag){
					GalleryHandler.obtainMessage(2).sendToTarget();
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							ExitAndFreeMemory();
						}
					}, 3000);//十秒后尚未加载完成则退出
				}
			}
		}, 10000);//十秒后尚未加载完成则退出
		getImageUrl();//获取视觉图片
	}
	
	public void getImageUrl(){
		myDialog = ProgressDialog.show(VisionActivity.this,"Vision", "正在努力加载图片...",true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
		myDialog.setCancelable(true);
		new Thread(){
			public void run(){
				try{
				FetchDataFromServer dataFromServer = new FetchDataFromServer();
				if(imageUrlList==null){
					imageUrlList=dataFromServer.getVisionPicture(2,1,1);
					myDialog.dismiss();
					GalleryHandler.obtainMessage(0).sendToTarget();
				}
				if(imageUrlList!=null){
					myDialog.dismiss();
					GalleryHandler.obtainMessage(0).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				//myDialog.dismiss();
			}
		}.start();
	}
	
	
	
	  //更新界面UI
    final Handler GalleryHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getImage();
				break;
			case 1:
				Toast.makeText(VisionActivity.this,"网络故障,请检查网络",Toast.LENGTH_SHORT).show();
				break;
			case 2:
				if(myDialog.isShowing()){
					myDialog.dismiss();
				}
				Toast.makeText(VisionActivity.this,"好可惜啊,访问出错了,下次再来吧",Toast.LENGTH_SHORT).show();
				break;
			default:
				//myDialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	private void getImage(){
		if(imageUrlList==null){
			GalleryHandler.obtainMessage(1).sendToTarget();
			return;//结束函数
		}
		if(visionCacheMap==null){visionCacheMap=new HashMap<String, Bitmap>();}
		visionMainFlipper.removeAllViews();
	 	mTotlePage=(int) Math.ceil(imageUrlList.size()/7.0);
	 	for(int i=0;i<mTotlePage;i++)
        {
        		
        		View view =  mInflater.inflate(R.layout.vision_pic_view, null);
    			List<ImageView> imageViews = new ArrayList<ImageView>();
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv1));
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv2));
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv3));
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv4));
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv5));
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv6));
    			imageViews.add((ImageView) view.findViewById(R.id.vision_grid_iv7));
    			if(imageUrlList!=null){
    				 int end=7*i+7;
    		          if(end>imageUrlList.size()) end=imageUrlList.size();
    				List<String> subList = imageUrlList.subList(7*i, end);
    				for(int j=0;j<subList.size();j++){
    					Bitmap bitmap=null;
    					final String path=subList.get(j);
    					if(visionCacheMap!=null){
    						if(visionCacheMap.size()!=0){
    							bitmap=visionCacheMap.get(path);
    						}
    					}
    					if(bitmap==null){
    						bitmap  = loadImageFromNetwork(path);  
    						visionCacheMap.put(path,bitmap);
    					}
    					imageViews.get(j).postInvalidate();  
    					imageViews.get(j).setImageBitmap(bitmap);
    					imageViews.get(j).setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								// 点击缩放图片
								Intent intent = new Intent();
								intent.setClass(VisionActivity.this,VisionPictureDetail.class);
								intent.putExtra("imagePath",path);
								startActivity(intent);
								overridePendingTransition(R.anim.view_push_down_in_out,R.anim.view_push_down_in_out);
							}
						});
    					imageViews.get(j).setOnTouchListener(FlipperTouchListener);
    				}
    			}
    			visionMainFlipper.addView(view);
    		}
	 		flag=true;
	}
	
	
	/*
	private void loadImageToImageView(final String url,final ImageView view){
		new Thread(new Runnable(){  
			Bitmap Bitmap = loadImageFromNetwork(url);  
		    public void run() {  
		        // post() 特别关键，就是到UI主线程去更新图片                  
		    	view.post(new Runnable(){  
		        public void run() {  
		            // TODO Auto-generated method stub  
		        	view.setImageBitmap(Bitmap);
		        	view.invalidate();
		        }}) ;  
		        }  
		}).start()  ;  
	}
	*/
	
	

	
	/**
	 * 获取图片公共方法
	 */
	private Bitmap loadImageFromNetwork(String imageUrl)  
	{  
	    Bitmap bitmap = null;  
	    // 可以在这里通过文件名来判断，是否本地有此图片
		bitmap = LoadFileToLocal.getHttpBitmap(imageUrl);  
	    if (bitmap == null) {  
	        //Log.d("test", "null drawable");  
	    } else {  
	       // Log.d("test", "not null drawable");  
	    }  
	      
	    return bitmap ;  
	}  
	

	
	/**
	 *FlipperTouch监听器
	 */
    private OnTouchListener FlipperTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			return detector.onTouchEvent(event);
		}
	};
	

    /**
	 * Filpper OnGestureListener
	 */
    private OnGestureListener GestureListener = new OnGestureListener() {

		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
				float arg3) {
			if (e1.getX() - e2.getX()>120) {
				visionMainFlipper.setInAnimation(AnimationUtils.loadAnimation(VisionActivity.this,
						R.anim.push_left_in_main));
				visionMainFlipper.setOutAnimation(AnimationUtils.loadAnimation(VisionActivity.this,
						R.anim.push_left_out_main));
				visionMainFlipper.showNext();
				mCurrentPage = (mCurrentPage+1)%mTotlePage;  
				
				return true;
			} else if (e1.getX() - e2.getX() < -120) {
				visionMainFlipper.setInAnimation(AnimationUtils.loadAnimation(VisionActivity.this,
						R.anim.push_right_in));
				visionMainFlipper.setOutAnimation(AnimationUtils.loadAnimation(VisionActivity.this,
						R.anim.push_right_out));
				visionMainFlipper.showPrevious();
				mCurrentPage = (mCurrentPage-1)%mTotlePage;
				
				return true;
			}
			return false;
		}

		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    };
    
    /**
	 * 按下返回键	*/
	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	 ExitAndFreeMemory();
	    	 return false;
	     }
	     return super.onKeyDown(keyCode, event);
	   }
	 
	 /**
	  * 退出并释放内存
	  */
	 private void ExitAndFreeMemory(){
		 this.finish();
	 	 if(visionCacheMap!=null){
	    	 for(Bitmap bitmap:visionCacheMap.values()){
	   			 if(!bitmap.isRecycled() && bitmap != null){
	   				    bitmap.recycle();
	   			 	}
	   		 }
	    	 visionCacheMap=null;
	    }
	 }
	 
	 
	 
	   
  
		

}
