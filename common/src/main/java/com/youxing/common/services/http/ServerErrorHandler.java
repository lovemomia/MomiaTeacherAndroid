package com.youxing.common.services.http;

import android.content.DialogInterface;

import com.youxing.common.app.YXActivity;
import com.youxing.common.app.YXApplication;
import com.youxing.common.services.account.AccountService;

/**
 * 服务器错误码拦截
 *
 * Created by Jun Deng on 15/8/31.
 */
public class ServerErrorHandler {

    /**
     *
     * @param errNo
     * @param errMsg
     * @return true：表示拦截到错误，false：未拦截
     */
    public boolean handlerError(int errNo, String errMsg) {
        if (errNo == 100001) {
            // token过期
            if (YXApplication.instance().currentActivity() != null) {
                final YXActivity activity = (YXActivity) YXApplication.instance().currentActivity();
                activity.showDialog(activity, errMsg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountService.instance().dispatchAccountChanged(null);
                        activity.startActivity("duola://login");
                    }
                });
            }
            return true;
        }
        return false;
    }

}
