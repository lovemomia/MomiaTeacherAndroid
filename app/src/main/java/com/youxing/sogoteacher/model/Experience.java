package com.youxing.sogoteacher.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jun Deng on 16/1/12.
 */
public class Experience implements Parcelable {

    private long id;
    private String school;
    private String post;
    private String time;
    private String content;

    public Experience() {

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 序列化

    public Experience(Parcel parcel) {
        id = parcel.readLong();
        school = parcel.readString();
        post = parcel.readString();
        time = parcel.readString();
        content = parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(school);
        dest.writeString(post);
        dest.writeString(time);
        dest.writeString(content);
    }
    public static final Parcelable.Creator<Experience> CREATOR = new Parcelable.Creator<Experience>() {
        @Override
        public Experience createFromParcel(Parcel source) {
            return new Experience(source);
        }
        @Override
        public Experience[] newArray(int size) {
            return new Experience[size];
        }
    };
}

