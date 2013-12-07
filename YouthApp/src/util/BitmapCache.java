package util;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class BitmapCache {
    static private BitmapCache cache;
    /** 用于Chche内容的存储 */
    private Hashtable<Integer, MySoftRef> hashRefs;
    /** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
    private ReferenceQueue<Bitmap> q;

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
      */
    private class MySoftRef extends SoftReference<Bitmap> {
        private Integer _key = 0;
        public MySoftRef(Bitmap bmp, ReferenceQueue<Bitmap> q, int key) {
            super(bmp, q);
            _key = key;
        }
    }

    private BitmapCache() {
        hashRefs = new Hashtable<Integer, MySoftRef>();
        q = new ReferenceQueue<Bitmap>();
    }

    /**
     * 取得缓存器实例
      */
    public static BitmapCache getInstance() {
        if (cache == null) {
            cache = new BitmapCache();
        }
        return cache;
    }

    /**
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
      */
    private void addCacheBitmap(Bitmap bmp, Integer key) {
        cleanCache();// 清除垃圾引用
         MySoftRef ref = new MySoftRef(bmp, q, key);
        hashRefs.put(key, ref);
    }

    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     */
    public Bitmap getBitmap(int resId, Context context) {
        Bitmap bmp = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
         if (hashRefs.containsKey(resId)) {
            MySoftRef ref = (MySoftRef) hashRefs.get(resId);
            bmp = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
         // 并保存对这个新建实例的软引用
         if (bmp == null) {
            // 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，
              // 无需再使用java层的createBitmap，从而节省了java层的空间。
              bmp = BitmapFactory.decodeStream(context.getResources()
                    .openRawResource(resId));
            this.addCacheBitmap(bmp, resId);
        }
        return bmp;
    }

    private void cleanCache() {
        MySoftRef ref = null;
        while ((ref = (MySoftRef) q.poll()) != null) {
            hashRefs.remove(ref._key);
        }
    }

    /**
     * 清除Cache内的全部内容
     */
    public void clearCache() {
        cleanCache();
        hashRefs.clear();
        System.gc();
        System.runFinalization();
    }
}