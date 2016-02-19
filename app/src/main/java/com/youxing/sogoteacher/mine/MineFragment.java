package com.youxing.sogoteacher.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.model.Account;
import com.youxing.common.services.account.AccountChangeListener;
import com.youxing.common.services.account.AccountService;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGFragment;
import com.youxing.sogoteacher.mine.views.MineHeaderView;
import com.youxing.sogoteacher.views.SimpleListItem;
import com.youxing.sogoteacher.views.TitleBar;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by Jun Deng on 15/8/3.
 */
public class MineFragment extends SGFragment implements AdapterView.OnItemClickListener, AccountChangeListener {

    private TitleBar titleBar;
    private ListView listView;
    private Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, null);
        titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
        listView = (ListView)rootView.findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleBar.getTitleTv().setText("我的");

        AccountService.instance().addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        AccountService.instance().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccountChange(AccountService service) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        int section = indexPath.section;
        int row = indexPath.row;

        if (section == 0) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://personinfo")));
        } else if (section == 1) {
            if (row == 0) {
                // 成为助教
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://applyteacher")));
            }
        } else if (section == 2) {
            if (row == 0) {
                // 意见反馈
//                startActivity("sgteacher://feedback");
                startConversationSystem(getActivity(), Conversation.ConversationType.SYSTEM, "10000", "系统消息");
            } else {
                startActivity("sgteacher://about");
            }
        }
    }

    public void startConversationSystem(Context context, Conversation.ConversationType conversationType, String targetId, String title) {
        if(context != null && !TextUtils.isEmpty(targetId) && conversationType != null) {
            if(RongContext.getInstance() == null) {
                throw new ExceptionInInitializerError("RongCloud SDK not init");
            } else {
                Uri uri = Uri.parse("rong://" + context.getApplicationInfo().processName + ".system").buildUpon().appendPath("conversation").appendPath(conversationType.getName().toLowerCase()).appendQueryParameter("targetId", targetId).appendQueryParameter("title", title).build();
                context.startActivity(new Intent("android.intent.action.VIEW", uri));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(getDLActivity());
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0 || section == 1) {
                return 1;
            }
            return 2;
        }

        @Override
        public int getSectionCount() {
            return 3;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                if (AccountService.instance().isLogin()) {
                    MineHeaderView headerView = MineHeaderView.create(getActivity());
                    Account account = AccountService.instance().account();
                    headerView.getAvartaIv().setImageUrl(account.getAvatar());
                    headerView.getNameTv().setText(account.getNickName());
                    headerView.getAgeTv().setText( account.getMobile());
                    view = headerView;

                } else {
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_mine_header_not_login, null);
                    view.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("duola://login"));
                            startActivity(intent);
                        }
                    });
                }

            } else {
                SimpleListItem simpleListItem = SimpleListItem.create(getActivity());
                simpleListItem.setShowArrow(true);
                if (section == 1) {
                    if (row == 0) {
                        simpleListItem.setTitle("成为助教");
                        simpleListItem.setIcon(R.drawable.ic_mine_order);

                    }
                } else if (section == 2) {
                    if (row == 0) {
                        simpleListItem.setTitle("系统消息");
                        simpleListItem.setIcon(R.drawable.ic_mine_feedback);
                        int unreadSys = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.SYSTEM);
                        if (unreadSys > 0) {
                            simpleListItem.getDotView().setVisibility(View.VISIBLE);
                        } else {
                            simpleListItem.getDotView().setVisibility(View.GONE);
                        }

                    } else {
                        simpleListItem.setTitle("关于我们");
                        simpleListItem.setIcon(R.drawable.ic_mine_setting);
                    }
                }
                view = simpleListItem;
            }
            return view;
        }

    }
}
