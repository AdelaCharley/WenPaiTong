package com.qunshang.wenpaitong.equnshang.model

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import com.qunshang.wenpaitong.equnshang.activity.MyCollectionItemBean
import com.qunshang.wenpaitong.equnshang.data.*

interface ApiWenBanTong_HeShuLin {
    @GET("CultureController/getProductList")
    fun loadProductList(
        @Query("companyId") companyId: Int?,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Call<BaseHttpListBean<CultureProductBean>>

    @GET("cultureController/getCompanyList")
    fun loadCompanyList(
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Call<BaseHttpListBean<CultureCompanyBean>>

    @GET("cultureController/getNewsList")
    fun loadNewsList(
        @Query("companyId") companyId: Int?,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Call<BaseHttpListBean<CultureNewsBean>>

    /* QunShang college ------------------------------------------*/
    @GET("collegeController/getNewList")
    fun loadCollegeNewList(
        @Query("agencyId") angencyId: Int?,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Call<BaseHttpListBean<CollegeNewItemBean>>

    /* personal center -------------------------------------------*/
    @GET("UserController/getCenterInfo")
    fun loadCenterInfo(
        @Query("userId") userId: Int
    ): Call<BaseHttpBean<UserFocusCollectNum>>

    @GET("UserController/getUserCollectList")
    fun loadMyCollectionList(
        @Query("userId") userId: Int,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Call<BaseHttpListBean<MyCollectionItemBean>>

    @FormUrlEncoded
    @POST("aliApi/sendResetSms.php")
    fun getLoginVCode(
        @Field("phoneNum") phoneNum: String?
    ): Call<VCodeBean>

    @FormUrlEncoded
    @POST("LoginController/modifyLoginPassword")
    fun modifyLoginPassword(
        @Field("userId") userId: String,
        @Field("bizId") bizId: String,
        @Field("checkCode") checkCode: String,
        @Field("newPassword") newPassword: String
    ): Call<BaseHttpBean<String>>

    @POST("UserController/deleteCollections")
    fun deleteCollections(
        @Body data: RequestBody
    ): Call<BaseHttpBean<String>>

    @GET("UserController/getWBTCredit")
    fun getWBTCredit(
        @Query("userId") userId: String
    ): Call<BaseHttpBean<String>>

    @GET("UserController/getWBTCreditLog")
    fun getWBTCreditLog(
        @Query("userId") userId: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): Call<BaseHttpListBean<WBTCreditBean>>

    @GET("UserController/getMoneyList")
    fun getCommissionList(
        @Query("userId") userId: Int
    ): Call<BaseHttpListBean<MyCommissionBean>>

    @GET("MarketActivityController/getNewcomerGiftInfo")
    fun getNewUserGiftTime(
        @Query("userId") userId: Int
    ): Call<BaseHttpBean<NewcomerGiftInfoBean>>
}