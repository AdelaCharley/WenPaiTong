package com.qunshang.wenpaitong.equnshang.data;
import java.io.Serializable;

//import lombok.Data;
//import lombok.NoArgsConstructor;

/**
 * 注册账号时，录入的用户信息
 * create by 何姝霖
 */

public class UserMsgBean {

    private Integer code;
    private String msg;
    public UserInfoBean data;

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

    public UserInfoBean getData() {
        return data;
    }

    public void setData(UserInfoBean data) {
        this.data = data;
    }

    public static class UserInfoBean implements Serializable {
        private Integer uid;
        private String utel = "";
        private String uname = "";
        private String headimage = "";
        private Integer gender = 0;
        private String introduce = "";
        private String identity = "";
        private Integer is_vip = 0;
        private Integer is_partner = 0; //0-empty 2-网点 3-办事处 4-联络处
        private String regtime = "";
        private String invitecode = "";
        private Integer inviteid = 0;
        private Integer invitenum = 0;
        private String credit = "";
        private Integer address = 0;
        private Integer logined = 0;

        private String tags = "";

        Integer isExist;

        public Integer getIsExist() {
            return isExist;
        }

        public void setIsExist(Integer isExist) {
            this.isExist = isExist;
        }

        String overduetime;

        public String getOverduetime() {
            return overduetime;
        }

        public void setOverduetime(String overduetime) {
            this.overduetime = overduetime;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        private String birthday = "";
        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public String getUtel() {
            return utel;
        }

        public void setUtel(String utel) {
            this.utel = utel;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getHeadimage() {
            return headimage;
        }

        public void setHeadimage(String headimage) {
            this.headimage = headimage;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getIdentity() {
            return identity;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public Integer getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(Integer is_vip) {
            this.is_vip = is_vip;
        }

        public Integer getIs_partner() {
            return is_partner;
        }

        public void setIs_partner(Integer is_partner) {
            this.is_partner = is_partner;
        }

        public String getRegtime() {
            return regtime;
        }

        public void setRegtime(String regtime) {
            this.regtime = regtime;
        }

        public String getInvitecode() {
            return invitecode;
        }

        public void setInvitecode(String invitecode) {
            this.invitecode = invitecode;
        }

        public Integer getInviteid() {
            return inviteid;
        }

        public void setInviteid(Integer inviteid) {
            this.inviteid = inviteid;
        }

        public Integer getInvitenum() {
            return invitenum;
        }

        public void setInvitenum(Integer invitenum) {
            this.invitenum = invitenum;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public Integer getAddress() {
            return address;
        }

        public void setAddress(Integer address) {
            this.address = address;
        }

        public Integer getLogined() {
            return logined;
        }

        public void setLogined(Integer logined) {
            this.logined = logined;
        }
    }
}
