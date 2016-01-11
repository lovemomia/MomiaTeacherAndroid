package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class TitleBarButton extends FrameLayout {

    private ImageView iconIv;
    private TextView textView;

    public TitleBarButton(Context context) {
        super(context);
    }

    public TitleBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (ImageView)findViewById(R.id.icon);
        textView = (TextView)findViewById(R.id.text);
    }

    public void setIcon(int resId) {
        iconIv.setImageResource(resId);
//        iconIv.setVisibility(View.VISIBLE);
//        textView.setVisibility(View.GONE);
    }

    public void setText(String text) {
        textView.setText(text);
//        textView.setVisibility(View.VISIBLE);
//        iconIv.setVisibility(View.GONE);
    }

    public ImageView getIconIv() {
        return iconIv;
    }

    public TextView getTextView() {
        return textView;
    }
}
