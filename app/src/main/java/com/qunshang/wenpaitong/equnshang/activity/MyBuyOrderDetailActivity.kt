package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_buy_order_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.MyBuyOrderProductsAdapter
import com.qunshang.wenpaitong.equnshang.data.MyBuyOrderBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import java.lang.Exception
import java.util.*

class MyBuyOrderDetailActivity : BaseActivity() {
    var orderId = -999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_buy_order_detail)
        orderId = intent.getIntExtra("id",-999)
        if (orderId == -999){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("订单详情")
        ApiManager.getInstance().getApiMallTest().loadMyBuyOrderDetailBean(orderId.toString()).enqueue(object :
            Callback<MyBuyOrderBean> {
            override fun onResponse(call: Call<MyBuyOrderBean>, response: Response<MyBuyOrderBean>) {
                if (response.body() != null){
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<MyBuyOrderBean>, t: Throwable) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::timer.isInitialized){
            timer.cancel()
        }
    }

    lateinit var timer : Timer

    fun initView(bean : MyBuyOrderBean){
        when (bean.data.orderStatus){
            10 -> {
                order_status.setText("待支付")
                //status_content.setText("您的订单待支付")
                timer = Timer()
                val task = object : TimerTask(){
                    override fun run() {
                        if (StringUtils.isEmpty(TimeUtil.getTimeRemainByDayString(bean.data.expiredTime))){
                            status_content.setText("订单已超时")
                            return
                        }
                        runOnUiThread {
                            status_content.setText("您的订单已提交，请在" + TimeUtil.getTimeRemainByDayString(bean.data.expiredTime) + "内完成支付，超时订单会自动关闭")
                        }
                    }

                }
                try {
                    timer.schedule(task,1000,1000)
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
            20 -> {

            }
            30 -> {
                order_status.setText("待发货")
                status_content.setText("您的订单已经支付完成，等待商家发货")
            }
            40 -> {
                order_status.setText("待收货")
                status_content.setText("您的订单已发货，等待送达")
            }
            50 -> {
                order_status.setText("已完成")
                status_content.setText("交易成功")
            }
            60 -> {
                order_status.setText("已关闭")
                status_content.setText("订单已经关闭")
            }
            73 -> {
                order_status.setText("已退款")
                status_content.setText("退款成功")
            }
        }
        videoname.setText(bean.data.address.addressName)
        phone.setText(bean.data.address.addressPhone)
        address.setText(bean.data.address.addressSite)
        storename.setText(bean.data.manufacture.manufactureName)
        work.setText("+" + bean.data.credit)
        //Glide.with(this).load(bean.data.product.skuList.posterUrl).into(image_store)
        //product_name.setText(bean.data.product.productName)
        //product_spec.setText(bean.data.product.skuList.value)
        //product_count.setText("x" + bean.data.product.skuList.number)
        //product_price.setText(bean.data.product.skuList.price.toString())
        products.adapter =
            MyBuyOrderProductsAdapter(
                this,
                bean.data.product
            )
        orderid.setText(bean.data.orderSn)
        time.setText(bean.data.createTime)
        productprice.setText("￥" + bean.data.orderPayAmount.toString())
        //orderfright.setText(bean.data.orderFreightAmount.toString())
        remark.setText(bean.data.note)
        realprice.setText("￥" + bean.data.orderPayAmount.toString())
    }
}