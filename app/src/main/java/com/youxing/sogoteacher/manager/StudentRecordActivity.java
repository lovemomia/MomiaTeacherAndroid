package com.youxing.sogoteacher.manager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
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
import com.youxing.sogoteacher.model.StudentRecordModel;
import com.youxing.sogoteacher.views.InputListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/1/15.
 */
public class StudentRecordActivity extends SGActivity implements InputListItem.InputChangeListener {

    private String coid;
    private String sid;
    private String cid;

    private Adapter adapter;

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

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
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
                showDialog(StudentRecordActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
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

        Map<String, Object> recordMap = new HashMap<String, Object>();
        recordMap.put("content", model.getData().getContent());
        List<Long> tagsList = new ArrayList<Long>();
        for (StudentRecordModel.StudentRecordTag tag : model.getData().getTags()) {
            if (tag.isSelected()) {
                tagsList.add(tag.getId());
            }
        }
        recordMap.put("tags", tagsList);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("coid", coid));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("cid", cid));
        params.add(new BasicNameValuePair("record", JSON.toJSONString(recordMap)));
        HttpService.post(Constants.domain() + "/teacher/student/record", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(StudentRecordActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(model.getData().getContent())) {
            showDialog(StudentRecordActivity.this, "请输入记录内容");
            return false;
        }
        return true;
    }

    @Override
    public void onInputChanged(InputListItem inputListItem, String textNow) {
        model.getData().setContent(textNow);
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(StudentRecordActivity.this);
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
            if (section < 3) {
                return 1;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                StudentListItem listItem = StudentListItem.create(StudentRecordActivity.this);
                listItem.setData(model.getData().getChild());
                return listItem;

            } else if (section == 1) {
                StudentDetailTagsItem listItem = StudentDetailTagsItem.create(StudentRecordActivity.this);
                listItem.setData(model.getData().getTags(), true);
                return listItem;

            } else {
                ContentInputListItem inputListItem = ContentInputListItem.create(StudentRecordActivity.this);
                inputListItem.setInputText(model.getData().getContent());
                inputListItem.setTitle("课堂记事");
                inputListItem.setInputChangeListener(StudentRecordActivity.this);
                return inputListItem;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(StudentRecordActivity.this);
                int padding = UnitTools.dip2px(StudentRecordActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(StudentRecordActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("提交");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
                padding = UnitTools.dip2px(StudentRecordActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }


}
