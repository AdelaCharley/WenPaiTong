package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class NewConcernBean {

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

        int agencyId;

        String agencyName;

        String agencyAvatar;

        String agencyDescription;

        int status = 0;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAgencyId() {
            return agencyId;
        }

        public void setAgencyId(int agencyId) {
            this.agencyId = agencyId;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

        public String getAgencyAvatar() {
            return agencyAvatar;
        }

        public void setAgencyAvatar(String agencyAvatar) {
            this.agencyAvatar = agencyAvatar;
        }

        public String getAgencyDescription() {
            return agencyDescription;
        }

        public void setAgencyDescription(String agencyDescription) {
            this.agencyDescription = agencyDescription;
        }
    }

}
