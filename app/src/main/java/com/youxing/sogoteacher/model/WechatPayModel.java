package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/8/21.
 */
public class WechatPayModel extends BaseModel {

    private WechatPayData data;

    public WechatPayData getData() {
        return data;
    }

    public void setData(WechatPayData data) {
        this.data = data;
    }

    public static class WechatPayData {
        private String appid;
        private String partnerid;
        private String package_app;
        private String timestamp;
        private String noncestr;
        private String prepayid;
        private String sign;
        private boolean successful;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPackage_app() {
            return package_app;
        }

        public void setPackage_app(String package_app) {
            this.package_app = package_app;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }
    }
}
