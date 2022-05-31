package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallV3ProductHistoryBean {

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

        private int viewId;
        private int productId;
        private String productName;
        private String productPosterUrl;
        private int sale;
        private double vipGroupPrice;
        private double marketPrice;
        private String tag;

        public int getViewId() {
            return viewId;
        }

        public void setViewId(int collectId) {
            this.viewId = collectId;
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

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }

        public double getVipGroupPrice() {
            return vipGroupPrice;
        }

        public void setVipGroupPrice(double vipGroupPrice) {
            this.vipGroupPrice = vipGroupPrice;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

}
