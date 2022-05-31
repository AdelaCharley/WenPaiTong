package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class WeekInviteListDataBean {

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

    public class DataBean {

        int status;

        List<Data> invitingList;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Data> getInvitingList() {
            return invitingList;
        }

        public void setInvitingList(List<Data> invitingList) {
            this.invitingList = invitingList;
        }

        public class Data {

            String headimageUrl;

            String userName;

            public String getHeadimageUrl() {
                return headimageUrl;
            }

            public void setHeadimageUrl(String headimageUrl) {
                this.headimageUrl = headimageUrl;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

    }

}
