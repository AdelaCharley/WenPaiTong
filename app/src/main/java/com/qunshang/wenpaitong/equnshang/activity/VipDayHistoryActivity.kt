package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_vip_day_history.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.VipDayHistoryAdapter
import com.qunshang.wenpaitong.equnshang.data.AddressBean
import com.qunshang.wenpaitong.equnshang.data.BindAddressBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VipDayHistoryBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class VipDayHistoryActivity : BaseActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vip_day_history)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("历史记录")
        initView()
    }

    fun initView(){
        ApiManager.getInstance().getApiLotteryTest().loadVipDayHistory(UserInfoViewModel.getUserId()).enqueue(object : Callback<VipDayHistoryBean>{
            override fun onResponse(call: Call<VipDayHistoryBean>, response: Response<VipDayHistoryBean>) {
                if (response.body() == null){
                    return
                }
                list.adapter = VipDayHistoryAdapter(this@VipDayHistoryActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<VipDayHistoryBean>, t: Throwable) {

            }


        })
    }

    companion object {
        var relationId = -69999

        val TYPE_RECEIVE_TICKET = 569
    }

    var isfirst = true

    override fun onResume() {
        super.onResume()
        if (isfirst){
            isfirst = false
        } else {
            initView()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_RECEIVE_TICKET){
            if (resultCode == AddressAdapter.RESULT_CODE){
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                ApiManager.getInstance().getApiLotteryTest().bindAddress(UserInfoViewModel.getUserId(),address.id!!,2,relationId = relationId.toString()).enqueue(object : Callback<BindAddressBean>{
                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<BindAddressBean>,
                        response: Response<BindAddressBean>
                    ) {
                        if (response.body() == null){
                            return
                        }
                        if (response.body()!!.code == 200){
                            DialogUtil.toast(this@VipDayHistoryActivity,"领取成功")
                            initView()
                        } else {
                            DialogUtil.toast(this@VipDayHistoryActivity,"领取失败," + response.body()!!.msg)
                        }
                    }

                    override fun onFailure(call: Call<BindAddressBean>, t: Throwable) {

                    }

                })
            }
        }
    }

}