package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class ExpressBean {

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

    public static class DataBean{

        Product product;

        Express express;

        Address address;

        List<ExpressMsg> expressMsg;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Express getExpress() {
            return express;
        }

        public void setExpress(Express express) {
            this.express = express;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public List<ExpressMsg> getExpressMsg() {
            return expressMsg;
        }

        public void setExpressMsg(List<ExpressMsg> expressMsg) {
            this.expressMsg = expressMsg;
        }

        public static class Product{

            int productId;

            String productName;

            String productImage;

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

            public String getProductImage() {
                return productImage;
            }

            public void setProductImage(String productImage) {
                this.productImage = productImage;
            }
        }

        public static class Express{
            String expressName;

            String expressSN;

            String expressPhone;

            String expressLogo;

            public String getExpressPhone() {
                return expressPhone;
            }

            public void setExpressPhone(String expressPhone) {
                this.expressPhone = expressPhone;
            }

            public String getExpressLogo() {
                return expressLogo;
            }

            public void setExpressLogo(String expressLogo) {
                this.expressLogo = expressLogo;
            }

            public String getExpressName() {
                return expressName;
            }

            public void setExpressName(String expressName) {
                this.expressName = expressName;
            }

            public String getExpressSN() {
                return expressSN;
            }

            public void setExpressSN(String expressSN) {
                this.expressSN = expressSN;
            }
        }

        public static class Address{
            String addressName;

            String addressPhone;

            String addressSite;

            public String getAddressName() {
                return addressName;
            }

            public void setAddressName(String addressName) {
                this.addressName = addressName;
            }

            public String getAddressPhone() {
                return addressPhone;
            }

            public void setAddressPhone(String addressPhone) {
                this.addressPhone = addressPhone;
            }

            public String getAddressSite() {
                return addressSite;
            }

            public void setAddressSite(String addressSite) {
                this.addressSite = addressSite;
            }
        }

        public static class ExpressMsg{
            String time;

            String ftime;

            String context;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getFtime() {
                return ftime;
            }

            public void setFtime(String ftime) {
                this.ftime = ftime;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }
        }

    }

}
