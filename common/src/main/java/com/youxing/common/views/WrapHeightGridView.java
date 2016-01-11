package com.youxing.common.views;

import android.widget.GridView;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class WrapHeightGridView extends GridView {

    public WrapHeightGridView(android.content.Context context,
                      android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
