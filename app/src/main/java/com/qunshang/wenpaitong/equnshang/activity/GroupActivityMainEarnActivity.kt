package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_group_main_earn.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.TotalIncomeBean

class GroupActivityMainEarnActivity : BaseActivity() {

    lateinit var adapter : com.qunshang.wenpaitong.equnshang.adapter.GroupMainTotalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main_earn)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("文版通收入")
        initView()
    }

    fun initView(){
        ApiManager.getInstance().getApiGroupMain().loadTotalIncome(
            userId = UserInfoViewModel.getUserId()
        ).enqueue(object : Callback<TotalIncomeBean>{
            override fun onResponse(
                call: Call<TotalIncomeBean>,
                response: Response<TotalIncomeBean>
            ) {
                if (response.body() == null){
                    return
                }
                income.setText("￥ " + response.body()!!.data.incomeTotal.toString())
                adapter = com.qunshang.wenpaitong.equnshang.adapter.GroupMainTotalAdapter(
                    this@GroupActivityMainEarnActivity,
                    response.body()!!.data.groupOwnerIncomeList
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<TotalIncomeBean>, t: Throwable) {

            }

        })
    }

}