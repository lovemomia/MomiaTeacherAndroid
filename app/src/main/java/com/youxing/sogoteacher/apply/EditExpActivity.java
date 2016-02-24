package com.youxing.sogoteacher.apply;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.apply.views.ContentInputListItem;
import com.youxing.sogoteacher.model.Experience;
import com.youxing.sogoteacher.model.ExperienceModel;
import com.youxing.sogoteacher.views.InputListItem;
import com.youxing.sogoteacher.views.SimpleListItem;
import com.youxing.sogoteacher.views.TwoLevelWheelView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑工作经历
 *
 * Created by Jun Deng on 16/1/12.
 */
public class EditExpActivity extends SGActivity implements AdapterView.OnItemClickListener, InputListItem.InputChangeListener {

    private Adapter adapter;

    private Experience model;
    private boolean canBeRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        model = getIntent().getParcelableExtra("exp");
        if (model == null) {
            model = new Experience();
        } else {
            canBeRemove = true;
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        getTitleBar().getRightBtn().setText("保存");
        getTitleBar().getRightBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        if (!check()) {
            return;
        }
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("experience", JSON.toJSONString(model)));
        HttpService.post(Constants.domain() + "/teacher/experience", params, ExperienceModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                ExperienceModel model = (ExperienceModel)response;
                Intent data = new Intent();
                data.putExtra("exp", model.getData());
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(EditExpActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private void remove() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", String.valueOf(model.getId())));
        HttpService.post(Constants.domain() + "/teacher/experience/delete", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                Intent data = new Intent();
                data.putExtra("exp", model);
                data.putExtra("remove", true);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(EditExpActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(model.getSchool())) {
            showDialog(EditExpActivity.this, "学校不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getPost())) {
            showDialog(EditExpActivity.this, "岗位不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getTime())) {
            showDialog(EditExpActivity.this, "在职时间不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getContent())) {
            showDialog(EditExpActivity.this, "工作内容不能为空");
            return false;
        }
        return true;
    }

    private void chooseTime() {
        final TwoLevelWheelView wheelView = TwoLevelWheelView.create(EditExpActivity.this);

        List<Map> data = new ArrayList<Map>();
        List<String> list = Arrays.asList("2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009",
                "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000");
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            List<String> value = new ArrayList<String>();
            value.add("至今");
            for (int j = 0; j <= i; j++) {
                value.add(list.get(j));
            }
            Map<String, List> map = new HashMap<String, List>();
            map.put(key, value);
            data.add(map);
        }
        wheelView.setData(data);

        new AlertDialog.Builder(this)
                .setTitle("选择一个时间段")
                .setView(wheelView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.setTime(wheelView.getLeftSelectData() + " - " + wheelView.getRightSelectData());
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == 2) {
            chooseTime();
        }
    }

    @Override
    public void onInputChanged(InputListItem inputListItem, String textNow) {
        int row = (Integer) inputListItem.getTag();
        if (row == 0) {
            model.setSchool(textNow);
        } else if (row == 1) {
            model.setPost(textNow);
        } else {
            model.setContent(textNow);
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(EditExpActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (canBeRemove) {
                return 3;
            }
            return 2;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 3;
            } else if (section == 1) {
                return 1;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                if (row == 0) {
                    InputListItem listItem = InputListItem.create(EditExpActivity.this);
                    listItem.setTitle("学校名称");
                    listItem.setInputHint("请输入学校名称");
                    if (model != null) {
                        listItem.setInputText(model.getSchool());
                    }
                    listItem.setTag(new Integer(row));
                    listItem.setInputChangeListener(EditExpActivity.this);
                    return listItem;
                } else if (row == 1) {
                    InputListItem listItem = InputListItem.create(EditExpActivity.this);
                    listItem.setTitle("岗位");
                    listItem.setInputHint("请输入工作岗位");
                    if (model != null) {
                        listItem.setInputText(model.getPost());
                    }
                    listItem.setTag(new Integer(row));
                    listItem.setInputChangeListener(EditExpActivity.this);
                    return listItem;
                } else {
                    SimpleListItem listItem = SimpleListItem.create(EditExpActivity.this);
                    listItem.setTitle("在职时间");
                    if (model != null) {
                        listItem.setSubTitle(model.getTime());
                    }
                    listItem.setShowArrow(true);
                    return listItem;
                }
            } else {
                ContentInputListItem inputListItem = ContentInputListItem.create(EditExpActivity.this);
                inputListItem.setInputText(model.getContent());
                inputListItem.setTag(2);
                inputListItem.setTitle("工作内容(500字以内)");
                inputListItem.setInputChangeListener(EditExpActivity.this);
                return inputListItem;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (canBeRemove && section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(EditExpActivity.this);
                int padding = UnitTools.dip2px(EditExpActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(EditExpActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("删除此工作经历");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove();
                    }
                });
                padding = UnitTools.dip2px(EditExpActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

}
