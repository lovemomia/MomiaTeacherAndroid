package com.youxing.sogoteacher.manager;

import android.content.Context;
import android.os.Bundle;
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
import com.youxing.sogoteacher.manager.views.StudentCommentListItem;
import com.youxing.sogoteacher.manager.views.StudentListItem;
import com.youxing.sogoteacher.model.Student;
import com.youxing.sogoteacher.model.StudentDetailModel;
import com.youxing.sogoteacher.views.SectionView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/1/15.
 */
public class StudentDetailActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private String cid;

    private Adapter adapter;

    private Student student;
    private List<StudentDetailModel.StudentDetailComment> commentList = new ArrayList<>();
    private boolean isEmpty;
    private boolean isEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        cid = getIntent().getData().getQueryParameter("id");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(StudentDetailActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        if (student == null) {
            showLoadingDialog(this);
        }
        int start = commentList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cid", cid));
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        HttpService.get(Constants.domain() + "/teacher/student", params, CacheType.DISABLE, StudentDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                StudentDetailModel model = (StudentDetailModel) response;
                student = model.getData().getChild();
                commentList.addAll(model.getData().getComments().getList());
                if (model.getData().getComments().getNextIndex() == 0 || model.getData().getComments().getTotalCount() <= commentList.size()) {
                    isEnd = true;
                }
                if (commentList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                StudentDetailActivity.this.showDialog(StudentDetailActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            if (student != null) {
                return 2;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 1;
            }
            if (isEmpty) {
                return 1;
            }
            if (isEnd) {
                return commentList.size();
            }
            return commentList.size() + 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                StudentListItem listItem = StudentListItem.create(StudentDetailActivity.this);
                listItem.setData(student, false);
                return listItem;

            } else if (row < commentList.size()) {
                StudentCommentListItem listItem = StudentCommentListItem.create(StudentDetailActivity.this);
                listItem.setData(commentList.get(row));
                return listItem;

            } else if (isEmpty) {
                View view = getEmptyView("还没有评价哦~", parent, convertView);
                return view;

            } else {
                requestData();
                View view = getLoadingView(parent, convertView);
                return view;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            SectionView sv = SectionView.create(StudentDetailActivity.this);
            sv.setTitle("所获评语");
            return sv;
        }
    }
}
