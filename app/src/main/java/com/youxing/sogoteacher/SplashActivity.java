package com.youxing.sogoteacher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.youxing.common.services.account.AccountService;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AccountService.instance().isLogin()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://home")));
                    finish();
                } else {
                    AccountService.instance().login(SplashActivity.this, new AccountService.LoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://home?isFirstLogin=true")));
                        }

                        @Override
                        public void onLoginFailed() {
                        }
                    });
                    finish();
                }
            }
        }, 2000);
    }

}
