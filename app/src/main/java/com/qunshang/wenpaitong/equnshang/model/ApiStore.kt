package com.qunshang.wenpaitong.equnshang.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.qunshang.wenpaitong.equnshang.data.*

interface ApiStore {

    @GET("O2OController/getVendorInfo")
    fun loadStoreInfo(@Query("userId") userId : String) : Call<MyPhysicalStoreBean>

    @GET("O2OController/addVendorInfo")
    fun uploadStoreInfo(@Query("vendorId") vendorId : Int,@Query("userId") userId : String,@Query("vendorHeadImg") vendorHeadImg : String,
    @Query("vendorName") vendorName : String,@Query("vendorPhone") vendorPhone : String,@Query("vendorLocation") vendorLocation : String,
    @Query("vendorLocationName") vendorLocationName : String,
    @Query("longitude") longitude : String,@Query("latitude") latitude : String,@Query("vendorOpeningTime") vendorOpeningTime : String,
    @Query("vendorCloseTime") vendorCloseTime : String,@Query("vendorDesc") vendorDesc : String,@Query("poiUid") poiUid : String,
    @Query("province") province : String,@Query("city") city : String,@Query("district") district : String) : Call<BaseHttpBean<String>>

    @GET("O2OController/addVendorPicture")
    fun addVendorPicture(@Query("vendorId") vendorId : Int,@Query("title") title : String,@Query("imgUrl") imgUrl : String) : Call<BaseHttpBean<String>>

    @GET("O2OController/getVendorPictureList")
    fun loadVendorPictures(@Query("vendorId") vendorId : Int,@Query("pageIndex") pageIndex : Int = 1,
                           @Query("pageSize") pageSize : Int = 50
                           ,@Query("timeOrder") timeOrder : String ? = null
    ) : Call<VendorPictureBean>

    @GET("O2OController/topPicture")
    fun topPictures(@Query("vendorId") vendorId : Int,@Query("pictureId") pictureId : Int) : Call<BaseHttpBean<String>>

    @GET("O2OController/deletePicture")
    fun deletePictures(@Query("vendorId") vendorId : Int,@Query("pictureId") pictureId : Int) : Call<BaseHttpBean<String>>

    @GET("O2OController/getVendorVideoList")
    fun loadVendorVideos(@Query("vendorId") vendorId : Int,@Query("pageIndex") pageIndex : Int = 1,
                           @Query("pageSize") pageSize : Int = 5 ) : Call<VendorVideoBean>

    @GET("O2OController/deleteVideo")
    fun deleteVideos(@Query("vendorId") vendorId : Int,@Query("videoId") videoId : Int) : Call<BaseHttpBean<String>>

    @GET("O2OController/addVendorVideo")
    fun addVendorVideo(@Query("vendorId")vendorId : Int,@Query("title") title: String,@Query("fileId") fileId : String,
    @Query("videoPosterUrl") videoPosterUrl : String,@Query("videoUrl") videoUrl : String) : Call<BaseHttpBean<String>>

    @GET("O2OController/getVendorBanner")
    fun getVendorBanner() : Call<BaseHttpListBean<String>>

    @GET("O2OController/getVendorList")
    fun loadStoreList(@Query("pageIndex") pageIndex : Int = 1,@Query("pageSize") pageSize : Int = 50,
                      @Query("longitude") longitude : String,@Query("latitude") latitude : String) : Call<PhysicalStoreBean>

    @GET("O2OController/getVendorDetailInfo")
    fun loadVendorDetailData(@Query("vendorId") vendorId : Int) : Call<VendorDetailBean>

    @GET("O2OController/getVendorDetail")
    fun loadCVendorDetailData(@Query("vendorId") vendorId : Int) : Call<CVendorDetailBean>

}