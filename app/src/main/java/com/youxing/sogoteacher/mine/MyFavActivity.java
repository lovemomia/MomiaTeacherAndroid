package com.youxing.sogoteacher.mine;

import android.os.Bundle;
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
import com.youxing.sogoteacher.model.ProductListModel;
import com.youxing.sogoteacher.model.Product;
import com.youxing.sogoteacher.views.EmptyView;
import com.youxing.sogoteacher.views.ProductListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/27.
 */
public class MyFavActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private Adapter adapter;

    private List<Product> productList = new ArrayList<Product>();
    private int nextIndex;
    private boolean isEmpty;
    private boolean isEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void requestData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", String.valueOf(nextIndex)));
        HttpService.get(Constants.domain() + "/user/favorite", params, CacheType.DISABLE, ProductListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                ProductListModel model = (ProductListModel) response;
                productList.addAll(model.getData().getList());
                nextIndex = model.getData().getNextIndex();
                if (nextIndex <= 0 || model.getData().getTotalCount() <= productList.size()) {
                    isEnd = true;
                }
                if (productList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(MyFavActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < productList.size()) {
            Product product = productList.get(position);
            startActivity("duola://productdetail?id=" + product.getId());
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
                return productList.size();
            }
            return productList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (isEmpty) {
                return EMPTY;
            }
            if (position < productList.size()) {
                return productList.get(position);
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
                String msg = "您还没有收藏活动哦，赶快去浏览一下吧~";
                EmptyView emptyView = EmptyView.create(MyFavActivity.this);
                emptyView.setMessage(msg);
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                Product product = (Product) item;
                ProductListItem couponListItem = ProductListItem.create(MyFavActivity.this);
                couponListItem.setData(product);
                view = couponListItem;
            }
            return view;
        }

    }

}
