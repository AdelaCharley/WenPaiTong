package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class IncomeDetailBean {

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

        String orderName;

        String orderTime;

        double orderMoney;

        double profitMoney;

        String userId;

        String userName;

        String state;

        public String getOrderName() {
            return orderName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public double getOrderMoney() {
            return orderMoney;
        }

        public void setOrderMoney(double orderMoney) {
            this.orderMoney = orderMoney;
        }

        public double getProfitMoney() {
            return profitMoney;
        }

        public void setProfitMoney(double profitMoney) {
            this.profitMoney = profitMoney;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}
