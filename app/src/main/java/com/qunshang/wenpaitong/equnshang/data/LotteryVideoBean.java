package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class LotteryVideoBean {

    private Integer code;
    private String msg;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean{
        String endTime;

        String prizeName;

        String prizeDescription;

        Integer videoId;

        String videoUrl;

        String videoPosterUrl;

        String videoDesc;

        Integer userId;

        String userUname;

        String userHeadimageUrl;

        Integer productId;

        String createTime;

        Integer isLike;

        Integer isFocus;

        Integer isUp;

        Integer likeCount;

        Integer commentCount;

        Integer upCount;

        String image;

        double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public String getPrizeDescription() {
            return prizeDescription;
        }

        public void setPrizeDescription(String prizeDescription) {
            this.prizeDescription = prizeDescription;
        }

        public Integer getVideoId() {
            return videoId;
        }

        public void setVideoId(Integer videoId) {
            this.videoId = videoId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoPosterUrl() {
            return videoPosterUrl;
        }

        public void setVideoPosterUrl(String videoPosterUrl) {
            this.videoPosterUrl = videoPosterUrl;
        }

        public String getVideoDesc() {
            return videoDesc;
        }

        public void setVideoDesc(String videoDesc) {
            this.videoDesc = videoDesc;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserUname() {
            return userUname;
        }

        public void setUserUname(String userUname) {
            this.userUname = userUname;
        }

        public String getUserHeadimageUrl() {
            return userHeadimageUrl;
        }

        public void setUserHeadimageUrl(String userHeadimageUrl) {
            this.userHeadimageUrl = userHeadimageUrl;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Integer getIsLike() {
            return isLike;
        }

        public void setIsLike(Integer isLike) {
            this.isLike = isLike;
        }

        public Integer getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(Integer isFocus) {
            this.isFocus = isFocus;
        }

        public Integer getIsUp() {
            return isUp;
        }

        public void setIsUp(Integer isUp) {
            this.isUp = isUp;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Integer getUpCount() {
            return upCount;
        }

        public void setUpCount(Integer upCount) {
            this.upCount = upCount;
        }
    }

}
