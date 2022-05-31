package com.qunshang.wenpaitong.equnshang.data;

import java.util.ArrayList;

public class WenBanTongCompanyDetailBean {

    private Integer code;
    private String msg;

    DataBean data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

        int companyId;

        String companyName;

        String companyPhone;

        String companyLogo;

        String companyPoster;

        String companyDesc;

        ArrayList<String> companyLicence;

        public ArrayList<String> getCompanyLicence() {
            return companyLicence;
        }

        public void setCompanyLicence(ArrayList<String> companyLicence) {
            this.companyLicence = companyLicence;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyPhone() {
            return companyPhone;
        }

        public void setCompanyPhone(String companyPhone) {
            this.companyPhone = companyPhone;
        }

        public String getCompanyLogo() {
            return companyLogo;
        }

        public void setCompanyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
        }

        public String getCompanyPoster() {
            return companyPoster;
        }

        public void setCompanyPoster(String companyPoster) {
            this.companyPoster = companyPoster;
        }

        public String getCompanyDesc() {
            return companyDesc;
        }

        public void setCompanyDesc(String companyDesc) {
            this.companyDesc = companyDesc;
        }
    }

}
