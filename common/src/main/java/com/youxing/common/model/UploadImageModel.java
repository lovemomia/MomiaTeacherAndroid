package com.youxing.common.model;

/**
 * Created by Jun Deng on 15/8/24.
 */
public class UploadImageModel extends BaseModel {

    private UploadImageData data;

    public UploadImageData getData() {
        return data;
    }

    public void setData(UploadImageData data) {
        this.data = data;
    }

    public static class UploadImageData {
        private String height;
        private String width;
        private String path;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }

}
