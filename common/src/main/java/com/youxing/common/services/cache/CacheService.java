package com.youxing.common.services.cache;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.jakewharton.disklrucache.DiskLruCache;
import com.youxing.common.app.Enviroment;
import com.youxing.common.app.YXApplication;
import com.youxing.common.utils.Daemon;
import com.youxing.common.utils.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 缓存服务
 *
 * Created by Jun Deng on 15/9/7.
 */
public class CacheService {

    private final static String TAG = "cache";

    private Context context;
    private DiskLruCache cache;

    private static CacheService instance;

    public static CacheService instance() {
        if (instance == null) {
            instance = new CacheService(YXApplication.instance());
        }
        return instance;
    }

    public CacheService(Context context) {
        this.context = context;

        init();
    }

    private void init() {
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            cache = DiskLruCache.open(cacheDir, Enviroment.versionCode(), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            Log.e(TAG, "init failed", e);
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取缓存数据，如果未命中key返回空
     *
     * @param key
     * @return
     */
    public byte[] get(String key) {
        if (cache == null) {
            Log.e(TAG, "can't get cache, cache init failed");
            return null;
        }

        try {
            String md5Key = md5(key);
            DiskLruCache.Snapshot snapShot = cache.get(md5Key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = is.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                return output.toByteArray();
            }
        } catch (IOException e) {
            Log.e(TAG, "get cache failed, key(" + key + ")", e);
        }
        return null;
    }

    /**
     * 缓存数据，如果已存在则重写缓存
     *
     * @param key
     * @param value
     */
    public void put(final String key, final byte[] value) {
        if (cache == null) {
            Log.e(TAG, "get cache failed, ");
            return;
        }

        new Handler(Daemon.looper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    String md5Key = md5(key);
                    DiskLruCache.Editor editor = cache.edit(md5Key);
                    if (editor != null) {
                        InputStream is = new ByteArrayInputStream(value);
                        OutputStream os = editor.newOutputStream(0);

                        int read;
                        byte[] data = new byte[4096];

                        while ((read = is.read(data, 0, data.length)) != -1) {
                            os.write(data, 0, read);
                        }

                        os.close();
                        editor.commit();
                    }
                    cache.flush();
                } catch (IOException e) {
                    Log.e(TAG, "put cache failed, key(" + key + ")", e);
                }
            }
        });
    }

    /**
     * 根据key移除对应的缓存数据
     *
     * @param key
     * @return
     */
    public void remove(final String key) {
        if (cache == null) {
            Log.e(TAG, "can't remove cache, cache init failed");
        }

        new Handler(Daemon.looper()).post(new Runnable() {
            @Override
            public void run() {
                String md5Key = md5(key);
                try {
                    cache.remove(md5Key);
                } catch (IOException e) {
                    Log.e(TAG, "remove cache failed, key(" + key + ")", e);
                }
            }
        });
    }

    /**
     * 清除所有缓存数据
     */
    public void clear() {
        if (cache == null) {
            Log.e(TAG, "can't clear cache, cache init failed");
            return;
        }

        new Handler(Daemon.looper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    cache.delete();
                } catch (IOException e) {
                    Log.e(TAG, "clear cache failed", e);
                }
            }
        });
    }

    private String md5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
