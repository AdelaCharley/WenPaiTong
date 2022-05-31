package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class ConcernData {

    private Integer code;
    private String msg;

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        int followId;
        String headimage_url;
        String uname;
        String create_time;

        public int getFollowId() {
            return followId;
        }

        public void setFollowId(int followId) {
            this.followId = followId;
        }

        public String getHeadimage_url() {
            return headimage_url;
        }

        public void setHeadimage_url(String headimage_url) {
            this.headimage_url = headimage_url;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }

}
