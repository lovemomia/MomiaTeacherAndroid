package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.Student;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class GoingStudentListItem extends LinearLayout {

    private CircularImage iconIv;
    private TextView nameTv;
    private TextView ageTv;
    private ImageView sexIv;
    private Button opBtn;

    public GoingStudentListItem(Context context) {
        super(context);
    }

    public GoingStudentListItem(Context context, AttributeSet attrs) {
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
        opBtn = (Button) findViewById(R.id.student_opbtn);
    }

    public static GoingStudentListItem create(Context context) {
        return (GoingStudentListItem) LayoutInflater.from(context).inflate(R.layout.layout_student_list_item_going, null);
    }

    public void setData(Student student) {
        iconIv.setImageUrl(student.getAvatar());
        nameTv.setText(student.getName());
        ageTv.setText(student.getAge());
        if ("男".endsWith(student.getSex())) {
            sexIv.setImageResource(R.drawable.ic_boy);
        } else {
            sexIv.setImageResource(R.drawable.ic_girl);
        }
        if (student.isCheckin()) {
            opBtn.setText("课中观察");
            opBtn.setBackgroundResource(R.drawable.btn_shape_green);
        } else {
            opBtn.setText("签到");
            opBtn.setBackgroundResource(R.drawable.btn_shape_red);
        }
    }

    public Button getOpBtn() {
        return opBtn;
    }
}
