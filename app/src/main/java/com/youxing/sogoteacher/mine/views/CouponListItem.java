package com.youxing.sogoteacher.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.Coupon;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class CouponListItem extends LinearLayout {

    private TextView priceTv;
    private TextView titleTv;
    private TextView descTv;
    private TextView dateTv;
    private ImageView tagIv;

    public CouponListItem(Context context) {
        super(context);
    }

    public CouponListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CouponListItem create(Context context) {
        return (CouponListItem) LayoutInflater.from(context).inflate(R.layout.layout_coupon_list_item, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        priceTv = (TextView) findViewById(R.id.price);
        titleTv = (TextView) findViewById(R.id.title);
        descTv = (TextView) findViewById(R.id.desc);
        dateTv = (TextView) findViewById(R.id.date);
        tagIv = (ImageView) findViewById(R.id.tag);
    }

    public void setData(Coupon coupon) {
        priceTv.setText("￥" + coupon.getDiscount());
        titleTv.setText(coupon.getTitle());
        descTv.setText(coupon.getDesc());
        dateTv.setText(coupon.getStartTime() + "至" + coupon.getEndTime() + "有效");
        if (coupon.getStatus() == 1) {
            tagIv.setImageResource(R.drawable.ic_coupon_tag_green);
        } else {
            tagIv.setImageResource(R.drawable.ic_coupon_tag_gray);
        }
    }
}
