package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_group_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.GroupMainMainAdapter
import com.qunshang.wenpaitong.equnshang.data.GroupMainListBean

class GroupMainActivity : BaseActivity() {

    lateinit var adapter : GroupMainMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main)
        toolbar_back.setOnClickListener { finish() }
        groupmainearn.setOnClickListener { startActivity(Intent(this,GroupActivityMainEarnActivity::class.java)) }
        initView()
    }

    fun initView(){
        ApiManager.getInstance().getApiGroupMain().loadMainList().enqueue(object : Callback<GroupMainListBean> {
            override fun onResponse(call: Call<GroupMainListBean>, response: Response<GroupMainListBean>) {
                if (response.body() == null){
                    return
                }
                adapter = GroupMainMainAdapter(
                    this@GroupMainActivity,
                    response.body()!!.getData()
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<GroupMainListBean>, t: Throwable) {

            }

        })
    }

}