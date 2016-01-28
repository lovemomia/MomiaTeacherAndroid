package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/28.
 */
public class IMGroupMemberModel extends BaseModel {

    private IMGroupMemberData data;

    public IMGroupMemberData getData() {
        return data;
    }

    public void setData(IMGroupMemberData data) {
        this.data = data;
    }

    public static class IMGroupMemberData {
        private List<User> teachers;
        private List<User> customers;

        public List<User> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<User> teachers) {
            this.teachers = teachers;
        }

        public List<User> getCustomers() {
            return customers;
        }

        public void setCustomers(List<User> customers) {
            this.customers = customers;
        }
    }
}
