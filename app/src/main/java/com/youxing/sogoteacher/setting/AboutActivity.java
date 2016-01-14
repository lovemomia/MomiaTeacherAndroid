package com.youxing.sogoteacher.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Enviroment;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.views.SimpleListItem;

/**
 * Created by Jun Deng on 15/8/31.
 */
public class AboutActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        if (indexPath.row == 0) {
            startActivity("sgteacher://web?url=http://www.duolaqinzi.com/agreement.html");
        }
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(AboutActivity.this);
        }

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getCountInSection(int section) {
            return 1;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            View view = LayoutInflater.from(AboutActivity.this).inflate(R.layout.layout_about_header, null);
            TextView versionTv = (TextView) view.findViewById(R.id.version);
            versionTv.setText("当前版本：V" + Enviroment.versionName());
            return view;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            SimpleListItem listItem = SimpleListItem.create(AboutActivity.this);
            listItem.setTitle("用户协议");
            listItem.setShowArrow(true);
            return listItem;
        }
    }
}
