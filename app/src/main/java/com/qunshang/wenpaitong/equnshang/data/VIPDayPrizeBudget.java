package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class VIPDayPrizeBudget {

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

        List<PrizeBean> prizeList;

        public List<PrizeBean> getPrizeList() {
            return prizeList;
        }

        public void setPrizeList(List<PrizeBean> prizeList) {
            this.prizeList = prizeList;
        }

        public static class PrizeBean {

            String desc;

            String name;

            String url;

            String prizeId;

            String kind;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPrizeId() {
                return prizeId;
            }

            public void setPrizeId(String prizeId) {
                this.prizeId = prizeId;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }
        }

    }

}
