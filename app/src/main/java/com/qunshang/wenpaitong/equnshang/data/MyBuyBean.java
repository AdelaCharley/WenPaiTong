package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class MyBuyBean {

    int code;

    String msg;

    List<List<DataBean>> data;

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

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {

        int orderId;

        String manufactureName;

        String createTime;

        int orderStatus;

        int productId;

        String productName;

        String productPosterUrl;

        double price;

        int sellStatus;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getManufactureName() {
            return manufactureName;
        }

        public void setManufactureName(String manufactureName) {
            this.manufactureName = manufactureName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
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

        public String getProductPosterUrl() {
            return productPosterUrl;
        }

        public void setProductPosterUrl(String productPosterUrl) {
            this.productPosterUrl = productPosterUrl;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getSellStatus() {
            return sellStatus;
        }

        public void setSellStatus(int sellStatus) {
            this.sellStatus = sellStatus;
        }
    }

}
