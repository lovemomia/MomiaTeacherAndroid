package com.youxing.common.services.config;

import android.content.Context;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.youxing.common.app.Constants;
import com.youxing.common.app.YXApplication;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 配置服务（配置从服务器读取）
 *
 * Created by Jun Deng on 15/9/8.
 */
public class ConfigService {

    private static final String TAG = "config";

    private Context context;

    /**
     * 用以订阅任意键值的变化通知
     * <p>
     * ConfigService.addListener(ANY, listener)<br>
     * ConfigService.removeListener(ANY, listener)
     */
    public static final String ANY = "*";

    private JSONObject root;
    private HashMap<String, ArrayList<ConfigChangeListener>> listeners = new HashMap<String, ArrayList<ConfigChangeListener>>();

    public ConfigService(Context context) {
        this.context = context;
    }

    private static ConfigService instance;

    public static ConfigService instance() {
        if (instance == null) {
            instance = new ConfigService(YXApplication.instance());
        }
        return instance;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        try {
            Boolean value = root().getBoolean(name);
            return value == null ? defaultValue : value;
        } catch (JSONException e) {
        }
        return defaultValue;
    }

    public int getInt(String name, int defaultValue) {
        try {
            Integer value = root().getInteger(name);
            return value == null ? defaultValue : value;
        } catch (JSONException e) {
        }
        return defaultValue;
    }

    public double getDouble(String name, double defaultValue) {
        try {
            Double value = root().getDouble(name);
            return value == null ? defaultValue : value;
        } catch (JSONException e) {
        }
        return defaultValue;
    }

    /**
     * 如果没有name或name为null，则返回defaultValue
     * <p>
     * 建议使用TextUtils.isEmpty()判断是否返回空
     */
    public String getString(String name, String defaultValue) {
        try {
            String value = root().getString(name);
            return value == null ? defaultValue : value;
        } catch (JSONException e) {
        }
        return defaultValue;
    }

    /**
     * 如果没有name或name对应的值不为JSONObject，则返回null
     */
    public JSONObject getJSONObject(String name) {
        try {
            return root().getJSONObject(name);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 读取指定字段的值，以type类型返回，如果值不存在或者解析失败返回null
     *
     * @param name
     * @param type 指定返回bean类型
     * @return
     */
    public Object getObject(String name, Class<?> type) {
        try {
            return root().getObject(name, type);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 从服务器获取并更新配置文件
     */
    public void refresh() {
        HttpService.get(Constants.domain() + "/config", null, CacheType.DISABLE, String.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                String result = (String) response;
                try {
                    JSONObject json = JSON.parseObject(result).getJSONObject("data");
                    setConfig(json);
                    if (Log.LEVEL < Integer.MAX_VALUE) {
                        Log.d(TAG, "success (Config) " + json.toString());
                    }
                } catch (Exception e) {
                    Log.w(TAG, "fail to refresh config is not a json object", e);
                }
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                Log.i(TAG, "fail to refresh config");
            }
        });
    }

    public void setConfig(JSONObject root) {
        if (root == null)
            return;

        if (Thread.currentThread().getId() != Looper.getMainLooper()
                .getThread().getId()) {
            Log.w(TAG, "setConfig must be run under main thread");
            if (Log.LEVEL < Integer.MAX_VALUE) {
                throw new RuntimeException("setConfig must be run under main thread");
            } else {
                return;
            }
        }
        File file = new File(getConfigDir(), new Random(
                System.currentTimeMillis()).nextInt()
                + ".tmp");
        if (!write(root, file)) {
            Log.w(TAG, "fail to write config to " + file);
            return;
        }
        if (!file.renameTo(getConfigFile())) {
            Log.w(TAG, "fail to move config file " + file);
            return;
        }
        JSONObject old = this.root;
        this.root = root;

        ArrayList<ConfigChangeListener> list = listeners.get(ANY);
        if (list != null) {
            for (ConfigChangeListener l : list) {
                l.onConfigChange(ANY, old, root);
            }
        }
        for (Map.Entry<String, ArrayList<ConfigChangeListener>> e : listeners
                .entrySet()) {
            String key = e.getKey();
            if (ANY.equals(key))
                continue;
            Object v1 = old.get(key);
            Object v2 = root.get(key);
            boolean eq = (v1 == null) ? (v2 == null) : (v1.equals(v2));
            if (eq)
                continue;
            list = e.getValue();
            Log.i(TAG, "config changed, " + key + " has " + list.size()
                    + " listeners, changed from (" + v1 + ") to (" + v2 + ")");
            for (ConfigChangeListener l : list) {
                l.onConfigChange(key, v1, v2);
            }
        }
    }

    /**
     * 订阅名字为key的配置项的变化通知
     * <p>
     * 注意只能获取boolean, int, double, String四种基本类型的变化<br>
     * 如果要获取JSONObject等其他复杂类型数据，可以监听ConfigService.ANY获取任意键值的变化
     *
     * @param key
     *            订阅的配置项的名字，也可以是ConfigService.ANY来获取任意键值的变化
     * @param l
     *            配置项发生变化时，回调的Listener
     */
    public void addListener(String key, ConfigChangeListener l) {
        synchronized (listeners) {
            ArrayList<ConfigChangeListener> list = listeners.get(key);
            if (list == null) {
                list = new ArrayList<ConfigChangeListener>();
                listeners.put(key, list);
            }
            if (!list.contains(l)) {
                list.add(l);
            }
        }
    }

    /**
     * 取消订阅名字为key的配置项的变化通知
     *
     * @param key
     *            取消订阅的配置项的名字，包括ConfigService.ANY
     *
     * @param l
     *            订阅时传入的Listener
     */
    public void removeListener(String key, ConfigChangeListener l) {
        synchronized (listeners) {
            ArrayList<ConfigChangeListener> list = listeners.get(key);
            if (list != null) {
                list.remove(l);
                if (list.isEmpty()) {
                    listeners.remove(key);
                }
            }
        }
    }

    private File getConfigDir() {
        File dir = new File(context.getFilesDir(), "config");
        if (!dir.isDirectory()) {
            dir.delete();
            dir.mkdir();
        }

        return dir;
    }

    private File getConfigFile() {
        return new File(getConfigDir(), "1"); // 1 is a base version code
    }

    // read from file, return null if fail
    @SuppressWarnings("resource")
    private JSONObject read() {
        File file = getConfigFile();
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                if (fis.available() > 1000000)
                    throw new IOException();
                byte[] buf = new byte[fis.available()];
                fis.read(buf);
                fis.close();
                String str = new String(buf, "UTF-8");
                JSONObject json = JSON.parseObject(str);
                return json;
            } catch (Exception e) {
            }
        } else {
        }
        return null;
    }

    private boolean write(JSONObject json, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.toString().getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JSONObject root() {
        if (root == null) {
            JSONObject dump = read();
            if (dump == null)
                dump = new JSONObject();
            if (root == null) {
                root = dump;
            }
        }
        return root;
    }

}
