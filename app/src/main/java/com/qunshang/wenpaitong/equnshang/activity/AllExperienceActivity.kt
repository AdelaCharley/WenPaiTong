package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_all_experience.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AllExperienceAdapter
import com.qunshang.wenpaitong.equnshang.data.AllExperienceBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class AllExperienceActivity : BaseActivity() {

    var prizeId = -999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_experience)
        prizeId = intent.getIntExtra("prizeId",-999)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("奖品")
        if (prizeId < 0){
            DialogUtil.toast(this,"出错了")
            return
        }
        ApiManager.getInstance().getApiLotteryTest().loadAllExperience(prizeId).enqueue(object : Callback<AllExperienceBean>{
            override fun onResponse(call: Call<AllExperienceBean>, response: Response<AllExperienceBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    return
                }
                list.adapter = AllExperienceAdapter(this@AllExperienceActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<AllExperienceBean>, t: Throwable) {

            }

        })
    }
}