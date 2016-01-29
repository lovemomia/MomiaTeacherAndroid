package com.youxing.sogoteacher.chat;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGFragment;
import com.youxing.sogoteacher.model.IMGroup;
import com.youxing.sogoteacher.model.IMGroupListModel;
import com.youxing.sogoteacher.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.message.InformationNotificationMessage;

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

        requestData();
        AccountService.instance().addListener(this);
    }

    private void requestData() {
        HttpService.get(Constants.domain() + "/im/user/group", null, CacheType.DISABLE, IMGroupListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                IMGroupListModel model = (IMGroupListModel) response;

                List<Group> grouplist = new ArrayList<Group>();
                for (int i = 0; i < model.getData().size(); i++) {
                    IMGroup imGroup = model.getData().get(i);
                    String id = String.valueOf(imGroup.getGroupId());
                    String name = imGroup.getGroupName();
                    grouplist.add(new Group(id, name, null));

                    RongCloudEvent.instance().getGroupCache().put(id, imGroup);
                }

                if (grouplist.size() > 0 && RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                    List<Conversation> conversations = RongIM.getInstance().getRongIMClient().getConversationList();
                    for (IMGroup group : model.getData()) {
                        boolean exist = false;
                        if (conversations != null) {
                            for (Conversation cvs : conversations) {
                                if (cvs.getConversationType() != Conversation.ConversationType.GROUP) {
                                    continue;
                                }
                                if (cvs.getTargetId().equals(String.valueOf(group.getGroupId()))) {
                                    exist = true;
                                    break;
                                }
                            }
                        }
                        if (!exist) {
                            RongIM.getInstance().getRongIMClient().insertMessage(Conversation.ConversationType.GROUP, String.valueOf(group.getGroupId()), "10000", InformationNotificationMessage.obtain("欢迎来到松果课堂~"), new RongIMClient.ResultCallback(){
                                @Override
                                public void onSuccess(Object o) {
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onRequestFailed(BaseModel error) {

            }
        });
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
