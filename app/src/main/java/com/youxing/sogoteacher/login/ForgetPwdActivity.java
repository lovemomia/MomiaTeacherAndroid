package com.youxing.sogoteacher.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.model.AccountModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class ForgetPwdActivity extends SGActivity implements View.OnClickListener {

    private EditText phoneEdit;
    private EditText pwdEdit;
    private EditText codeEdit;
    private Button codeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        phoneEdit = (EditText) findViewById(R.id.forgetpwd_phone_edit);
        pwdEdit = (EditText) findViewById(R.id.forgetpwd_pwd_edit);
        codeEdit = (EditText) findViewById(R.id.forgetpwd_code_edit);

        findViewById(R.id.forgetpwd_btn).setOnClickListener(this);
        codeBtn = (Button) findViewById(R.id.forgetpwd_get_code_btn);
        codeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.forgetpwd_btn) {
            if (check()) {
                requestForgetpwd();
            }

        } else if (v.getId() == R.id.forgetpwd_get_code_btn) {
            if (checkPhone()) {
                requestVercode();
            }
        }
    }

    private boolean check() {
        if (phoneEdit.getText().toString().trim().length() == 0) {
            showDialog(this, "手机号不能为空");
            return false;
        }
        if (pwdEdit.getText().toString().trim().length() == 0) {
            showDialog(this, "密码不能为空");
            return false;
        }
        if (codeEdit.getText().toString().trim().length() == 0) {
            showDialog(this, "验证码不能为空");
            return false;
        }
        return true;
    }

    private boolean checkPhone() {
        if (phoneEdit.getText().toString().trim().length() == 0) {
            showDialog(this, "手机号不能为空");
            return false;
        }
        return true;
    }

    private void requestVercode() {
        showLoadingDialog(this, "正在获取验证码，请稍候...", null);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobile", phoneEdit.getText().toString().trim()));
//        params.add(new BasicNameValuePair("type", "forgetpwd"));

        HttpService.post(Constants.domain() + "/auth/send", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                codeBtn.setEnabled(false);
                codeBtn.setText("60s");
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        codeBtn.setText(millisUntilFinished / 1000 + "s");
                    }

                    public void onFinish() {
                        codeBtn.setText("获取");
                        codeBtn.setEnabled(true);
                    }
                }.start();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(ForgetPwdActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestForgetpwd() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mobile", phoneEdit.getText().toString().trim()));
        params.add(new BasicNameValuePair("password", pwdEdit.getText().toString().trim()));
        params.add(new BasicNameValuePair("code", codeEdit.getText().toString().trim()));

        HttpService.post(Constants.domain() + "/auth/password", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                AccountService.instance().dispatchAccountChanged(model.getData());

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(ForgetPwdActivity.this, error.getErrmsg());
            }
        });
    }



}
