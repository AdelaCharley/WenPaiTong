package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class MyCommentBean {

    private Integer code;
    private String msg;

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        String headimage;

        String uname;

        String time;

        String cotent;

        public String getHeadimage() {
            return headimage;
        }

        public void setHeadimage(String headimage) {
            this.headimage = headimage;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCotent() {
            return cotent;
        }

        public void setCotent(String cotent) {
            this.cotent = cotent;
        }
    }

}
