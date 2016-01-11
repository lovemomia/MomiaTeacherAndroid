package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class OrderListModel extends BaseModel {

    private OrderListData data;

    public OrderListData getData() {
        return data;
    }

    public void setData(OrderListData data) {
        this.data = data;
    }

    public static class OrderListData {
        private long totalCount;
        private long nextIndex;
        private List<Order> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public List<Order> getList() {
            return list;
        }

        public void setList(List<Order> list) {
            this.list = list;
        }

        public long getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(long nextIndex) {
            this.nextIndex = nextIndex;
        }
    }
}
