package com.youxing.sogoteacher.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.chat.views.GroupMemberListItem;
import com.youxing.sogoteacher.model.IMGroupMemberModel;
import com.youxing.sogoteacher.model.User;
import com.youxing.sogoteacher.views.SectionView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imlib.model.Conversation;

/**
 * Created by Jun Deng on 16/1/28.
 */
public class GroupMemberListActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private String id;
    private Adapter adapter;
    private IMGroupMemberModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        id = getIntent().getData().getQueryParameter("id");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        HttpService.get(Constants.domain() + "/im/group/member", params, CacheType.DISABLE, IMGroupMemberModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (IMGroupMemberModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                GroupMemberListActivity.this.showDialog(GroupMemberListActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        int section = indexPath.section;
        int row = indexPath.row;

        User user;
        if (section == 0) {
            user = model.getData().getTeachers().get(row);
        } else {
            user = model.getData().getCustomers().get(row);
        }

        startConversationSystem(this, Conversation.ConversationType.PRIVATE, String.valueOf(user.getId()), user.getNickName());
    }

    public void startConversationSystem(Context context, Conversation.ConversationType conversationType, String targetId, String title) {
        if(context != null && !TextUtils.isEmpty(targetId) && conversationType != null) {
            if(RongContext.getInstance() == null) {
                throw new ExceptionInInitializerError("RongCloud SDK not init");
            } else {
                Uri uri = Uri.parse("rong://" + context.getApplicationInfo().processName).buildUpon().appendPath("conversation").appendPath(conversationType.getName().toLowerCase()).appendQueryParameter("targetId", targetId).appendQueryParameter("title", title).build();
                context.startActivity(new Intent("android.intent.action.VIEW", uri));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(GroupMemberListActivity.this);
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return model.getData().getTeachers().size();
            }
            return model.getData().getCustomers().size();
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                return 2;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            GroupMemberListItem listItem = GroupMemberListItem.create(GroupMemberListActivity.this);
            if (section == 0) {
                listItem.setData(model.getData().getTeachers().get(row));

            } else {
                listItem.setData(model.getData().getCustomers().get(row));
            }
            return listItem;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            SectionView sectionView = SectionView.create(GroupMemberListActivity.this);
            if (section == 0) {
                sectionView.setTitle("老师");

            } else {
                sectionView.setTitle("群成员");
            }
            return sectionView;
        }
    }


}
