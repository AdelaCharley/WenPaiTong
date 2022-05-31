package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class UserPartnerBean {

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

        List<PartnerData> levelA;

        List<PartnerData> levelB;

        List<PartnerData> levelC;

        int countListLevelA;

        int countListLevelB;

        int countListLevelC;

        int more;

        public int getMore() {
            return more;
        }

        public void setMore(int more) {
            this.more = more;
        }

        public List<PartnerData> getLevelA() {
            return levelA;
        }

        public void setLevelA(List<PartnerData> levelA) {
            this.levelA = levelA;
        }

        public List<PartnerData> getLevelB() {
            return levelB;
        }

        public void setLevelB(List<PartnerData> levelB) {
            this.levelB = levelB;
        }

        public List<PartnerData> getLevelC() {
            return levelC;
        }

        public void setLevelC(List<PartnerData> levelC) {
            this.levelC = levelC;
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

        public int getCountListLevelC() {
            return countListLevelC;
        }

        public void setCountListLevelC(int countListLevelC) {
            this.countListLevelC = countListLevelC;
        }

        public static class PartnerData {

            int id;

            int user_id;

            int uppartner_id_1;

            int uppartner_id_2;

            int uppartner_id_3;

            int manage_partner_id;

            int city_partner_id;

            String create_time;

            String partner_company_id;

            int code_num;

            String last_modify_time;

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

            public int getUppartner_id_1() {
                return uppartner_id_1;
            }

            public void setUppartner_id_1(int uppartner_id_1) {
                this.uppartner_id_1 = uppartner_id_1;
            }

            public int getUppartner_id_2() {
                return uppartner_id_2;
            }

            public void setUppartner_id_2(int uppartner_id_2) {
                this.uppartner_id_2 = uppartner_id_2;
            }

            public int getUppartner_id_3() {
                return uppartner_id_3;
            }

            public void setUppartner_id_3(int uppartner_id_3) {
                this.uppartner_id_3 = uppartner_id_3;
            }

            public int getManage_partner_id() {
                return manage_partner_id;
            }

            public void setManage_partner_id(int manage_partner_id) {
                this.manage_partner_id = manage_partner_id;
            }

            public int getCity_partner_id() {
                return city_partner_id;
            }

            public void setCity_partner_id(int city_partner_id) {
                this.city_partner_id = city_partner_id;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getPartner_company_id() {
                return partner_company_id;
            }

            public void setPartner_company_id(String partner_company_id) {
                this.partner_company_id = partner_company_id;
            }

            public int getCode_num() {
                return code_num;
            }

            public void setCode_num(int code_num) {
                this.code_num = code_num;
            }

            public String getLast_modify_time() {
                return last_modify_time;
            }

            public void setLast_modify_time(String last_modify_time) {
                this.last_modify_time = last_modify_time;
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
