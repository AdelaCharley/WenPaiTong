package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_supplement.*
import kotlinx.android.synthetic.main.activity_supplement.prize_number
import kotlinx.android.synthetic.main.activity_supplement.prizeproducturl
import kotlinx.android.synthetic.main.activity_supplement.productname
import kotlinx.android.synthetic.main.activity_supplement.time_day
import kotlinx.android.synthetic.main.activity_supplement.time_time
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class SupplementActivity : BaseActivity() {

    lateinit var data : LastLotteryResultBean.DataBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplement)
        data = intent.getSerializableExtra("data") as LastLotteryResultBean.DataBean
        if (!this::data.isInitialized){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("中奖信息")
        ApiManager.getInstance().getApiVideoTest().loadPrizeInfo(UserInfoViewModel.getUserId(),data.activityId.toString()).enqueue(object :
            Callback<PrizeInfoBean> {
            override fun onResponse(call: Call<PrizeInfoBean>, response: Response<PrizeInfoBean>) {
                if (response.body() != null){
                    initView(response.body()!!)
                    this@SupplementActivity.relationId = response.body()!!.data.numberInfo.activityNumberRelationId
                }
            }

            override fun onFailure(call: Call<PrizeInfoBean>, t: Throwable) {

            }

        })
    }

    var relationId : Long = -9999

    private val TYPE_RECEIVE_TICKET = 569

    fun initView(bean : PrizeInfoBean){
        Log.i(Constants.logtag,"status是" + bean.data.prizeInfo.prizeStatus)
        prize_number.setText(bean.data.numberInfo.number)
        val split = bean.data.numberInfo.winTime.split(" ")
        if (split.size == 2){
            time_day.setText(split[0])
            time_time.setText(split[1])
        }
        Glide.with(this).load(bean.data.prizeInfo.prizeImageUrl).into(prizeproducturl)
        productname.setText(bean.data.prizeInfo.prizeName)
        exchange.setOnClickListener {
            val intent = Intent(this,ChooseAddressActivity::class.java)
            startActivityForResult(intent,TYPE_RECEIVE_TICKET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_RECEIVE_TICKET){
            if (resultCode == AddressAdapter.RESULT_CODE){
                //val id = data?.getStringExtra("addressId")
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                ApiManager.getInstance().getApiLotteryTest().bindAddress(UserInfoViewModel.getUserId(),address.id!!,0,relationId = relationId.toString()).enqueue(object : Callback<BindAddressBean>{
                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<BindAddressBean>,
                        response: Response<BindAddressBean>
                    ) {
                        if (response.body() == null){
                            return
                        }
                        if (response.body()!!.code == 200){
                            DialogUtil.toast(this@SupplementActivity,"领取成功")
                            val intent = Intent(this@SupplementActivity, WinInfoActivity::class.java)
                            intent.putExtra("activityId", this@SupplementActivity.data.activityId)
                            startActivity(intent)
                            finish()
                        } else {
                            DialogUtil.toast(this@SupplementActivity,"领取失败," + response.body()!!.msg)
                        }
                    }

                    override fun onFailure(call: Call<BindAddressBean>, t: Throwable) {

                    }

                })
            }
        }
    }

}