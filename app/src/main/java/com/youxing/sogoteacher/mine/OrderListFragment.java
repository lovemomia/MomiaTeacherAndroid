package com.youxing.sogoteacher.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.youxing.sogoteacher.app.SGFragment;
import com.youxing.sogoteacher.mine.views.OrderListItem;
import com.youxing.sogoteacher.model.Order;
import com.youxing.sogoteacher.model.OrderListModel;
import com.youxing.sogoteacher.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderListFragment extends SGFragment implements AdapterView.OnItemClickListener {

    private View rootView;
    private boolean rebuild;

    private ListView listView;
    private Adapter adapter;

    private List<Order> orderList = new ArrayList<Order>();
    private boolean isEmpty;
    private boolean isEnd;
    private int status = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt("status");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_list, null);
            listView = (ListView)rootView.findViewById(R.id.listView);
            adapter = new Adapter();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            rebuild = true;
        } else {
            rebuild = false;
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (rebuild) {
//            requestData();
//        }
    }

    private void requestData() {
        int start = orderList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", String.valueOf(status)));
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        HttpService.get(Constants.domain() + "/user/order", params, CacheType.DISABLE, OrderListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                OrderListModel model = (OrderListModel) response;
                orderList.addAll(model.getData().getList());
                if (model.getData().getNextIndex() == 0 || model.getData().getTotalCount() <= orderList.size()) {
                    isEnd = true;
                }
                if (orderList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item instanceof Order) {
            Order order = (Order) item;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://orderdetail?oid=" +
                    order.getId() + "&pid=" + order.getProductId())));
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
                return orderList.size();
            }
            return orderList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (isEmpty) {
                return EMPTY;
            }
            if (position < orderList.size()) {
                return orderList.get(position);
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
                if (status == 2) {
                    msg = "您还没有待付款订单哦，快去逛一下吧~";
                } else if (status == 3) {
                    msg = "您还没有未消费订单哦，快去逛一下吧~";
                } else {
                    msg = "订单列表为空";
                }
                EmptyView emptyView = EmptyView.create(getActivity());
                emptyView.setMessage(msg);
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                Order order = (Order) item;
                OrderListItem orderListItem = OrderListItem.create(getActivity());
                orderListItem.setData(order);
                view = orderListItem;
            }
            return view;
        }

    }

}
