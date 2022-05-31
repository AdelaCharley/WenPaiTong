package com.qunshang.wenpaitong.equnshang.model

import okhttp3.ResponseBody
import com.qunshang.wenpaitong.equnshang.data.*
import retrofit2.Call
import retrofit2.http.*

interface ApiLottery {

    @GET("TodayLotteryController/getUserLotteryNumberList")
    fun loadTodayTickets(@Query("userId") userId : String) : Call<TodayTicketBean>

    @GET("TodayLotteryController/getTodayInviteList")
    fun loadInviteList(@Query("userId") userId : String) : Call<ToDayInviteListBean>

    @GET("TodayLotteryController/getTodayLotteryActivityList")
    fun loadTodayLotteryBean(@Query("userId") userId: String) : Call<ToDayLotteryActivityBean>

    @GET("TodayLotteryController/betNumber")
    fun betBumber(@Query("userId") userId: String,
                  @Query("activityId") activityId : String,
                  @Query("number") number : String) : Call<BaseHttpBean<Int>>

    @GET("WeekTask/getWeekTaskStatus")
    fun getWeekStatus(@Query("userId") userId : String) : Call<WeekVideoStatus>

    @FormUrlEncoded
    @POST("WeekTask/obtainTask")
    fun receiveTicket(@Field("userId") userId: String) : Call<WeekReceiveTicketBean>

    @FormUrlEncoded
    @POST("WeekTask/insertUserTaskVideoStatus")
    fun insertVideoWatchCompelete(@Field("userId") userId: String) : Call<BaseHttpBean<String>>

    @GET("WeekTask/getWeekTaskPrizeInfo")
    fun loadWeekPrize(@Query("userId") userId: String) : Call<WeekPrizeBean>

    @GET("VipActivityController/getVipNearlyActivityId")
    fun getVIPDayActivityId() : Call<ResponseBody>

    @GET("VipActivityController/getVipActivityPrizeList")
    fun getPrizes(@Query("activityId") activityId : Int) : Call<VIPDayPrizeBudget>

    @GET("VipActivityController/getVipActivityWinNumber")
    fun loadPrizeNumbers(@Query("activityId") activityId : Int) : Call<PrizeNumberListBean>

    @GET("VipActivityController/getVipLotteryNumberByUserId")
    fun loadMyPrizeNumber(@Query("activityId") activityId : Int,
                          @Query("userId") userId: String,
                          @Query("isVip") isVip : Int = 1,
                          @Query("pastActivityId") pastActivityId : Int = 0) : Call<MyTicketNumberBean>

    @GET("PastLotteryController/bindWinAddress")
    fun bindAddress  (@Query("userId") userId: String,@Query("addressId") addressId : String,@Query("type") type : Int,@Query("relationId") relationId : String) : Call<BindAddressBean>

    @GET("WeekTask/getHistoryTaskList")
    fun getWeekTaskHistory(@Query("userId") userId: String) : Call<WeekHistoryTaskBean>

    @GET("WeekTask/getHelpPeopleList")
    fun loadHistoryHelpList(@Query("taskUserRelationId")taskUserRelationId : String) : Call<HelpListBean>

    @GET("WeekTask/getFinishedTaskDetail")
    fun loadFinishedTaskDetail(@Query("taskUserRelationId") taskUserRelationId : String?) : Call<WeekPrizeHistoryBean>

    @GET("ExperienceController/addExperience")
    fun publishExperience(@Query("prizeId") prizeId : String,@Query("title") title : String,@Query("content") content : String,
    @Query("userId") userId : String,@Query("type") type : String,@Query("relationId") relationId : String,
    @Query("winTime") winTime : String,@Query("newId") newId : String = "0") : Call<BaseHttpBean<String>>

    @GET("ExperienceController/addExperienceWithoutImg")
    fun publishExperienceNoImage(@Query("prizeId") prizeId : String,@Query("title") title : String,@Query("content") content : String,
                                 @Query("userId") userId : String,@Query("type") type : String,@Query("relationId") relationId : String,
                                 @Query("winTime") winTime : String,@Query("imageUrl") newId : String) : Call<BaseHttpBean<String>>

    @GET("PrizeController/getPrizeInfoV2")
    fun loadPrizeInfo(@Query("prizeId") prizeId : Int) : Call<PrizeInfoDetailBean>

    @GET("ExperienceController/getAllExperience")
    fun loadAllExperience(@Query("prizeId") prizeId : Int) : Call<AllExperienceBean>

    @GET("VipActivityController/getHistoryLotteryList")
    fun loadVipDayHistory(@Query("userId")userId : String) : Call<VipDayHistoryBean>

    @GET("VipActivityController/getVipPrizeStatus")
    fun loadVipPrizeStatus(@Query("userId") userId: String) : Call<VIPPrizeBean>

}