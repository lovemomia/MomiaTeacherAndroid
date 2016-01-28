package com.youxing.sogoteacher.chat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.IMGroup;
import com.youxing.sogoteacher.model.User;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class GroupNoticeView extends LinearLayout {

    private TextView timeTv;
    private TextView addressTv;
    private TextView routeTv;
    private TextView noticeTv;

    public GroupNoticeView(Context context) {
        super(context);
    }

    public GroupNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        timeTv = (TextView) findViewById(R.id.time);
        addressTv = (TextView) findViewById(R.id.address);
        routeTv = (TextView) findViewById(R.id.route);
        noticeTv = (TextView) findViewById(R.id.notice);
    }

    public static GroupNoticeView create(Context context) {
        return (GroupNoticeView) LayoutInflater.from(context).inflate(R.layout.layout_group_notice, null);
    }

    public void setData(IMGroup group) {
        if (group == null) {
            return;
        }
        timeTv.setText(group.getTime());
        addressTv.setText(group.getAddress());
        routeTv.setText(group.getRoute());
        noticeTv.setText(group.getTips());
    }

}
