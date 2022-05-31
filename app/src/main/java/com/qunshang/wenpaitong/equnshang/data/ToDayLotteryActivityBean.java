package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class ToDayLotteryActivityBean {

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

        int activityId;

        String endTime;

        int peopleNumber;

        int prizeId;

        String description;

        String prizeImageUrl;

        double prizePrice;

        List<String> tags;

        int status;

        List<ParticipateNumberList> participateNumberList;

        public static class ParticipateNumberList {

            String number;

            int status;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public int getNumber_status() {
                return status;
            }

            public void setNumber_status(int number_status) {
                this.status = number_status;
            }
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getPeopleNumber() {
            return peopleNumber;
        }

        public void setPeopleNumber(int peopleNumber) {
            this.peopleNumber = peopleNumber;
        }

        public int getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(int prizeId) {
            this.prizeId = prizeId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrizeImageUrl() {
            return prizeImageUrl;
        }

        public void setPrizeImageUrl(String prizeImageUrl) {
            this.prizeImageUrl = prizeImageUrl;
        }

        public double getPrizePrice() {
            return prizePrice;
        }

        public void setPrizePrice(double prizePrice) {
            this.prizePrice = prizePrice;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ParticipateNumberList> getParticipateNumberList() {
            return participateNumberList;
        }

        public void setParticipateNumberList(List<ParticipateNumberList> participateNumberList) {
            this.participateNumberList = participateNumberList;
        }
    }

}
