package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/12.
 */
public class ApplyTeacherModel extends BaseModel {

    private ApplyTeacherData data;

    public ApplyTeacherData getData() {
        return data;
    }

    public void setData(ApplyTeacherData data) {
        this.data = data;
    }

    public static class ApplyTeacherData {
        private int status;
        private String msg;
        private String pic;
        private String name;
        private String idNo;
        private String sex;
        private String birthday;
        private String address;
        private List<Experience> experiences;
        private List<Education> educations;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<Experience> getExperiences() {
            return experiences;
        }

        public void setExperiences(List<Experience> experiences) {
            this.experiences = experiences;
        }

        public List<Education> getEducations() {
            return educations;
        }

        public void setEducations(List<Education> educations) {
            this.educations = educations;
        }
    }
}
