package com.youxing.sogoteacher.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.youxing.common.app.YXFragment;
import com.youxing.sogoteacher.views.EmptyView;
import com.youxing.sogoteacher.views.ProgressHUD;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class SGFragment extends YXFragment {

    private Dialog managerDialog;

    public SGActivity getDLActivity() {
        return (SGActivity)getActivity();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    // *************** UI操作 ***************

    public void showLoadingDialog(Context context) {
        showLoadingDialog(context, null, null);
    }

    public void showLoadingDialog(Context context, String msg,
                                  DialogInterface.OnCancelListener cancelListener) {
        dismissDialog();

        managerDialog = ProgressHUD.show(context, msg, true, true, cancelListener);
    }

    public void showDialog(Context context, String message) {
        showDialog(context, null, message, "确定", null, null, null);
    }

    public void showDialog(Context context, String message, DialogInterface.OnClickListener okListener) {
        showDialog(context, null, message, "确定", okListener, null, null);
    }

    public void showDialog(Context context, String title, String message, String ok) {
        showDialog(context, title, message, ok, null, null, null);
    }

    public void showDialog(Context context, String title,
                           String message, String ok,
                           DialogInterface.OnClickListener okListener, String cancel,
                           DialogInterface.OnClickListener cancelListener) {
        dismissDialog();

        AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setTitle(title);
        dlg.setMessage(message);
        if (!TextUtils.isEmpty(ok)) {
            dlg.setButton(DialogInterface.BUTTON_POSITIVE, ok, okListener);
        }
        if (!TextUtils.isEmpty(cancel)) {
            dlg.setButton(DialogInterface.BUTTON_NEGATIVE, cancel,
                    cancelListener);
        }
        dlg.show();
        dlg.setCancelable(false);
        managerDialog = dlg;
    }

    public void dismissDialog() {
        if (managerDialog != null && managerDialog.isShowing()) {
            managerDialog.dismiss();
        }
    }

}
