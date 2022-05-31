package com.qunshang.wenpaitong.equnshang.model

import com.qunshang.wenpaitong.equnshang.data.*
import retrofit2.Call
import retrofit2.http.*

interface ApiAddress {

    @GET("findUserAddress.php")
    fun loadAddress(
        @Query("userId") userId: String
    ) : Call<AddressBean>

    @FormUrlEncoded
    @POST("addUserAddress.php")
    fun saveAddress(
        @Field("userId")userId: String,
        @Field("name")name : String,
        @Field("phone")phone : String,
        @Field("site")site : String,
        @Field("isDefault")isDefault : String
    ) : Call<OperateAddressBean>

    @FormUrlEncoded
    @POST("updateUserAddress.php")
    fun modifyAddress(
        @Field("userId")userId: String,
        @Field("name")name : String,
        @Field("phone")phone : String,
        @Field("site")site : String,
        @Field("isDefault")isDefault : String,
        @Field("addressId") addressId : String
    ) : Call<OperateAddressBean>

    @FormUrlEncoded
    @POST("deleteUserAddress.php")
    fun deleteAddress(
        @Field("addressId")addressId: String
    ): Call<OperateAddressBean>

    @GET("findVideoDataByUserId.php")
    fun loadVideoDataContent(
        @Query("userId") userId: String
    ) : Call<VideoContentDataBean>

    @GET("findVideoListByUserId.php")
    fun loadVideoBean(
        @Query("userId") userId: String
    ) : Call<EditVideoBean>

    @GET("deleteVideoByVideoId.php")
    fun deleteVideo(
        @Query("videoId") videoId : String
    ) : Call<BaseHttpBean<Int>>

    @GET("findNewLuckyList.php")
    fun loadNewPartnerList() : Call<NewPartnerBean>
}