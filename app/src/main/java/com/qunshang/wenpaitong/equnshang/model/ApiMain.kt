package com.qunshang.wenpaitong.equnshang.model

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import com.qunshang.wenpaitong.equnshang.data.*
import javax.annotation.Generated

/**
 * 通知接口
 * create by 何姝霖
 */
interface ApiMain {

    //获取个人信息（暂时放在这儿，应该放主页的）
    @GET("UserController/getUserInfo")
    fun loadPersonalInfo(
        @Query("userId") userId: String
    ): Call<UserMsgBean>

    //当前视频发布者信息页面
    @GET("NoticeController/getNoticeList")
    fun loadSystemInform(@Query("userId") userId: String?): Call<BaseHttpListBean<SystemInformBean>>

    @GET("UserController/getMemberNumber")
    fun loadPriateAreaData(@Query("userId") userId: String) : Call<BaseHttpListBean<Int>>

    @GET("MessageController/getUnCheckNum")
    fun getUnReadMessageCount(@Query("userId") userId: String) : Call<BaseHttpBean<Int>>

    @GET("MessageController/watchAllNotice")
    fun makeUnReadToRead(@Query("userId") userId : String) : Call<ResponseBody>

    @GET("Device/updateApp")
    fun checkVersionToUpdate(@Query("version") version : Int) : Call<VersionBean>

    @GET("Device/saveInfo")
    fun uploadPhoneState(@Query("appid") appid : String = "__UNI__89F14E3",@Query("imei") imei : String = "",
                         @Query("platform") platform : String = "a",
                         @Query("model") model : String,
    @Query("app_version") app_version : String,
                         @Query("plus_version") plus_version : String = "",
                         @Query("os") os : String,@Query("net") net : String = "",@Query("user_id") user_id : String) : Call<ResponseBody>

    @GET("MarketActivityController/getInviteCodePicture")
    fun loadActivityStatus() : Observable<QrCodeActivityBean>

    @GET("MarketActivityController/getInviteCodePicture")
    fun loadActivityStatusCommon() : Call<QrCodeActivityBean>

    @GET("DailyTaskController/completeTask")
    fun completeTask(@Query("userId") userId : String,@Query("type") type : Int) : Call<BaseHttpBean<String>>

}