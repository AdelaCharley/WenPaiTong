package com.qunshang.wenpaitong.equnshang.data;

import android.text.SpannableString;

public class ArticleDetailBean {

    int code;

    String msg;

    DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {

        String title;

        GocInfo gocInfo;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public GocInfo getGocInfo() {
            return gocInfo;
        }

        public void setGocInfo(GocInfo gocInfo) {
            this.gocInfo = gocInfo;
        }

        public static class GocInfo{

            String name;

            String image;

            SpannableString content;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public SpannableString getContent() {
                return content;
            }

            public void setContent(SpannableString content) {
                this.content = content;
            }
        }

    }

}
