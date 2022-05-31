package com.qunshang.wenpaitong.equnshang.data;

public class CVendorDetailBean {

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

        String vendorHeadImg;

        String vendorName;

        String vendorPhone;

        String vendorOpeningTime;

        String vendorCloseTime;

        String vendorLocation;

        String vendorLocationName;

        double longitude;

        double latitude;

        String vendorDesc;

        int status;

        String errorMsg;

        String poiUid;

        String province;

        String city;

        String district;

        public int getVendorId() {
            return vendorId;
        }

        public void setVendorId(int vendorId) {
            this.vendorId = vendorId;
        }

        public String getVendorHeadImg() {
            return vendorHeadImg;
        }

        public void setVendorHeadImg(String vendorHeadImg) {
            this.vendorHeadImg = vendorHeadImg;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getVendorPhone() {
            return vendorPhone;
        }

        public void setVendorPhone(String vendorPhone) {
            this.vendorPhone = vendorPhone;
        }

        public String getVendorOpeningTime() {
            return vendorOpeningTime;
        }

        public void setVendorOpeningTime(String vendorOpeningTime) {
            this.vendorOpeningTime = vendorOpeningTime;
        }

        public String getVendorCloseTime() {
            return vendorCloseTime;
        }

        public void setVendorCloseTime(String vendorCloseTime) {
            this.vendorCloseTime = vendorCloseTime;
        }

        public String getVendorLocation() {
            return vendorLocation;
        }

        public void setVendorLocation(String vendorLocation) {
            this.vendorLocation = vendorLocation;
        }

        public String getVendorLocationName() {
            return vendorLocationName;
        }

        public void setVendorLocationName(String vendorLocationName) {
            this.vendorLocationName = vendorLocationName;
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

        public String getVendorDesc() {
            return vendorDesc;
        }

        public void setVendorDesc(String vendorDesc) {
            this.vendorDesc = vendorDesc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getPoiUid() {
            return poiUid;
        }

        public void setPoiUid(String poiUid) {
            this.poiUid = poiUid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
    }

}
