package com.qunshang.wenpaitong.equnshang.activity

/*
import android.content.Intent

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_apply_after_sale.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.order_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.OrderDetailBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ApplyAfterSaleActivity : BaseActivity() {

    var orderId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_after_sale)
        toolbar_title.setText("申请售后")
        toolbar_back.setOnClickListener { finish() }
        this.orderId = intent.getIntExtra("orderId",0)
        StringUtils.log("申请售后页面的orderId是" + orderId)
        ApiManager.getInstance().getApiMallTest().loadOrderDetail(orderId.toString()).enqueue(object :
            Callback<OrderDetailBean> {
            override fun onResponse(call: Call<OrderDetailBean>, response: Response<OrderDetailBean>) {
                if (response.body() != null){
                    if (response.body()!!.code == 200){
                        initView(response.body()!!)
                    }

                }
            }

            override fun onFailure(call: Call<OrderDetailBean>, t: Throwable) {

            }

        })
    }

    fun initView(orderBean: OrderDetailBean){
        root.visibility = View.VISIBLE
        Glide.with(this).load(orderBean.data.product.skuList.posterUrl).into(img_goods)
        tv_goods_name.setText(orderBean.data.product.productName)
        tv_goods_size.setText(orderBean.data.product.skuList.value)
        tv_goods_price.setText(orderBean.data.product.skuList.price.toString())
        tv_goods_quantity.setText("x" + orderBean.data.product.skuList.number)
        layout_return_goods.setOnClickListener {
            val intent = Intent(this, DoApplyAfterSaleActivity::class.java)
            intent.putExtra("orderId",orderId)
            intent.putExtra("type",10)
            startActivity(intent)
        }
        layout_return_goods_money.setOnClickListener {
            val intent = Intent(this, DoApplyAfterSaleActivity::class.java)
            intent.putExtra("orderId",orderId)
            intent.putExtra("type",20)
            startActivity(intent)
        }
        layout_exchange.setOnClickListener {
            val intent = Intent(this, DoApplyAfterSaleActivity::class.java)
            intent.putExtra("orderId",orderId)
            intent.putExtra("type",30)
            startActivity(intent)
        }
    }

}*/
