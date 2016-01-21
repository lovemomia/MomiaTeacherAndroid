package com.youxing.sogoteacher.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.manager.views.StudentListItem;
import com.youxing.sogoteacher.model.Student;
import com.youxing.sogoteacher.model.StudentListModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/1/21.
 */
public class StudentListActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private String coid;
    private String sid;
    private int status;

    private Adapter adapter;
    private StudentListModel model;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StudentAddCommentActivity.ACTION_STUDENT_COMMENTED)) {
                requestData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        coid = getIntent().getData().getQueryParameter("coid");
        sid = getIntent().getData().getQueryParameter("sid");
        status = Integer.valueOf(getIntent().getData().getQueryParameter("status"));

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(StudentAddCommentActivity.ACTION_STUDENT_COMMENTED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void requestData() {
        showLoadingDialog(this);

        String path = status == 0 ? "/teacher/course/notfinished/student" : "/teacher/course/finished/student";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("coid", coid));
        params.add(new BasicNameValuePair("sid", sid));
        HttpService.get(Constants.domain() + path, params, CacheType.DISABLE, StudentListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (StudentListModel) response;
                if (model.getData() == null || model.getData().size() == 0) {
                    showEmptyView("学生列表为空");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                StudentListActivity.this.showDialog(StudentListActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Student student = model.getData().get(position);
        if (status != 0 && !student.isCommented()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://studentaddcomment?coid=" +
                    coid + "&sid=" + sid + "&cid=" + student.getId())));

        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://studentdetail?id=" +
                    student.getId())));
        }
    }

    class Adapter extends BasicAdapter {

        public Adapter() {
        }

        @Override
        public int getCount() {
            if (model != null) {
                return model.getData().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return model.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Student student = (Student)getItem(position);
            StudentListItem listItem = StudentListItem.create(StudentListActivity.this);
            listItem.setData(student, status != 0);
            return listItem;
        }

    }
}
