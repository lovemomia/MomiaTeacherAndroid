package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/21.
 */
public class StudentListModel extends BaseModel {

    private List<Student> data;

    public List<Student> getData() {
        return data;
    }

    public void setData(List<Student> data) {
        this.data = data;
    }
}
