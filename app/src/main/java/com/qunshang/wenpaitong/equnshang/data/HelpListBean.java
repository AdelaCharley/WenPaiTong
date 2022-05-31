package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class HelpListBean {

    int code;

    String msg;

    List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        String userName;

        String helpTime;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHelpTime() {
            return helpTime;
        }

        public void setHelpTime(String helpTime) {
            this.helpTime = helpTime;
        }
    }

}
