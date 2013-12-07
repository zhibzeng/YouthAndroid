package com.swjtu.youthapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class LoadImageThread implements Runnable{
	private String imageUrl;   
    HashMap<String,Bitmap> cache;   
    LoadImageThread(String url,HashMap<String,Bitmap> cache){   
        this.imageUrl = url;   
        this.cache = cache;   
    }   
    public void run(){   
        try {   
            //将指定的路径下的图片加载到定义的cache当中去   
            InputStream imageStream = new URL(imageUrl).openStream();   
            Bitmap map = BitmapFactory.decodeStream(imageStream);   
            cache.put(imageUrl, map);
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }   

}
