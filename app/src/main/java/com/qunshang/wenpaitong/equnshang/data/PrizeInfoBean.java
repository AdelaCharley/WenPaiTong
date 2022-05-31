package com.qunshang.wenpaitong.equnshang.data;

public class PrizeInfoBean {

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

        int experienceId;

        NumberInfo numberInfo;

        PrizeInfo prizeInfo;

        ObtainInfo obtainInfo;

        public int getExperienceId() {
            return experienceId;
        }

        public void setExperienceId(int experienceId) {
            this.experienceId = experienceId;
        }

        public NumberInfo getNumberInfo() {
            return numberInfo;
        }

        public void setNumberInfo(NumberInfo numberInfo) {
            this.numberInfo = numberInfo;
        }

        public PrizeInfo getPrizeInfo() {
            return prizeInfo;
        }

        public void setPrizeInfo(PrizeInfo prizeInfo) {
            this.prizeInfo = prizeInfo;
        }

        public ObtainInfo getObtainInfo() {
            return obtainInfo;
        }

        public void setObtainInfo(ObtainInfo obtainInfo) {
            this.obtainInfo = obtainInfo;
        }

        public static class NumberInfo{

            long activityNumberRelationId;

            String number;

            String winTime;

            public long getActivityNumberRelationId() {
                return activityNumberRelationId;
            }

            public void setActivityNumberRelationId(long activityNumberRelationId) {
                this.activityNumberRelationId = activityNumberRelationId;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getWinTime() {
                return winTime;
            }

            public void setWinTime(String winTime) {
                this.winTime = winTime;
            }
        }

        public static class PrizeInfo {

            int prizeId;

            String prizeName;

            String prizeImageUrl;

            int prizeStatus;

            public int getPrizeId() {
                return prizeId;
            }

            public void setPrizeId(int prizeId) {
                this.prizeId = prizeId;
            }

            public String getPrizeName() {
                return prizeName;
            }

            public void setPrizeName(String prizeName) {
                this.prizeName = prizeName;
            }

            public String getPrizeImageUrl() {
                return prizeImageUrl;
            }

            public void setPrizeImageUrl(String prizeImageUrl) {
                this.prizeImageUrl = prizeImageUrl;
            }

            public int getPrizeStatus() {
                return prizeStatus;
            }

            public void setPrizeStatus(int prizeStatus) {
                this.prizeStatus = prizeStatus;
            }
        }

        public static class ObtainInfo {

            String obtainTime;

            AddressInfo addressInfo;

            public String getObtainTime() {
                return obtainTime;
            }

            public void setObtainTime(String obtainTime) {
                this.obtainTime = obtainTime;
            }

            public AddressInfo getAddressInfo() {
                return addressInfo;
            }

            public void setAddressInfo(AddressInfo addressInfo) {
                this.addressInfo = addressInfo;
            }

            public class AddressInfo {

                String name;

                String phone;

                String address;

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

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }
            }

        }

    }

}
