package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

//import lombok.Data;
//import lombok.NoArgsConstructor;

/**
 * 评论对话框
 * create by 何姝霖
 */
//@NoArgsConstructor
//@Data
public class CommentBean {

    private Integer code;
    private String msg;
    private List<DataDTO> data;

    public List<DataDTO> getData() {
        return data;
    }

    //@NoArgsConstructor
    //@Data
    public static class DataDTO {
        private Integer replyId;
        private Integer videoId;
        private Integer userId;
        private String userHeadimageUrl;
        private String userName;
        private String createTime;
        private Integer likeNum;
        private Integer deleteStatus;
        private String content;
        private Integer isUp;

        private boolean isLiked = false;

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public Integer getReplyId() {
            return replyId;
        }

        public void setReplyId(Integer replyId) {
            this.replyId = replyId;
        }

        public Integer getVideoId() {
            return videoId;
        }

        public void setVideoId(Integer videoId) {
            this.videoId = videoId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserHeadimageUrl() {
            return userHeadimageUrl;
        }

        public void setUserHeadimageUrl(String userHeadimageUrl) {
            this.userHeadimageUrl = userHeadimageUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Integer getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(Integer likeNum) {
            this.likeNum = likeNum;
        }

        public Integer getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(Integer deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getIsUp() {
            return isUp;
        }

        public void setIsUp(Integer isUp) {
            this.isUp = isUp;
        }
    }
}
