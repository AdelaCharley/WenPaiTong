package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_search_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : BaseActivity() {

    var keyword: String? = null

    var categoryId: Int? = null

    var brandId: Int? = null

    var pageIndex = 1

    val pageSize = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        keyword = intent.getStringExtra("keyword")
        categoryId = intent.getIntExtra("categoryId",-9999)
        if (categoryId == -9999){
            categoryId = null
        }
        brandId = intent.getIntExtra("brandId",-9999)
        if (brandId == -9999){
            brandId = null
        }
        initView()
    }

    fun initView() {
        cancel.setOnClickListener {
            finish()
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        searchProducts()
    }

    fun searchProducts() {


        ApiManager.getInstance().getApiMallTest()
            .searchProduct(categoryId, pageIndex, pageSize, brandId, keyword)
            .enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.AMallProductBeanV2> {
                override fun onResponse(
                    call: Call<com.qunshang.wenpaitong.equnshang.data.AMallProductBeanV2>,
                    response: Response<com.qunshang.wenpaitong.equnshang.data.AMallProductBeanV2>
                ) {
                    if (response.body() == null) {
                        return
                    }

                    val adapter = com.qunshang.wenpaitong.equnshang.adapter.AMallAdapterV2(
                        this@SearchResultActivity,
                        response.body()!!.data
                    )
                    list.adapter = adapter

                    //isProductLoaded = true

                }

                override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.AMallProductBeanV2>, t: Throwable) {

                }

            })

        /*ApiManager.getInstance().getApiMallTest().loadSearchAMallResult(UserInfoViewModel.getUserId(),keyword).enqueue(object : Callback<AMallProductBean>{
            override fun onResponse(call: Call<AMallProductBean>, response: Response<AMallProductBean>) {
                if (response.body() != null){
                    list.adapter = AMallAdapter(this@SearchResultActivity,response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AMallProductBean>, t: Throwable) {

            }

        })*/

    }
}