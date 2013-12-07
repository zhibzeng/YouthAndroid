package com.swjtu.youthapp;
import com.swjtu.youthapp.common.LoadFileToLocal;
import com.swjtu.youthapp.widget.GalleryAdapter;
import com.swjtu.youthapp.widget.VisionDetailGallery;
import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Toast;
public class VisionPictureDetail extends Activity  implements OnTouchListener{
	// 屏幕宽度
		public static int screenWidth;
		// 屏幕高度
		public static int screenHeight;
		private VisionDetailGallery gallery;
		private String imagePath=null;
		private LinearLayout visionMask;
		private static int clickNum=0;
		private Button visionDownloadPic;
		private final static String VisionImagePath = "/YouthAppData/images/vision/";
		
		public void onCreate(Bundle savedInstanceState) {
			imagePath = getIntent().getStringExtra("imagePath");
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.vision_pic_detail);
			screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
			screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
			visionMask = (LinearLayout) findViewById(R.id.visionMask);
			
			gallery = (VisionDetailGallery) findViewById(R.id.gallery);
			gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
			gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
			gallery.setAdapter(new GalleryAdapter(this,imagePath));
			gallery.setSelection(VisionActivity.imageUrlList.indexOf(imagePath));
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
						clickNum++;
						if(clickNum%2==1){
							visionMask.setVisibility(View.VISIBLE);
						}else{
							visionMask.setVisibility(View.GONE);
						}
					
				}
			});
			
			visionDownloadPic = (Button) findViewById(R.id.visionDownloadPic);
			visionDownloadPic.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String path = null;
					path=VisionActivity.imageUrlList.get(gallery.getSelectedItemPosition());
					if(path==null)return;
					if (android.os.Environment.getExternalStorageState().equals(
							android.os.Environment.MEDIA_MOUNTED)){
						LoadFileToLocal loadImageToLocal = new LoadFileToLocal();
						loadImageToLocal.LoadFile(path,VisionImagePath);
						Toast.makeText(VisionPictureDetail.this, "图片已存至"+VisionImagePath, 
								Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(VisionPictureDetail.this,"SD卡失踪了?", 
								Toast.LENGTH_SHORT).show();
					}
					
					
					
				}
			});

		

		}

		float beforeLenght = 0.0f; // 两触点距离
		float afterLenght = 0.0f; // 两触点距离
		boolean isScale = false;
		float currentScale = 1.0f;// 当前图片的缩放比率

		private class GalleryChangeListener implements OnItemSelectedListener {

			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentScale = 1.0f;
				isScale = false;
				beforeLenght = 0.0f;
				afterLenght = 0.0f;

			}

			
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		}

		
		public boolean onTouch(View v, MotionEvent event) {

			// Log.i("","touched---------------");
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_POINTER_DOWN:// 多点缩放
				beforeLenght = spacing(event);
				if (beforeLenght > 5f) {
					isScale = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isScale) {
					afterLenght = spacing(event);
					if (afterLenght < 5f)
						break;
					float gapLenght = afterLenght - beforeLenght;
					if (gapLenght == 0) {
						break;
					} else if (Math.abs(gapLenght) > 5f) {
						// FrameLayout.LayoutParams params =
						// (FrameLayout.LayoutParams) gallery.getLayoutParams();
						float scaleRate = gapLenght / 854;// 缩放比例
						// Log.i("",
						// "scaleRate："+scaleRate+" currentScale:"+currentScale);
						// Log.i("", "缩放比例：" +
						// scaleRate+" 当前图片的缩放比例："+currentScale);
						// params.height=(int)(800*(scaleRate+1));
						// params.width=(int)(480*(scaleRate+1));
						// params.height = 400;
						// params.width = 300;
						// gallery.getChildAt(0).setLayoutParams(new
						// Gallery.LayoutParams(300, 300));
						Animation myAnimation_Scale = new ScaleAnimation(currentScale, currentScale + scaleRate, currentScale, currentScale + scaleRate, Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f);
						// Animation myAnimation_Scale = new
						// ScaleAnimation(currentScale, 1+scaleRate, currentScale,
						// 1+scaleRate);
						myAnimation_Scale.setDuration(100);
						myAnimation_Scale.setFillAfter(true);
						myAnimation_Scale.setFillEnabled(true);
						// gallery.getChildAt(0).startAnimation(myAnimation_Scale);

						// gallery.startAnimation(myAnimation_Scale);
						currentScale = currentScale + scaleRate;
						// gallery.getSelectedView().setLayoutParams(new
						// Gallery.LayoutParams((int)(480), (int)(800)));
						// Log.i("",
						// "===========:::"+gallery.getSelectedView().getLayoutParams().height);
						// gallery.getSelectedView().getLayoutParams().height=(int)(800*(currentScale));
						// gallery.getSelectedView().getLayoutParams().width=(int)(480*(currentScale));
						gallery.getSelectedView().setLayoutParams(new Gallery.LayoutParams((int) (480 * (currentScale)), (int) (854 * (currentScale))));
						// gallery.getSelectedView().setLayoutParams(new
						// Gallery.LayoutParams((int)(320*(scaleRate+1)),
						// (int)(480*(scaleRate+1))));
						// gallery.getSelectedView().startAnimation(myAnimation_Scale);
						// isScale = false;
						beforeLenght = afterLenght;
					}
					return true;
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				isScale = false;
				break;
			}

			return false;
		}

		/**
		 * 就算两点间的距离
		 */
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}
}
