package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;

public class WenBanTongOrderDetailBean implements Serializable {

    private Integer code;

    private String msg;

    DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        WenBanTongZiTiBean.DataBean user;

        WenBanTongOrderListBean.DataBean.OrderBean order;

        WenBanTongOrderListBean.DataBean.ProductBean product;

        WenBanTongOrderListBean.DataBean.CompanyBean company;

        PickBean pickUpAddress;

        public WenBanTongZiTiBean.DataBean getUser() {
            return user;
        }

        public void setUser(WenBanTongZiTiBean.DataBean user) {
            this.user = user;
        }

        public WenBanTongOrderListBean.DataBean.OrderBean getOrder() {
            return order;
        }

        public void setOrder(WenBanTongOrderListBean.DataBean.OrderBean order) {
            this.order = order;
        }

        public WenBanTongOrderListBean.DataBean.ProductBean getProduct() {
            return product;
        }

        public void setProduct(WenBanTongOrderListBean.DataBean.ProductBean product) {
            this.product = product;
        }

        public WenBanTongOrderListBean.DataBean.CompanyBean getCompany() {
            return company;
        }

        public void setCompany(WenBanTongOrderListBean.DataBean.CompanyBean company) {
            this.company = company;
        }

        public PickBean getPickUpAddress() {
            return pickUpAddress;
        }

        public void setPickUpAddress(PickBean pickUpAddress) {
            this.pickUpAddress = pickUpAddress;
        }

        public static class PickBean implements Serializable {

            String name;

            String phone;

            String site;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getSite() {
                return site;
            }

            public void setSite(String site) {
                this.site = site;
            }
        }

    }

}
