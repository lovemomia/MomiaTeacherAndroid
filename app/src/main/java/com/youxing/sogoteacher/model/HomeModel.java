package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/6/17.
 */
public class HomeModel extends BaseModel {

    private HomeData data;

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }

    public static class HomeData {

        private List<HomeBanner> banners;
        private List<Product> products;
        private int nextpage;

        public List<HomeBanner> getBanners() {
            return banners;
        }

        public List<Product> getProducts() {
            return products;
        }

        public int getNextpage() {
            return nextpage;
        }

        public void setBanners(List<HomeBanner> banners) {
            this.banners = banners;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public void setNextpage(int nextpage) {
            this.nextpage = nextpage;
        }
    }

    public static class HomeBanner {

        private String cover;
        private String action;

        public String getAction() {
            return action;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

}
