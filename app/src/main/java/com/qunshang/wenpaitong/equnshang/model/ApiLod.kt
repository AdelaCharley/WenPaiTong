package com.qunshang.wenpaitong.equnshang.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiLod {

    @FormUrlEncoded
    @POST("identity.php")
    fun identifyedBean(
        @Field("uid") uid : String
    ) : Call<ResponseBody>

}