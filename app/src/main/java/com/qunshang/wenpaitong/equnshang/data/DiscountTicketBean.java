package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.List;

public class DiscountTicketBean {

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

    public static class DataBean implements Serializable {

        int userCouponsId;

        double price;

        int relationId;

        String useType;

        String relationName;

        int minPoint;

        String overTime;

        int useStatus;

        public String getUseType() {
            return useType;
        }

        public void setUseType(String useType) {
            this.useType = useType;
        }

        public int getRelationId() {
            return relationId;
        }

        public void setRelationId(int relationId) {
            this.relationId = relationId;
        }

        public String getRelationName() {
            return relationName;
        }

        public void setRelationName(String relationName) {
            this.relationName = relationName;
        }

        public int getMinPoint() {
            return minPoint;
        }

        public void setMinPoint(int minPoint) {
            this.minPoint = minPoint;
        }

        public int getUserCouponsId() {
            return userCouponsId;
        }

        public void setUserCouponsId(int userCouponsId) {
            this.userCouponsId = userCouponsId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getOverTime() {
            return overTime;
        }

        public void setOverTime(String overTime) {
            this.overTime = overTime;
        }

        public int getUseStatus() {
            return useStatus;
        }

        public void setUseStatus(int useStatus) {
            this.useStatus = useStatus;
        }
    }

}
