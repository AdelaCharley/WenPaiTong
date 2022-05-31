package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.ArrayList;

public class VendorDetailBean {

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

        int vendorId;

        String mainImage;

        String vendorName;

        String vendorDesc;

        String vendorPhone;

        String businessTime;

        String vendorLocation;

        double longitude;

        double latitude;

        String errorMsg;

        int status;

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        ArrayList<Image> vendorImageList;

        public int getVendorId() {
            return vendorId;
        }

        public void setVendorId(int vendorId) {
            this.vendorId = vendorId;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getVendorDesc() {
            return vendorDesc;
        }

        public void setVendorDesc(String vendorDesc) {
            this.vendorDesc = vendorDesc;
        }

        public String getVendorPhone() {
            return vendorPhone;
        }

        public void setVendorPhone(String vendorPhone) {
            this.vendorPhone = vendorPhone;
        }

        public String getBusinessTime() {
            return businessTime;
        }

        public void setBusinessTime(String businessTime) {
            this.businessTime = businessTime;
        }

        public String getVendorLocation() {
            return vendorLocation;
        }

        public void setVendorLocation(String vendorLocation) {
            this.vendorLocation = vendorLocation;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public ArrayList<Image> getVendorImageList() {
            return vendorImageList;
        }

        public void setVendorImageList(ArrayList<Image> vendorImageList) {
            this.vendorImageList = vendorImageList;
        }

        public static class Image implements Serializable{

            int pictureId;

            String pictureUrl;

            public int getPictureId() {
                return pictureId;
            }

            public void setPictureId(int pictureId) {
                this.pictureId = pictureId;
            }

            public String getPictureUrl() {
                return pictureUrl;
            }

            public void setPictureUrl(String pictureUrl) {
                this.pictureUrl = pictureUrl;
            }
        }

    }

}
