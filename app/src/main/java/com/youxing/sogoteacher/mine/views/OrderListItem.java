package com.youxing.sogoteacher.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.Order;
import com.youxing.sogoteacher.utils.PriceUtils;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderListItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView priceTv;
    private TextView peopleTv;

    public OrderListItem(Context context) {
        super(context);
    }

    public OrderListItem(Context context, AttributeSet attrs) {
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

    public static OrderListItem create(Context context) {
        return (OrderListItem) LayoutInflater.from(context).inflate(R.layout.layout_order_list_item, null);
    }

    public void setData(Order order) {
        iconIv.setImageUrl(order.getCover());
        titleTv.setText(order.getTitle());
        priceTv.setText("总价：￥" + PriceUtils.formatPriceString(order.getTotalFee()));
        peopleTv.setText("人数：" + order.getParticipants());
    }

}
