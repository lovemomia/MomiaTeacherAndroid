package com.youxing.sogoteacher.material.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.YXNetworkImageView;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.MaterialListModel;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class MaterialListItem extends LinearLayout {

    private YXNetworkImageView iconIv;
    private TextView titleTv;
    private TextView peopleTv;

    public MaterialListItem(Context context) {
        super(context);
    }

    public MaterialListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (YXNetworkImageView) findViewById(R.id.icon);
        iconIv.setDefaultImageResId(R.drawable.bg_default_image);
        titleTv = (TextView) findViewById(R.id.title);
        peopleTv = (TextView) findViewById(R.id.people);
    }

    public static MaterialListItem create(Context context) {
        return (MaterialListItem) LayoutInflater.from(context).inflate(R.layout.layout_material_list_item, null);
    }

    public void setData(MaterialListModel.Material material) {
        iconIv.setImageUrl(material.getCover());
        titleTv.setText(material.getTitle());
        peopleTv.setText(material.getSubject());
    }

}
