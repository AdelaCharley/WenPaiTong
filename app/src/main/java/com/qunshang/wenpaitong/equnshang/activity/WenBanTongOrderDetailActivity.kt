package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_wen_ban_tong_order_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.WenBanTongOrderDetailBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongOrderDetailActivity : BaseActivity() {

    var orderSn : String?= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setSystemBarColor(R.color.wenbantongorderdetailcolor)
        changeToGreyButTranslucent()
        setContentView(R.layout.activity_wen_ban_tong_order_detail)
        orderSn = intent.getStringExtra("orderSn")
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("交易详情")
        top.background = null
        System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadOrderDetail(orderSn).enqueue(object : Callback<WenBanTongOrderDetailBean>{
            override fun onResponse(
                call: Call<WenBanTongOrderDetailBean>,
                response: Response<WenBanTongOrderDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    val data = response.body()!!.data
                    root.visibility = View.VISIBLE
                    zitiinfo.text = data.user.realName + " " + data.user.phone
                    Glide.with(this@WenBanTongOrderDetailActivity).load(data.company.companyAvatar).into(label_img)
                    shopname.setText(data.company.companyName)
                    Glide.with(this@WenBanTongOrderDetailActivity).load(data.product.productPoster).into(product_image)
                    product_name.setText(data.product.productName)
                    product_single_price.setText(data.product.orderProductPrice.toString())
                    wenbantongcount.setText(data.product.productTag)
                    count.setText("x" + data.order.productQuantity)
                    tv_price.setText(data.order.payAmount)
                    jioayidanhao.setText("交易单号：" + data.order.orderSn)
                    xiadanshijian.setText("下单时间：" + data.order.createTime)
                    tihuoshijian.setText("提货时间：" + data.order.deliveryTime)

                    status.setText("未支付")
                    when (data.order.orderStatus) {
                        20 -> {
                            bottom.visibility = View.VISIBLE
                            applyexchangemoney.setOnClickListener {
                                val intent = Intent(this@WenBanTongOrderDetailActivity,WenBanTongApplyExchangeActivity::class.java)
                                intent.putExtra("data",data)
                                startActivity(intent)
                            }
                            tihuoshijian.visibility = View.GONE
                            status.setText("已支付")
                            layout_tihuoxinxi.visibility = View.GONE
                        }
                        30 -> {
                            tihuoshijian.visibility = View.VISIBLE
                            bottom.visibility = View.VISIBLE
                            applyexchangemoney.setText("查看物流")
                            applyexchangemoney.setOnClickListener {
                                if (StringUtils.isEmpty(data.order.deliveryCode)){
                                    DialogUtil.toast(this@WenBanTongOrderDetailActivity,"平台暂未发货")
                                    return@setOnClickListener
                                }
                                val intent = Intent(this@WenBanTongOrderDetailActivity,ExpressDetailActivityV2::class.java)
                                intent.putExtra("type","wenbantong")
                                intent.putExtra("orderSn",data.order.orderSn)
                                startActivity(intent)
                            }
                            layout_tihuoxinxi.visibility = View.VISIBLE
                            zitiaddress.text = data.pickUpAddress.site
                            contacter.text = data.pickUpAddress.name + " " + data.pickUpAddress.phone
                            status.setText("已提货")
                        }

                        35 -> {
                            tihuoshijian.visibility = View.GONE
                            bottom.visibility = View.GONE
                            status.setText("退款中")
                            layout_tihuoxinxi.visibility = View.GONE
                        }

                        40 -> {
                            tihuoshijian.visibility = View.GONE
                            layout_tihuoxinxi.visibility = View.GONE
                            bottom.visibility = View.GONE
                            status.setText("已退款")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WenBanTongOrderDetailBean>, t: Throwable) {

            }

        })
    }

}