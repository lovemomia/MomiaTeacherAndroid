package com.youxing.sogoteacher.app;

import com.umeng.analytics.MobclickAgent;
import com.youxing.common.app.YXFragment;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class SGFragment extends YXFragment {

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

}
