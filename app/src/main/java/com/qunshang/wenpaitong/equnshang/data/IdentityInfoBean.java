package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;

public class IdentityInfoBean implements Serializable {

    String realname;

    String realidentity;

    String realaddress;

    String realgender;

    String realbirth;

    String realnation;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRealidentity() {
        return realidentity;
    }

    public void setRealidentity(String realidentity) {
        this.realidentity = realidentity;
    }

    public String getRealaddress() {
        return realaddress;
    }

    public void setRealaddress(String realaddress) {
        this.realaddress = realaddress;
    }

    public String getRealgender() {
        return realgender;
    }

    public void setRealgender(String realgender) {
        this.realgender = realgender;
    }

    public String getRealbirth() {
        return realbirth;
    }

    public void setRealbirth(String realbirth) {
        this.realbirth = realbirth;
    }

    public String getRealnation() {
        return realnation;
    }

    public void setRealnation(String realnation) {
        this.realnation = realnation;
    }
}
