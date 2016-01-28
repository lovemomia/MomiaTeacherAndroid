package com.youxing.sogoteacher.chat.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxing.common.views.CircularImage;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.User;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class GroupMemberListItem extends LinearLayout {

    private CircularImage iconIv;
    private TextView nameTv;

    public GroupMemberListItem(Context context) {
        super(context);
    }

    public GroupMemberListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconIv = (CircularImage) findViewById(R.id.avatar);
        iconIv.setDefaultImageResId(R.drawable.ic_default_avatar);
        nameTv = (TextView) findViewById(R.id.name);
    }

    public static GroupMemberListItem create(Context context) {
        return (GroupMemberListItem) LayoutInflater.from(context).inflate(R.layout.layout_group_member_list_item, null);
    }

    public void setData(User user) {
        iconIv.setImageUrl(user.getAvatar());
        nameTv.setText(user.getNickName());
    }

}
