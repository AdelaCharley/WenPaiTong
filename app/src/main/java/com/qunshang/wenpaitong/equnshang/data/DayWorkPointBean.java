package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class DayWorkPointBean {

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
        String currentCredit;

        String averCredit;

        List<Credit> todayCreditByKind;

        List<LastSevenDayData> lastSevenCredit;

        public List<LastSevenDayData> getLastSevenCredit() {
            return lastSevenCredit;
        }

        public void setLastSevenCredit(List<LastSevenDayData> lastSevenCredit) {
            this.lastSevenCredit = lastSevenCredit;
        }

        public String getCurrentCredit() {
            return currentCredit;
        }

        public void setCurrentCredit(String currentCredit) {
            this.currentCredit = currentCredit;
        }

        public String getAverCredit() {
            return averCredit;
        }

        public void setAverCredit(String averCredit) {
            this.averCredit = averCredit;
        }

        public List<Credit> getTodayCreditByKind() {
            return todayCreditByKind;
        }

        public void setTodayCreditByKind(List<Credit> todayCreditByKind) {
            this.todayCreditByKind = todayCreditByKind;
        }

        public class Credit{

            String name;

            String number;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
            /*
            {"code":200,"msg":"获取积分成功!","data":{"currentCredit":"425909.1","averCredit":11119.74,"todayCreditByKind":[{"name":"点赞","number":"0.0"},{"name":"评论","number":"0.0"},{"name":"浏览","number":"1.1"},{"name":"拉新","number":"0.0"},{"name":"消费","number":"0.0"},{"name":"内容","number":"0"}],"lastSevenCredit":[{"day":"08-25","number":"2.0"},{"day":"08-26","number":"183206.1"},{"day":"08-27","number":"2.1"},{"day":"08-28","number":"0.0"},{"day":"08-29","number":"0.0"},{"day":"08-30","number":"1.1"},{"day":"08-31","number":"1.1"}],"qunCoin":337}}
             */

        }

        public class LastSevenDayData {
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
