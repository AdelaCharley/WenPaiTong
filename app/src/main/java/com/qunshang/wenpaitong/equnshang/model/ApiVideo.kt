package com.qunshang.wenpaitong.equnshang.model

import com.qunshang.wenpaitong.equnshang.data.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiVideo {

    @FormUrlEncoded
    @POST("collegeController/getVideoList")   //获取视频列表
    fun loadVideoList(@Field("userId")userId: String?,
                      @Field("agencyId") agencyId : Int? = null,
                      @Field("pageIndex")pageIndex : Int = 1,
                      @Field("pageSize") pageSize : Int = 5): Call<RecommendVideoBean>

    @FormUrlEncoded
    @POST("ManufacturerVideoController/getManufacturerVideoList")
    fun loadQiShiTongVideo(@Field("userId")userId: String?): Call<QiShiTongVideoBean>

    @FormUrlEncoded
    @POST("VideoController/getPastLotteryVideoList")  //获取往日抽奖视频接口
    fun loadLashLotteryVideo(@Field("userId")userId: String?): Call<LotteryVideoBean>

    @FormUrlEncoded
    @POST("VideoController/getPioneerVideoList")//创业邦视频接口
    fun loadPioneerVideo(@Field("userId")userId: String?): Call<RecommendVideoBean>

    @FormUrlEncoded
    @POST("VideoController/getTodayLotteryVideoList")
    fun loadNowDayLotteryVideo(@Field("userId")userId: String?): Call<LotteryVideoBean>

    @FormUrlEncoded
    @POST("VideoController/getVipActivityVideoList")
    fun loadVIPDayLotteryVideo(@Field("userId")userId: String?): Call<LotteryVideoBean>

    @FormUrlEncoded
    @POST("VideoController/getDailyTaskVideoList")
    fun loadWeekLotteryVideo(@Field("userId")userId: String?): Call<LotteryVideoBean>

    //当前视频发布者信息页面
    @GET("UserController/getUserPageInfo")
    fun loadPublisher(@Query("userId") userId: String?,
                      @Query("publishId") publishId: String): Call<ResponseBody>

    //点赞当前视频
    @GET("VideoController/doUp")
    fun upVideo(@Query("userId") userId: String,
                @Query("videoId") videoId: Int,
                @Query("publishId") publishId: String): Call<UpLikeBean>

    //收藏当前视频
    @GET("VideoController/doLike")
    fun likeVideo(@Query("userId") userId: String,
                  @Query("videoId") videoId: Int,
                  @Query("publishId") publishId: String): Call<UpLikeBean>


    /*-----评论-----*/
    //加载当前视频评论
    @GET("VideoController/getVideoReplyList")
    fun loadComment(@Query("userId")userId: String,@Query("videoId") videoId: Int): Call<CommentBean>
    //喜欢or取消喜欢评论
    @GET("VideoController/likeComment")
    fun likeComment(@Query("userId") userId: String,
                    @Query("commentId") commentId: String): Call<BaseHttpBean<Int>>
    //发表评论
    @GET("VideoController/submitVideoReply")
    fun sendComment(@Query("userId") userId: String,
                    @Query("videoId") videoId: Int,
                    @Query("publishId") publishId: String,
                    @Query("replyContent") replyContent: String
    ): Call<BaseHttpBean<CommentBean.DataDTO>>

    @GET("VideoController/searchVideoList")
    fun loadSearchedVideoList(@Query("userId") userId: String,@Query("keyWord")keyWord : String) : Call<
            //SearchVideoBean
            RecommendVideoBean
            >

    @GET("PastLotteryController/getPastLotteryWinList")
    fun loadLastUserGetLottery() : Call<BaseHttpBean<String>>

    @GET("PastLotteryController/getPastLotteryList")
    fun loadMyLotteryList(@Query("userId") userId: String) : Call<MyLastLotteryData>

    @GET("TodayLotteryController/getSwiperList")
    fun loadImages(@Query("key") key : String) : Call<SwipeDataBean>

    @GET("PastLotteryController/getPastLotteryList")
    fun loadLastLotteryResult(@Query("userId") userId: String) : Call<LastLotteryResultBean>

    @GET("TodayLotteryController/getWinInfo")
    fun loadPrizeInfo(@Query("userId") userId: String,@Query("activityId") activityId : String) : Call<PrizeInfoBean>

    @GET("ExperienceController/getExperienceInfo")
    fun loadExperienceInfo(@Query("experienceId") experienceId : String,@Query("userId") userId : String) : Call<ExperienceInfoBean>

    @GET("ExperienceController/listExperienceComments")
    fun loadExperience(@Query("experienceId") experienceId : String,@Query("start") start : String,@Query("userId") userId : String) : Call<ExperienceBean>

    @GET("ExperienceController/getCommentInfo")
    fun loadReplys(@Query("commentId") commentId : String,@Query("userId") userId : String,@Query("start") start : String) : Call<ReplyBean>

    @GET("TodayLotteryController/insertUserObtainStatus")
    fun updateVideoStatus(@Query("userId") userId : String) : Call<BaseHttpBean<Int>>//更新用户视频状态

    @GET("TodayLotteryController/getTodayLotteryNumberObtainStatus")
    fun getVideoStatus(@Query("userId") userId : String) : Call<DailyTaskStatusBean>//获取用户的奖券状态

    @GET("TodayLotteryController/obtainLotteryNumber")
    fun receiveTicket(@Query("userId") userId : String) : Call<BaseHttpBean<Int>>

    @GET("WeekTask/getTaskPoster")
    fun loadWeekImages() : Call<SwipeDataBean>

    @GET("WeekTask/getWeekTaskInviteInfo")
    fun loadWeekInviteList(@Query("userId") userId: String) : Call<WeekInviteListDataBean>

    @GET("ManufacturerVideoController/upManufacturerVideo")
    fun upManufactureVideo(@Query("userId") userId: String,@Query("manufacturerVideoId") manufacturerVideoId : String) : Call<UpLikeBean>

    @GET("ManufacturerVideoController/likeManufacturerVideo")
    fun likeManufactureVideo(@Query("userId") userId: String,@Query("manufacturerVideoId") manufacturerVideoId : String) : Call<UpLikeBean>

    @GET("ManufacturerVideoController/getVideoReplyList")
    fun loadManufactureComments(@Query("userId") userId: String,@Query("videoId") videoId: Int): Call<CommentBean>

    //发表评论
    @GET("ManufacturerVideoController/commentManufacturerVideo")
    fun sendManufactureComment(@Query("userId") userId: String,
                    @Query("videoId") videoId: Int,
                    @Query("publishId") publishId: String,
                    @Query("replyContent") replyContent: String
    ): Call<BaseHttpBean<CommentBean.DataDTO>>

    @GET("ManufacturerVideoController/likeComment")
    fun likeManufactureComment(@Query("userId") userId: String,
                    @Query("commentId") commentId: String): Call<BaseHttpBean<Int>>

    @GET("VideoController/focusUser")
    fun focusWriter(@Query("userId") userId: String,@Query("leadId") leadId : String) : Call<BaseHttpBean<Boolean>>

    @GET("ManufacturerVideoController/focusUser")
    fun focusManufactureWriter(@Query("userId") userId: String,@Query("leadId") leadId : String) : Call<BaseHttpBean<Boolean>>

    @GET("ExperienceController/upExperience")
    fun upExperience(@Query("experienceId")experienceId : Int,@Query("userId") userId: String) : Call<BaseHttpBean<Int>>

    @FormUrlEncoded
    @POST("ExperienceController/upComment")
    fun upComment(@Field("commentId") commentId : String,@Field("userId") userId: String) : Call<BaseHttpBean<Int>>

    @GET("ExperienceController/comment")
    fun commentExperienceOrComment(@Query("experienceId") experienceId : String,
                                   @Query("commentId") commentId : String,
                                   @Query("content") content : String, @Query("userId") userId: String) : Call<SubmitCommentForExperienceBean>

    @GET("VideoController/videoPasswordCreat")
    fun loadVideoPassword(@Query("userId") userId: String,@Query("videoId") videoId: Int) : Observable<VideoPasswordBean>

    @GET("VideoController/watchVideo")
    fun watchVideoToBrowse(@Query("userId") userId: String) : Call<ResponseBody>

}