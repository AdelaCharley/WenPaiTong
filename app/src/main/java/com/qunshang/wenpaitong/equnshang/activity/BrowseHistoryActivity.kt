package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_browse_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.BrowseHistoryAdapter
import com.qunshang.wenpaitong.equnshang.data.BrowserHistoryBean
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class BrowseHistoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_history)
        toolbar_back.setOnClickListener { finish() }
        initView()
        loadData()
    }

    fun initView(){
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
    }

    fun loadData(){
        ApiManager.getInstance().getApiPersonalTest().loadProductBrowse(UserInfoViewModel.getUserId()).enqueue(object : Callback<BrowserHistoryBean>{
            override fun onResponse(call: Call<BrowserHistoryBean>, response: Response<BrowserHistoryBean>) {
                val data = UserHelper.analyseBrowseList(response.body())
                list.adapter = BrowseHistoryAdapter(
                    this@BrowseHistoryActivity,
                    data
                )
            }

            override fun onFailure(call: Call<BrowserHistoryBean>, t: Throwable) {

            }

        })
    }

}