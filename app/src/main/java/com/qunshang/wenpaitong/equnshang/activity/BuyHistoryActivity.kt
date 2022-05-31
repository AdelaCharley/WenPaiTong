package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_buy_history.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BuyHistoryAdapter
import com.qunshang.wenpaitong.equnshang.data.BuyHistoryBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class BuyHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_history)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("购买记录")
        ApiManager.getInstance().getApiAMallV3().loadBuyHistory(
            UserInfoViewModel.getUserId()
        ,
        ).enqueue(object : Callback<BuyHistoryBean>{
            override fun onResponse(
                call: Call<BuyHistoryBean>,
                response: Response<BuyHistoryBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    return
                }
                if (response.body()!!.data == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    list.visibility = View.GONE
                    layout_empty.visibility = View.VISIBLE
                    text_status.setText("暂无记录")
                }
                list.adapter = BuyHistoryAdapter(this@BuyHistoryActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<BuyHistoryBean>, t: Throwable) {

            }

        })
    }
}