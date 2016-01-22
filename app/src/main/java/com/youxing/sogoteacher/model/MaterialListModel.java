package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/22.
 */
public class MaterialListModel extends BaseModel {

    private MaterialListData data;

    public MaterialListData getData() {
        return data;
    }

    public void setData(MaterialListData data) {
        this.data = data;
    }

    public static class Material {

        private long id;
        private String cover;
        private String title;
        private String subject;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    public static class MaterialListData {

        private long totalCount;
        private long nextIndex;
        private List<Material> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public long getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(long nextIndex) {
            this.nextIndex = nextIndex;
        }

        public List<Material> getList() {
            return list;
        }

        public void setList(List<Material> list) {
            this.list = list;
        }
    }
}
