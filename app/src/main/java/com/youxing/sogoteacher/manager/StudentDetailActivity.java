package com.youxing.sogoteacher.manager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;

/**
 * Created by Jun Deng on 16/1/15.
 */
public class StudentDetailActivity extends SGActivity implements AdapterView.OnItemClickListener {

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(StudentDetailActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class Adapter extends GroupStyleAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            return 0;
        }

        @Override
        public int getCountInSection(int section) {
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            return null;
        }
    }
}
