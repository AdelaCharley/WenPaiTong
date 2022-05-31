package com.qunshang.wenpaitong.equnshang.model;

import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * 登录功能所有的网络接口
 * create by 何姝霖
 */
public interface ApiLogin {
    /*-----"登录"页面-----*/

    //获取验证码
    @FormUrlEncoded
    @POST("aliApi/sendLoginSms.php")
    Call<ResponseBody> getLoginVCode(
            @Field("phoneNum") String phoneNum
    );
    //获取用户信息
    @GET("LoginController/newLogin")
    Call<BaseHttpBean> getLoginUserMsg(
            @Query("phone") String phoneData,
            @Query("checkCode") String checkCode,
            @Query("bizId") String bizId,
            @Query("password") String password
    );



    /*-----"注册"页面-----*/

    //获取验证码
    @FormUrlEncoded
    @POST("aliApi/sendRegisterSms.php")
    Call<ResponseBody> getRegisterVCode(
            @Field("phoneNum") String phoneNum
    );
    //获取用户信息
    @GET("LoginController/register")
    Call<BaseHttpBean> getRegisterUserMsg(
            @Query("phoneData") String phoneData,
            @Query("checkCode") String checkCode,
            @Query("bizId") String bizId
    );



    /*-----"填写推荐人"页面-----*/

    //根据填写号码，展示对于推荐人信息
    @GET("LoginController/getInviterInfo")
    Call<ResponseBody> getReferee(
            @Query("phoneData") String phoneData
    );

    //为正在注册的用户绑定邀请人
    @GET("LoginController/updateInviteInfo")
    Call<BaseHttpBean> updateInviteInfo(
            @Query("userId") String userId,
            @Query("phoneData") String phoneData
    );



    /*-----"填写详细信息"界面-----*/

    //上传头像
    @Multipart
    @POST("LoginController/uploadImage")
    Call<String> uploadHeadImage(
            @PartMap Map<String, ResponseBody> files
    );
    //录入个人信息
    @FormUrlEncoded
    @POST("LoginController/editUserInfo")
    Call<ResponseBody> enterUserInfo(
            @FieldMap Map<String, String> userInfoMap
    );
}
