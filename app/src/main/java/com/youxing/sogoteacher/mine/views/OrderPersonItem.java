package com.youxing.sogoteacher.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.model.OrderPerson;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class OrderPersonItem extends LinearLayout implements View.OnClickListener {

    private View editView;
    private TextView nameTv;
    private TextView typeTv;
    private TextView sex;
    private View selectIv;

    private OrderPerson person;

    public OrderPersonItem(Context context) {
        super(context);
    }

    public OrderPersonItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editView = findViewById(R.id.edit);
        editView.setOnClickListener(this);
        nameTv = (TextView) findViewById(R.id.name);
        typeTv = (TextView) findViewById(R.id.type);
        sex = (TextView) findViewById(R.id.sex);
        selectIv = findViewById(R.id.select);
    }

    public static OrderPersonItem create(Context context) {
        return (OrderPersonItem) LayoutInflater.from(context).inflate(R.layout.layout_order_person_item, null);
    }

    @Override
    public void onClick(View v) {
        if (person != null) {
            ((SGActivity)getContext()).startActivityForResult("duola://orderupdateperson?personId=" + person.getId(), 1);
        }
    }

    public void setData(OrderPerson person) {
        this.person = person;
        nameTv.setText(person.getName());
        typeTv.setText(person.getType());
        sex.setText(person.getSex());
        selectIv.setVisibility(person.isSelect() ? View.VISIBLE : View.GONE);
    }
}
