package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.ArrayList;
/*import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;*/

//@NoArgsConstructor
//@Data
public class QiShiTongVideoBean {

    private Integer code;
    private String msg;
    private ArrayList<DataBean> data;

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

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    //@NoArgsConstructor
    //@Data
    public static class DataBean implements Serializable {
        private Integer videoId;//
        private String videoUrl;//
        private String videoPosterUrl;  //视频封面
        private String videoDesc;//

        private Integer manufacturerId;//
        private String vendorName;//
        private String manufacturerHeadImgUrl;//

        private Integer productId;//
        private String productName;//
        private String posterUrl;
        private Double vipGroupPrice;

        private String createTime;
        private Integer isFocus;//是否关注，是-1，否-0
        private Integer isUp;   //是否点赞，是-1，否-0
        private Integer isLike; //是否喜爱，是-1，否-0
        private Integer upCount;//
        private Integer likeCount;//
        private Integer commentCount;//
        private Integer skuCount;

        private Double productPurchasePrice;
        private String productPosterUrl;

        private Integer threshold;



        public Integer getVideoId() {
            return videoId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public String getVideoPosterUrl() {
            return videoPosterUrl;
        }

        public String getVideoDesc() {
            return videoDesc;
        }

        public Integer getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public Double getVipGroupPrice() {
            return vipGroupPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public Integer getIsLike() {
            return isLike;
        }

        public Integer getIsFocus() {
            return isFocus;
        }

        public Integer getIsUp() {
            return isUp;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public Integer getUpCount() {
            return upCount;
        }

        public Integer getSkuCount() {
            return skuCount;
        }

        public void setVideoId(Integer videoId) {
            this.videoId = videoId;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public void setVideoPosterUrl(String videoPosterUrl) {
            this.videoPosterUrl = videoPosterUrl;
        }

        public void setVideoDesc(String videoDesc) {
            this.videoDesc = videoDesc;
        }


        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public void setVipGroupPrice(Double vipGroupPrice) {
            this.vipGroupPrice = vipGroupPrice;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setIsLike(Integer isLike) {
            this.isLike = isLike;
        }

        public void setIsFocus(Integer isFocus) {
            this.isFocus = isFocus;
        }

        public void setIsUp(Integer isUp) {
            this.isUp = isUp;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public void setUpCount(Integer upCount) {
            this.upCount = upCount;
        }

        public void setSkuCount(Integer skuCount) {
            this.skuCount = skuCount;
        }

        public Integer getManufacturerId() {
            return manufacturerId;
        }

        public void setManufacturerId(Integer manufacturerId) {
            this.manufacturerId = manufacturerId;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getManufacturerHeadImgUrl() {
            return manufacturerHeadImgUrl;
        }

        public void setManufacturerHeadImgUrl(String manufacturerHeadImgUrl) {
            this.manufacturerHeadImgUrl = manufacturerHeadImgUrl;
        }

        public Double getProductPurchasePrice() {
            return productPurchasePrice;
        }

        public void setProductPurchasePrice(Double productPurchasePrice) {
            this.productPurchasePrice = productPurchasePrice;
        }

        public String getProductPosterUrl() {
            return productPosterUrl;
        }

        public void setProductPosterUrl(String productPosterUrl) {
            this.productPosterUrl = productPosterUrl;
        }

        public Integer getThreshold() {
            return threshold;
        }

        public void setThreshold(Integer threshold) {
            this.threshold = threshold;
        }
    }
}
