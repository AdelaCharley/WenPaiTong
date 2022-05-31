package com.qunshang.wenpaitong.equnshang.data;

public class FreightMoneyBean {

    int code;

    String msg;

    DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        double freightAmount;

        public double getFreightAmount() {
            return freightAmount;
        }

        public void setFreightAmount(double freightAmount) {
            this.freightAmount = freightAmount;
        }
    }

}
