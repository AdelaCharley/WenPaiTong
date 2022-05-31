package com.qunshang.wenpaitong.equnshang.data;

//import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ExchangeBean {

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

        private String orderSn;
        private int type;

        String afterSaleSn;
        private int productId;
        private String productName;
        private String skuPic;
        private int productQuantity;
        private String skuInfo;
        private int status;
        private String statusText;

        private double productSinglePrice;

        public double getProductSinglePrice() {
            return productSinglePrice;
        }

        public void setProductSinglePrice(double productSinglePrice) {
            this.productSinglePrice = productSinglePrice;
        }

        VendorAddress vendorAddress;

        public VendorAddress getVendorAddress() {
            return vendorAddress;
        }

        public void setVendorAddress(VendorAddress vendorAddress) {
            this.vendorAddress = vendorAddress;
        }

        public static class VendorAddress implements Serializable {
            String vendorAddressName;

            String vendorAddressPhone;

            String vendorAddressLocation;

            public String getVendorAddressName() {
                return vendorAddressName;
            }

            public void setVendorAddressName(String vendorAddressName) {
                this.vendorAddressName = vendorAddressName;
            }

            public String getVendorAddressPhone() {
                return vendorAddressPhone;
            }

            public void setVendorAddressPhone(String vendorAddressPhone) {
                this.vendorAddressPhone = vendorAddressPhone;
            }

            public String getVendorAddressLocation() {
                return vendorAddressLocation;
            }

            public void setVendorAddressLocation(String vendorAddressLocation) {
                this.vendorAddressLocation = vendorAddressLocation;
            }
        }

        public String getAfterSaleSn() {
            return afterSaleSn;
        }

        public void setAfterSaleSn(String afterSaleSn) {
            this.afterSaleSn = afterSaleSn;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getSkuPic() {
            return skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }

        public int getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(int productQuantity) {
            this.productQuantity = productQuantity;
        }

        public String getSkuInfo() {
            return skuInfo;
        }

        public void setSkuInfo(String skuInfo) {
            this.skuInfo = skuInfo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }
    }
}
