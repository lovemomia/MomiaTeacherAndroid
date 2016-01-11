package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class OrderPersonListModel extends BaseModel {

    private List<OrderPerson> data;

    public List<OrderPerson> getData() {
        return data;
    }

    public void setData(List<OrderPerson> data) {
        this.data = data;
    }
}
