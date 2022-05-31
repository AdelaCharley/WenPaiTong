package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class BMallProductBean {

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

        int productId;

        String productName;

        String productPosterUrl;

        double price;

        int skuNum;

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

        public int getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(int skuNum) {
            this.skuNum = skuNum;
        }
    }

}
