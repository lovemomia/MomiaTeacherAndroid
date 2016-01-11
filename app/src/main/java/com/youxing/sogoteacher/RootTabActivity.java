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
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.chat.ChatListFragment;
import com.youxing.sogoteacher.manager.CourseManagerFragment;
import com.youxing.sogoteacher.material.TeachMaterialFragment;
import com.youxing.sogoteacher.mine.MineFragment;

public class RootTabActivity extends SGActivity {

    private FragmentTabHost tabHost;

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
                                R.drawable.ic_tab_home)), CourseManagerFragment.class,
                null);
        tabHost.addTab(
                tabHost.newTabSpec("chatlist").setIndicator(
                        createTabItem("学生群组",
                                R.drawable.ic_tab_home)), ChatListFragment.class,
                null);
        tabHost.addTab(
                tabHost.newTabSpec("material").setIndicator(
                        createTabItem("教材教具",
                                R.drawable.ic_tab_home)), TeachMaterialFragment.class,
                null);
        tabHost.addTab(
                tabHost.newTabSpec("mine").setIndicator(
                        createTabItem("我的",
                                R.drawable.ic_tab_mine)),
                MineFragment.class, null);

        // Umeng
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.silentUpdate(this);
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
}
