package com.youxing.sogoteacher.mine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.model.OrderPerson;
import com.youxing.sogoteacher.model.OrderPersonModel;
import com.youxing.sogoteacher.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class OrderUpdatePersonActivity extends SGActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private Adapter adapter;

    private String personId;
    private OrderPerson person;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        personId = getIntent().getData().getQueryParameter("personId");
        if (!TextUtils.isEmpty(personId)) {
            isUpdate = true;
            requestData();
        } else {
            getTitleBar().getRightBtn().setText("完成");
            getTitleBar().getRightBtn().setOnClickListener(OrderUpdatePersonActivity.this);

            person = new OrderPerson();
            person.setSex("男");
            person.setIdType(1);
        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", personId));
        HttpService.get(Constants.domain() + "/participant", params, CacheType.DISABLE, OrderPersonModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                person = ((OrderPersonModel) response).getData();
                adapter.notifyDataSetChanged();

                getTitleBar().getRightBtn().setText("完成");
                getTitleBar().getRightBtn().setOnClickListener(OrderUpdatePersonActivity.this);
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(OrderUpdatePersonActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private boolean check() {
        if (person.getName() == null) {
            showDialog(OrderUpdatePersonActivity.this, "姓名不能为空");
            return false;
        }
        if (person.getBirthday() == null) {
            showDialog(OrderUpdatePersonActivity.this, "生日不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (check()) {
            showLoadingDialog(this);

            String path = isUpdate ? "/participant/update" : "/participant";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("participant", JSON.toJSONString(person)));
            HttpService.post(Constants.domain() + path, params, BaseModel.class, new RequestHandler() {
                @Override
                public void onRequestFinish(Object response) {
                    dismissDialog();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onRequestFailed(BaseModel error) {
                    dismissDialog();
                    showDialog(OrderUpdatePersonActivity.this, error.getErrmsg());
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == 0) {
            showInputDialog("请输入真实姓名", new OnInputDoneListener() {
                @Override
                public void onInputDone(String text) {
                    person.setName(text);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (indexPath.row == 1) {
            showSexChooseDialog();
        } else if (indexPath.row == 2) {
            showBirthdayChooseDialog();
        } else if (indexPath.row == 3) {
            showIdNoTypeChooseDialog();

        } else if (indexPath.row == 4) {
            showInputDialog("请输入证件号码", new OnInputDoneListener() {
                @Override
                public void onInputDone(String text) {
                    person.setIdNo(text);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(OrderUpdatePersonActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (isUpdate && person == null) {
                return 0;
            }
            return 1;
        }

        @Override
        public int getCountInSection(int section) {
            return 5;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            SimpleListItem item = SimpleListItem.create(OrderUpdatePersonActivity.this);
            item.setShowArrow(true);
            if (row == 0) {
                item.setTitle("姓名");
                if (person != null) {
                    item.setSubTitle(person.getName());
                }

            } else if (row == 1) {
                item.setTitle("性别");
                if (person != null) {
                    item.setSubTitle(person.getSex());
                }

            } else if (row == 2) {
                item.setTitle("出生日期");
                if (person != null) {
                    item.setSubTitle(person.getBirthday());
                }

            } else if (row == 3) {
                item.setTitle("证件类型");
                if (person != null) {
                    item.setSubTitle(person.getIdType() == 2 ? "护照" : "身份证");
                }

            } else if (row == 4) {
                item.setTitle("证件号码");
                if (person != null) {
                    item.setSubTitle(person.getIdNo());
                }
            }
            return item;
        }
    }

    private void showInputDialog(String title, final OnInputDoneListener listener) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(input).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onInputDone(input.getText().toString());
            }
        }).setNegativeButton("取消", null).show();
    }

    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderUpdatePersonActivity.this);
        builder.setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        person.setSex("男");
                        break;

                    case 1:
                        person.setSex("女");
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void showIdNoTypeChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderUpdatePersonActivity.this);
        builder.setItems(new String[]{"身份证", "护照"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        person.setIdType(1);
                        break;

                    case 1:
                        person.setIdType(2);
                        break;
                    default:
                        person.setIdType(1);
                }
                adapter.notifyDataSetChanged();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void showBirthdayChooseDialog() {
        // 生日
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(OrderUpdatePersonActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthday = dateFormat.format(cal.getTime());
                person.setBirthday(birthday);
                adapter.notifyDataSetChanged();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    interface OnInputDoneListener {
        void onInputDone(String text);
    }

}
