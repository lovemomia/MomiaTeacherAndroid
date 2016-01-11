package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 *
 * Created by Jun Deng on 15/6/9.
 */
public class SectionView extends LinearLayout {

    private TextView titleView;

    public SectionView(Context context) {
        this(context, null);
    }

    public SectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static SectionView create(Context context) {
        SectionView view = (SectionView) LayoutInflater.from(context).inflate(R.layout.layout_section_text, null);
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleView = (TextView)findViewById(R.id.title);
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
    }
}
