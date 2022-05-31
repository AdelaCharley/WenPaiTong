package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallProductBean {

    private Integer code;
    private String msg;

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {

        int productId;

        String productName;

        String productPosterUrl;

        double price;

        double vipPrice;

        double vipGroupPrice;

        int total;

        int current;

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

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public double getVipGroupPrice() {
            return vipGroupPrice;
        }

        public void setVipGroupPrice(double vipGroupPrice) {
            this.vipGroupPrice = vipGroupPrice;
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
    }

}
