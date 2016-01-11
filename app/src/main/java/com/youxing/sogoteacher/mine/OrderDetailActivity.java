package com.youxing.sogoteacher.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.youxing.sogoteacher.mine.views.OrderDetailInfoView;
import com.youxing.sogoteacher.model.OrderDetailModel;
import com.youxing.sogoteacher.utils.PriceUtils;
import com.youxing.sogoteacher.views.ProductListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderDetailActivity extends SGActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private String oid;
    private String pid;

    private ListView listView;
    private Adapter adapter;

    private OrderDetailModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        oid = getIntent().getData().getQueryParameter("oid");
        pid = getIntent().getData().getQueryParameter("pid");

        requestData();
    }

    private void requestData() {
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("pid", pid));
        HttpService.get(Constants.domain() + "/user/order/detail", params, CacheType.DISABLE, OrderDetailModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (OrderDetailModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(OrderDetailActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.section == 0) {
            startActivity("duola://productdetail?id=" + pid);
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://cashpay?pom=" + JSON.toJSONString(model))));
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(OrderDetailActivity.this);
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
            if (section == 0) {
                return 1;
            } else if (section == 1 || section == 2) {
                return 2;
            }
            return 0;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            int status = model.getData().getStatus();
            if (section == 3 && (status == 1 || status == 2)) {
                LinearLayout ll = new LinearLayout(OrderDetailActivity.this);
                int padding = UnitTools.dip2px(OrderDetailActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(OrderDetailActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("去支付");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(OrderDetailActivity.this);
                padding = UnitTools.dip2px(OrderDetailActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }

        @Override
        public int getHeightForSectionView(int section) {
            if (section == 0) {
                return 0;
            } else if (section == 3) {
                return 60;
            }
            return super.getHeightForSectionView(section);
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0) {
                ProductListItem item = ProductListItem.create(OrderDetailActivity.this);
                item.setData(model.getData());
                view = item;
            } else {
                if (row == 0) {
                    TextView tv = new TextView(OrderDetailActivity.this);
                    tv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                            AbsListView.LayoutParams.WRAP_CONTENT));
                    int padding = UnitTools.dip2px(OrderDetailActivity.this, 10);
                    tv.setPadding(padding, padding, padding, padding);
                    tv.setTextColor(getResources().getColor(R.color.text_gray));
                    tv.setTextSize(14);
                    if (section == 1) {
                        tv.setText("订单信息");
                    } else {
                        tv.setText("联系人信息");
                    }
                    view = tv;

                } else {
                    OrderDetailInfoView infoView = new OrderDetailInfoView(OrderDetailActivity.this);
                    if (section == 1) {
                        List<NameValuePair> infoList = new ArrayList<NameValuePair>();
                        infoList.add(new BasicNameValuePair("订单编号：", String.valueOf(model.getData().getId())));
                        infoList.add(new BasicNameValuePair("下单时间：", model.getData().getAddTime()));
                        infoList.add(new BasicNameValuePair("数         量：", String.valueOf(model.getData().getParticipants())));
                        infoList.add(new BasicNameValuePair("总         价：", PriceUtils.formatPriceString(model.getData().getTotalFee())));
                        infoView.setInfo(infoList);
                    } else {
                        List<NameValuePair> infoList = new ArrayList<NameValuePair>();
                        infoList.add(new BasicNameValuePair("姓         名：", model.getData().getContacts()));
                        infoList.add(new BasicNameValuePair("手         机：", model.getData().getMobile()));
                        infoView.setInfo(infoList);
                    }
                    view = infoView;
                }
            }
            return view;
        }
    }

}
