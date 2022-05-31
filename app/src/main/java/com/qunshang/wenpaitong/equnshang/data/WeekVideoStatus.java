package com.qunshang.wenpaitong.equnshang.data;

public class WeekVideoStatus {

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

        int status;

        String taskEndTime;

        int inviteNumber;

        int taskUserRelationId;

        int isEnd;

        int isStop;

        String prizeImageRow;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTaskEndTime() {
            return taskEndTime;
        }

        public void setTaskEndTime(String taskEndTime) {
            this.taskEndTime = taskEndTime;
        }

        public int getInviteNumber() {
            return inviteNumber;
        }

        public void setInviteNumber(int inviteNumber) {
            this.inviteNumber = inviteNumber;
        }

        public int getTaskUserRelationId() {
            return taskUserRelationId;
        }

        public void setTaskUserRelationId(int taskUserRelationId) {
            this.taskUserRelationId = taskUserRelationId;
        }

        public int getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(int isEnd) {
            this.isEnd = isEnd;
        }

        public int getIsStop() {
            return isStop;
        }

        public void setIsStop(int isStop) {
            this.isStop = isStop;
        }

        public String getPrizeImageRow() {
            return prizeImageRow;
        }

        public void setPrizeImageRow(String prizeImageRow) {
            this.prizeImageRow = prizeImageRow;
        }
    }

}
