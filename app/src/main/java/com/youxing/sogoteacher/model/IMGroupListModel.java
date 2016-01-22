package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/22.
 */
public class IMGroupListModel extends BaseModel {

    private List<IMGroup> data;

    public List<IMGroup> getData() {
        return data;
    }

    public void setData(List<IMGroup> data) {
        this.data = data;
    }
}
