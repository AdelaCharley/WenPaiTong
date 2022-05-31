package com.qunshang.wenpaitong.equnshang.model

import okhttp3.RequestBody
import com.qunshang.wenpaitong.equnshang.adapter.HomepageNavBean
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.data.ShoppingNavBean
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


import retrofit2.http.POST

/**
 *
 * create by 何姝霖
 */
interface ApiPersonal {
    /*-----"我的"主页面-----*/
    //用户更改头像
    @FormUrlEncoded
    @POST("UserController/alterHeadimage")
    fun alterHeadImage(
        @Field("userId") userId: String,
        @Field("url") url: String
    ): Call<ResponseBody>

    //获取"我的"页面商店信息导航数据（商品收藏、店铺关注、浏览记录、优惠劵）
    @GET("UserController/getManuBehavior")
    fun loadShoppingNav(
     @Query("userId") userId: String,
    ): Call<BaseHttpListBean<ShoppingNavBean>>

    //获取"我的主页"导航数据（关注、点赞、收藏、评论）
    @GET("UserController/getUserPageData")
    fun loadHomepageNav(
        @Query("userId") userId: String,
        @Query("publisherId") publisherId: String
    ): Call<BaseHttpBean<HomepageNavBean>>


    @GET("VideoController/focusUser")
    fun loadDoConcern(@Query("userId") userId : String,@Query("leadId") leadId : String) : Call<com.qunshang.wenpaitong.equnshang.data.DoConcernBean>



    /*-----"我的主页"页面-----*/

    //获取关注列表
    //{"code":200,"msg":"成功","data":[]}
    @GET("UserController/getAttentionList")
    fun loadAttentionList(
        @Query("userId") userId: String
    ): Call<com.qunshang.wenpaitong.equnshang.data.ConcernData>

    //获取点赞列表
    //{"code":200,"msg":"成功","data":[]}
    @GET("UserController/getUpList")
    fun loadUpList(
        @Query("userId") userId: String,
    ): Call<
            RecommendVideoBean
            >

    //获取收藏列表
    //{"code":200,"msg":"获取评论成功","data":[]}
    @GET("UserController/getLikeList")
    fun loadLikeList(
        @Query("userId") userId: String,
    ): Call<RecommendVideoBean>

    //获取个人评论列表
    //{"code":200,"msg":"获取关注列表成功","data":[{"followId":1483,"headimage_url":"http:\/\/api.equnshang.com\/headimg\/mmexport1616122936800.png","uname":"群商视界运营君","create_time":"2021-07-27 14:55:38"}]}
    @GET("UserController/getCommentList")
    fun loadCommentList(
        @Query("userId") userId: String
    ): Call<com.qunshang.wenpaitong.equnshang.data.MyCommentBean>

    //获取粉丝列表
    // {"code":200,"msg":"获取粉丝列表成功","data":[]}
    @GET("UserController/getFansList")
    fun loadFansList(
        @Query("userId") userId: String,
    ): Call<ResponseBody>

    @GET("UserController/getFocusManufactuerList")
    fun loadFocusManufactuerList(@Query("userId") userId: String) : Call<FocusManufactuerBean>

    /*-----"我的订单"页面-----*/

    //获取"我的"页面商店信息导航数据（商品收藏、店铺关注、浏览记录、优惠劵）
    @GET("OrderMallController/getAmallOrderListV2")
    fun loadOrderList(
        @Query("userId") userId: String,
        @Query("orderStatus") orderStatus: Int,  //-1->全部订单；0->待付款；1->待成团，2->待发货；3->已发货；4->已完成；5->已关闭；6->无效订单
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
    ): Call<BaseHttpListBean<OrderBean>>

    @GET("OrderMallController/getDefaultAddress")
    fun loadAddressList(@Query("userId") userId: String): Call<BaseHttpBean<List<AddressBean>>>

    @GET("UserController/getProductCollection")
    fun loadUserProductCollection(@Query("userId") userId: String) : Call<UserProductCollectionBean>

    @GET("UserController/deleteCollect")
    fun unstarProductCollection(@Query("deleteList") deleteList : String) : Call<BaseHttpBean<String>>

    @GET("ManufacturerVideoController/focusUser")
    fun focusMan(@Query("userId")userId: String,@Query("leadId") leadId: String) : Call<BaseHttpBean<Boolean>>

    @GET("UserController/getProductBrowser")
    fun loadProductBrowse(@Query("userId") userId: String) : Call<BrowserHistoryBean>

    @GET("CouponController/getUserCouponList")
    fun loadDiscountTickert(@Query("userId") userId: String,@Query("useStatus") useStatus : String,
                            @Query("pageIndex") pageIndex : Int,@Query("pageSize") pageSize: Int) : Call<DiscountTicketBean>

    @GET("OrderMallController/getDefaultAddress")
    fun loadDefaultAddress(@Query("userId") userId: String) : Call<DefaultAddressBean>

    @GET("BehaviorController/getQunCoinInfo")
    fun loadWorkPoint(@Query("userId") userId: String) : Call<WorkPointBean>

    @GET("BehaviorController/getTodayBehaviorInfo")
    fun loadCurrentDayWorkPoint(@Query("userId") userId: String) : Call<DayWorkPointBean>

