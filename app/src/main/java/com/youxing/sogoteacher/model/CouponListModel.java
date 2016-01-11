package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class CouponListModel extends BaseModel {

    private CouponListData data;

    public CouponListData getData() {
        return data;
    }

    public void setData(CouponListData data) {
        this.data = data;
    }

    public static class CouponListData {
        private long totalCount;
        private List<Coupon> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public List<Coupon> getList() {
            return list;
        }

        public void setList(List<Coupon> list) {
            this.list = list;
        }
    }
}
