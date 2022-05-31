package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class SendCommentBean {

    private Integer code;
    private String msg;
    private List<DataBean> data;

    public static class DataBean{

    }

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
