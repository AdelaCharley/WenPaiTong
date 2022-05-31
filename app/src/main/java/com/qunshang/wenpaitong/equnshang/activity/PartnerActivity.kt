package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_partner.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.NewPartnerBean

class PartnerActivity : BaseActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("店铺")
        //content.setImage(ImageSource.resource(R.mipmap.bg_partner))
        ApiManager.getInstance().getApiAddress().loadNewPartnerList().enqueue(object : Callback<NewPartnerBean>{
            override fun onResponse(call: Call<NewPartnerBean>, response: Response<NewPartnerBean>) {
                if (response.body() == null){
                    return
                }
                list.adapter = com.qunshang.wenpaitong.equnshang.adapter.NewPartnerAdapter(
                    this@PartnerActivity,
                    response.body()!!.data
                )
            }

            override fun onFailure(call: Call<NewPartnerBean>, t: Throwable) {

            }

        })
    }
}