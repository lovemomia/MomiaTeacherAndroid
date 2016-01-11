package com.youxing.common.views;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.youxing.common.services.image.ImageService;

/**
 * 网络图片组件
 *
 * Created by Jun Deng on 15/6/4.
 */
public class YXNetworkImageView extends NetworkImageView {

    public YXNetworkImageView(Context context) {
        this(context, (AttributeSet)null);
    }

    public YXNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YXNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置网络图片地址
     *
     * @param url
     */
    public void setImageUrl(String url) {
        super.setImageUrl(url, ImageService.getLoader());
    }
}
