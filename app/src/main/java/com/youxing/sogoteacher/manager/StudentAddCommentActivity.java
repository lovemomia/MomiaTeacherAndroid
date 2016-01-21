package com.youxing.sogoteacher.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.apply.views.ContentInputListItem;
import com.youxing.sogoteacher.manager.views.StudentDetailTagsItem;
import com.youxing.sogoteacher.manager.views.StudentListItem;
import com.youxing.sogoteacher.manager.views.StudentRecordItem;
import com.youxing.sogoteacher.model.StudentRecordModel;
import com.youxing.sogoteacher.views.InputListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/1/21.
 */
public class StudentAddCommentActivity extends SGActivity implements InputListItem.InputChangeListener, AdapterView.OnItemClickListener {

    public static final String ACTION_STUDENT_COMMENTED = "com.youxing.sogoteacher.STUDENT_COMMENTED";

    private String coid;
    private String sid;
    private String cid;

    private Adapter adapter;

    private String comment;
    private StudentRecordModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        coid = getIntent().getData().getQueryParameter("coid");
        sid = getIntent().getData().getQueryParameter("sid");
        cid = getIntent().getData().getQueryParameter("cid");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("coid", coid));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("cid", cid));
        HttpService.get(Constants.domain() + "/teacher/student/record", params, CacheType.DISABLE, StudentRecordModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                model = (StudentRecordModel)response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(StudentAddCommentActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private void submit() {
        if (!check()) {
            return;
        }
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("coid", coid));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("cid", cid));
        params.add(new BasicNameValuePair("comment", comment));
        HttpService.post(Constants.domain() + "/teacher/student/comment", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                finish();

                sendBroadcast(new Intent(ACTION_STUDENT_COMMENTED));
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(StudentAddCommentActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(comment)) {
            showDialog(StudentAddCommentActivity.this, "您还没有输入评语哦！");
            return false;
        }
        return true;
    }

    @Override
    public void onInputChanged(InputListItem inputListItem, String textNow) {
        comment = textNow;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath ip = adapter.getIndexForPosition(position);
        if (ip.section == 0) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://studentdetail?id=" +
                    model.getData().getChild().getId())));
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(StudentAddCommentActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                return 4;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 1) {
                return 2;

            } else if (section < 3) {
                return 1;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                StudentListItem listItem = StudentListItem.create(StudentAddCommentActivity.this);
                listItem.setData(model.getData().getChild());
                return listItem;

            } else if (section == 1) {
                if (row == 0) {
                    StudentDetailTagsItem listItem = StudentDetailTagsItem.create(StudentAddCommentActivity.this);
                    listItem.setData(model.getData().getTags(), false);
                    return listItem;
                } else {
                    StudentRecordItem listItem = StudentRecordItem.create(StudentAddCommentActivity.this);
                    listItem.setContent(model.getData().getContent());
                    return listItem;
                }

            } else {
                ContentInputListItem inputListItem = ContentInputListItem.create(StudentAddCommentActivity.this);
                inputListItem.setInputText(model.getData().getContent());
                inputListItem.setTitle("主教评语(500字以内)");
                inputListItem.setInputChangeListener(StudentAddCommentActivity.this);
                return inputListItem;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(StudentAddCommentActivity.this);
                int padding = UnitTools.dip2px(StudentAddCommentActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(StudentAddCommentActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("发送给家长");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
                padding = UnitTools.dip2px(StudentAddCommentActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }



}
