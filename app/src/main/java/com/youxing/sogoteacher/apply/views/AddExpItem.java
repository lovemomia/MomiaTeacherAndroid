package com.youxing.sogoteacher.apply.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 16/1/12.
 */
public class AddExpItem extends LinearLayout {

    private TextView titleTv;

    public AddExpItem(Context context) {
        this(context, null);
    }

    public AddExpItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static AddExpItem create(Context context) {
        return (AddExpItem) LayoutInflater.from(context).inflate(R.layout.layout_applyteacher_addexp, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.titleTv = (TextView) findViewById(R.id.title);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }
}
