package com.youxing.common.services.image;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.youxing.common.app.YXApplication;

/**
 * 图片服务
 *
 * Created by Jun Deng on 15/6/4.
 */
public class ImageService {

    private volatile static RequestQueue requestQueue;
    private volatile static ImageLoader imageLoader;

    public static RequestQueue getQueue() {
        if (requestQueue == null) {
            synchronized (ImageService.class) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(YXApplication.instance());
                }
            }
        }
        return requestQueue;
    }

    public static ImageLoader getLoader() {
        if (imageLoader == null) {
            synchronized (ImageService.class) {
                if (imageLoader == null) {
                    imageLoader = new ImageLoader(getQueue(), new BitmapCache());
                }
            }
        }
        return imageLoader;
    }

}
