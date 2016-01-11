package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class EmptyView extends LinearLayout {

    private TextView messageTv;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        messageTv = (TextView) findViewById(R.id.message);
    }

    public static EmptyView create(Context context) {
        return (EmptyView) LayoutInflater.from(context).inflate(R.layout.layout_empty, null);
    }

    public void setMessage(String message) {
        messageTv.setText(message);
    }
}
