package com.youxing.sogoteacher.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.youxing.sogoteacher.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Jun Deng on 15/8/25.
 */
public class ViewPagerIndicatorView extends LinearLayout implements TabIndicatorView.OnIndicateChangeListener, OnPageChangeListener {
    private TabIndicatorView tabIndicatorView;
    private ViewPager viewPager;

    public ViewPagerIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public ViewPagerIndicatorView(Context context) {
        super(context);
        this.initView();
    }

    private void initView() {
        this.setOrientation(LinearLayout.VERTICAL);

        this.tabIndicatorView = new TabIndicatorView(getContext());
        this.viewPager = new ViewPager(getContext());
        this.viewPager.setId(R.id.pager);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(tabIndicatorView, params);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(viewPager, params);

        this.tabIndicatorView.setOnIndicateChangeListener(this);
        this.viewPager.setOnPageChangeListener(this);

    }

    /**
     * 设置显示标签文字及对应内容布局
     *
     * @param pageMap
     *            标题及对应View map数据
     */
    public void setupLayout(List<String> titleList, Map<String, View> pageMap) {
        if (pageMap == null || pageMap.size() == 0) {
            throw new NullPointerException();
        }

        final List<View> viewList = new ArrayList<View>();

        for (String title : titleList) {
            View view = pageMap.get(title);
            viewList.add(view);
        }

        // 初始化TextTabIndicateView及ViewPager
        this.tabIndicatorView.setupLayout(titleList);
        this.viewPager.setAdapter(new MyPagerAdapter(viewList));
    }

    /**
     * 设置显示标签文字及对应fragment
     *
     * @param titleList
     * @param pageMap
     */
    public void setupFragment(List<String> titleList, Map<String, Fragment> pageMap) {
        if (pageMap == null || pageMap.size() == 0) {
            throw new NullPointerException();
        }

        final List<Fragment> pageList = new ArrayList<Fragment>();

        for (String title : titleList) {
            Fragment fragment = pageMap.get(title);
            pageList.add(fragment);
        }

        // 初始化TextTabIndicateView及ViewPager
        this.tabIndicatorView.setupLayout(titleList);
        this.viewPager.setAdapter(new MyFragmentPagerAdapter(pageList));
    }

    @Override
    public void onTabChanged(int position) {
        viewPager.setCurrentItem(position, true);
    }

    @Override
    public void onPageSelected(int position) {
        this.tabIndicatorView.setCurrentTab(position, false);//设置不通知接口返回位置
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> viewList = new ArrayList<View>();

        public MyPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(viewList.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(viewList.get(arg1));
            return viewList.get(arg1);
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public MyFragmentPagerAdapter(List<Fragment> fragments) {
            super(((FragmentActivity)getContext()).getSupportFragmentManager());
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

}
