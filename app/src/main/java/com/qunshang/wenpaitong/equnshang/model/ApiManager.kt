package com.qunshang.wenpaitong.equnshang.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.qunshang.wenpaitong.equnshang.Constants

/**
 * 因为登录活动中的retrofit和apiLogin都是一样的，所以写了这个类
 * （我也不知道为啥要叫manager，我甚至都不知道manager是啥，但隐约记得某个教程里好像是这么命名的）
 * create by 何姝霖
 */
class ApiManager {
    private constructor()
    /*测试环境（新）*/
    private val testUrl = Constants.baseURL + "/tp/public/index.php/"

    private val optestUrl = Constants.baseURL + "/OOPHandlers/api/"

    private val oldUrl = Constants.baseURL + "/handlers/"

    private val retrofitTest = Retrofit.Builder()
        .baseUrl(testUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val opRetrofitTest = Retrofit.Builder()
        .baseUrl(optestUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitOld = Retrofit.Builder()
        .baseUrl(oldUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val apiLoginTest = retrofitTest.create(ApiLogin::class.java)//目录”login“中所有项目用到的接口
    private val apiMainTest = retrofitTest.create(ApiMain::class.java)  //目录”main“中所有项目用到的接口
    private val apiVideoTest = retrofitTest.create(ApiVideo::class.java)//目录”video“中所有项目用到的接口
    private val apiPersonalTest = retrofitTest.create(ApiPersonal::class.java) //目录”myHome“中所有项目用到的接口
    private val apiMallTest = retrofitTest.create(ApiMall::class.java)
    private val apiAddress = opRetrofitTest.create(ApiAddress::class.java)
    private val apiLotteryTest = retrofitTest.create(ApiLottery::class.java)
    private val apiGroupMain = retrofitTest.create(ApiGroupMain::class.java)

    private val apiwenbantongZhangjun = retrofitTest.create(ApiWenBanTong_ZhangJun::class.java)

    private val apiwenbantongHeshulin = retrofitTest.create(ApiWenBanTong_HeShuLin::class.java)

    private val apiStore = retrofitTest.create(ApiStore::class.java)

    private val apiOld = retrofitOld.create(ApiLod::class.java)

    private val apiAMallV3 = retrofitTest.create(ApiAMallV3::class.java)

    fun getApiGroupMain (): ApiGroupMain {
        return apiGroupMain
    }

    fun getApiAMallV3() : ApiAMallV3{
        return apiAMallV3
    }

    fun getApiWenBanTong_ZhangJun() : ApiWenBanTong_ZhangJun{
        return apiwenbantongZhangjun
    }

    fun getApiWenBanTong_HeShuLin() : ApiWenBanTong_HeShuLin{
        return apiwenbantongHeshulin
    }

    fun getApiOld(): ApiLod {
        return apiOld
    }

    fun getApiStore() : ApiStore {
        return apiStore
    }

    fun getApiLoginTest(): ApiLogin {
        return apiLoginTest
    }

    fun getApiMainTest(): ApiMain {
        return apiMainTest
    }

    fun getApiVideoTest(): ApiVideo {
        return apiVideoTest
    }

    fun getApiPersonalTest(): ApiPersonal {
        return apiPersonalTest
    }

    fun getApiLotteryTest() : ApiLottery {
        return apiLotteryTest
    }

    /*fun getQT () :ApiPersonal{
        return apiPersonalTest1
    }*/

    private val opRetrofitTest1 = Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl(testUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    //private val apiPersonalTest1 = opRetrofitTest1.create(ApiPersonal::class.java)

    fun getApiMallTest(): ApiMall{
        return apiMallTest
    }

    fun getApiAddress() : ApiAddress {
        return apiAddress
    }

    /*测试环境（旧）*/
    private val testUrlOld = Constants.baseURL + "/"
    private val retrofitTestOld = Retrofit.Builder()
        .baseUrl(testUrlOld)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiLoginTestOld = retrofitTestOld.create(ApiLogin::class.java)
    private val apiwenbantongHeshulinOld = retrofitTestOld.create(ApiWenBanTong_HeShuLin::class.java)

    fun getApiLoginTestOld(): ApiLogin {
        return apiLoginTestOld
    }

    fun getApiWenbantongHeshulinOld(): ApiWenBanTong_HeShuLin {
        return apiwenbantongHeshulinOld
    }

    companion object{
        lateinit var manager: ApiManager
        fun getInstance() : ApiManager {
            if (!this::manager.isInitialized){
                manager = ApiManager()
            }
            return manager
        }
    }



}