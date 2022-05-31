package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ren_mai.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserFansBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class RenMaiActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ren_mai)
        changeToGreyButTranslucent()
        exit.setOnClickListener { finish() }
        ApiManager.getInstance().getApiPersonalTest().loadFansData(userId = UserInfoViewModel.getUserId()).enqueue(object :
            Callback<UserFansBean> {
            override fun onResponse(call: Call<UserFansBean>, response: Response<UserFansBean>) {
                if (response.body() == null){
                    return
                }
                val adapter = com.qunshang.wenpaitong.equnshang.adapter.ManageContentAdapter(
                    this@RenMaiActivity,
                    response.body()!!.data.levelA
                )
                list.adapter = adapter
                count.setText(response.body()!!.data.countListLevelA.toString())
            }

            override fun onFailure(call: Call<UserFansBean>, t: Throwable) {

            }

        })
    }
}