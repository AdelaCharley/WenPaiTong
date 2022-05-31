package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class PrizeNumberListBean {

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

    public void setData(List<DataBean> numberList) {
        this.data = numberList;
    }

    public static class DataBean {

        String kind;

        int level;

        List<NumberBean> numberList;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public List<NumberBean> getNumberList() {
            return numberList;
        }

        public void setNumberList(List<NumberBean> numberList) {
            this.numberList = numberList;
        }

        public static class NumberBean {

            String number;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }

    }

}
