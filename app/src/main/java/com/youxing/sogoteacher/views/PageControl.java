package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.youxing.sogoteacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类似于iOS PageControl风格的小圆点标识组件
 *
 * Created by Jun Deng on 15/6/15.
 */
public class PageControl extends LinearLayout {

    private Context context;
    private List<ImageView> dots = new ArrayList<ImageView>();

    public PageControl(Context context) {
        this(context, null);
    }

    public PageControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setNumberOfPages(int numberOfPages) {
        dots.clear();
        removeAllViews();
        for (int i = 0; i < numberOfPages; i++) {
            ImageView dot = new ImageView(context);
            LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            dot.setLayoutParams(layoutParams);
            dots.add(dot);
            addView(dot);
        }
    }

    public void setCurrentPage(int page) {
        for (int i = 0; i < dots.size(); i++) {
            ImageView dot = dots.get(i);
            if (i == page) {
                dot.setImageResource(R.drawable.ic_page_control_dot_select);
            } else {
                dot.setImageResource(R.drawable.ic_page_control_dot_normal);
            }
        }
    }

}
