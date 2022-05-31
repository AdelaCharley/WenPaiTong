package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_browse_history_v2.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BrowseHistoryAdapterV2
import com.qunshang.wenpaitong.equnshang.data.AMallV3ProductHistoryBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class BrowseHistoryActivityV2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_history_v2)
        toolbar_title.setText("我的足迹")
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
        ApiManager.getInstance().getApiAMallV3().loadAMallV3BroseHistoryBean(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<AMallV3ProductHistoryBean> {
            override fun onResponse(call: Call<AMallV3ProductHistoryBean>, response: Response<AMallV3ProductHistoryBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    return
                }
                list.adapter = BrowseHistoryAdapterV2(
                    this@BrowseHistoryActivityV2,
                    response.body()!!.data
                )
            }

            override fun onFailure(call: Call<AMallV3ProductHistoryBean>, t: Throwable) {
//Log.i(Constants.logtag,t.message)
            }

        })
    }

}