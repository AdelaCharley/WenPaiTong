package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class NewGongFenBean {

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

        String currentCredit;

        String todayCredit;

        String sevenCredit;

        List <SevenBean> lastSevenCredit;

        public String getCurrentCredit() {
            return currentCredit;
        }

        public void setCurrentCredit(String currentCredit) {
            this.currentCredit = currentCredit;
        }

        public String getTodayCredit() {
            return todayCredit;
        }

        public void setTodayCredit(String todayCredit) {
            this.todayCredit = todayCredit;
        }

        public String getSevenCredit() {
            return sevenCredit;
        }

        public void setSevenCredit(String sevenCredit) {
            this.sevenCredit = sevenCredit;
        }

        public List<SevenBean> getLastSevenCredit() {
            return lastSevenCredit;
        }

        public void setLastSevenCredit(List<SevenBean> lastSevenCredit) {
            this.lastSevenCredit = lastSevenCredit;
        }

        public static class SevenBean {

            String day;

            String number;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }

    }

}
