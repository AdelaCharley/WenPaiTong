package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class SearchVideoBean {

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

    public class DataBean{

        int videoId;

        String videoUrl;

        String videoPosterUrl;

        String videoDesc;

        int userId;

        String userUname;

        String userHeadimageUrl;

        int productId;

        String productName;

        String posterUrl;

        double vipGroupPrice;

        String createTime;

        int isLike;

        int isFocus;

        int isUp;

        int likeCount;

        int commentCount;

        int upCount;

        int skuCount;

        public int getVideoId() {
            return videoId;
        }

        public void setVideoId(int videoId) {
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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public double getVipGroupPrice() {
            return vipGroupPrice;
        }

        public void setVipGroupPrice(double vipGroupPrice) {
            this.vipGroupPrice = vipGroupPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public int getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(int isFocus) {
            this.isFocus = isFocus;
        }

        public int getIsUp() {
            return isUp;
        }

        public void setIsUp(int isUp) {
            this.isUp = isUp;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getUpCount() {
            return upCount;
        }

        public void setUpCount(int upCount) {
            this.upCount = upCount;
        }

        public int getSkuCount() {
            return skuCount;
        }

        public void setSkuCount(int skuCount) {
            this.skuCount = skuCount;
        }
    }

}
