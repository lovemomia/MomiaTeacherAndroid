package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/15.
 */
public class CourseGoingModel extends BaseModel {

    private CourseGoingData data;

    public CourseGoingData getData() {
        return data;
    }

    public void setData(CourseGoingData data) {
        this.data = data;
    }

    public static class CourseGoingData {

        private Course course;
        private List<Student> students;

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }
    }
}
