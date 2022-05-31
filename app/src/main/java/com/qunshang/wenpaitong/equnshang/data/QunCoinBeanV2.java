package com.qunshang.wenpaitong.equnshang.data;

public class QunCoinBeanV2 {

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

        String totalQunCoin;

        public String getTotalQunCoin() {
            return totalQunCoin;
        }

        public void setTotalQunCoin(String totalQunCoin) {
            this.totalQunCoin = totalQunCoin;
        }

        CurrBean userQunCoin;

        public CurrBean getUserQunCoin() {
            return userQunCoin;
        }

        public void setUserQunCoin(CurrBean userQunCoin) {
            this.userQunCoin = userQunCoin;
        }

        public static class CurrBean {

            String currentNumber;

            String yesterdayIncreaseNumber;

            String lastWeekIncreaseNumber;

            String lastMonthIncreaseNumber;

            public String getCurrentNumber() {
                return currentNumber;
            }

            public void setCurrentNumber(String currentNumber) {
                this.currentNumber = currentNumber;
            }

            public String getYesterdayIncreaseNumber() {
                return yesterdayIncreaseNumber;
            }

            public void setYesterdayIncreaseNumber(String yesterdayIncreaseNumber) {
                this.yesterdayIncreaseNumber = yesterdayIncreaseNumber;
            }

            public String getLastWeekIncreaseNumber() {
                return lastWeekIncreaseNumber;
            }

            public void setLastWeekIncreaseNumber(String lastWeekIncreaseNumber) {
                this.lastWeekIncreaseNumber = lastWeekIncreaseNumber;
            }

            public String getLastMonthIncreaseNumber() {
                return lastMonthIncreaseNumber;
            }

            public void setLastMonthIncreaseNumber(String lastMonthIncreaseNumber) {
                this.lastMonthIncreaseNumber = lastMonthIncreaseNumber;
            }

        }
    }

}
