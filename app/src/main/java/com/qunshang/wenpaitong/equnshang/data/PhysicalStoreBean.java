package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class PhysicalStoreBean {

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

        int vendorId;

        String vendorName;

        String vendorHeadImg;

        String vendorDesc;

        String distance;

        String tag;

        public int getVendorId() {
            return vendorId;
        }

        public void setVendorId(int vendorId) {
            this.vendorId = vendorId;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getVendorHeadImg() {
            return vendorHeadImg;
        }

        public void setVendorHeadImg(String vendorHeadImg) {
            this.vendorHeadImg = vendorHeadImg;
        }

        public String getVendorDesc() {
            return vendorDesc;
        }

        public void setVendorDesc(String vendorDesc) {
            this.vendorDesc = vendorDesc;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

}
