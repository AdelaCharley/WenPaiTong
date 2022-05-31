package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class WeekPrizeHistoryBean {

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

        String taskUserRelationId;

        int finishedTaskStatus;

        int experienceId;

        PrizeInfo prizeInfo;

        ObtainInfo obtainInfo;

        List<HelpInfo> helpUserInfo;

        public String getTaskUserRelationId() {
            return taskUserRelationId;
        }

        public void setTaskUserRelationId(String taskUserRelationId) {
            this.taskUserRelationId = taskUserRelationId;
        }

        public int getFinishedTaskStatus() {
            return finishedTaskStatus;
        }

        public void setFinishedTaskStatus(int finishedTaskStatus) {
            this.finishedTaskStatus = finishedTaskStatus;
        }

        public int getExperienceId() {
            return experienceId;
        }

        public void setExperienceId(int experienceId) {
            this.experienceId = experienceId;
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

        public List<HelpInfo> getHelpUserInfo() {
            return helpUserInfo;
        }

        public void setHelpUserInfo(List<HelpInfo> helpUserInfo) {
            this.helpUserInfo = helpUserInfo;
        }

        public static class PrizeInfo {

            int prizeId;

            List<String> tags;

            String imageUrl;

            String name;

            public int getPrizeId() {
                return prizeId;
            }

            public void setPrizeId(int prizeId) {
                this.prizeId = prizeId;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ObtainInfo {

            AddressInfo addressInfo;

            public static class AddressInfo {

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

            String obtainTime;

            public AddressInfo getAddressInfo() {
                return addressInfo;
            }

            public void setAddressInfo(AddressInfo addressInfo) {
                this.addressInfo = addressInfo;
            }

            public String getObtainTime() {
                return obtainTime;
            }

            public void setObtainTime(String obtainTime) {
                this.obtainTime = obtainTime;
            }
        }

        public static class HelpInfo {

            String userName;

            String headimageUrl;

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getHeadimageUrl() {
                return headimageUrl;
            }

            public void setHeadimageUrl(String headimageUrl) {
                this.headimageUrl = headimageUrl;
            }
        }

    }

}
