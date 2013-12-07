package com.swjtu.youthapp.common;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class LoadBitmapFromLocal {
	public  Bitmap LoadBitmap(String url) throws IOException{
		String filename = android.os.Environment.getExternalStorageDirectory()
     				+ url;
		/*
		BitmapFactory.Options option = new BitmapFactory.Options();  
		    option.inPreferredConfig = Bitmap.Config.RGB_565;   
		    option.inPreferredConfig = Bitmap.Config.RGB_565;   
		    option.inPurgeable = true;  
		    option.inInputShareable = true;  
		   // option.inJustDecodeBounds = true;  
		    Bitmap bitmap=null;
		    try{
		    	bitmap = BitmapFactory.decodeFile(filename, option);
		    }catch (OutOfMemoryError e) {
		        // 提示系统，进行内存回收
		        System.gc();
		   }finally{
			   return bitmap;
		   }
           */
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;   
		opts.inPreferredConfig = Bitmap.Config.RGB_565;   
		opts.inPurgeable = true;  
		opts.inInputShareable = true;  
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, opts);
		opts.inSampleSize =computeSampleSize(opts,800);       
		opts.inJustDecodeBounds = false;
		Bitmap bitmap=null;
		try {
			bitmap = BitmapFactory.decodeFile(filename, opts);
		    } catch (OutOfMemoryError err) {
		    	return bitmap;
		    }
		return bitmap;
         }
	
	public static int computeSampleSize(BitmapFactory.Options options, int target) {  
	    int w = options.outWidth;  
	    int h = options.outHeight;  
	    int candidateW = w / target;  
	    int candidateH = h / target;  
	    int candidate = Math.max(candidateW, candidateH);  
	    if (candidate == 0)  
	        return 1;  
	    if (candidate > 1) {  
	        if ((w > target) && (w / candidate) < target)  
	            candidate -= 1;  
	    }  
	    if (candidate > 1) {  
	        if ((h > target) && (h / candidate) < target)  
	            candidate -= 1;  
	    }
		return candidate; 
	}
}
