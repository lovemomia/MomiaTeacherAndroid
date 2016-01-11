package com.youxing.sogoteacher.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/8/28.
 */
public class PlayFellowModel extends BaseModel {

    private List<PlayFellowGroup> data;

    public List<PlayFellowGroup> getData() {
        return data;
    }

    public void setData(List<PlayFellowGroup> data) {
        this.data = data;
    }

    public static class PlayFellowGroup {
        private String joined;
        private String time;
        private boolean selected;
        private List<PlayFellowPerson> playmates;

        public String getJoined() {
            return joined;
        }

        public void setJoined(String joined) {
            this.joined = joined;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public List<PlayFellowPerson> getPlaymates() {
            return playmates;
        }

        public void setPlaymates(List<PlayFellowPerson> playmates) {
            this.playmates = playmates;
        }
    }

    public static class PlayFellowPerson {
        private long id;
        private String avatar;
        private String nickName;
        private List<String> children;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public List<String> getChildren() {
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }
    }
}
