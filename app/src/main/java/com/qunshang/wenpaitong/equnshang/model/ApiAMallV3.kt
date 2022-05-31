package com.qunshang.wenpaitong.equnshang.model
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import com.qunshang.wenpaitong.equnshang.data.*

interface ApiAMallV3 {

    @GET("MessageController/getOrderNoticeList")
    fun loadAMallV3Message(
        @Query("userId") userId : String,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 100
    ) : Call<AMallV3MessageBean>

    @GET("OrderMallController/getServiceFeeRecord")
    fun loadBuyHistory(
        @Query("userId") userId : String,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 100
    ) : Call<BuyHistoryBean>

    @GET("MallController/getHomePage")
    fun loadAMallV3MainBean() : Call<AMallV3MainBean>

    @GET("MallController/getAmallProductListV3")
    fun searchAMallV3(
        @Query("categoryId") categoryId : Int?,
        @Query("keywords") keyword : String ?,
        @Query("manufacturerId") manufacturerId : Int?,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50,
        @Query("sortBy") sortBy : Int = 0
    ) : Call<AMallV3SearchProductBean>

    @GET("ProductCategory/getCategoryV2")
    fun loadFirstCategorys(
        @Query("categoryId") categoryId : Int? = null
    ) : Call<AMallV3CategoryFirstBean>

    @GET("UserController/getOrderCount")
    fun loadAMallV3OrderCount(
        @Query("userId") userId : String
    ) : Call<AMallV3OrderCountBean>

    @GET("MallController/getAmallProductDetailV2")
    fun loadProductDetail(
        @Query("userId") userId: String,
        @Query("productId") productId : String
    ) : Call<ProductBeanV2>

    @POST("OrderMallController/submitAmallOrderV2")
    fun submitAMallV3Order(
        @Body body : RequestBody
    ) : Call<BaseHttpBean<Int>>

    @POST("OrderMallController/submitServiceFeeOrder")
    fun submitAMallV3FeeOrder(
        @Body body : RequestBody
    ) : Call<BaseHttpBean<String>>

    @GET("OrderGroupController/getOrderGroupList")
    fun loadGroupList(
        @Query("userId") userId : String,
        @Query("status") status : Int,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 100
    ) : Call<MyGroupBean>

    @GET("MessageController/countOrderNoticeList")
    fun getMyMessages(
        @Query("userId") userId : String
    ) : Call<BaseHttpBean<Int>>

    @GET("MessageController/checkOrderNoticeList")
    fun readMessage(
        @Query("userId") userId: String,
        @Query("orderNoticeId") orderNoticeId : String
    ) : Call<BaseHttpBean<String>>

    @GET("MallController/getFirstCategoryPage")
    fun loadFirstPageCategory(
        @Query("categoryId") categoryId : Int
    ) : Call<AMallV3FirstCategoryPageBean>

    @GET("ManufacturerController/getManufactureInfoV2")
    fun loadManufactureInfo(
        @Query("manufactureId") manufactureId : Int,
        @Query("userId") userId : String
    ) : Call<ManufactureV2Bean>

    @GET("ManufacturerController/getManufactureVideo")
    fun loadManufactureVideo(
        @Query("userId") userId : String,
        @Query("manufactureId") manufactureId : Int,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<QiShiTongVideoBean>

    @GET("AfterSaleController/getAfterSaleDetail")
    fun loadAfterSaleDetail(
        @Query("afterSaleSn")afterSaleSn: String?
    ): Call<BaseHttpBean<AfterSaleDetailBean>>

    @GET("OrderGroupController/getOrderGroupDetail")
    fun loadGroupDetail(
        @Query("userId") userId : String,
        @Query("orderGroupSn") orderGroupSn : String? = null,
        @Query("orderId") orderId : Int ?= 0
    ) : Call<BaseHttpBean<PinTuanDetailBean>>

    @GET("UserController/getProductCollectionV2")
    fun loadAMallV3ProductStarBean(
        @Query("userId") userId : String,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<AMallV3ProductStarBean>

    @GET("UserController/getProductBrowserV2")
    fun loadAMallV3BroseHistoryBean(
        @Query("userId") userId : String,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 50
    ) : Call<AMallV3ProductHistoryBean>

    @GET("UserController/deleteView")
    fun deleteMyHistory(
        @Query("deleteList") deleteList : String
    ) : Call<ResponseBody>

    @GET("AfterSaleController/getUserAfterSaleList")
    fun loadAMallV3ExchangeList(
        @Query("userId") userId : String,
        @Query("type") type : String ?= null,
        @Query("pageIndex") pageIndex : Int = 1,
        @Query("pageSize") pageSize : Int = 100) : Call<ExchangeBean>

    @GET("OrderMallController/getAmallOrderListV2")
    fun loadAMallV3OrderList(
        @Query("userId") userId: String,
        @Query("orderStatus")orderStatus : String,
        @Query("pageIndex") pageIndex : String,
        @Query("pageSize") pageSize : String) : Call<OrderBean>

    @GET("AfterSaleController/submitAfterSale")
    fun submitAfterSaleApply(
        @Query("userId") userId : String,
        @Query("orderSn") orderSn : String,@Query("type") type : Int,
        @Query("refundReason") refundReason : String,
        @Query("refundExplain") refundExplain : String,
        @Query("refundPic") refundPic : String,
        @Query("addressId") addressId : String
    ) : Call<ResponseBody>

    @GET("AfterSaleController/getExpressCompany")
    fun loadExpressCompany(): Call<BaseHttpListBean<ExpressCompanyBean>>

    @FormUrlEncoded
    @POST("AfterSaleController/userSendProduct")
    fun sendProduct(
        @Field("afterSaleSn") afterSaleSn: String,
        @Field("expressCompanyCode") expressCompanyCode: String,
        @Field("expressSn") expressSn: String
    ): Call<BaseHttpBean<String>>

    @GET("UserController/getManuBehavior")
    fun loadMyStarInfoNumber(
        @Query("userId") userId: String
    ) : Call<AMallV3StarInfoBean>

    @GET("OrderMallController/getFreightAmount")
    fun loadExpressMoney(
        @Query("skuId") skuId : Int,
        @Query("addressId") addressId : String,
        @Query("skuCount") skuCount : Int
    ) : Call<FreightMoneyBean>

    @GET("MallController/getVipIntroduce")
    fun loadVipPartnerImage() : Observable<VipImageBean>

}