package com.youxing.common.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.gridlayout.BuildConfig;
import android.text.TextUtils;

import com.github.mmin18.layoutcast.LayoutCast;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.config.ConfigService;
import com.youxing.common.utils.Log;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class YXApplication extends Application {

    private static YXApplication instance;

    public static YXApplication instance() {
        if (instance == null) {
            throw new IllegalStateException("Application has not been created");
        }

        return instance;
    }

    static YXApplication _instance() {
        return instance;
    }

    public YXApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
    }

    // **** Application Life Circle **** //

    /**
     * Application的Activity堆栈第一次不为空时调用
     * <p>
     * 在第一个Activity.onCreate()调用之前被调用
     */
    public void onApplicationStart() {
        Log.i("application", "onApplicationStart");

        // config
        ConfigService.instance().refresh();
    }

    /**
     * Application从后台唤醒到前台时调用
     * <p>
     * onApplicationStart<br>
     * |-onApplicationResume<br>
     * |-onApplicationPause<br>
     * onApplicationStop
     */
    public void onApplicationResume() {
        Log.i("application", "onApplicationResume");
    }

    /**
     * Application从前台置为后台时调用，比如按Home键
     * <p>
     * onApplicationStart<br>
     * |-onApplicationResume<br>
     * |-onApplicationPause<br>
     * onApplicationStop
     */
    public void onApplicationPause() {
        Log.i("application", "onApplicationPause");
    }

    /**
     * Application的Activity堆栈第一次为空时调用
     * <p>
     * 在最后一个Activity.onDestory()调用之后被调用 <br>
     * 按Home键返回或被其他应用覆盖不会触发<br>
     * 只有一直按Back退出才会触发
     */
    public void onApplicationStop() {
        Log.i("application", "onApplicationStop");
    }

    private static int liveCounter;
    private static int activeCounter;
    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if ((--liveCounter) == 0) {
                    YXApplication.instance().onApplicationStop();
                }
            }
            if (msg.what == 2) {
                // skip a event loop in case onPause or onDestory takes too long
                sendEmptyMessageDelayed(3, 100);
            }
            if (msg.what == 3) {
                if ((--activeCounter) == 0) {
                    YXApplication.instance().onApplicationPause();
                }
            }
        }
    };

    private Activity currentAct;

    public Activity currentActivity() {
        return currentAct;
    }

    public void activityOnCreate(Activity a) {
        if (liveCounter++ == 0) {
            onApplicationStart();
        }
        currentAct = a;
    }

    public void activityOnResume(Activity a) {
        if (activeCounter++ == 0) {
            onApplicationResume();
        }
        currentAct = a;
    }

    public void activityOnPause(Activity a) {
        handler.sendEmptyMessage(2);
        currentAct = null;
    }

    public void activityOnDestory(Activity a) {
        handler.sendEmptyMessage(1);
        currentAct = null;
    }

    // **** Application Life Circle END **** //

    public Intent urlMap(Intent intent) {
        do {
            Uri uri = intent.getData();
            if (uri == null) {
                break;
            }
            if (uri.getScheme() == null || !"duola".equals(uri.getScheme())) {
                break;
            }

            MappingManager mm = mappingManager();
            if (mm == null) {
                break;
            }

            String host = uri.getHost();
            if (TextUtils.isEmpty(host))
                break;

            host = host.toLowerCase();
            MappingManager.MappingPage page = mm.getPage(host);
            if (page == null) {
                break;
            }

            if (page.needLogin && !AccountService.instance().isLogin()) {
                Intent loginIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("duola://login"));
                loginIntent.putExtra("_destination", uri.toString());
                intent = loginIntent;
            }

        } while (false);

        return intent;
    }

    protected MappingManager mappingManager() {
        return new MappingManager();
    }
}
