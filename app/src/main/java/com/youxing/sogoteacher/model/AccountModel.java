package com.youxing.sogoteacher.model;

import com.youxing.common.model.Account;
import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class AccountModel extends BaseModel {

    private Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }
}
