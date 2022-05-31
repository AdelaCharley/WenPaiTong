package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallV3MessageBean {

    int code;

    String msg;

    List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String title;
        private String context;
        private String createTime;
        private int orderId;
        private String afterSaleSn;
        private String orderGroupSn;
        private String jumpType;
        private String skuPic;

        private String orderNoticeId;

        public String getOrderNoticeId() {
            return orderNoticeId;
        }

        public void setOrderNoticeId(String orderNoticeId) {
            this.orderNoticeId = orderNoticeId;
        }

        private int isCheck;

        public int getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(int isCheck) {
            this.isCheck = isCheck;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getAfterSaleSn() {
            return afterSaleSn;
        }

        public void setAfterSaleSn(String afterSaleSn) {
            this.afterSaleSn = afterSaleSn;
        }

        public String getOrderGroupSn() {
            return orderGroupSn;
        }

        public void setOrderGroupSn(String orderGroupSn) {
            this.orderGroupSn = orderGroupSn;
        }

        public String getJumpType() {
            return jumpType;
        }

        public void setJumpType(String jumpType) {
            this.jumpType = jumpType;
        }

        public String getSkuPic() {
            return skuPic;
        }

        public void setSkuPic(String skuPic) {
            this.skuPic = skuPic;
        }
    }

}
