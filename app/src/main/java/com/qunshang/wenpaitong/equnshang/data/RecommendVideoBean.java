package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.ArrayList;

//import lombok.Data;
//import lombok.NoArgsConstructor;

//@NoArgsConstructor
//@Data
public class RecommendVideoBean implements Serializable {

    private int code;
    private String msg;
    private ArrayList<DataBean> data;

    public ArrayList<DataBean> getData() {
        return data;
    }

    //@NoArgsConstructor
    //@Data
    public static class DataBean implements Serializable {
        /*private int videoId;
        private String videoUrl;
        private String videoPosterUrl;  //视频封面
        private String videoDesc;

        private int userId;
        private String userUname;
        private String userHeadimageUrl;

        private int productId;
        private String productName;
        private String posterUrl;
        private int vipGroupPrice;

        private String createTime;
        private int isFocus;//是否关注，是-1，否-0
        private int isUp;   //是否点赞，是-1，否-0
        private int isLike; //是否喜爱，是-1，否-0
        private int upCount;
        private int likeCount;
        private int commentCount;
        private int skuCount;*/
        /*private int agencyId;

        String avatar;

        String agencyName;

        int newsId;

        String title;

        List<String> newsPoster;

        String createTime;

        int browserNum;

        int upNum;

        int commentNum;*/
        /*private String videoUrl;
        private String videoPosterUrl;  //视频封面
        private String videoDesc;

        private int userId;
        private String userUname;
        private String userHeadimageUrl;

        private int productId;
        private String productName;
        private String posterUrl;
        private int vipGroupPrice;

        private String createTime;
        private int isFocus;//是否关注，是-1，否-0
        private int isUp;   //是否点赞，是-1，否-0
        private int isLike; //是否喜爱，是-1，否-0
        private int upCount;
        private int likeCount;
        private int commentCount;
        private int skuCount;*/

        private int videoId;
        private String videoUrl;
        private String videoPosterUrl;  //视频封面
        private String videoDesc;

        private int agencyId;
        private String agencyName;
        private String agencyAvatar;

        private String createTime;
        private int isFocus;//是否关注，是-1，否-0
        private int isUp;   //是否点赞，是-1，否-0
        private int isLike; //是否喜爱，是-1，否-0
        private int upCount;
        private int likeCount;
        private int commentCount;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public int getUpCount() {
            return upCount;
        }

        public void setUpCount(int upCount) {
            this.upCount = upCount;
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
    }
}
