package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.List;

public class ProductBean implements Serializable {

    Integer code;

    String msg;

    DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        Integer productId;

        String productName;

        SwipeList swiperList;

        VipGroupCard vipGroupCard;

        List<AttributeList> attributeList;

        List<SpecList> specList;

        List<SkuList> skuList;

        Manufacture manufacture;

        Experience experience;

        List<String> detailInfo;

        int isCollect;

        int isControlPrice;

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

        public SwipeList getSwiperList() {
            return swiperList;
        }

        public void setSwiperList(SwipeList swiperList) {
            this.swiperList = swiperList;
        }

        public VipGroupCard getVipGroupCard() {
            return vipGroupCard;
        }

        public void setVipGroupCard(VipGroupCard vipGroupCard) {
            this.vipGroupCard = vipGroupCard;
        }

        public List<AttributeList> getAttributeList() {
            return attributeList;
        }

        public void setAttributeList(List<AttributeList> attributeList) {
            this.attributeList = attributeList;
        }

        public List<SpecList> getSpecList() {
            return specList;
        }

        public void setSpecList(List<SpecList> specList) {
            this.specList = specList;
        }

        public List<SkuList> getSkuList() {
            return skuList;
        }

        public void setSkuList(List<SkuList> skuList) {
            this.skuList = skuList;
        }

        public Manufacture getManufacture() {
            return manufacture;
        }

        public void setManufacture(Manufacture manufacture) {
            this.manufacture = manufacture;
        }

        public Experience getExperience() {
            return experience;
        }

        public void setExperience(Experience experience) {
            this.experience = experience;
        }

        public List<String> getDetailInfo() {
            return detailInfo;
        }

        public void setDetailInfo(List<String> detailInfo) {
            this.detailInfo = detailInfo;
        }

        public static class Experience implements Serializable{

        }

        public static class SwipeList implements Serializable {

            String videos;

            List<String> imgList;

            public String getVideos() {
                return videos;
            }

            public void setVideos(String videoUrl) {
                this.videos = videoUrl;
            }

            public List<String> getImgList() {
                return imgList;
            }

            public void setImgList(List<String> imgUrlList) {
                this.imgList = imgUrlList;
            }
        }

        public static class SkuList implements Serializable{
            Integer id;

            String value;

            String image;

            double price;

            double vipPrice;

            double vipGroupPrice;

            double stock;

            double number;

            int minQuantity;

            int step;

            String threshold;

            double fanNormalPrice;

            double controlPrice;

            double groupOwnerPrice;

            public double getGroupOwnerPrice() {
                return groupOwnerPrice;
            }

            public void setGroupOwnerPrice(double groupOwnerPrice) {
                this.groupOwnerPrice = groupOwnerPrice;
            }

            int buyCount;//这个字段是专门在购买的时候用的，请求接口的时候并不会返回这个数据。

            public int getBuyCount() {
                return buyCount;
            }

            public void setBuyCount(int buyCount) {
                this.buyCount = buyCount;
            }

            public double getFanNormalPrice() {
                return fanNormalPrice;
            }

            public void addCount(int buyCount){
                this.buyCount += buyCount;
            }

            public void setFanNormalPrice(double fanNormalPrice) {
                this.fanNormalPrice = fanNormalPrice;
            }

            public double getControlPrice() {
                return controlPrice;
            }

            public void setControlPrice(double controlPrice) {
                this.controlPrice = controlPrice;
            }

            public int getMinQuantity() {
                return minQuantity;
            }

            public void setMinQuantity(int minQuantity) {
                this.minQuantity = minQuantity;
            }

            public int getStep() {
                return step;
            }

            public void setStep(int step) {
                this.step = step;
            }

            public String getThreshold() {
                return threshold;
            }

            public void setThreshold(String threshold) {
                this.threshold = threshold;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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

            public double getStock() {
                return stock;
            }

            public void setStock(double stock) {
                this.stock = stock;
            }

            public double getNumber() {
                return number;
            }

            public void setNumber(double number) {
                this.number = number;
            }
        }

        public static class VipGroupCard implements Serializable{
            int total;

            int current;

            int currentGroup;

            public int getCurrentGroup() {
                return currentGroup;
            }

            public void setCurrentGroup(int currentGroup) {
                this.currentGroup = currentGroup;
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
        }

        public static class AttributeList implements Serializable{
            String title;

            String text;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

        public static class SpecList implements Serializable{
            String name;

            List<String> list;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getList() {
                return list;
            }

            public void setList(List<String> list) {
                this.list = list;
            }
        }

        public static class Manufacture implements Serializable{
            Integer manufactureId;

            String manufactureName;

            String manufactureLogoUrl;

            public Integer getManufactureId() {
                return manufactureId;
            }

            public void setManufactureId(Integer manufactureId) {
                this.manufactureId = manufactureId;
            }

            public String getManufactureName() {
                return manufactureName;
            }

            public void setManufactureName(String manufactureName) {
                this.manufactureName = manufactureName;
            }

            public String getManufactureLogoUrl() {
                return manufactureLogoUrl;
            }

            public void setManufactureLogoUrl(String manufactureLogoUrl) {
                this.manufactureLogoUrl = manufactureLogoUrl;
            }
        }

        public int getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(int isCollect) {
            this.isCollect = isCollect;
        }

        public int getIsControlPrice() {
            return isControlPrice;
        }

        public void setIsControlPrice(int isControlPrice) {
            this.isControlPrice = isControlPrice;
        }
    }
}