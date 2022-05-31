package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.List;

public class WenBanTongOrderListBean implements Serializable {

    private Integer code;

    private String msg;

    List<DataBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

        OrderBean order;

        ProductBean product;

        CompanyBean company;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public CompanyBean getCompany() {
            return company;
        }

        public void setCompany(CompanyBean company) {
            this.company = company;
        }

        public static class OrderBean implements Serializable {

            int orderId;

            String orderSn;

            int orderStatus;

            String payAmount;

            int productQuantity;

            String deliveryTime;

            String deliveryCode;

            public String getDeliveryTime() {
                return deliveryTime;
            }

            public void setDeliveryTime(String deliveryTime) {
                this.deliveryTime = deliveryTime;
            }

            String createTime;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getProductQuantity() {
                return productQuantity;
            }

            public void setProductQuantity(int productQuantity) {
                this.productQuantity = productQuantity;
            }

            public String getDeliveryCode() {
                return deliveryCode;
            }

            public void setDeliveryCode(String deliveryCode) {
                this.deliveryCode = deliveryCode;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getOrderSn() {
                return orderSn;
            }

            public void setOrderSn(String orderSn) {
                this.orderSn = orderSn;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(String payAmount) {
                this.payAmount = payAmount;
            }
        }

        public static class ProductBean implements Serializable {

            int productId;

            String productName;

            String productPoster;

            String orderProductPrice;

            String productTag;

            public String getProductTag() {
                return productTag;
            }

            public void setProductTag(String productTag) {
                this.productTag = productTag;
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

            public String getProductPoster() {
                return productPoster;
            }

            public void setProductPoster(String productPoster) {
                this.productPoster = productPoster;
            }

            public String getOrderProductPrice() {
                return orderProductPrice;
            }

            public void setOrderProductPrice(String orderProductPrice) {
                this.orderProductPrice = orderProductPrice;
            }

        }

        public static class CompanyBean implements Serializable {

            int companyId;

            String companyName;

            String companyAvatar;

            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getCompanyAvatar() {
                return companyAvatar;
            }

            public void setCompanyAvatar(String companyAvatar) {
                this.companyAvatar = companyAvatar;
            }
        }

    }

}
