package com.qunshang.wenpaitong.equnshang.data;

public class MyPhysicalStoreBean {

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

        VendorDetail vendorDetail;

        AnalyzeData analyzeData;

        String shareUrl;

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public VendorDetail getVendorDetail() {
            return vendorDetail;
        }

        public void setVendorDetail(VendorDetail vendorDetail) {
            this.vendorDetail = vendorDetail;
        }

        public AnalyzeData getAnalyzeData() {
            return analyzeData;
        }

        public void setAnalyzeData(AnalyzeData analyzeData) {
            this.analyzeData = analyzeData;
        }

        public static class VendorDetail{

            int vendorId;

            String vendorName;

            String vendorHeadImg;

            int isOpen;

            int status;

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

            public int getIsOpen() {
                return isOpen;
            }

            public void setIsOpen(int isOpen) {
                this.isOpen = isOpen;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class AnalyzeData {

            int todayScanUserCount;

            int weekScanUserCount;

            int totalScanUserCount;

            int vendorPictureCount;

            int vendorVideoCount;

            public int getTodayScanUserCount() {
                return todayScanUserCount;
            }

            public void setTodayScanUserCount(int todayScanUserCount) {
                this.todayScanUserCount = todayScanUserCount;
            }

            public int getWeekScanUserCount() {
                return weekScanUserCount;
            }

            public void setWeekScanUserCount(int weekScanUserCount) {
                this.weekScanUserCount = weekScanUserCount;
            }

            public int getTotalScanUserCount() {
                return totalScanUserCount;
            }

            public void setTotalScanUserCount(int totalScanUserCount) {
                this.totalScanUserCount = totalScanUserCount;
            }

            public int getVendorPictureCount() {
                return vendorPictureCount;
            }

            public void setVendorPictureCount(int vendorPictureCount) {
                this.vendorPictureCount = vendorPictureCount;
            }

            public int getVendorVideoCount() {
                return vendorVideoCount;
            }

            public void setVendorVideoCount(int vendorVideoCount) {
                this.vendorVideoCount = vendorVideoCount;
            }
        }

    }

}
