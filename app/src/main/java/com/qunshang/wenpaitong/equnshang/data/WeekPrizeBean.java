package com.qunshang.wenpaitong.equnshang.data;
import java.util.List;

public class WeekPrizeBean {

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

        String taskUserRelationId;

        int status;

        PrizeInfo prizeInfo;

        public String getTaskUserRelationId() {
            return taskUserRelationId;
        }

        public void setTaskUserRelationId(String taskUserRelationId) {
            this.taskUserRelationId = taskUserRelationId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public PrizeInfo getPrizeInfo() {
            return prizeInfo;
        }

        public void setPrizeInfo(PrizeInfo prizeInfo) {
            this.prizeInfo = prizeInfo;
        }

        public class PrizeInfo {

            int prizeId;

            List<String> tags;

            String imageUrl;

            String name;

            int stock;

            int used;

            public int getPrizeId() {
                return prizeId;
            }

            public void setPrizeId(int prizeId) {
                this.prizeId = prizeId;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public int getUsed() {
                return used;
            }

            public void setUsed(int used) {
                this.used = used;
            }
        }

    }

}
