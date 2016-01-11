package com.youxing.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 单位换算
 *
 * Created by Jun Deng on 15/6/4.
 */
public class UnitTools {

    public static int dip2px(Context context, float dipValue) {
        if(context == null) {
            return (int)dipValue;
        } else {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dipValue * scale + 0.5F);
        }
    }

    public static int px2dip(Context context, float pxValue) {
        if(context == null) {
            return (int)pxValue;
        } else {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int)(pxValue / scale + 0.5F);
        }
    }

    public static float px2sp(Context context, Float px) {
        if(context == null) {
            return (float)px.intValue();
        } else {
            float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
            return px.floatValue() / scaledDensity;
        }
    }

    public static float sp2px(Context context, float sp) {
        if(context == null) {
            return sp;
        } else {
            Resources r = context.getResources();
            float size = TypedValue.applyDimension(2, sp, r.getDisplayMetrics());
            return size;
        }
    }

}
