package com.swjtu.youthapp.widget;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
public class WebImageCache extends LruCache<String, Bitmap> {
   private static WebImageCache gWebImage;
   private String sDir;
   private ExecutorService mExecutor;

   public static WebImageCache getInstance() {
       if (gWebImage == null) {
           int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
           // Use 1/8th of the available memory for this memory cache.
           int cacheSize = maxMemory / 8; 
           Log.i("WebImageCache", "WebImageCache cache size:"+cacheSize);
           gWebImage = new WebImageCache(cacheSize);
       }
       return gWebImage;
   }

   private WebImageCache(int maxSize) {
       super(maxSize);
       
       File sdCard = Environment.getExternalStorageDirectory();
       sDir = sdCard.getAbsolutePath() + "/wunding_ulp/cache/";
       File dir = new File (sDir);
       dir.mkdirs();        
       mExecutor = Executors.newFixedThreadPool(5);
   }

   @Override
   protected int sizeOf(String key, Bitmap bitmap) {
       // The cache size will be measured in kilobytes rather than
       // number of items.
        Log.e("", "sizeOf:"+bitmap.getRowBytes() * bitmap.getHeight() / 1024);
       return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
   }

   public void loadBitmap(ImageView imageView, final String imageURL, Bitmap def)
 {
       if (imageURL == null || imageURL.length() == 0) {
    	   if (def != null)
    		   imageView.setImageBitmap(def);            
           return;
       }
       final WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(imageView);
       //在内存缓存中，则返回Bitmap对象
       final String sName = urlToLocal(imageURL);
       Bitmap bmp = get(sName);
       if(bmp != null)
       {
    	   imageView.setImageBitmap(bmp);
    	   return;
       }
 
       if (def != null)
    	   imageView.setImageBitmap(def);
 
 final Handler handler = new Handler(Looper.getMainLooper())
 {
   @Override
   public void handleMessage(Message msg)
   {
     if (imageViewReference != null && msg.obj != null) {
    	 final ImageView imageView = imageViewReference.get();
    	 if (imageView != null && imageView.isShown()) {
    		 imageView.setImageBitmap((Bitmap)msg.obj);
    	 }
     }        
   }
 };

 mExecutor.execute(new Runnable() {
           public void run() {
               Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
               String sPath = sDir + sName;
               Bitmap bitmap;
               FileInputStream fis;
               FileOutputStream fos;
               // 对本地缓存的查找
               try {
            	   fis = new FileInputStream(sPath);
            	   bitmap = BitmapFactory.decodeStream(fis, null, null);
            	   fis.close();
                   if (bitmap != null) {
                        Log.e("", "find local file:"+sPath);
                       put(sName, bitmap);
                       Message newmsg = handler.obtainMessage(0, bitmap);
                       handler.sendMessage(newmsg);                            
                       return;
                   }
                   else {
                       //可能错误的文件，删除
                       new File(sPath).delete();
                   }

               } catch (FileNotFoundException e) {
                   try {
                       new File(sPath).createNewFile();
                   } catch (IOException e1) {
                       e1.printStackTrace();
                       return;
                   }
               } catch (Exception e) {
                   e.printStackTrace();
                   return;
               }

                Log.e("", "WebImageCache start downlaod:"+imageURL);
                Log.e("", "to "+sPath);
               
               //没有就下载
               InputStream is = downloadImage(imageURL);
               if(is==null){
            	   Log.d("inputStream == null","inputStream == null");
            	   Log.d("inputStream == null","inputStream == null");
            	   Log.d("inputStream == null","inputStream == null");
            	   
               }
               //写入缓存
               FileLock fl = null;
               try {
                   fos = new FileOutputStream(sPath);
                   fl = fos.getChannel().tryLock();
                   if (fl != null && fl.isValid()) {
               byte[] buffer = new byte[1024];
               int len1 = 0;
               while ((len1 = is.read(buffer)) > 0) {
                   fos.write(buffer, 0, len1);
               }
               fl.release();
                       fos.close();
                   }
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
                   return;
               } catch (IOException e) {
                   e.printStackTrace();
                   return;
               } finally {
                   if (fl != null && fl.isValid()) {
                       try {
                           fl.release();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
               
               // 加入内存缓存
               bitmap = BitmapFactory.decodeFile(sPath);
               if (bitmap != null) {
                   put(sName, bitmap);
                   Message newmsg = handler.obtainMessage(0, bitmap);
                   handler.sendMessage(newmsg);                            
                   return;
               }

           }
       });

 }
   static String urlToLocal(String url) {
       return String.valueOf(Math.abs(url.hashCode()));
   }

   private static InputStream downloadImage(String urlString){
       Uri url = Uri.parse(urlString);
       String scheme = url.getScheme();

       if (ContentResolver.SCHEME_FILE.equals(scheme)) {
           try {
               return new FileInputStream(url.getPath());
           } catch (Exception e) {
               e.printStackTrace();
               return null;
           }
       }
 
       DefaultHttpClient httpClient = new DefaultHttpClient();
       HttpGet request = new HttpGet(urlString);
       HttpResponse response;
       try {
           response = httpClient.execute(request);
           return response.getEntity().getContent();
       } catch (ClientProtocolException e) {
           e.printStackTrace();
       } catch (IOException e) {

           e.printStackTrace();
       }
       return null;
      
   
   }

}