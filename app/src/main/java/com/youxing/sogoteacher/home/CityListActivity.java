package com.youxing.sogoteacher.home;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.City;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.CityManager;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.model.CityListModel;
import com.youxing.sogoteacher.views.SectionView;
import com.youxing.sogoteacher.views.SimpleListItem;

/**
 * Created by Jun Deng on 15/8/27.
 */
public class CityListActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private Adapter adapter;

    private CityListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    private void requestData() {
        showLoadingDialog(CityListActivity.this);

        HttpService.get(Constants.domain() + "/city", null, CacheType.DISABLE, CityListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (CityListModel) response;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(CityListActivity.this, error.getErrmsg());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        City city = model.getData().get(indexPath.row);
        CityManager.instance().setChoosedCity(city);
        finish();
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(CityListActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (model == null) {
                return 0;
            }
            return 2;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return model.getData().size();
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            SimpleListItem view = SimpleListItem.create(CityListActivity.this);
            view.setTitle(model.getData().get(row).getName());
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 0) {
                SectionView sectionView = SectionView.create(CityListActivity.this);
                sectionView.setTitle("已开通城市列表");
                return sectionView;
            } else {
                View view = getEmptyView("更多城市即将开通", parent, convertView);
                return view;
            }
        }
    }

}
