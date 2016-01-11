package com.youxing.common.services.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.youxing.common.app.YXApplication;
import com.youxing.common.model.Account;
import com.youxing.common.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户服务
 *
 * Created by Jun Deng on 15/6/9.
 */
public class AccountService {

    private static final String TAG = "account";

    private Context context;
    private Account account;

    private LoginListener loginListener;

    private List<AccountChangeListener> listenerList = new ArrayList<AccountChangeListener>();

    public AccountService(Context context) {
        this.context = context;
        this.account = readAccount();
    }

    private static AccountService instance;

    public static AccountService instance() {
        if (instance == null) {
            instance = new AccountService(YXApplication.instance());
        }
        return instance;
    }

    public Account account() {
        return account;
    }

    public void login(Activity act, LoginListener listener) {
        loginListener = listener;
        act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://login")));
    }

    public void logout() {
        dispatchAccountChanged(null);
    }

    public void cancelLogin() {
        if (loginListener != null) {
            loginListener.onLoginFailed();
            loginListener = null;
        }
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return account != null;
    }

    public void dispatchAccountChanged(Account newAccount) {
        if (loginListener != null && newAccount != null) {
            loginListener.onLoginSuccess();
            loginListener = null;
        }

        this.account = newAccount;
        saveAccount(newAccount);

        for (AccountChangeListener listener : listenerList) {
            listener.onAccountChange(this);
        }
    }

    /**
     * 添加帐号状态监听
     *
     * @param listener
     */
    public void addListener(AccountChangeListener listener) {
        this.listenerList.add(listener);
    }

    /**
     * 移除帐号状态监听
     *
     * @param listener
     */
    public void removeListener(AccountChangeListener listener) {
        this.listenerList.remove(listener);
    }

    private Account readAccount() {
        SharedPreferences sp = sharedPreferences();
        String json = sp.getString("account", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Account account = null;
        try {
            account = JSON.parseObject(json, Account.class);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return account;
    }

    private void saveAccount(Account account) {
        if (account != null) {
            sharedPreferences().edit().putString("account", JSON.toJSONString(account)).commit();
        } else {
            sharedPreferences().edit().remove("account").commit();
        }
    }

    private SharedPreferences sharedPreferences() {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public interface LoginListener {
        void onLoginSuccess();
        void onLoginFailed();
    }
}
