package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

import com.qunshang.wenpaitong.equnshang.activity.SearchResultActivity;

public class PrizeInfoDetailBean {

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

    public static class DataBean {

        int prizeId;

        int stock;

        String name;

        List<Parameter> parameter;

        String manufactureName;

        String description;

        String origin;

        double marketPrice;

        String swiperImageUrl;

        String introduceImageUrl;

        int status;

        String tags;

        ExperienceBean experienceInfo;

        public int getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(int prizeId) {
            this.prizeId = prizeId;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Parameter> getParameter() {
            return parameter;
        }

        public void setParameter(List<Parameter> parameter) {
            this.parameter = parameter;
        }

        public static class Parameter {

            String key;

            String value;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public String getManufactureName() {
            return manufactureName;
        }

        public void setManufactureName(String manufactureName) {
            this.manufactureName = manufactureName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getSwiperImageUrl() {
            return swiperImageUrl;
        }

        public void setSwiperImageUrl(String swiperImageUrl) {
            this.swiperImageUrl = swiperImageUrl;
        }

        public String getIntroduceImageUrl() {
            return introduceImageUrl;
        }

        public void setIntroduceImageUrl(String introduceImageUrl) {
            this.introduceImageUrl = introduceImageUrl;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public ExperienceBean getExperienceInfo() {
            return experienceInfo;
        }

        public void setExperienceInfo(ExperienceBean experienceInfo) {
            this.experienceInfo = experienceInfo;
        }

        public static class ExperienceBean {

            int experienceId;

            String imagUrl;

            String userName;

            String headImageUrl;

            String content;

            public int getExperienceId() {
                return experienceId;
            }

            public void setExperienceId(int experienceId) {
                this.experienceId = experienceId;
            }

            public String getImagUrl() {
                return imagUrl;
            }

            public void setImagUrl(String imagUrl) {
                this.imagUrl = imagUrl;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getHeadImageUrl() {
                return headImageUrl;
            }

            public void setHeadImageUrl(String headImageUrl) {
                this.headImageUrl = headImageUrl;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

    }

}
