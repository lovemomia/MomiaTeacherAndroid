package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 16/1/21.
 */
public class StudentRecordItem extends LinearLayout {

    private TextView titleTv;
    private TextView contentTv;

    public StudentRecordItem(Context context) {
        super(context);
    }

    public StudentRecordItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static StudentRecordItem create(Context context) {
        return (StudentRecordItem) LayoutInflater.from(context).inflate(R.layout.layout_list_item_student_record, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setContent(String content) {
        contentTv.setText(content);
    }
}
