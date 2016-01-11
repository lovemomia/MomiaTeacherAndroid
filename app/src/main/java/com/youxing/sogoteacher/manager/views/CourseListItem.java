package com.youxing.sogoteacher.manager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.Course;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class CourseListItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView priceTv;
    private TextView peopleTv;

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
    }

    public static CourseListItem create(Context context) {
        return (CourseListItem) LayoutInflater.from(context).inflate(R.layout.layout_course_list_item, null);
    }

    public void setData(Course course) {
        iconIv.setImageUrl(course.getCover());
        titleTv.setText(course.getTitle());
        priceTv.setText(course.getScheduler());
        peopleTv.setText(course.getAddress());
    }

}
