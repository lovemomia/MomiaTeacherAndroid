package com.youxing.sogoteacher.app;

import android.app.ActivityManager;
import android.content.Context;

import com.youxing.common.app.MappingManager;
import com.youxing.common.app.YXApplication;
import com.youxing.sogoteacher.chat.RongCloudEvent;
import com.youxing.sogoteacher.chat.provider.MyPrivateConversationProvider;
import com.youxing.sogoteacher.chat.provider.MyTextMessageItemProvider;

import io.rong.imkit.RongIM;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class SGApplication extends YXApplication {

    @Override
    protected MappingManager mappingManager() {
        return new SGMappingManager();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initRongCloud();
    }

    private void initRongCloud() {
        /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            RongIM.init(this);

            /**
             * 融云SDK事件监听处理
             *
             * 注册相关代码，只需要在主进程里做。
             */
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
                RongCloudEvent.init();
                RongIM.getInstance().registerConversationTemplate(new MyPrivateConversationProvider());
                RongIM.getInstance().registerMessageTemplate(new MyTextMessageItemProvider());
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
