package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.List;

public class ExperienceBean implements Serializable {

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

    public static class DataBean implements Serializable {

        int id;

        int experienceId;

        String sendMsg;

        int userId;

        int upNum;

        int replyNum;

        int parentId;

        String createTime;

        String userName;

        String headImgSrc;

        int isUp;

        List<Reply> replyComments;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getExperienceId() {
            return experienceId;
        }

        public void setExperienceId(int experienceId) {
            this.experienceId = experienceId;
        }

        public String getSendMsg() {
            return sendMsg;
        }

        public void setSendMsg(String sendMsg) {
            this.sendMsg = sendMsg;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUpNum() {
            return upNum;
        }

        public void setUpNum(int upNum) {
            this.upNum = upNum;
        }

        public int getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(int replyNum) {
            this.replyNum = replyNum;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadImgSrc() {
            return headImgSrc;
        }

        public void setHeadImgSrc(String headImgSrc) {
            this.headImgSrc = headImgSrc;
        }

        public int getIsUp() {
            return isUp;
        }

        public void setIsUp(int isUp) {
            this.isUp = isUp;
        }

        public List<Reply> getReplyComments() {
            return replyComments;
        }

        public void setReplyComments(List<Reply> replyComments) {
            this.replyComments = replyComments;
        }

        public class Reply implements Serializable {

            int id;

            int experienceId;

            String sendMsg;

            int userId;

            int upNum;

            int replyNum;

            int parentId;

            String createTime;

            String userName;

            String headImgSrc;

            int isUp;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getExperienceId() {
                return experienceId;
            }

            public void setExperienceId(int experienceId) {
                this.experienceId = experienceId;
            }

            public String getSendMsg() {
                return sendMsg;
            }

            public void setSendMsg(String sendMsg) {
                this.sendMsg = sendMsg;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUpNum() {
                return upNum;
            }

            public void setUpNum(int upNum) {
                this.upNum = upNum;
            }

            public int getReplyNum() {
                return replyNum;
            }

            public void setReplyNum(int replyNum) {
                this.replyNum = replyNum;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getHeadImgSrc() {
                return headImgSrc;
            }

            public void setHeadImgSrc(String headImgSrc) {
                this.headImgSrc = headImgSrc;
            }

            public int getIsUp() {
                return isUp;
            }

            public void setIsUp(int isUp) {
                this.isUp = isUp;
            }
        }

    }

}
