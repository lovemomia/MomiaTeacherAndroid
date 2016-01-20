package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/18.
 */
public class StudentDetailModel extends BaseModel {

    private StudentDetailData data;

    public StudentDetailData getData() {
        return data;
    }

    public void setData(StudentDetailData data) {
        this.data = data;
    }

    public static class StudentDetailComment {

        private String date;
        private String title;
        private String content;
        private String teacher;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }
    }

    public static class StudentDetailCommentList {

        private List<StudentDetailComment> list;
        private int nextIndex;
        private int totalCount;

        public List<StudentDetailComment> getList() {
            return list;
        }

        public void setList(List<StudentDetailComment> list) {
            this.list = list;
        }

        public int getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class StudentDetailData {

        private Student child;
        private StudentDetailCommentList comments;

        public Student getChild() {
            return child;
        }

        public void setChild(Student student) {
            this.child = student;
        }

        public StudentDetailCommentList getComments() {
            return comments;
        }

        public void setComments(StudentDetailCommentList comments) {
            this.comments = comments;
        }
    }
}
