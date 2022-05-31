package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class WorkPointBean {

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

    public class DataBean{

        double currentPrice;

        double compareToYesterday;

        List<DayValue> lastSevenDay;

        NumberInfo numberInfo;

        public double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(double currentPrice) {
            this.currentPrice = currentPrice;
        }

        public double getCompareToYesterday() {
            return compareToYesterday;
        }

        public void setCompareToYesterday(double compareToYesterday) {
            this.compareToYesterday = compareToYesterday;
        }

        public List<DayValue> getLastSevenDay() {
            return lastSevenDay;
        }

        public void setLastSevenDay(List<DayValue> lastSevenDay) {
            this.lastSevenDay = lastSevenDay;
        }

        public NumberInfo getNumberInfo() {
            return numberInfo;
        }

        public void setNumberInfo(NumberInfo numberInfo) {
            this.numberInfo = numberInfo;
        }

        public class DayValue{
            String date;

            String value;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public class NumberInfo{

            int userQunCoin;

            int currentNumber;

            int yesterdayIncreaseNumber;

            int lastWeekIncreaseNumber;

            int lastMonthIncreaseNumber;

            public int getUserQunCoin() {
                return userQunCoin;
            }

            public void setUserQunCoin(int userQunCoin) {
                this.userQunCoin = userQunCoin;
            }

            public int getCurrentNumber() {
                return currentNumber;
            }

            public void setCurrentNumber(int currentNumber) {
                this.currentNumber = currentNumber;
            }

            public int getYesterdayIncreaseNumber() {
                return yesterdayIncreaseNumber;
            }

            public void setYesterdayIncreaseNumber(int yesterdayIncreaseNumber) {
                this.yesterdayIncreaseNumber = yesterdayIncreaseNumber;
            }

            public int getLastWeekIncreaseNumber() {
                return lastWeekIncreaseNumber;
            }

            public void setLastWeekIncreaseNumber(int lastWeekIncreaseNumber) {
                this.lastWeekIncreaseNumber = lastWeekIncreaseNumber;
            }

            public int getLastMonthIncreaseNumber() {
                return lastMonthIncreaseNumber;
            }

            public void setLastMonthIncreaseNumber(int lastMonthIncreaseNumber) {
                this.lastMonthIncreaseNumber = lastMonthIncreaseNumber;
            }
        }

    }

}
