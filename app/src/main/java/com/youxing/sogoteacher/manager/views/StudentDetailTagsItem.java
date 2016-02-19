package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.app.Enviroment;
import com.youxing.common.utils.UnitTools;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.StudentRecordModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/20.
 */
public class StudentDetailTagsItem extends LinearLayout implements View.OnClickListener {

    private static final float TAG_TEXT_SIZE = 12;
    private static final int TEXT_LEFT_PADDING = 20;
    private static final int TEXT_TOP_PADDING = 12;
    private static final int PADDING = 20;

    private LinearLayout contentLay;

    private List<StudentRecordModel.StudentRecordTag> tags;

    public StudentDetailTagsItem(Context context) {
        this(context, null);
    }

    public StudentDetailTagsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static StudentDetailTagsItem create(Context context) {
        return (StudentDetailTagsItem) LayoutInflater.from(context).inflate(R.layout.layout_list_item_student_tags, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentLay = (LinearLayout) findViewById(R.id.content_lay);
    }

    public void setData(List<StudentRecordModel.StudentRecordTag> tags, boolean selectAble) {
        this.tags = tags;
        int layWidth = Enviroment.screenWidth(getContext()) - 2 * UnitTools.dip2px(getContext(), PADDING);
        int width = 0;
        LinearLayout lineLay = new LinearLayout(getContext());
        lineLay.setPadding(0, 0, 0, PADDING);
        contentLay.addView(lineLay);
        for (int i = 0; i < tags.size(); i++) {
            StudentRecordModel.StudentRecordTag tag = tags.get(i);
            if (!selectAble && !tag.isSelected()) {
                continue;
            }

            TextView tv = new TextView(getContext());
            tv.setBackgroundResource(R.drawable.bg_student_tag);
            tv.setTextSize(TAG_TEXT_SIZE);

            int textWidth = computeTextSize(tv, tag.getName()) + 2 * TEXT_LEFT_PADDING;
            width += PADDING + textWidth;
            if (width > (layWidth)) {
                lineLay = new LinearLayout(getContext());
                lineLay.setPadding(0, 0, 0, PADDING);
                contentLay.addView(lineLay);
                width = PADDING + textWidth;
            }


            setTextAppear(tv, tag.isSelected());
            LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (lineLay.getChildCount() == 0) {
                lp.setMargins(0, 0, 0, 0);
            } else {
                lp.setMargins(PADDING, 0, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setPadding(TEXT_LEFT_PADDING, TEXT_TOP_PADDING, TEXT_LEFT_PADDING, TEXT_TOP_PADDING);
            tv.setText(tag.getName());
            tv.setTag(i);
            if (selectAble) {
                tv.setOnClickListener(this);
            }
            lineLay.addView(tv);
        }
    }

    private int computeTextSize(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        return (int)textLength;
    }

    private void setTextAppear(TextView tv, boolean selected) {
        tv.setSelected(selected);
        if (selected) {
            tv.setTextColor(getResources().getColor(R.color.white));

        } else {
            tv.setTextColor(getResources().getColor(R.color.text_deep_gray));
        }
    }

    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        if (tags != null) {
            StudentRecordModel.StudentRecordTag srt = tags.get(tag);
            if (srt.isSelected()) {
                srt.setSelected(false);

            } else {
                srt.setSelected(true);
            }
            setTextAppear((TextView)v, srt.isSelected());
        }
    }
}
