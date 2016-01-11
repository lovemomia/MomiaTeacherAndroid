package com.youxing.sogoteacher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/6/12.
 */
public class InputListItem extends LinearLayout {

    private TextView titleTv;
    private EditText inputEt;

    public InputListItem(Context context) {
        this(context, null);
    }

    public InputListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static InputListItem create(Context context) {
        return (InputListItem)LayoutInflater.from(context).inflate(R.layout.layout_list_item_input, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTv = (TextView)findViewById(R.id.title);
        inputEt = (EditText)findViewById(R.id.input);
    }

}
