package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/8/6.
 */
public class TitleBar extends FrameLayout {

    private TextView titleTv;
    private TitleBarButton leftBtn;
    private TitleBarButton rightBtn;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(android.R.id.title);
        leftBtn = (TitleBarButton) findViewById(R.id.title_left_btn);
        rightBtn = (TitleBarButton) findViewById(R.id.title_right_btn);
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public TitleBarButton getLeftBtn() {
        return leftBtn;
    }

    public TitleBarButton getRightBtn() {
        return rightBtn;
    }
}
