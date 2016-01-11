package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class ProductListModel extends BaseModel {

    private ProductListData data;

    public ProductListData getData() {
        return data;
    }

    public void setData(ProductListData data) {
        this.data = data;
    }

    public static class ProductListData {
        private long totalCount;
        private int nextIndex;
        private List<Product> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public int getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }

        public List<Product> getList() {
            return list;
        }

        public void setList(List<Product> list) {
            this.list = list;
        }
    }
}
