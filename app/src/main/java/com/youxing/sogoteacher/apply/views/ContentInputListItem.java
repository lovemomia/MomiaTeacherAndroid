package com.youxing.sogoteacher.apply.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.views.InputListItem;

/**
 * Created by Jun Deng on 15/6/12.
 */
public class ContentInputListItem extends InputListItem {

    public ContentInputListItem(Context context) {
        this(context, null);
    }

    public ContentInputListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ContentInputListItem create(Context context) {
        return (ContentInputListItem)LayoutInflater.from(context).inflate(R.layout.layout_list_item_content_input, null);
    }

}
