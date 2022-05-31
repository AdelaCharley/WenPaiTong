package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallV3MainBean {

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

        String top;

        List<BannerBean> banner;

        List<CategoryBean> category;

        List<ActivityBean> activity;

        public String getTop() {
            return top;
        }

        public void setTop(String top) {
            this.top = top;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public List<ActivityBean> getActivity() {
            return activity;
        }

        public void setActivity(List<ActivityBean> activity) {
            this.activity = activity;
        }

        public static class CategoryBean {

            int categoryId;

            String name;

            String icon;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }

        public static class BannerBean {

            int productId;

            String bannerUrl;

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getBannerUrl() {
                return bannerUrl;
            }

            public void setBannerUrl(String bannerUrl) {
                this.bannerUrl = bannerUrl;
            }
        }

        public static class ActivityBean {

            String activityUrl;

            String jumpUrl;

            String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getActivityUrl() {
                return activityUrl;
            }

            public void setActivityUrl(String activityUrl) {
                this.activityUrl = activityUrl;
            }

            public String getJumpUrl() {
                return jumpUrl;
            }

            public void setJumpUrl(String jumpUrl) {
                this.jumpUrl = jumpUrl;
            }
        }

    }

}
