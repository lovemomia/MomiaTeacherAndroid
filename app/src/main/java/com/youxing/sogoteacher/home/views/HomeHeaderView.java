package com.youxing.sogoteacher.home.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youxing.common.adapter.RecyclingPagerAdapter;
import com.youxing.common.views.YXNetworkImageView;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.model.HomeModel;
import com.youxing.sogoteacher.views.PageControl;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Jun Deng on 15/6/15.
 */
public class HomeHeaderView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private AutoScrollViewPager pager;
    private PageControl pageControl;

    private int pageCount;
    private ImagePagerAdapter adapter;

    public HomeHeaderView(Context context) {
        super(context);
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static HomeHeaderView create(Context context) {
        return (HomeHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_home_header, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        pager = (AutoScrollViewPager) findViewById(R.id.pager);
        pageControl = (PageControl) findViewById(R.id.pageControl);
    }

    public void setData(final List<HomeModel.HomeBanner> banners) {
        pageCount = banners.size();
        pageControl.setNumberOfPages(pageCount);
        pageControl.setCurrentPage(0);

        adapter = new ImagePagerAdapter(getContext(), banners).setInfiniteLoop(true);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        pager.setInterval(2000);
        pager.startAutoScroll();
        pager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % pageCount);
    }

    @Override
    public void onPageSelected(int position) {
        pageControl.setCurrentPage(position % pageCount);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    public class ImagePagerAdapter extends RecyclingPagerAdapter {

        private Context context;
        private List<HomeModel.HomeBanner> banners;

        private int           size;
        private boolean       isInfiniteLoop;

        public ImagePagerAdapter(Context context, List<HomeModel.HomeBanner> banners) {
            this.context = context;
            this.banners = banners;
            this.size = pageCount;
            isInfiniteLoop = false;
        }

        @Override
        public int getCount() {
            // Infinite loop
            return isInfiniteLoop ? Integer.MAX_VALUE : pageCount;
        }

        /**
         * get really position
         *
         * @param position
         * @return
         */
        private int getPosition(int position) {
            return isInfiniteLoop ? position % size : position;
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                YXNetworkImageView imageView = new YXNetworkImageView(getContext());
                imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setDefaultImageResId(R.drawable.bg_default_image);
                view = holder.imageView = imageView;
                view.setTag(holder);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int postion = pager.getCurrentItem();
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banners.get(adapter.getPosition(postion)).getAction())));
                    }
                });
            } else {
                holder = (ViewHolder)view.getTag();
            }
            holder.imageView.setImageUrl(banners.get(getPosition(position)).getCover());
            return view;
        }

        private class ViewHolder {
            YXNetworkImageView imageView;
        }

        /**
         * @return the isInfiniteLoop
         */
        public boolean isInfiniteLoop() {
            return isInfiniteLoop;
        }

        /**
         * @param isInfiniteLoop the isInfiniteLoop to set
         */
        public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
            this.isInfiniteLoop = isInfiniteLoop;
            return this;
        }
    }
}
