package com.youxing.common.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class Account {

    private String token;
    private String avatar;
    private String birthday;
    private String name;
    private String nickName;
    private String sex;
    private String mobile;
    private String address;
    private List<Child> children;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public String getAgeOfChild() {
        if (children == null || children.size() == 0) {
            return null;
        }
        Child child = children.get(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(child.getBirthday());
        } catch (ParseException e) {
            return null;
        }

        Calendar childCalendar = Calendar.getInstance();
        childCalendar.setTime(date);
        int childYear = childCalendar.get(Calendar.YEAR);
        int childMonth = childCalendar.get(Calendar.MONTH);
        int childDay = childCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        int nowDay = childCalendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder(child.getSex() + "孩");
        if (nowYear < childYear || (nowYear == childYear && nowMonth < childMonth)) {
            return sb.append("还未出生").toString();
        }

        int age = nowYear - childYear - 1;
        if ((nowMonth > childMonth) || (nowMonth == childMonth && nowDay >= childDay)) {
            age++;
        }

        if (age > 0) {
            if (age == 1 && nowMonth < childMonth) {
                int month = 12 + nowMonth - childMonth;
                return sb.append(month + "个月").toString();
            } else {
                return sb.append(age + "岁").toString();
            }
        }

        // 几个月
        int month = nowMonth - childMonth;
        return sb.append(month + "个月").toString();
    }

}
