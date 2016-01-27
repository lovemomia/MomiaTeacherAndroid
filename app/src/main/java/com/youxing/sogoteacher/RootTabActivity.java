package com.youxing.sogoteacher;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.chat.ChatListFragment;
import com.youxing.sogoteacher.manager.CourseManagerFragment;
import com.youxing.sogoteacher.material.TeachMaterialFragment;
import com.youxing.sogoteacher.mine.MineFragment;
import com.youxing.sogoteacher.model.IMTokenModel;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class RootTabActivity extends SGActivity {

    private FragmentTabHost tabHost;

    private boolean isFirstLogin;

    @Override
    protected int titleFeatureId() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabHost.addTab(
                tabHost.newTabSpec("manager").setIndicator(
                        createTabItem("课程管理",
                                R.drawable.ic_tab_manager)), CourseManagerFragment.class,
                null);
        tabHost.addTab(
                tabHost.newTabSpec("chatlist").setIndicator(
                        createTabItem("学生群组",
                                R.drawable.ic_tab_group)), ChatListFragment.class,
                null);
        tabHost.addTab(
                tabHost.newTabSpec("material").setIndicator(
                        createTabItem("教材教具",
                                R.drawable.ic_tab_material)), TeachMaterialFragment.class,
                null);
        tabHost.addTab(
                tabHost.newTabSpec("mine").setIndicator(
                        createTabItem("我的",
                                R.drawable.ic_tab_mine)),
                MineFragment.class, null);

        // Umeng
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.silentUpdate(this);

        // RongCloud
        doRCIMConnect(3);

        isFirstLogin = Boolean.valueOf(getIntent().getData().getQueryParameter("isFirstLogin"));
        if (isFirstLogin) {
            startActivity("sgteacher://applyteacher?fromLogin=true");
        }
    }

    private View createTabItem(String name, int iconRes) {
        View root = getLayoutInflater().inflate(R.layout.layout_tab_item, null);
        ImageView iconView = (ImageView) root.findViewById(R.id.tab_icon);
        iconView.setImageResource(iconRes);

        TextView nameView = (TextView) root.findViewById(R.id.tab_text);
        nameView.setText(name);

        return root;
    }

    private long mPrevbackPress = -1;

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - mPrevbackPress <= 1000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次返回退出松果亲子", Toast.LENGTH_SHORT).show();
        }
        mPrevbackPress = t;
    }

    private void doRCIMConnect(final int tryTime) {
        String imToken = AccountService.instance().account().getImToken();
        if (imToken == null) {
            return;
        }

        RongIM.connect(AccountService.instance().account().getImToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token

                if (tryTime > 0) {
                    HttpService.post(Constants.domain() + "/im/token", null, IMTokenModel.class, new RequestHandler() {
                        @Override
                        public void onRequestFinish(Object response) {
                            IMTokenModel model = (IMTokenModel) response;
                            AccountService.instance().account().setImToken(model.getData());
                            doRCIMConnect(tryTime - 1);
                        }

                        @Override
                        public void onRequestFailed(BaseModel error) {

                        }
                    });
                }
            }

            @Override
            public void onSuccess(String userId) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }
}
