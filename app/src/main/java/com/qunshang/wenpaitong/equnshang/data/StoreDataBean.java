package com.qunshang.wenpaitong.equnshang.data;

import java.util.ArrayList;
import java.util.List;

public class StoreDataBean {

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

    public class DataBean{

        List<Product> products;

        ArrayList<QiShiTongVideoBean.DataBean> videos;

        int manufactureId;

        String manufactureLogoUrl;

        String manufactureName;

        int isFocus;

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public ArrayList<QiShiTongVideoBean.DataBean> getVideos() {
            return videos;
        }

        public void setVideos(ArrayList<QiShiTongVideoBean.DataBean> videos) {
            this.videos = videos;
        }

        public int getManufactureId() {
            return manufactureId;
        }

        public void setManufactureId(int manufactureId) {
            this.manufactureId = manufactureId;
        }

        public String getManufactureLogoUrl() {
            return manufactureLogoUrl;
        }

        public void setManufactureLogoUrl(String manufactureLogoUrl) {
            this.manufactureLogoUrl = manufactureLogoUrl;
        }

        public String getManufactureName() {
            return manufactureName;
        }

        public void setManufactureName(String manufactureName) {
            this.manufactureName = manufactureName;
        }

        public int getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(int isFocus) {
            this.isFocus = isFocus;
        }

        public class Product{

            int productId;

            String productName;

            String productPosterUrl;

            int isControlPrice;

            int controlPrice;

            double price;

            double vipPrice;

            double vipGroupPrice;

            double purchasePrice;

            int total;

            int current;

            int skuNum;

            int sale;

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

            public String getProductPosterUrl() {
                return productPosterUrl;
            }

            public void setProductPosterUrl(String productPosterUrl) {
                this.productPosterUrl = productPosterUrl;
            }

            public int getIsControlPrice() {
                return isControlPrice;
            }

            public void setIsControlPrice(int isControlPrice) {
                this.isControlPrice = isControlPrice;
            }

            public int getControlPrice() {
                return controlPrice;
            }

            public void setControlPrice(int controlPrice) {
                this.controlPrice = controlPrice;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getVipPrice() {
                return vipPrice;
            }

            public void setVipPrice(double vipPrice) {
                this.vipPrice = vipPrice;
            }

            public double getVipGroupPrice() {
                return vipGroupPrice;
            }

            public void setVipGroupPrice(double vipGroupPrice) {
                this.vipGroupPrice = vipGroupPrice;
            }

            public double getPurchasePrice() {
                return purchasePrice;
            }

            public void setPurchasePrice(double purchasePrice) {
                this.purchasePrice = purchasePrice;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getCurrent() {
                return current;
            }

            public void setCurrent(int current) {
                this.current = current;
            }

            public int getSkuNum() {
                return skuNum;
            }

            public void setSkuNum(int skuNum) {
                this.skuNum = skuNum;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }
        }

        public class Videos{

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

            public Integer getProductId() {
                return productId;
            }

            public void setProductId(Integer productId) {
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

            public Double getVipGroupPrice() {
                return vipGroupPrice;
            }

            public void setVipGroupPrice(Double vipGroupPrice) {
                this.vipGroupPrice = vipGroupPrice;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
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

            public Integer getIsLike() {
                return isLike;
            }

            public void setIsLike(Integer isLike) {
                this.isLike = isLike;
            }

            public Integer getUpCount() {
                return upCount;
            }

            public void setUpCount(Integer upCount) {
                this.upCount = upCount;
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

            public Integer getSkuCount() {
                return skuCount;
            }

            public void setSkuCount(Integer skuCount) {
                this.skuCount = skuCount;
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

}
