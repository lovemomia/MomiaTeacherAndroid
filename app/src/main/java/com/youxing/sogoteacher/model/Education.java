package com.youxing.sogoteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jun Deng on 16/1/12.
 */
public class Education implements Parcelable {

    private long id;
    private String school;
    private String major;
    private String level;
    private String time;

    public Education() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // 序列化

    public Education(Parcel parcel) {
        id = parcel.readLong();
        school = parcel.readString();
        major = parcel.readString();
        time = parcel.readString();
        level = parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(school);
        dest.writeString(major);
        dest.writeString(time);
        dest.writeString(level);
    }
    public static final Parcelable.Creator<Education> CREATOR = new Parcelable.Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel source) {
            return new Education(source);
        }
        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };
}
