package com.youxing.sogoteacher.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.utils.UnitTools;
import com.youxing.sogoteacher.R;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class OrderDetailInfoView extends LinearLayout {

    public OrderDetailInfoView(Context context) {
        this(context, null);
    }

    public OrderDetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo(List<NameValuePair> infoList) {
        int padding = UnitTools.dip2px(getContext(), 10);
        this.setPadding(padding, padding, padding, padding);
        this.setOrientation(LinearLayout.VERTICAL);

        for (NameValuePair pair : infoList) {
            LinearLayout ll = new LinearLayout(getContext());
            ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            TextView nameTv = new TextView(getContext());
            nameTv.setTextSize(12);
            nameTv.setTextColor(getResources().getColor(R.color.text_light_gray));
            nameTv.setText(pair.getName());
            nameTv.setPadding(0, 0, 0, 4);
            ll.addView(nameTv);

            TextView valueTv = new TextView(getContext());
            valueTv.setTextSize(12);
            valueTv.setTextColor(getResources().getColor(R.color.text_deep_gray));
            valueTv.setText(pair.getValue());
            ll.addView(valueTv);

            addView(ll);
        }
    }

}
