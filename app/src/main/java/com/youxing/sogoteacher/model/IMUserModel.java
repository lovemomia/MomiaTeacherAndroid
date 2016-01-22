package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 16/1/22.
 */
public class IMUserModel extends BaseModel {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
