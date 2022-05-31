package com.qunshang.wenpaitong.equnshang.data;

//import lombok.Data;
//import lombok.NoArgsConstructor;

/**
 * create by 何姝霖
 */
//@NoArgsConstructor
//@Data
public class UpLikeBean {

    private Integer code;
    private String msg;
    private DataDTO data;

    public DataDTO getData() {
        return data;
    }

    //@NoArgsConstructor
    //@Data
    public static class DataDTO {
        private Integer statusCode;

        public Integer getStatusCode() {
            return statusCode;
        }
    }
}
