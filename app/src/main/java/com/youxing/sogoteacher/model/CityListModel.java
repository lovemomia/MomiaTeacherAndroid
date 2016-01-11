package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;
import com.youxing.common.model.City;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/27.
 */
public class CityListModel extends BaseModel {

    private List<City> data;

    public List<City> getData() {
        return data;
    }

    public void setData(List<City> data) {
        this.data = data;
    }
}
