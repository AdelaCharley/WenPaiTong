package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_week_history.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.WeekHistoryTaskAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WeekHistoryTaskBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class WeekHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isEnd = false
        setContentView(R.layout.activity_week_history)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("历史任务")
        ApiManager.getInstance().getApiLotteryTest().getWeekTaskHistory(UserInfoViewModel.getUserId()).enqueue(object : Callback<WeekHistoryTaskBean>{
            override fun onResponse(
                call: Call<WeekHistoryTaskBean>,
                response: Response<WeekHistoryTaskBean>
            ) {
                if (response.body() == null){
                    return
                }
                list.adapter = WeekHistoryTaskAdapter(this@WeekHistoryActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<WeekHistoryTaskBean>, t: Throwable) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        isEnd = true
    }

    companion object {
        var isEnd = false
    }

}