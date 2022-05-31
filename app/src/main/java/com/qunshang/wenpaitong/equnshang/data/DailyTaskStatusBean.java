package com.qunshang.wenpaitong.equnshang.data;

public class DailyTaskStatusBean {

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

        int inviteNumber;

        //int taskUserRelationId;

        int isEnd;

        int isStop;

        String image;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getInviteNumber() {
            return inviteNumber;
        }

        public void setInviteNumber(int inviteNumber) {
            this.inviteNumber = inviteNumber;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}
