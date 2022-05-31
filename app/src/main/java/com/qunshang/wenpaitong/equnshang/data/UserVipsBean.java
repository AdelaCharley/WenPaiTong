package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class UserVipsBean {

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

        List<UserFansBean.DataBean.FansData> levelA;

        List<UserFansBean.DataBean.FansData> levelB;

        int countListLevelA;

        int countListLevelB;

        public List<UserFansBean.DataBean.FansData> getLevelA() {
            return levelA;
        }

        public void setLevelA(List<UserFansBean.DataBean.FansData> levelA) {
            this.levelA = levelA;
        }

        public List<UserFansBean.DataBean.FansData> getLevelB() {
            return levelB;
        }

        public void setLevelB(List<UserFansBean.DataBean.FansData> levelB) {
            this.levelB = levelB;
        }

        public int getCountListLevelA() {
            return countListLevelA;
        }

        public void setCountListLevelA(int countListLevelA) {
            this.countListLevelA = countListLevelA;
        }

        public int getCountListLevelB() {
            return countListLevelB;
        }

        public void setCountListLevelB(int countListLevelB) {
            this.countListLevelB = countListLevelB;
        }

        public static class FansData {

            int id;

            int user_id;

            int user_is_vip;

            int user_is_partner;

            int up_user_id_1;

            int up_user_id_2;

            int type;

            String create_time;

            String uname;

            String utel;

            String headimage_url;

            String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getUser_is_vip() {
                return user_is_vip;
            }

            public void setUser_is_vip(int user_is_vip) {
                this.user_is_vip = user_is_vip;
            }

            public int getUser_is_partner() {
                return user_is_partner;
            }

            public void setUser_is_partner(int user_is_partner) {
                this.user_is_partner = user_is_partner;
            }

            public int getUp_user_id_1() {
                return up_user_id_1;
            }

            public void setUp_user_id_1(int up_user_id_1) {
                this.up_user_id_1 = up_user_id_1;
            }

            public int getUp_user_id_2() {
                return up_user_id_2;
            }

            public void setUp_user_id_2(int up_user_id_2) {
                this.up_user_id_2 = up_user_id_2;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getUtel() {
                return utel;
            }

            public void setUtel(String utel) {
                this.utel = utel;
            }

            public String getHeadimage_url() {
                return headimage_url;
            }

            public void setHeadimage_url(String headimage_url) {
                this.headimage_url = headimage_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

    }

}
