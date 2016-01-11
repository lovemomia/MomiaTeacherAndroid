package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class ProductCalendarMonthModel extends BaseModel {

    private List<ProductCalendarGroup> data;

    public List<ProductCalendarGroup> getData() {
        return data;
    }

    public void setData(List<ProductCalendarGroup> data) {
        this.data = data;
    }

    public static class ProductCalendarGroup {
        private String date;
        private List<Product> products;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

}
