package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class VersionBean {

    private Integer code;

    private String msg;

    DataBean data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        int status;

        String msg;

        String url;

        List<String> updateFeature;

        public List<String> getUpdateFeature() {
            return updateFeature;
        }

        public void setUpdateFeature(List<String> updateFeature) {
            this.updateFeature = updateFeature;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
