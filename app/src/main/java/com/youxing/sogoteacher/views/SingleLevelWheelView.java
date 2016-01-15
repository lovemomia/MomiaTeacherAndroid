package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.youxing.sogoteacher.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/1/14.
 */
public class SingleLevelWheelView extends LinearLayout implements AbstractWheelPicker.OnWheelChangeListener {

    private WheelCurvedPicker whell;

    private String selectData;

    public SingleLevelWheelView(Context context) {
        super(context);
    }

    public SingleLevelWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static SingleLevelWheelView create(Context context) {
        return (SingleLevelWheelView) LayoutInflater.from(context).inflate(R.layout.layout_single_level_wheel, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.whell = (WheelCurvedPicker) findViewById(R.id.wheel_view);

        whell.setTextColor(getResources().getColor(R.color.text_light_gray));
        whell.setCurrentTextColor(getResources().getColor(R.color.text_deep_gray));
        whell.setItemSpace(20);
        whell.setTextSize(36);
    }

    public void setItemIndex(int index) {
        whell.setItemIndex(index);
    }

    public void setData(List<String> data) {
        whell.setOnWheelChangeListener(this);
        whell.setData(data);
    }

    public String getSelectData() {
        return selectData;
    }

    @Override
    public void onWheelScrolling(float deltaX, float deltaY) {
    }

    @Override
    public void onWheelSelected(int index, String data) {
        this.selectData = data;
    }

    @Override
    public void onWheelScrollStateChanged(int state) {
    }
}
