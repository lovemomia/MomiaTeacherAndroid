package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.youxing.sogoteacher.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/1/14.
 */
public class TwoLevelWheelView extends LinearLayout implements AbstractWheelPicker.OnWheelChangeListener {

    private WheelCurvedPicker leftWhell;
    private WheelCurvedPicker rightWhell;

    private List<Map> data;
    private String leftSelectData;
    private String rightSelectData;

    public TwoLevelWheelView(Context context) {
        super(context);
    }

    public TwoLevelWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static TwoLevelWheelView create(Context context) {
        return (TwoLevelWheelView) LayoutInflater.from(context).inflate(R.layout.layout_two_level_wheel, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.leftWhell = (WheelCurvedPicker) findViewById(R.id.wheel_view_left);
        this.rightWhell = (WheelCurvedPicker) findViewById(R.id.wheel_view_right);

        leftWhell.setTextColor(getResources().getColor(R.color.text_light_gray));
        leftWhell.setCurrentTextColor(getResources().getColor(R.color.text_deep_gray));
        leftWhell.setItemSpace(20);
        leftWhell.setTextSize(36);

        rightWhell.setTextColor(getResources().getColor(R.color.text_light_gray));
        rightWhell.setCurrentTextColor(getResources().getColor(R.color.text_deep_gray));
        rightWhell.setItemSpace(20);
        rightWhell.setTextSize(36);
    }

    public void setData(List<Map> data) {
        this.data = data;
        List<String> leftList = new ArrayList<String>();
        for (Map map: data) {
            String key = (String)map.keySet().iterator().next();
            leftList.add(key);

        }
        leftWhell.setOnWheelChangeListener(this);
        leftWhell.setData(leftList);
    }

    public String getLeftSelectData() {
        return leftSelectData;
    }

    public String getRightSelectData() {
        return rightSelectData;
    }

    @Override
    public void onWheelScrolling(float deltaX, float deltaY) {
    }

    @Override
    public void onWheelSelected(int index, String data) {
        if (this.data != null) {
            this.leftSelectData = data;
            Map map = this.data.get(index);
            List<String> rightList = (List<String>)map.values().iterator().next();
            rightWhell.setData(rightList);
            rightWhell.setItemIndex(0);
            rightWhell.invalidate();
            rightWhell.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
                @Override
                public void onWheelScrolling(float deltaX, float deltaY) {

                }

                @Override
                public void onWheelSelected(int index, String data) {
                    TwoLevelWheelView.this.rightSelectData = data;
                }

                @Override
                public void onWheelScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onWheelScrollStateChanged(int state) {
    }
}
