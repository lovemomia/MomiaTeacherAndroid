package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.Student;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class StudentListItem extends LinearLayout {

    private CircularImage iconIv;
    private TextView nameTv;
    private TextView ageTv;
    private ImageView sexIv;
    private ImageView commentIv;

    public StudentListItem(Context context) {
        super(context);
    }

    public StudentListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (CircularImage) findViewById(R.id.avatar);
        iconIv.setDefaultImageResId(R.drawable.ic_default_avatar);
        nameTv = (TextView) findViewById(R.id.name);
        ageTv = (TextView) findViewById(R.id.age);
        sexIv = (ImageView) findViewById(R.id.sex);
        commentIv = (ImageView) findViewById(R.id.comment);
    }

    public static StudentListItem create(Context context) {
        return (StudentListItem) LayoutInflater.from(context).inflate(R.layout.layout_student_list_item, null);
    }

    public void setData(Student student) {
        setData(student, false);
    }

    public void setData(Student student, boolean isShowCommentStatus) {
        iconIv.setImageUrl(student.getAvatar());
        nameTv.setText(student.getName());
        ageTv.setText(student.getAge());
        if ("男".endsWith(student.getSex())) {
            sexIv.setImageResource(R.drawable.ic_boy);
        } else {
            sexIv.setImageResource(R.drawable.ic_girl);
        }
        if (isShowCommentStatus) {
            if (student.isCommented()) {
                commentIv.setImageResource(R.drawable.ic_comment);
            } else {
                commentIv.setImageResource(R.drawable.ic_uncomment);
            }
        }
    }

}
