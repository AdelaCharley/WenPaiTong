package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class CategoryBean {

    int code;

    String msg;

    Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        List<CategoryData> category;

        List<BrandData> brand;

        public List<CategoryData> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryData> category) {
            this.category = category;
        }

        public List<BrandData> getBrand() {
            return brand;
        }

        public void setBrand(List<BrandData> brand) {
            this.brand = brand;
        }

        public static class CategoryData {

            int categoryId;

            String categoryName;

            String categoryIcon;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getCategoryIcon() {
                return categoryIcon;
            }

            public void setCategoryIcon(String categoryIcon) {
                this.categoryIcon = categoryIcon;
            }
        }

        public static class BrandData {

            int brandId;

            String brandName;

            String brandIcon;

            public int getBrandId() {
                return brandId;
            }

            public void setBrandId(int brandId) {
                this.brandId = brandId;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getBrandIcon() {
                return brandIcon;
            }

            public void setBrandIcon(String brandIcon) {
                this.brandIcon = brandIcon;
            }
        }

    }

}
