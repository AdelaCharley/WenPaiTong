package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallProductBeanV2 {

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

        int isControlPrice;

        double controlPrice;

        double price;

        double vipPrice;

        double vipGroupPrice;

        int total;

        int current;

        int sale;

        Discount discount;

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

        public int getIsControlPrice() {
            return isControlPrice;
        }

        public void setIsControlPrice(int isControlPrice) {
            this.isControlPrice = isControlPrice;
        }

        public double getControlPrice() {
            return controlPrice;
        }

        public void setControlPrice(double controlPrice) {
            this.controlPrice = controlPrice;
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

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }

        public Discount getDiscount() {
            return discount;
        }

        public void setDiscount(Discount discount) {
            this.discount = discount;
        }

        public static class Discount {

            String discountName;

            String discountInfo;

            public String getDiscountName() {
                return discountName;
            }

            public void setDiscountName(String discountName) {
                this.discountName = discountName;
            }

            public String getDiscountInfo() {
                return discountInfo;
            }

            public void setDiscountInfo(String discountInfo) {
                this.discountInfo = discountInfo;
            }
        }

    }

}
