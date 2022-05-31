package com.qunshang.wenpaitong.equnshang.data;

public class NewRenMaiPeopleBean {

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

        int directPeopleNum;

        int allPeopleNum;

        public int getDirectPeopleNum() {
            return directPeopleNum;
        }

        public void setDirectPeopleNum(int directPeopleNum) {
            this.directPeopleNum = directPeopleNum;
        }

        public int getAllPeopleNum() {
            return allPeopleNum;
        }

        public void setAllPeopleNum(int allPeopleNum) {
            this.allPeopleNum = allPeopleNum;
        }
    }

}
