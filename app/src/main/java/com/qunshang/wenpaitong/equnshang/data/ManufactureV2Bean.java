package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManufactureV2Bean implements Serializable {

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

    public static class DataBean implements Serializable {

        /**
         * Copyright 2021 json.cn
         */
        /**
         * Auto-generated: 2021-11-23 14:32:25
         *
         * @author json.cn (i@json.cn)
         * @website http://www.json.cn/java2pojo/
         */
        private int manufactureId;
        private String manufactureLogoUrl;
        private String manufactureName;
        private int isFocus;
        private String totalSale;
        private int productCount;
        private String openDate;
        private String location;
        private String businessLicence;
        private String desc;
        private String servicePhone;

        ArrayList<String> allBusinessLicence;

        public ArrayList<String> getAllBusinessLicence() {
            return allBusinessLicence;
        }

        public void setAllBusinessLicence(ArrayList<String> allBusinessLicence) {
            this.allBusinessLicence = allBusinessLicence;
        }

        public int getManufactureId() {
            return manufactureId;
        }

        public void setManufactureId(int manufactureId) {
            this.manufactureId = manufactureId;
        }

        public String getManufactureLogoUrl() {
            return manufactureLogoUrl;
        }

        public void setManufactureLogoUrl(String manufactureLogoUrl) {
            this.manufactureLogoUrl = manufactureLogoUrl;
        }

        public String getManufactureName() {
            return manufactureName;
        }

        public void setManufactureName(String manufactureName) {
            this.manufactureName = manufactureName;
        }

        public int getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(int isFocus) {
            this.isFocus = isFocus;
        }

        public String getTotalSale() {
            return totalSale;
        }

        public void setTotalSale(String totalSale) {
            this.totalSale = totalSale;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public String getOpenDate() {
            return openDate;
        }

        public void setOpenDate(String openDate) {
            this.openDate = openDate;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getBusinessLicence() {
            return businessLicence;
        }

        public void setBusinessLicence(String businessLicence) {
            this.businessLicence = businessLicence;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getServicePhone() {
            return servicePhone;
        }

        public void setServicePhone(String servicePhone) {
            this.servicePhone = servicePhone;
        }
    }

}
