package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class AMallV3OrderCountBean {

    public int code;

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

        List<Integer> order;

        List<Integer> tool;

        public List<Integer> getOrder() {
            return order;
        }

        public void setOrder(List<Integer> order) {
            this.order = order;
        }

        public List<Integer> getTool() {
            return tool;
        }

        public void setTool(List<Integer> tool) {
            this.tool = tool;
        }
    }

}
