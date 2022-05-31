package com.qunshang.wenpaitong.equnshang.data;

import java.io.Serializable;
import java.util.List;

public class EditVideoBean implements Serializable {

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

    public static class DataBean implements Serializable {

        String id;

        String video_desc;

        String create_time;

        String video_poster_url;

        String is_private;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getVideo_poster_url() {
            return video_poster_url;
        }

        public void setVideo_poster_url(String video_poster_url) {
            this.video_poster_url = video_poster_url;
        }

        public String getIs_private() {
            return is_private;
        }

        public void setIs_private(String is_private) {
            this.is_private = is_private;
        }
    }
}
