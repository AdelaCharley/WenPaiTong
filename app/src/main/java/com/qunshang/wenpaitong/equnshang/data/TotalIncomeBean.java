package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class TotalIncomeBean {

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

        double incomeTotal;

        List<Data> groupOwnerIncomeList;

        public double getIncomeTotal() {
            return incomeTotal;
        }

        public void setIncomeTotal(double incomeTotal) {
            this.incomeTotal = incomeTotal;
        }

        public List<Data> getGroupOwnerIncomeList() {
            return groupOwnerIncomeList;
        }

        public void setGroupOwnerIncomeList(List<Data> groupOwnerIncomeList) {
            this.groupOwnerIncomeList = groupOwnerIncomeList;
        }

        public static class Data {
            int groupOwnerId;

            String groupOwnerName;

            String groupOwnerImage;

            int productNum;

            public int getGroupOwnerId() {
                return groupOwnerId;
            }

            public void setGroupOwnerId(int groupOwnerId) {
                this.groupOwnerId = groupOwnerId;
            }

            public String getGroupOwnerName() {
                return groupOwnerName;
            }

            public void setGroupOwnerName(String groupOwnerName) {
                this.groupOwnerName = groupOwnerName;
            }

            public String getGroupOwnerImage() {
                return groupOwnerImage;
            }

            public void setGroupOwnerImage(String groupOwnerImage) {
                this.groupOwnerImage = groupOwnerImage;
            }

            public int getProductNum() {
                return productNum;
            }

            public void setProductNum(int productNum) {
                this.productNum = productNum;
            }
        }

    }

}
