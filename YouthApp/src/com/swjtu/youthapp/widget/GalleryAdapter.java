package com.swjtu.youthapp.widget;
import com.swjtu.youthapp.R;
import com.swjtu.youthapp.VisionActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView.ScaleType;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	public GalleryAdapter(Context context,String imagePath) {
		this.context = context;
	}

	
	public int getCount() {
		return VisionActivity.imageUrlList.size();
		//return images.length;
	}

	
	public Object getItem(int position) {
		return null;
	}

	
	public long getItemId(int position) {
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		//Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), images[0]);
		Bitmap bmp = VisionActivity.visionCacheMap.get(VisionActivity.imageUrlList.get(position));
		MyImageView view = new MyImageView(context, bmp.getWidth(), bmp.getHeight());
		//MyImageView view = new MyImageView(context,800,480);
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setScaleType(ScaleType.FIT_CENTER);
		view.setImageBitmap(bmp);
		return view;
	}

}
