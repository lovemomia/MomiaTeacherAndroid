package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.Course;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class CourseListItem extends FrameLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView priceTv;
    private TextView peopleTv;
    private ImageView commentIv;

    public CourseListItem(Context context) {
        super(context);
    }

    public CourseListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        iconIv.setDefaultImageResId(R.drawable.bg_default_image);
        titleTv = (TextView) findViewById(R.id.title);
        priceTv = (TextView) findViewById(R.id.price);
        peopleTv = (TextView) findViewById(R.id.people);
        commentIv = (ImageView) findViewById(R.id.comment_status);
    }

    public static CourseListItem create(Context context) {
        return (CourseListItem) LayoutInflater.from(context).inflate(R.layout.layout_course_list_item, null);
    }

    public void setData(Course course) {
        setData(course, false);
    }

    public void setData(Course course, boolean showCommentIv) {
        iconIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        priceTv.setText(course.getScheduler());
        peopleTv.setText(course.getAddress());
        if (showCommentIv) {
            commentIv.setVisibility(View.VISIBLE);
            if (course.isCommented()) {
                commentIv.setImageResource(R.drawable.ic_comment);
            } else {
                commentIv.setImageResource(R.drawable.ic_uncomment);
            }
        } else {
            commentIv.setVisibility(View.GONE);
        }
    }

}
