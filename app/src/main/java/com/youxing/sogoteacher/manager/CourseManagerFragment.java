package com.youxing.sogoteacher.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youxing.common.utils.CityManager;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGFragment;
import com.youxing.sogoteacher.views.TitleBar;
import com.youxing.sogoteacher.views.ViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class CourseManagerFragment extends SGFragment {

    private View rootView;
    private boolean rebuild;

    private TitleBar titleBar;
    private ViewPagerIndicatorView viewPagerIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_course_manager, null);

            titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
            this.viewPagerIndicatorView = (ViewPagerIndicatorView) rootView.findViewById(R.id.viewpager_indicator_view);
            List<String> titleList = new ArrayList<String>();
            titleList.add("上课中");
            titleList.add("待上课");
            titleList.add("已上课");
            final Map<String, Fragment> map = new HashMap();
            map.put("上课中", createCourseListFragment(0));
            map.put("待上课", createCourseListFragment(0));
            map.put("已上课", createCourseListFragment(1));
            this.viewPagerIndicatorView.setupFragment(titleList, map);

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
            titleBar.getTitleTv().setText("课程管理");
        }
    }

    private Fragment createCourseGoingFragment() {
        CourseGoingFragment fragment = new CourseGoingFragment();
        return fragment;
    }

    private Fragment createCourseListFragment(int status) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }
}
