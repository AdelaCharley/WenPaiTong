package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class UserProductCollectionBean {

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

    public class DataBean {

        int productId;

        String productName;

        String productPosterUrl;

        double vipGroupPrice;

        double price;

        double vipPrice;

        int total;

        int current;

        int id;

        boolean isCheckBoxVisble = false;

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

        public double getVipGroupPrice() {
            return vipGroupPrice;
        }

        public void setVipGroupPrice(double vipGroupPrice) {
            this.vipGroupPrice = vipGroupPrice;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCheckBoxVisble(boolean checkBoxVisble) {
            isCheckBoxVisble = checkBoxVisble;
        }

        public boolean isCheckBoxVisble() {
            return isCheckBoxVisble;
        }
    }
}