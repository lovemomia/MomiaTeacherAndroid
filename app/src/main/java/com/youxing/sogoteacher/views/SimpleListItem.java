package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 *
 * 简单的包含title和subtitle的列表item
 *
 * Created by Jun Deng on 15/8/12.
 */
public class SimpleListItem extends LinearLayout {

    private ImageView iconIv;
    private TextView titleTv;
    private TextView subTitleTv;
    private ImageView arrowIv;

    public SimpleListItem(Context context) {
        this(context, null);
    }

    public SimpleListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static SimpleListItem create(Context context) {
        SimpleListItem view = (SimpleListItem) LayoutInflater.from(context).inflate(R.layout.layout_simple_list_item, null);
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (ImageView) findViewById(R.id.icon);
        titleTv = (TextView)findViewById(R.id.title);
        subTitleTv = (TextView) findViewById(R.id.subTitle);
        arrowIv = (ImageView) findViewById(R.id.arrow);
    }

    public void setIcon(int iconResId) {
        iconIv.setImageResource(iconResId);
        iconIv.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        this.titleTv.setText(title);
    }

    public void setSubTitle(String title) {
        this.subTitleTv.setText(title);
    }

    public void setShowArrow(boolean show) {
        this.arrowIv.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public ImageView getIconIv() {
        return iconIv;
    }

    public TextView getTitleTv() {
        return titleTv;
    }

    public TextView getSubTitleTv() {
        return subTitleTv;
    }

    public ImageView getArrowIv() {
        return arrowIv;
    }
}
