package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.List;

public class OrderBean {

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

        int orderId;

        String orderSn;

        String orderGroupSn;

        int manufactureId;

        String manufactureName;

        int status;

        String productPosterUrl;

        int productId;

        double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        String productName;

        String productSkuName;

        String productSkuNumber;

        double singlePrice;

        double payAmount;

        public double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(double payAmount) {
            this.payAmount = payAmount;
        }

        String expiredTime;

        String credit;

        String getCreditTime;

        double freightAmount;

        String afterSaleSn;

        public String getAfterSaleSn() {
            return afterSaleSn;
        }

        public void setAfterSaleSn(String afterSaleSn) {
            this.afterSaleSn = afterSaleSn;
        }

        public double getFreightAmount() {
            return freightAmount;
        }

        public void setFreightAmount(double freightAmount) {
            this.freightAmount = freightAmount;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getManufactureId() {
            return manufactureId;
        }

        public void setManufactureId(int manufactureId) {
            this.manufactureId = manufactureId;
        }

        public String getManufactureName() {
            return manufactureName;
        }

        public void setManufactureName(String manufactureName) {
            this.manufactureName = manufactureName;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public String getOrderGroupSn() {
            return orderGroupSn;
        }

        public void setOrderGroupSn(String orderGroupSn) {
            this.orderGroupSn = orderGroupSn;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getProductPosterUrl() {
            return productPosterUrl;
        }

        public void setProductPosterUrl(String productPosterUrl) {
            this.productPosterUrl = productPosterUrl;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductSkuName() {
            return productSkuName;
        }

        public void setProductSkuName(String productSkuName) {
            this.productSkuName = productSkuName;
        }

        public String getProductSkuNumber() {
            return productSkuNumber;
        }

        public void setProductSkuNumber(String productSkuNumber) {
            this.productSkuNumber = productSkuNumber;
        }

        public double getSinglePrice() {
            return singlePrice;
        }

        public void setSinglePrice(double singlePrice) {
            this.singlePrice = singlePrice;
        }

        public String getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getGetCreditTime() {
            return getCreditTime;
        }

        public void setGetCreditTime(String getCreditTime) {
            this.getCreditTime = getCreditTime;
        }
    }

}
