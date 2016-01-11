package com.youxing.common.model;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class BaseModel {

    private int errno;
    private String errmsg;
    private long time;

    public int getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public long getTime() {
        return time;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
