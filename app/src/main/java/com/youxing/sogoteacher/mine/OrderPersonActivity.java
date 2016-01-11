package com.youxing.sogoteacher.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.youxing.sogoteacher.mine.views.OrderPersonItem;
import com.youxing.sogoteacher.model.OrderPerson;
import com.youxing.sogoteacher.model.OrderPersonListModel;
import com.youxing.sogoteacher.views.SectionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class OrderPersonActivity extends SGActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private final static int REQUEST_CODE_UPDATE_PERSON = 1;

    private ListView listView;
    private Adapter adapter;

    private boolean select;
    private int adult;
    private int child;
    private OrderPersonListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_person);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);

        getTitleBar().getRightBtn().setText("新增");
        getTitleBar().getRightBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult("duola://orderupdateperson", REQUEST_CODE_UPDATE_PERSON);
            }
        });

        select = getIntent().getBooleanExtra("select", false);
        adult = getIntent().getIntExtra("adult", 0);
        child = getIntent().getIntExtra("child", 0);

        if (!select) {
            findViewById(R.id.done).setVisibility(View.GONE);
        }

        requestData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            requestData();
        }
    }

    private void requestData() {
        showLoadingDialog(this);

        HttpService.get(Constants.domain() + "/participant/list", null, CacheType.DISABLE, OrderPersonListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (OrderPersonListModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(OrderPersonActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onClick(View v) {
        List<Long> selectIds = selectIds();
        if (selectIds != null && selectIds.size() > 0) {
            Intent data = new Intent();
            data.putExtra("selectPersons", JSON.toJSONString(selectIds));
            setResult(RESULT_OK, data);
            finish();
        } else {
            showDialog(this, "选择的出行人不合要求，请重新选择");
        }
    }

    private List<Long> selectIds() {
        List<Long> selectList = new ArrayList<Long>();
        int adultNum = 0;
        int childNum = 0;
        for (OrderPerson person : model.getData()) {
            if (person.isSelect()) {
                selectList.add(person.getId());
                if (person.getType().equals("儿童")) {
                    childNum ++;
                } else {
                    adultNum ++;
                }
            }
        }
        if (adultNum == adult && childNum == child) {
            return selectList;
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (select) {
            GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
            OrderPerson person = model.getData().get(indexPath.row);
            boolean select = person.isSelect();
            person.setSelect(!select);
            adapter.notifyDataSetChanged();
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(OrderPersonActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (model != null) {
                return 1;
            }
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            return model.getData().size();
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            OrderPersonItem item = OrderPersonItem.create(OrderPersonActivity.this);
            item.setData(model.getData().get(row));
            return item;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (select && section == 0) {
                SectionView sectionView = SectionView.create(OrderPersonActivity.this);
                StringBuilder sb = new StringBuilder("请选择");
                if (adult > 0) {
                    sb.append("成人" + adult + "名");
                }
                if (child > 0) {
                    if (adult > 0) {
                        sb.append("，");
                    }
                    sb.append("儿童" + child + "名");
                }
                sectionView.setTitle(sb.toString());
                return sectionView;
            }
            return super.getViewForSection(convertView, parent, section);
        }

        @Override
        public int getHeightForSectionView(int section) {
            if (!select) {
                return 0;
            }
            return super.getHeightForSectionView(section);
        }
    }

}
