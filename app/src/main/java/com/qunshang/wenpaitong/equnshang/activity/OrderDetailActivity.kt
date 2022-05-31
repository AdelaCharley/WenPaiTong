package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.OrderDetailBean
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import java.lang.Exception
import java.util.*

class OrderDetailActivity : BaseActivity (){

    var orderId = -999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        orderId = intent.getIntExtra("id",-999)
        if (orderId == -999){
            DialogUtil.toast(this,"出错了")
            return
        }
        //ToastUtil.toast(this,"" + orderId)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("订单详情")
        ApiManager.getInstance().getApiMallTest().loadOrderDetail(orderId.toString()).enqueue(object : Callback<OrderDetailBean>{
            override fun onResponse(call: Call<OrderDetailBean>, response: Response<OrderDetailBean>) {
                if (response.body() != null){
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<OrderDetailBean>, t: Throwable) {

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

    fun initView(bean : OrderDetailBean){
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
                    timer.schedule(task,0,1000)
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
            20 -> {
                order_status.setText("待成团")
                status_content.setText("您的订单已经支付完成，正在等待成团")
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
                order_status.setText("已退货")
                status_content.setText("退货成功")
            }
        }

        if (!StringUtils.isEmpty(bean.data.paymentTime)){
            layout_paytime.visibility = View.VISIBLE
            paytime.setText(bean.data.paymentTime)
        } else {
            layout_paytime.visibility = View.GONE
        }

        videoname.setText(bean.data.address.addressName)
        phone.setText(bean.data.address.addressPhone)
        address.setText(bean.data.address.addressSite)
        storename.setText(bean.data.manufacture.manufactureName)
        Glide.with(this).load(bean.data.product.skuList.posterUrl).into(image_store)
        product_name.setText(bean.data.product.productName)
        product_spec.setText(bean.data.product.skuList.value)
        product_count.setText("x" + bean.data.product.skuList.number)
        product_price.setText("￥" + bean.data.product.skuList.price.toString())
        orderid.setText(bean.data.orderSn)
        time.setText(bean.data.createTime)

        productprice.setText("￥" + bean.data.product.skuList.number.toInt() * bean.data.product.skuList.price)

        discount.setText("-" + bean.data.orderDiscountAmount)
        coupon.setText("-" + bean.data.orderCouponAmount)
        credit.setText("+" + bean.data.credit)
        if (bean.data.orderFreightAmount != 0.0){
            orderfright.setText(bean.data.orderFreightAmount.toString())
        } else {
            orderfright.setText("免运费")
        }
        remark.setText(bean.data.note)
        realprice.setText("￥" + bean.data.orderPayAmount.toString())
        //realprice.setText("￥" + bean.data.product.skuList.number.toInt() * bean.data.product.skuList.price)
    }

}