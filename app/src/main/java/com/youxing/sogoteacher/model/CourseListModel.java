package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/1/11.
 */
public class CourseListModel extends BaseModel {

    private CourseListData data;

    public CourseListData getData() {
        return data;
    }

    public void setData(CourseListData data) {
        this.data = data;
    }

    public static class CourseListData {
        private long totalCount;
        private long nextIndex;
        private List<Course> list;

        public List<Course> getList() {
            return list;
        }

        public void setList(List<Course> list) {
            this.list = list;
        }

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
    }
}
