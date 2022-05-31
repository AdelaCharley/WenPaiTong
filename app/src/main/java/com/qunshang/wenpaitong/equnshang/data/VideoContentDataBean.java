package com.qunshang.wenpaitong.equnshang.data;

import java.util.List;

public class VideoContentDataBean {

    int statusCode;

    String message;

    List<DataBean> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        String video_desc;

        String create_time;

        int upCount;

        int likeCount;

        int replyCount;

        int shareCount;

        public String getVideo_desc() {
            return video_desc;
        }

        public void setVideo_desc(String video_desc) {
            this.video_desc = video_desc;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getUpCount() {
            return upCount;
        }

        public void setUpCount(int upCount) {
            this.upCount = upCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }
    }

}
