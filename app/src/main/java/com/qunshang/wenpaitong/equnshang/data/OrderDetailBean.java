package com.qunshang.wenpaitong.equnshang.data;

import java.util.StringTokenizer;

public class OrderDetailBean {

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

        int orderId;

        String orderSn;

        int orderStatus;

        int payType;

        double orderProductAmount;

        double orderFreightAmount;

        double orderPayAmount;

        double orderDiscountAmount;

        double orderCouponAmount;

        Address address;

        Manufacture manufacture;

        Product product;

        String createTime;

        String expiredTime;

        String paymentTime;

        String deliveryTime;

        String receiveTime;

        String commentTime;

        String credit;

        String note;

        String orderGroupSn;

        String afterSaleSn;

        String statusDesc;

        public String getOrderGroupSn() {
            return orderGroupSn;
        }

        public void setOrderGroupSn(String orderGroupSn) {
            this.orderGroupSn = orderGroupSn;
        }

        public String getAfterSaleSn() {
            return afterSaleSn;
        }

        public void setAfterSaleSn(String afterSaleSn) {
            this.afterSaleSn = afterSaleSn;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public double getOrderCouponAmount() {
            return orderCouponAmount;
        }

        public void setOrderCouponAmount(double orderCouponAmount) {
            this.orderCouponAmount = orderCouponAmount;
        }

        public void setOrderProductAmount(double orderProductAmount) {
            this.orderProductAmount = orderProductAmount;
        }

        public void setOrderFreightAmount(double orderFreightAmount) {
            this.orderFreightAmount = orderFreightAmount;
        }

        public void setOrderPayAmount(double orderPayAmount) {
            this.orderPayAmount = orderPayAmount;
        }

        public double getOrderDiscountAmount() {
            return orderDiscountAmount;
        }

        public void setOrderDiscountAmount(double orderDiscountAmount) {
            this.orderDiscountAmount = orderDiscountAmount;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
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

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public double getOrderProductAmount() {
            return orderProductAmount;
        }

        public void setOrderProductAmount(int orderProductAmount) {
            this.orderProductAmount = orderProductAmount;
        }

        public double getOrderFreightAmount() {
            return orderFreightAmount;
        }

        public void setOrderFreightAmount(int orderFreightAmount) {
            this.orderFreightAmount = orderFreightAmount;
        }

        public double getOrderPayAmount() {
            return orderPayAmount;
        }

        public void setOrderPayAmount(int orderPayAmount) {
            this.orderPayAmount = orderPayAmount;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Manufacture getManufacture() {
            return manufacture;
        }

        public void setManufacture(Manufacture manufacture) {
            this.manufacture = manufacture;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getPaymentTime() {
            return paymentTime;
        }

        public void setPaymentTime(String paymentTime) {
            this.paymentTime = paymentTime;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getCommentTime() {
            return commentTime;
        }

        public void setCommentTime(String commentTime) {
            this.commentTime = commentTime;
        }

        public class Address{

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

        public class Manufacture{

            int manufactureId;

            String manufactureName;

            String manufactureHeadImgUrl;

            public String getManufactureHeadImgUrl() {
                return manufactureHeadImgUrl;
            }

            public void setManufactureHeadImgUrl(String manufactureHeadImgUrl) {
                this.manufactureHeadImgUrl = manufactureHeadImgUrl;
            }

            public int getManufactureId() {
                return manufactureId;
            }

            public void setManufactureId(int manufactureId) {
                this.manufactureId = manufactureId;
            }

            public String getManufactureName() {
                return manufactureName;
            }

            public void setManufactureName(String manufactureName) {
                this.manufactureName = manufactureName;
            }
        }

        public class Product{

            int productId;

            String productName;

            SkuList skuList;

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

            public SkuList getSkuList() {
                return skuList;
            }

            public void setSkuList(SkuList skuList) {
                this.skuList = skuList;
            }

            public class SkuList{

                int id;

                String value;

                double price;

                String number;

                String posterUrl;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getPosterUrl() {
                    return posterUrl;
                }

                public void setPosterUrl(String posterUrl) {
                    this.posterUrl = posterUrl;
                }
            }

        }

    }

}
