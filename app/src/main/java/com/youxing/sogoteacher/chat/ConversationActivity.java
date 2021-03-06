package com.youxing.sogoteacher.chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.chat.views.GroupNoticeView;

import java.util.Locale;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Jun Deng on 16/1/21.
 */
public class ConversationActivity extends SGActivity {

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();

        getIntentDate(intent);

        if (mConversationType == Conversation.ConversationType.GROUP) {
            getTitleBar().getRightBtn().setIcon(R.drawable.ic_action_group);
            getTitleBar().getRightBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity("sgteacher://groupmember?id=" + mTargetId);
                }
            });
            getTitleBar().getRightBtn2().setIcon(R.drawable.ic_action_notice);
            getTitleBar().getRightBtn2().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(ConversationActivity.this).create();
                    dialog.show();
                    GroupNoticeView noticeView = GroupNoticeView.create(ConversationActivity.this);
                    noticeView.setData(RongCloudEvent.instance().getGroupCache().get(mTargetId));
                    dialog.getWindow().setContentView(noticeView);
                }
            });
        }
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        String title = intent.getData().getQueryParameter("title");
        setTitle(title);

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

}
