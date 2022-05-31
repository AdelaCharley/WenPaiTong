package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class UpVideoBean {

    /*
    .{"code":200,"msg":"成功","data":[{"videoId":24,"videoPosterUrl":"http:\/\/api.equnshang.com\/video\/video23.PNG","videoUrl":"http:\/\/1305049841.vod2.myqcloud.com\/dbb9202dvodtranscq1305049841\/112bd91b5285890814646098928\/v.f566219.mp4","videoDesc":"五涟 系列纪录片三","userId":1,"userUname":"李亚宏","userHeadimageUrl":"http:\/\/api.equnshang.com\/headimg\/hdImg_ca15bf645220a78d93b890d643bc5d6c1618387925628.jpg","productId":0,"productName":null,"posterUrl":null,"createTime":"2020-11-05 13:22:25","isLike":5,"isFocus":0,"isUp":5,"likeCount":324,"commentCount":206,"upCount":8075,"skuCount":0,"vipGroupPrice":0},{"videoId":109,"videoPosterUrl":"http:\/\/1305049841.vod2.myqcloud.com\/04cf3285vodcq1305049841\/8741e0cd5285890815216416647\/cover.jpeg","videoUrl":"http:\/\/1305049841.vod2.myqcloud.com\/dbb9202dvodtranscq1305049841\/8741e0cd5285890815216416647\/v.f566219.mp4","videoDesc":"生活","userId":684,"userUname":"毛哥不混?","userHeadimageUrl":"http:\/\/api.equnshang.com\/headimg\/1612060281765_1611808379-IMG_2735.JPG","productId":0,"productName":null,"posterUrl":null,"createTime":"2021-03-07 18:40:27","isLike":4,"isFocus":0,"isUp":4,"likeCount":180,"commentCount":52,"upCount":3516,"skuCount":0,"vipGroupPrice":0},{"videoId":98,"videoPosterUrl":"http:\/\/1305049841.vod2.myqcloud.com\/04cf3285vodcq1305049841\/93cef1cd5285890815026809246\/cover.jpeg","videoUrl":"http:\/\/1305049841.vod2.myqcloud.com\/dbb9202dvodtranscq1305049841\/93cef1cd5285890815026809246\/v.f566219.mp4","videoDesc":"茶褐素","userId":717,"userUname":"马 炜","userHeadimageUrl":"http:\/\/api.equnshang.com\/headimg\/1614701982864_1598404851-IMG_1336.jpg","productId":0,"productName":null,"posterUrl":null,"createTime":"2021-03-04 15:40:10","isLike":3,"isFocus":0,"isUp":3,"likeCount":204,"commentCount":77,"upCount":3791,"skuCount":0,"vipGroupPrice":0},{"videoId":298,"videoPosterUrl":"http:\/\/1305049841.vod2.myqcloud.com\/04cf3285vodcq1305049841\/e366efe03701925919145804133\/cover.png","videoUrl":"http:\/\/1305049841.vod2.myqcloud.com\/dbb9202dvodtranscq1305049841\/e366efe03701925919145804133\/v.f566219.mp4","videoDesc":"写不断铅笔，自动调节，连续书写","userId":15,"userUname":"用户15","userHeadimageUrl":"http:\/\/api.equnshang.com\/headimg\/1603518743346_IMG_20200730_162301.jpg","productId":0,"productName":null,"posterUrl":null,"createTime":"2021-06-08 22:47:19","isLike":2,"isFocus":0,"isUp":2,"likeCount":60,"commentCount":13,"upCount":1033,"skuCount":0,"vipGroupPrice":0}]}
     */

    private Integer code;
    private String msg;

    private List<UpVideoBean.DataBean> data;

    public List<UpVideoBean.DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        int videoId;
        String videoPosterUrl;
        String videoUrl;
        String videoDesc;
        int userId;
        String userUname;
        String userHeadimageUrl;

        int productId;
        String productName;
        String posterUrl;
        String createTime;
        int isLike;
        int isFocus;
        int isUp;
        int likeCount;
        int commentCount;
        int upCount;
        int skuCount;
        double vipGroupPrice;

        public int getVideoId() {
            return videoId;
        }

        public void setVideoId(int videoId) {
            this.videoId = videoId;
        }

        public String getVideoPosterUrl() {
            return videoPosterUrl;
        }

        public void setVideoPosterUrl(String videoPosterUrl) {
            this.videoPosterUrl = videoPosterUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
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

        public double getVipGroupPrice() {
            return vipGroupPrice;
        }

        public void setVipGroupPrice(double vipGroupPrice) {
            this.vipGroupPrice = vipGroupPrice;
        }
    }
}
