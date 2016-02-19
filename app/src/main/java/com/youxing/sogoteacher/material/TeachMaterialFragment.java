package com.youxing.sogoteacher.material;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.youxing.sogoteacher.material.views.MaterialListItem;
import com.youxing.sogoteacher.model.MaterialListModel;
import com.youxing.sogoteacher.views.EmptyView;
import com.youxing.sogoteacher.views.TitleBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class TeachMaterialFragment extends SGFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TitleBar titleBar;
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh;
    private ListView listView;
    private Adapter adapter;

    private List<MaterialListModel.Material> materialList = new ArrayList<>();
    private boolean isEmpty;
    private boolean isEnd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material, null);
        titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
        listView = (ListView)rootView.findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleBar.getTitleTv().setText("教材教具");
    }

    private void refresh() {
        isEmpty = false;
        isEnd = false;
        materialList.clear();

        requestData();
    }

    private void requestData() {
        int start = materialList.size();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        HttpService.get(Constants.domain() + "/teacher/material/list", params, CacheType.DISABLE, MaterialListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                if (isRefresh) {
                    isRefresh = false;
                    swipeLayout.setRefreshing(false);
                }
                MaterialListModel model = (MaterialListModel) response;
                materialList.addAll(model.getData().getList());
                if (model.getData().getNextIndex() == 0 || model.getData().getTotalCount() <= materialList.size()) {
                    isEnd = true;
                }
                if (materialList.size() == 0) {
                    isEmpty = true;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                if (isRefresh) {
                    isRefresh = false;
                    swipeLayout.setRefreshing(false);
                }
                getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < materialList.size()) {
            MaterialListModel.Material material = materialList.get(position);
            String url = "http://" + (Constants.DEBUG ? "m.momia.cn" : "m.sogokids.com") + "/course/material?id=" + material.getId();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://web?url=" + URLEncoder.encode(url))));
        }

    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        refresh();
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
                return materialList.size();
            }
            return materialList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (isEmpty) {
                return EMPTY;
            }
            if (position < materialList.size()) {
                return materialList.get(position);
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
                String msg = "教材教具列表为空~";
                EmptyView emptyView = EmptyView.create(getActivity());
                emptyView.setMessage(msg);
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                MaterialListModel.Material material = (MaterialListModel.Material) item;
                MaterialListItem listItem = MaterialListItem.create(getActivity());
                listItem.setData(material);
                view = listItem;
            }
            return view;
        }

    }

}
