package degenius.ekspedisi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by HP on 11/12/2016.
 */

public class CustomImageLoader {
    private static CustomImageLoader cgv;
    private static Context ct;
    private RequestQueue rq;
    private ImageLoader il;

    private CustomImageLoader(Context context){
        this.ct = context;
        this.rq = getRq();

        il = new ImageLoader(rq, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
            cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized CustomImageLoader getInstance(Context context){
        if(cgv == null){
            cgv = new CustomImageLoader(context);
        }
        return cgv;
    }

    public RequestQueue getRq(){
        if (rq == null){
            Cache cache = new DiskBasedCache(ct.getCacheDir(),10*1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            rq = new RequestQueue(cache,network);
            rq.start();
        }
        return rq;
    }

    public ImageLoader getIl(){
        return il;
    }
}
