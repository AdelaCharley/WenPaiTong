package com.qunshang.wenpaitong.equnshang.model

import io.reactivex.rxjava3.core.Observable
import com.qunshang.wenpaitong.equnshang.data.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiMall {

    @GET("MallController/getAmallProductList")
    fun loadAMallProducts(@Query("userId") userId: String): Call<AMallProductBean>

    @GET("MallController/getAmallProductDetail")
    fun loadProductDetail(@Query("userId") userId: String,@Query("productId") productId : String) : Call<ProductBean>

    @GET("MallController/collectProductV2")
    fun loadStarProduct(@Query("userId") userId: String,@Query("productId")productId: String) : Call<StarProductBean>

    @GET("MallController/collectProduct")
    fun loadStarProductOLD(@Query("userId") userId: String,@Query("productId")productId: String) : Call<StarProductBeanOLD>

    @GET("OrderMallController/getAmallOrderListV2")
    fun loadOrders(@Query("userId") userId: String,
                   @Query("orderStatus")orderStatus : String,
                   @Query("pageIndex") pageIndex : String,
                   @Query("pageSize") pageSize : String) : Call<OrderBean>

    @GET("OrderMallController/confirmAmallOrderExpress")
    fun confirmReceipt(@Query("orderId") orderId : String) : Call<BaseHttpBean<Int>>

    @GET("OrderMallController/getAmallOrderInfo")
    fun loadOrderDetail(@Query("orderId") orderId : String) : Call<OrderDetailBean>

    @GET("MallController/searchAmallProduct")
    fun loadSearchAMallResult(@Query("userId") userId: String,@Query("keyword") keyword : String) : Call<AMallProductBean>

    @GET("OrderMallController/cancelAmallOrder")
    fun cancelOrder(@Query("orderId") orderId: String) : Call<BaseHttpBean<Int>>

    @GET("OrderMallController/getAmallExpressInfo")
    fun loadAMallExpress(@Query("orderId") orderId: String) : Call<ExpressBean>

    @GET("OrderMallController/getBmallExpressInfo")
    fun loadBMallExpress(@Query("orderId") orderId: String) : Call<ExpressBean>

    @POST("OrderMallController/submitAmallOrder")
    fun submitAMallOrder(@Body body : RequestBody) : Call<BaseHttpBean<Int>>

    @POST("OrderMallController/submitBmallOrder")
    fun submitBMallOrder(@Body body: RequestBody) : Call<BaseHttpBean<String>>

    @GET("ProductCategory/getCategory")
    fun loadAllCategory() : Call<CategoryBean>

    @GET("ProductCategory/getCategory")
    fun loadCategroy(@Query("categoryId") categoryId : Int,@Query("pageIndex") pageIndex : Int,@Query("pageSize") pageSize : Int) : Call<CategoryBean>

    @GET("MallController/getAmallBannerList")
    fun loadAMallBanner() : Call<ABMallBannerBean>

    @GET("MallController/getAmallProductListV2")
    fun loadProduct(@Query("categoryId") categoryId: Int,@Query("pageIndex") pageIndex: Int,@Query("pageSize") pageSize: Int) : Call<AMallProductBeanV2>

    @GET("MallController/getAmallProductListV2")
    fun searchProduct(@Query("categoryId") categoryId: Int?,
                      @Query("pageIndex") pageIndex: Int,
                      @Query("pageSize") pageSize: Int,
                      @Query("brandId") brandId: Int?,
                      @Query("keyword") keyword : String?) : Call<AMallProductBeanV2>

    @GET("MallController/getBmallProductList")
    fun getBMallList() : Call<BMallProductBean>

    @GET("MallController/getBmallBannerList")
    fun getBMallBanner() : Call<ABMallBannerBean>

    @GET("MallController/getBmallProductDetail")
    fun loadBMallProductDetail(@Query("userId") userId: String,@Query("productId") productId: String) : Call<ProductBean>

    @GET("MallController/getManufactureDetailInfo")
    fun loadManufactureQuantity(@Query("manufactureId")manufactureId : Int) : Observable<ManufactureQualifactionBean>

    @GET("MallController/getFaq")
    fun loadAMallQuestions() : Call<AMallQuestionBean>

    @GET("MallController/searchBmallProduct")
    fun loadBMallProducts(@Query("userId")userId : String,@Query("keyword") keyword : String,@Query("manufactureId") manufactureId : Int): Call<BMallProductBean>

    @GET("ProductManageController/getPurchaseList")
    fun loadMyBuyBean (@Query("userId") userId: String) : Call<MyBuyBean>

    @GET("OrderMallController/getBmallOrderInfo")
    fun loadMyBuyOrderDetailBean(@Query("orderId") orderId: String) : Call<MyBuyOrderBean>

    @GET("OrderMallController/confirmBmallOrderExpress")
    fun confirmBMallOrder(@Query("orderId") orderId : String) : Call<BaseHttpBean<Int>>

    @GET("OrderMallController/cancelBmallOrder")
    fun cancelBMallOrder(@Query("orderId") orderId : String) : Call<BaseHttpBean<Int>>

    @GET("OrderMallController/deleteBmallOrder")
    fun deleteBMallOrder(@Query("orderId")orderId : String) : Call<BaseHttpBean<Int>>

    @GET("OrderMallController/deleteAmallOrder")
    fun deleteAMallOrder(@Query("orderId")orderId : String) : Call<BaseHttpBean<Int>>

    @GET("CouponController/orderCouponList")
    fun loadProductDiscountTicket(@Query("userId") userId: String,@Query("productId") productId : Int,@Query("buyType") buyType : Int = 3) : Call<DiscountTicketBean>

    @Headers("Accept-Language:zh-cn,zh;q=0.5")
    @GET("PayController/payAmallOrder")
    fun payAliAMall(@Query("provider") provider : String,
                    @Query("orderId") orderId : String?,
                    @Query("payUserId") payUserId : String) : Call<ResponseBody>

    @Headers("Accept-Language:zh-cn,zh;q=0.5")
    @GET("PayController/payCultureOrder")
    fun payWenBanTong(@Query("provider") provider : String,
                    @Query("orderSn") orderId : String?,
                    @Query("payUserId") payUserId : String?) : Call<ResponseBody>

    @Headers("Accept-Language:zh-cn,zh;q=0.5")
    @GET("PayController/payBmallOrder")
    fun payAliBMall(@Query("provider") provider : String,
                    @Query("orderId") orderId : String?) : Call<AliPayBean>

    @GET("OrderMallController/modifyBmallOrderAddress")
    fun modifyBMallAddress(@Query("orderId")orderId : Int,@Query("addressId") addressId : Int) : Call<BaseHttpBean<Int>>

    @GET("OrderMallController/modifyAmallOrderAddress")
    fun modifyAMallAddress(@Query("orderId")orderId : Int,@Query("addressId") addressId : Int) : Call<BaseHttpBean<Int>>

    @GET("DailyTaskController/getCustomerServiceQrcode")
    fun loadCustomInfo(@Query("key") key : String) : Call<CustomerBean>

    @GET("CultureController/getCultureOrderExpressInfo")
    fun loadWenBanTongExpress(@Query("orderSn") orderSn : String) : Observable<ExpressBean>

}