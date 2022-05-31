package com.qunshang.wenpaitong.equnshang.data;


import java.util.List;

public class AMallV3SearchProductBean {

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

        List<ProductBean> productList;

        int productCount;

        public List<ProductBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductBean> productList) {
            this.productList = productList;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public static class ProductBean {

            int productId;

            String productName;

            String productPosterUrl;

            int isControlPrice;

            double controlPrice;

            double vipGroupPrice;

            double marketPrice;

            String tag;

            int sale;

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
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

}
