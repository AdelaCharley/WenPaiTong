package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallV3StarInfoBean {


    private Integer code;

    private String msg;

    List<DataBean> data;

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

    public static class DataBean {

        String behaviorTxt;

        int number;

        public String getBehaviorTxt() {
            return behaviorTxt;
        }

        public void setBehaviorTxt(String behaviorTxt) {
            this.behaviorTxt = behaviorTxt;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

}
