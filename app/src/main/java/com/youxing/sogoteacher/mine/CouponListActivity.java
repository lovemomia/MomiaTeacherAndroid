package com.youxing.sogoteacher.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.youxing.sogoteacher.mine.views.CouponListItem;
import com.youxing.sogoteacher.model.Coupon;
import com.youxing.sogoteacher.model.CouponListModel;
import com.youxing.sogoteacher.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class CouponListActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private Adapter adapter;

    private List<Coupon> couponList = new ArrayList<Coupon>();
    private boolean isEmpty;
    private boolean isEnd;
    private int status = 1; // 1.未使用 2.已使用 3.已过期
    private String oid;
    private int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        String statusStr = getIntent().getData().getQueryParameter("status");
        if (!TextUtils.isEmpty(statusStr)) {
            status = Integer.valueOf(statusStr);
        }
        oid = getIntent().getData().getQueryParameter("oid");
        String selectStr = getIntent().getData().getQueryParameter("select");
        if (!TextUtils.isEmpty(selectStr)) {
            select = Integer.valueOf(selectStr);
        }
    }

    private void requestData() {
        int start = couponList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", String.valueOf(status)));
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        params.add(new BasicNameValuePair("oid", oid));
        HttpService.get(Constants.domain() + "/user/coupon", params, CacheType.DISABLE, CouponListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                CouponListModel model = (CouponListModel) response;
                couponList.addAll(model.getData().getList());
                if (model.getData().getTotalCount() <= couponList.size()) {
                    isEnd = true;
                }
                if (couponList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(CouponListActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (select == 1 && position < couponList.size()) {
            Coupon coupon = couponList.get(position);
            Intent data = new Intent();
            data.putExtra("coupon", coupon.getId());
            setResult(RESULT_OK, data);
            finish();
        }
    }

    class Adapter extends BasicAdapter {

        public Adapter() {
        }

        @Override
        public int getCount() {
            if (isEmpty) {
                return 1;
            }
            if (isEnd) {
                return couponList.size();
            }
            return couponList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (isEmpty) {
                return EMPTY;
            }
            if (position < couponList.size()) {
                return couponList.get(position);
            }
            return LOADING;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            View view = null;
            if (item == EMPTY) {
                String msg;
                if (status == 1) {
                    msg = "您还没有可用红包，\n邀请伙伴加入可以获得更多红包~";
                } else {
                    msg = "还没有红包哟~";
                }
                EmptyView emptyView = EmptyView.create(CouponListActivity.this);
                emptyView.setMessage(msg);
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                Coupon coupon = (Coupon) item;
                CouponListItem couponListItem = CouponListItem.create(CouponListActivity.this);
                couponListItem.setData(coupon);
                view = couponListItem;
            }
            return view;
        }

    }
}
