package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityExpressCompanyBinding
import com.qunshang.wenpaitong.equnshang.adapter.ExpressCompanyAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.ExpressCompanyBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class ExpressCompanyActivity : BaseActivity() {
    lateinit var binding: ActivityExpressCompanyBinding
    var data: List<ExpressCompanyBean> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpressCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        binding.toolbar.toolbarTitle.text = "快递公司"
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
    }

    private fun loadData() {
        ApiManager.getInstance()
            .getApiAMallV3()
            .loadExpressCompany()
            .enqueue(object : Callback<BaseHttpListBean<ExpressCompanyBean>>{
                override fun onResponse(
                    call: Call<BaseHttpListBean<ExpressCompanyBean>>,
                    response: Response<BaseHttpListBean<ExpressCompanyBean>>
                ) {
                    if (response.isSuccessful) {
                        data = response.body()!!.data
                        setRecycleView()
                    } else {
                        Log.e("ExpressCompanyActivity", "onResponse: $response", )
                        return
                    }
                }
                override fun onFailure(call: Call<BaseHttpListBean<ExpressCompanyBean>>, t: Throwable) {}
            })
    }

    private fun setRecycleView(){
        val manager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = manager
        if (data == null){
            return
        }
        val adapter = ExpressCompanyAdapter(this,data!!)
        binding.recyclerView.adapter = adapter
    }

}