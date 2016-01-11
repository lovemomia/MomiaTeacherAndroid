package com.youxing.sogoteacher.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/27.
 */
public class FeedbackActivity extends SGActivity {

    private TextView contentTv;
    private TextView emailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        contentTv = (TextView) findViewById(R.id.content);
        emailTv = (TextView) findViewById(R.id.email);

        getTitleBar().getRightBtn().setText("提交");
        getTitleBar().getRightBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    requestData();
                }
            }
        });
    }

    private boolean check() {
        String content = contentTv.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showDialog(this, "内容不能为空");
            return false;
        }
        if (content.length() > 200) {
            showDialog(this, "内容不能多于200字");
            return false;
        }
        if (TextUtils.isEmpty(emailTv.getText().toString())) {
            showDialog(this, "联系方式不能为空");
            return false;
        }
        return true;
    }

    private void requestData() {
        showLoadingDialog(this, "正在提交，请稍候...", null);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("content", contentTv.getText().toString()));
        params.add(new BasicNameValuePair("email", emailTv.getText().toString()));
        HttpService.post(Constants.domain() + "/feedback", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                Toast.makeText(FeedbackActivity.this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(FeedbackActivity.this, error.getErrmsg());
            }
        });
    }

}
