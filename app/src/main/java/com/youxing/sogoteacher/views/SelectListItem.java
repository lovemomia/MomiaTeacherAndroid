package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/6/12.
 */
public class SelectListItem extends LinearLayout {

    private TextView titleTv;
    private TextView subTitleTv;

    public SelectListItem(Context context) {
        this(context, null);
    }

    public SelectListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static SelectListItem create(Context context) {
        return (SelectListItem) LayoutInflater.from(context).inflate(R.layout.layout_list_item_select, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView)findViewById(R.id.title);
        subTitleTv = (TextView)findViewById(R.id.subTitle);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setSubTitle(String subTitle) {
        subTitleTv.setText(subTitle);
    }

}
