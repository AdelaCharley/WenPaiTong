package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class WeekHistoryTaskBean {

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

        String taskUserRelationId;

        int helpNum;

        String prizeName;

        String prizeImgSrc;

        String taskTime;

        int status;

        public String getTaskUserRelationId() {
            return taskUserRelationId;
        }

        public void setTaskUserRelationId(String taskUserRelationId) {
            this.taskUserRelationId = taskUserRelationId;
        }

        public int getHelpNum() {
            return helpNum;
        }

        public void setHelpNum(int helpNum) {
            this.helpNum = helpNum;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public String getPrizeImgSrc() {
            return prizeImgSrc;
        }

        public void setPrizeImgSrc(String prizeImgSrc) {
            this.prizeImgSrc = prizeImgSrc;
        }

        public String getTaskTime() {
            return taskTime;
        }

        public void setTaskTime(String taskTime) {
            this.taskTime = taskTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}
