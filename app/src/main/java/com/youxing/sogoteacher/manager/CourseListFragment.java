package com.youxing.sogoteacher.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.youxing.sogoteacher.manager.views.CourseListItem;
import com.youxing.sogoteacher.model.Course;
import com.youxing.sogoteacher.model.CourseListModel;
import com.youxing.sogoteacher.views.EmptyView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class CourseListFragment extends SGFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh;
    private ListView listView;
    private Adapter adapter;

    private List<Course> courseList = new ArrayList<Course>();
    private boolean isEmpty;
    private boolean isEnd;
    private int status;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StudentAddCommentActivity.ACTION_STUDENT_COMMENTED)) {
                refresh();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt("status");

        if (status != 0) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(StudentAddCommentActivity.ACTION_STUDENT_COMMENTED);
            getActivity().registerReceiver(receiver, filter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_refresh_list, null);
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
    }

    @Override
    public void onResume() {
        super.onResume();
//        refresh();
    }

    @Override
    public void onDestroy() {
        if (status != 0) {
            getActivity().unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    private void refresh() {
        courseList.clear();
        isEmpty = false;
        isEnd = false;

        requestData();
    }

    private void requestData() {
        int start = courseList.size();
        String path = status == 0 ? "/teacher/course/notfinished" : "/teacher/course/finished";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", String.valueOf(start)));
        params.add(new BasicNameValuePair("count", "20"));
        HttpService.get(Constants.domain() + path, params, CacheType.DISABLE, CourseListModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                if (isRefresh) {
                    isRefresh = false;
                    swipeLayout.setRefreshing(false);
                }
                CourseListModel model = (CourseListModel) response;
                courseList.addAll(model.getData().getList());
                if (model.getData().getNextIndex() == 0 || model.getData().getTotalCount() <= courseList.size()) {
                    isEnd = true;
                }
                if (courseList.size() == 0) {
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
        Object item = parent.getItemAtPosition(position);
        if (item instanceof Course) {
            Course course = (Course) item;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://studentlist?coid=" +
                    course.getCourseId() + "&sid=" + course.getCourseSkuId() + "&status=" + status)));
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
                return courseList.size();
            }
            return courseList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (isEmpty) {
                return EMPTY;
            }
            if (position < courseList.size()) {
                return courseList.get(position);
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
                String msg = "课程列表为空~";
                EmptyView emptyView = EmptyView.create(getActivity());
                emptyView.setMessage(msg);
                view = emptyView;

            } else if (item == LOADING) {
                requestData();
                view = getLoadingView(parent, convertView);

            } else {
                Course course = (Course) item;
                CourseListItem courseListItem = CourseListItem.create(getActivity());

                courseListItem.setData(course, status != 0);
                view = courseListItem;
            }
            return view;
        }

    }

}
