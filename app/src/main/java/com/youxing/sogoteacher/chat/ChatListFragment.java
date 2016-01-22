package com.youxing.sogoteacher.chat;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGFragment;
import com.youxing.sogoteacher.views.TitleBar;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class ChatListFragment extends SGFragment implements AccountChangeListener {

    private TitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);
        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        titleBar.getTitleTv().setText("学生群组");

        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
//                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountService.instance().addListener(this);
    }

    @Override
    public void onDestroy() {
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccountChange(AccountService service) {
    }



}
