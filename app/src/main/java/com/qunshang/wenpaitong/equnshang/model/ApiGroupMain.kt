package com.qunshang.wenpaitong.equnshang.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiGroupMain {

    @GET("GroupOwnerController/getGroupOwnerList")
    fun loadMainList(
        @Query("keyword") keyword : String = "",
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<com.qunshang.wenpaitong.equnshang.data.GroupMainListBean>

    @GET("GroupOwnerController/IncomeTotal")
    fun loadTotalIncome(
        @Query("userId") userId : String = "261",
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<com.qunshang.wenpaitong.equnshang.data.TotalIncomeBean>

    @GET("GroupOwnerController/getGroupOwnerProductList")
    fun loadGroupMainProductList(
        @Query("gocId") gocId : Int,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<com.qunshang.wenpaitong.equnshang.data.GroupMainProductBean>

    @GET("GroupOwnerController/GroupOwnerIncome")
    fun getIncomeOfGroupMain(
        @Query("userId") userId: String = "261",
        @Query("gocId") gocId : Int = 1,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean>

    @GET("GroupOwnerController/getHomePageInfo")
    fun getGroupMainPageInfo(
        @Query("gocId") gocId : Int,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<com.qunshang.wenpaitong.equnshang.data.GroupMainHomePageInfoBean>

    @GET("GroupOwnerController/getArticleDetail")
    fun getArticleDetailBean(
        @Query("article_id") article_id : Int
    ) : Call<com.qunshang.wenpaitong.equnshang.data.ArticleDetailBean>

    @GET("GroupOwnerController/getArticleDetail")
    fun getArticleDetailBeanForString(
        @Query("article_id") article_id : Int
    ) : Call<ResponseBody>

    @GET("GroupOwnerController/IncomeDetail")
    fun loadIncomeDetail(
        @Query("userId") userId: String = "261",
        @Query("productId") productId : Int,
        @Query("state") state : Int,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<com.qunshang.wenpaitong.equnshang.data.IncomeDetailBean>

}