    @GET("UserController/getBehaviorInfo")
    fun loadNewCurrentDayWorkPoint(@Query("userId") userId: String) : Call<NewGongFenBean>

    @GET("BehaviorController/getYesterdayCreditInfo")
    fun loadYesterDayWorkPoint(@Query("userId") userId: String) : Call<YCWorkPointBean>

    @GET("BehaviorController/getCurrentWeekCreditInfo")
    fun loadCurrentWeekWorkPoint(@Query("userId") userId: String) : Call<YCWorkPointBean>

    @GET("BehaviorController/getCurrentMonthCreditInfo")
    fun loadCurrentMonthWorkPoint(@Query("userId") userId: String) : Call<YCWorkPointBean>

    @GET("BehaviorController/getLastMonthCreditInfo")
    fun loadLastMonthWorkPoint(@Query("userId") userId: String) : Call<YCWorkPointBean>

    @GET("BehaviorController/getQunCoinInfo")
    fun loadGroupTicketWorkPoint(@Query("userId") userId: String) : Call<QunCoinBean>

    @GET("BehaviorController/getQunCoinInfoV2")
    fun loadGroupTicketWorkPointV2(@Query("userId") userId: String) : Call<QunCoinBeanV2>

    @GET("LoginController/updateInviteInfo")
    fun bindInviter(@Query("userId") userId: String,@Query("phoneData") phoneData :String) : Call<BaseHttpBean<Int>>

    @GET("LoginController/editUserInfo")
    fun updatePersonalData(@Query("userId") userId: String,
                           @Query("uname") uname : String,
                           @Query("gender") gender : String,
                           //@Query("birthday") birthday : String,
                           //@Query("tags") tags : String,
                           @Query("headimageUrl") headimageUrl : String) : Call<UserMsgBean>

    @GET("UserController/getPartnerVideoList")
    fun loadPublishs(@Query("userId") userId: String , @Query("publisherId") publisherId: String) : Call<RecommendVideoBean>

    @GET("ManufacturerController/getManufactureInfo")
    fun loadManufactureInfo(@Query("manufactureId") manufactureId : String,
                            @Query("origin") origin : String,
                            @Query("userId") userId : String) : Call<StoreDataBean>

    @FormUrlEncoded
    @POST("VideoManageController/updateVideoPrivate")
    fun alertPrivate(
        @Field("videoId") videoId: String
    ): Call<BaseHttpBean<Int>>

    @GET("VideoController/editVideo")
    fun editVideo(@Query("videoId") videoId : String,@Query("video_desc") video_desc : String,@Query("video_isPrivate") video_isPrivate : String,
    @Query("video_tag") video_tag : String = "") : Call<BaseHttpBean<Int>>

    @FormUrlEncoded
    @POST("VideoController/deleteVideo")
    fun deleteVideo(@Field("videoId") videoId: String) : Call<BaseHttpBean<Int>>


    @GET("VodController/uploadVideoSign")
    fun getSig() : Call<Signature>

    //@FormUrlEncoded
    @GET("VideoController/uploadVideoData")
    fun uploadData(@Query("fileIdsId") fileIdsId : String, @Query("videoPosterUrl") videoPosterUrl : String,
                   @Query("videoUrl")videoUrl : String, @Query("userId")userId : String, @Query("videoDesc") videoDesc : String,
                   @Query("videoTag") videoTag : String, @Query("isPrivate") isPrivate : Int, @Query("productId") productId : Int = 0) : Call<BaseHttpBean<Int>>

    @GET("UserManageController/findFanList")
    fun loadFansData(@Query("userId") userId: String = "1",
                     @Query("type") type : Int = 0,
                     @Query("pageIndex") pageIndex : Int = 0,
                     @Query("pageSize") pageSize : Int = 100) : Call<UserFansBean>

    @GET("UserManageController/findVipList")
    fun loadVipsData(@Query("userId") userId: String = "1",
                     @Query("type") type : Int = 0,
                     @Query("pageIndex") pageIndex : Int = 0,
                     @Query("pageSize") pageSize : Int = 100) : Call<UserVipsBean>

    @GET("UserManageController/findPartnerList")
    fun loadPartnerData(@Query("userId") userId: String = "1",
                        @Query("type") type : Int = 0,
                        @Query("pageIndex") pageIndex : Int = 0,
                        @Query("pageSize") pageSize : Int = 100
    ) : Call<UserPartnerBean>

    @POST("UserController/submitIdCardInfo")
    fun submitIdentity(@Body body : RequestBody) : Call<BaseHttpBean<Int>>

    @GET("UserController/identifyIdCardInfo")
    fun identifyIdCard(@Query("userId") userId : String,@Query("IDCardFace") IDCardFace : String,@Query("IDCardBack") IDCardBack : String) : Call<IdBean>

    @GET("O2OController/scanVendor")
    fun signIn(@Query("vendorId") vendorId : Int,@Query("userId") userId : Int) : Call<ResponseBody>

    @GET("UserController/getUserOutStatus")
    fun loadLogOutStatus(@Query("userId") userId: String) : Call<LogOutStatusBean>

    @GET("UserController/submitUserOut")
    fun submitLogOutApply(@Query("userId") userId : String) : Call<BaseHttpBean<String>>

}