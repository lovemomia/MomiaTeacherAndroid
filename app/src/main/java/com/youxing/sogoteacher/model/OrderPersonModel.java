package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/8/26.
 */
public class OrderPersonModel extends BaseModel {

    private OrderPerson data;

    public OrderPerson getData() {
        return data;
    }

    public void setData(OrderPerson data) {
        this.data = data;
    }
}
