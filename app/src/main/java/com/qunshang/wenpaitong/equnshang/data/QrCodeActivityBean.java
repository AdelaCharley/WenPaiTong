package com.qunshang.wenpaitong.equnshang.data;

public class QrCodeActivityBean {

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

        int isActivity;

        String activityPicture;

        public int getIsActivity() {
            return isActivity;
        }

        public void setIsActivity(int isActivity) {
            this.isActivity = isActivity;
        }

        public String getActivityPicture() {
            return activityPicture;
        }

        public void setActivityPicture(String activityPicture) {
            this.activityPicture = activityPicture;
        }
    }

}
