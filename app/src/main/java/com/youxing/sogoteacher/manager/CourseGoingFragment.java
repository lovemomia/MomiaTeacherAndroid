package com.youxing.sogoteacher.manager;

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
import com.youxing.sogoteacher.manager.views.CourseListItem;
import com.youxing.sogoteacher.manager.views.GoingStudentListItem;
import com.youxing.sogoteacher.model.Course;
import com.youxing.sogoteacher.model.CourseGoingModel;
import com.youxing.sogoteacher.model.Student;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class CourseGoingFragment extends SGFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View rootView;
    private boolean rebuild;

    private ListView listView;
    private Adapter adapter;

    private CourseGoingModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (rebuild) {
            requestData();
        }
    }

    private void requestData() {
        showLoadingDialog(getActivity());
        HttpService.get(Constants.domain() + "/teacher/course/ongoing", null, CacheType.DISABLE, CourseGoingModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                model = (CourseGoingModel) response;
                if (model.getData().getCourse() == null) {
                    showEmptyView("课程还没有开始哦～");
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
            }
        });
    }

    private void requestCheckin(final Student student) {
        showLoadingDialog(getActivity());
        HttpService.post(Constants.domain() + "/teacher/course/checkin", null, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                student.setCheckin(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                getDLActivity().showDialog(getDLActivity(), error.getErrmsg());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Student student = model.getData().getStudents().get(position - 1);
        if (student.isCheckin()) {
            Course course = model.getData().getCourse();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://studentrecord?coid=" +
                    course.getCourseId() + "&sid=" + course.getCourseSkuId() + "&cid=" + student.getId())));

        } else {
            requestCheckin(student);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            Student student = model.getData().getStudents().get(position - 1);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://studentdetail?id=" +
                    student.getId())));
        }
    }

    class Adapter extends BasicAdapter {

        public Adapter() {
        }

        @Override
        public int getCount() {
            if (model == null) {
                return 0;
            }
            return model.getData().getStudents().size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return model.getData().getCourse();
            }
            return model.getData().getStudents().get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            View view = null;
            if (position == 0) {
                Course course = (Course) item;
                CourseListItem courseListItem = CourseListItem.create(getActivity());
                courseListItem.setData(course);
                view = courseListItem;

            } else {
                Student student = (Student)item;
                GoingStudentListItem listItem = GoingStudentListItem.create(getActivity());
                listItem.setData(student);
                listItem.getOpBtn().setTag(new Integer(position));
                listItem.getOpBtn().setOnClickListener(CourseGoingFragment.this);
                view = listItem;
            }
            return view;
        }

    }
}
