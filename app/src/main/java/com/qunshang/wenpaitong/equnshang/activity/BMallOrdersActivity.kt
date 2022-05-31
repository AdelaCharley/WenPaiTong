package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_buy_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.BMallOrderAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class BMallOrdersActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_buy_list)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("我的采购")
        toolbar_right.setOnClickListener {
            val intent = Intent(this,AddressActivityV2::class.java)
            startActivity(intent)
        }
        ApiManager.getInstance().getApiMallTest().loadMyBuyBean(UserInfoViewModel.getUserId()).enqueue(object : Callback<MyBuyBean>{
            override fun onResponse(call: Call<MyBuyBean>, response: Response<MyBuyBean>) {
                if (response.body() == null){
                    return
                }
                list.adapter = BMallOrderAdapter(this@BMallOrdersActivity, response.body()!!.data.get(0))
            }

            override fun onFailure(call: Call<MyBuyBean>, t: Throwable) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_CHOOSE_ADDRESS){
            if (resultCode == AddressAdapter.RESULT_CODE){
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                if (orderId != -999){
                    ApiManager.getInstance().getApiMallTest().modifyBMallAddress(orderId,address.id.toInt()).enqueue(object : Callback<BaseHttpBean<Int>>{
                        override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {
                            if (response.body() == null){
                                return
                            }
                            if (response.body()!!.code == 200){
                                DialogUtil.toast(this@BMallOrdersActivity,"修改成功")
                            } else {
                                DialogUtil.toast(this@BMallOrdersActivity,response.body()!!.msg)
                            }
                        }

                        override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                        }

                    })
                }
            }
        }
    }

    companion object {
        var orderId = -999

        val TYPE_CHOOSE_ADDRESS = 200
    }

}