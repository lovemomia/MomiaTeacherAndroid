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
import com.youxing.sogoteacher.model.Education;
import com.youxing.sogoteacher.model.EducationModel;
import com.youxing.sogoteacher.views.InputListItem;
import com.youxing.sogoteacher.views.SimpleListItem;
import com.youxing.sogoteacher.views.SingleLevelWheelView;
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
 * Created by Jun Deng on 16/1/12.
 */
public class EditEduActivity extends SGActivity implements AdapterView.OnItemClickListener, InputListItem.InputChangeListener {

    private Adapter adapter;

    private Education model;
    private boolean canBeRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        model = getIntent().getParcelableExtra("edu");
        if (model == null) {
            model = new Education();
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
        params.add(new BasicNameValuePair("education", JSON.toJSONString(model)));
        HttpService.post(Constants.domain() + "/teacher/education", params, EducationModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                EducationModel model = (EducationModel)response;
                Intent data = new Intent();
                data.putExtra("edu", model.getData());
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(EditEduActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
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
        HttpService.post(Constants.domain() + "/teacher/education/delete", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                Intent data = new Intent();
                data.putExtra("edu", model);
                data.putExtra("remove", true);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(EditEduActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
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
            showDialog(EditEduActivity.this, "学校不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getMajor())) {
            showDialog(EditEduActivity.this, "专业不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getLevel())) {
            showDialog(EditEduActivity.this, "学历不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getTime())) {
            showDialog(EditEduActivity.this, "在线时间不能为空");
            return false;
        }
        return true;
    }

    private void chooseLevel() {
        final SingleLevelWheelView wheelView = SingleLevelWheelView.create(EditEduActivity.this);

        List<String> data = Arrays.asList("大专", "本科", "硕士", "博士", "其他");
        wheelView.setData(data);
        wheelView.setItemIndex(1);

        new AlertDialog.Builder(this)
                .setTitle("选择学历")
                .setView(wheelView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.setLevel(wheelView.getSelectData());
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    private void chooseTime() {
        final TwoLevelWheelView wheelView = TwoLevelWheelView.create(EditEduActivity.this);

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
            chooseLevel();

        } else if (indexPath.row == 3) {
            chooseTime();
        }
    }

    @Override
    public void onInputChanged(InputListItem inputListItem, String textNow) {
        int row = (Integer) inputListItem.getTag();
        if (row == 0) {
            model.setSchool(textNow);
        } else if (row == 1) {
            model.setMajor(textNow);
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(EditEduActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (canBeRemove) {
                return 2;
            }
            return 1;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 4;
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (row == 0) {
                InputListItem listItem = InputListItem.create(EditEduActivity.this);
                listItem.setTitle("学校");
                listItem.setInputHint("请输入学校名称");
                if (model != null) {
                    listItem.setInputText(model.getSchool());
                }
                listItem.setTag(new Integer(row));
                listItem.setInputChangeListener(EditEduActivity.this);
                return listItem;

            } else if (row == 1) {
                InputListItem listItem = InputListItem.create(EditEduActivity.this);
                listItem.setTitle("专业");
                listItem.setInputHint("请输入就读专业");
                listItem.setInputText(model.getMajor());
                listItem.setTag(new Integer(row));
                listItem.setInputChangeListener(EditEduActivity.this);
                return listItem;

            } else if (row == 2) {
                SimpleListItem listItem = SimpleListItem.create(EditEduActivity.this);
                listItem.setTitle("学历");
                listItem.setSubTitle(model.getLevel());
                listItem.setShowArrow(true);
                return listItem;

            } else {
                SimpleListItem listItem = SimpleListItem.create(EditEduActivity.this);
                listItem.setTitle("在校时间");
                listItem.setSubTitle(model.getTime());
                listItem.setShowArrow(true);
                return listItem;
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (canBeRemove && section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(EditEduActivity.this);
                int padding = UnitTools.dip2px(EditEduActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(EditEduActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("删除此教育经历");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove();
                    }
                });
                padding = UnitTools.dip2px(EditEduActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

}
