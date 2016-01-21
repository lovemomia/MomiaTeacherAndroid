package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/20.
 */
public class StudentRecordModel extends BaseModel {

    private StudentRecordData data;

    public StudentRecordData getData() {
        return data;
    }

    public void setData(StudentRecordData data) {
        this.data = data;
    }

    public static class StudentRecordTag {

        private long id;
        private String name;
        private boolean selected;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public static class StudentRecordData {

        private Student child;
        private List<StudentRecordTag> tags;
        private String content;

        public Student getChild() {
            return child;
        }

        public void setChild(Student child) {
            this.child = child;
        }

        public List<StudentRecordTag> getTags() {
            return tags;
        }

        public void setTags(List<StudentRecordTag> tags) {
            this.tags = tags;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
