package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/8/21.
 */
public class PayCheckModel extends BaseModel {

    private PayCheckData data;

    public PayCheckData getData() {
        return data;
    }

    public void setData(PayCheckData data) {
        this.data = data;
    }

    public static class PayCheckData {
        private String thumb;
        private String title;
        private String abstracts;
        private String url;

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAbstracts() {
            return abstracts;
        }

        public void setAbstracts(String abstracts) {
            this.abstracts = abstracts;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
