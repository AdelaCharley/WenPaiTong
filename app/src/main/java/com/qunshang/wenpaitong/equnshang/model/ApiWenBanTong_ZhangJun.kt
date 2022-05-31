package com.qunshang.wenpaitong.equnshang.model

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import com.qunshang.wenpaitong.equnshang.data.*

interface ApiWenBanTong_ZhangJun {

    @GET("CultureController/getCompanyDetail")
    fun loadWenBanTongCompanyDetailInfo(@Query("companyId") companyId : Int) : Call<WenBanTongCompanyDetailBean>

    @GET("CultureController/getProductDetail")
    fun loadProductDetail(@Query("productId") productId : String) : Call<WenBanTongProductBean>

    @GET("CultureController/getRealPeopleInfo")
    fun loadZiTi(@Query("userId") userId : String) : Call<WenBanTongZiTiBean>

    @POST("CultureController/createOrder")
    fun generateWenBanTongOrder(@Body body : RequestBody) : Call<BaseHttpBean<String>>

    @GET("CultureController/getOrderList")
    fun loadOrderList(@Query("userId") userId : String,
                      @Query("status") status : String,
                      @Query("pageIndex") pageIndex : Int,
                      @Query("pageSize") pageSize : Int) : Call<WenBanTongOrderListBean>

    @GET("CultureController/getOrderDetail")
    fun loadOrderDetail(@Query("orderSn") orderSn : String?) : Call<WenBanTongOrderDetailBean>

    @GET("CultureController/refundOrder")
    fun applyExchange(@Query("orderSn") orderSn : String,@Query("reason") reason : String) : Call<BaseHttpBean<String>>

    @GET("CultureController/getBannerList")
    fun loadBanner() : Call<BaseHttpListBean<String>>

    @GET("CultureController/getBannerListV2")
    fun loadBannerV2() : Call<WenBanTongBannerV2Bean>

    @GET("CollegeController/focusOrNotAgency")
    fun concernCollegeOrUnConcern(@Query("userId") userId: String,@Query("agencyId") agencyId : Int?) : Observable<BaseHttpBean<String>>

    @GET("CollegeController/getAgencyCenter")
    fun loadPublisherCenter(@Query("userId") userId : String,@Query("agencyId") agencyId : Int) : Observable<BaseHttpBean<PublisherCenterBean>>

    @GET("UserController/getUserFocusList")
    fun loadNewConcernList(@Query("userId") userId: String?,@Query("pageIndex") pageIndex : Int = 1,@Query("pageSize") pageSize : Int) : Call<NewConcernBean>

    @GET("UserController/getUserpeople")
    fun loadNewRenMaiList(@Query("userId") userId: String ,
                          @Query("type") type : Int?,
                          @Query("keyword") keyword : String? = null,
                          @Query("pageIndex") pageIndex : Int = 1,
                          @Query("pageSize") pageSize : Int) : Call<NewRenMaiBean>

    @GET("UserController/getUserPeopleNum")
    fun loadPeopleCount(@Query("userId") userId: String) : Call<NewRenMaiPeopleBean>

    @GET("CultureController/pickUp")
    fun submitWenBanTongOrder(@Query("orderSn") orderSn: String,
                              @Query("name") name : String,
                              @Query("phone")phone : String,
                              @Query("site")site : String
    ) : Observable<BaseHttpBean<String>>

}