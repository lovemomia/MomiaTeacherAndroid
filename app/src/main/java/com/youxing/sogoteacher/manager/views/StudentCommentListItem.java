package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.StudentDetailModel;

/**
 * Created by Jun Deng on 16/1/18.
 */
public class StudentCommentListItem extends LinearLayout {

    private TextView dateTv;
    private TextView titleTv;
    private TextView contentTv;
    private TextView teacherTv;

    public StudentCommentListItem(Context context) {
        super(context);
    }

    public StudentCommentListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateTv = (TextView) findViewById(R.id.date);
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
        teacherTv = (TextView) findViewById(R.id.teacher);
    }

    public static StudentCommentListItem create(Context context) {
        return (StudentCommentListItem) LayoutInflater.from(context).inflate(R.layout.layout_student_comment_list_item, null);
    }

    public void setData(StudentDetailModel.StudentDetailComment comment) {
        dateTv.setText(comment.getDate());
        titleTv.setText(comment.getTitle());
        contentTv.setText(comment.getContent());
        teacherTv.setText(comment.getTeacher());
    }

}
