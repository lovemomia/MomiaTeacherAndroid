package com.youxing.sogoteacher.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.BasicAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.City;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.CityManager;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGFragment;
import com.youxing.sogoteacher.home.views.HomeHeaderView;
import com.youxing.sogoteacher.home.views.HomeListItem;
import com.youxing.sogoteacher.model.HomeModel;
import com.youxing.sogoteacher.model.Product;
import com.youxing.sogoteacher.views.TitleBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 *
 * Created by Jun Deng on 15/8/3.
 */
public class HomeFragment extends SGFragment implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, RequestHandler, CityManager.CityChangeListener {

    private View rootView;
    private boolean rebuild;

    private SwipeRefreshLayout swipeLayout;
    private TitleBar titleBar;
    private ListView listView;
    private Adapter adapter;

    private List<HomeModel.HomeBanner> bannerList = new ArrayList<HomeModel.HomeBanner>();
    private List<Product> productList = new ArrayList<Product>();
    private int nextPage = -1;

    private boolean isRefresh;
    private boolean hasBannel;
    private boolean isEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_home, null);

            titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
            listView = (ListView)rootView.findViewById(R.id.listView);
            listView.setOnItemClickListener(this);
            adapter = new Adapter();
            listView.setAdapter(adapter);

            swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                    android.R.color.holo_orange_light, android.R.color.holo_red_light);

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

        if (rebuild) {
            initTitle();

            requestData();
        }

        CityManager.instance().addListener(this);
    }

    private void initTitle() {
        titleBar.getTitleTv().setText("松果亲子");

        titleBar.getLeftBtn().setText(CityManager.instance().getChoosedCity().getName());
        Drawable img = getResources().getDrawable(R.drawable.ic_arrow_down);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        titleBar.getLeftBtn().getTextView().setCompoundDrawables(null, null, img, null);
        titleBar.getLeftBtn().getTextView().setCompoundDrawablePadding(10);
        titleBar.getLeftBtn().getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("duola://citylist");
            }
        });
        titleBar.getRightBtn().setIcon(R.drawable.ic_action_date);
        titleBar.getRightBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("duola://productcalendar");
            }
        });
    }

    private void requestData() {
        if (nextPage == -1) {
            getDLActivity().showLoadingDialog(getActivity(), null, null);
        }

        String url = Constants.domain() + "/home";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pageindex", String.valueOf(nextPage == -1 ? 0 : nextPage)));

        HttpService.get(url, params, CacheType.DISABLE, HomeModel.class, this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        nextPage = -1;
        bannerList.clear();
        productList.clear();
        hasBannel = false;
        isEmpty = false;
        requestData();
    }

    @Override
    public void onCityChanged(City newCity) {
        onRefresh();
        titleBar.getLeftBtn().setText(newCity.getName());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int index = position - 1;
        if (index >= 0 && index < productList.size()) {
            Product product = productList.get(index);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("duola://productdetail?id=" + product.getId())));
        }
    }

    @Override
    public void onRequestFinish(Object response) {
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }

        HomeModel homeModel = (HomeModel) response;
        productList.addAll(homeModel.getData().getProducts());

        if (homeModel.getData().getBanners() != null && homeModel.getData().getBanners().size() > 0) {
            bannerList.addAll(homeModel.getData().getBanners());
            hasBannel = true;
        }

        if (nextPage == -1) {
            getDLActivity().dismissDialog();
        }
        nextPage = homeModel.getData().getNextpage();
        if (nextPage == 0) {
            isEmpty = true;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRequestFailed(BaseModel error) {
        getDLActivity().dismissDialog();
        if (isRefresh) {
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }
        getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
    }

    class Adapter extends BasicAdapter {

        private Object BANNEL = new Object();
        private Object SECTION = new Object();

        @Override
        public int getCount() {
            if (nextPage == -1) {
                return 0;
            }
            return productList.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return hasBannel ? BANNEL : SECTION;
            }

            int pos = position - 1;
            if (pos < productList.size()) {
                return productList.get(pos);
            }
            return isEmpty ? EMPTY : LOADING;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            View view = convertView;
            if (item == BANNEL) {
                HomeHeaderView header = HomeHeaderView.create(getActivity());
                header.setData(bannerList);
                view = header;

            } else if (item == SECTION) {
                view = new View(getActivity());
                view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 0));

            } else if (item == EMPTY) {
                view = getEmptyView("- 更多活动敬请期待 -", parent, convertView);
            } else if (item == LOADING) {
                view = getLoadingView(parent, convertView);
                requestData();
            } else {
                if (!(view instanceof HomeListItem)) {
                    view = HomeListItem.create(getDLActivity());
                }
                ((HomeListItem) view).setData((Product)item);
            }
            return view;
        }

    }

    @Override
    public void onDestroy() {
        HttpService.abort(this);
        CityManager.instance().removeListener(this);
        super.onDestroy();
    }

}
