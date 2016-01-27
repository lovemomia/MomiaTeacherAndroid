package com.youxing.sogoteacher.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.sogoteacher.R;

/**
 * Created by Jun Deng on 15/8/24.
 */
public class MineHeaderView extends LinearLayout {

    private CircularImage avartaIv;
    private TextView nameTv;
    private TextView ageTv;

    public MineHeaderView(Context context) {
        super(context);
    }

    public MineHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static MineHeaderView create(Context context) {
        return (MineHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_mine_header, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        avartaIv = (CircularImage) findViewById(R.id.avatar);
        avartaIv.setDefaultImageResId(R.drawable.ic_default_avatar_gray);
        nameTv = (TextView) findViewById(R.id.name);
        ageTv = (TextView) findViewById(R.id.age);
    }

    public CircularImage getAvartaIv() {
        return avartaIv;
    }

    public TextView getNameTv() {
        return nameTv;
    }

    public TextView getAgeTv() {
        return ageTv;
    }
}
