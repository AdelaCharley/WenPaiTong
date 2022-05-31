package com.qunshang.wenpaitong.equnshang.activity

import android.graphics.Color

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.activity_wen_ban_tong_apply_exchange.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.WenBanTongOrderDetailBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.view.WenBanTongApplyReasonDialog

class WenBanTongApplyExchangeActivity : BaseActivity (){

    lateinit var data : WenBanTongOrderDetailBean.DataBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarColor(R.color.wenbantongorderdetailcolor)
        changeToGreyButTranslucent()
        setContentView(R.layout.activity_wen_ban_tong_apply_exchange)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("申请退款")
        top.background = null
        data = intent.getSerializableExtra("data") as WenBanTongOrderDetailBean.DataBean
        initView(data)
    }

    fun initView(data : WenBanTongOrderDetailBean.DataBean){
        Glide.with(this).load(data.company.companyAvatar).into(label_img)
        shopname.setText(data.company.companyName)
        Glide.with(this).load(data.product.productPoster).into(product_image)
        product_name.setText(data.product.productName)
        product_single_price.setText(data.product.orderProductPrice.toString())
        wenbantongcount.setText(data.product.productTag)
        count.setText("x" + data.order.productQuantity)
        tv_price.setText(data.order.payAmount)
        root.visibility = View.VISIBLE
        pleasechoosereason.setOnClickListener {
            XPopup.Builder(this)
                .moveUpToKeyboard(false)
                .enableDrag(true)
                .isDestroyOnDismiss(true)
                .asCustom(WenBanTongApplyReasonDialog(this))
                .show()
        }
        StringUtils.log("这个orderSn是" + data.order.orderSn)
        applyexchange.setOnClickListener {
            WaitDialog.show(this,"载入中")

            ApiManager.getInstance().getApiWenBanTong_ZhangJun().applyExchange(data.order.orderSn,pleasechoosereason.text.toString()).enqueue(object : Callback<BaseHttpBean<String>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<String>>,
                    response: Response<BaseHttpBean<String>>
                ) {
                    WaitDialog.dismiss()
                    if (response.body() == null){
                        return
                    }
                    val gson = Gson()
                    StringUtils.log(gson.toJson(response.body()!!))
                    if (response.body()!!.code == 200){
                        TipDialog.show(this@WenBanTongApplyExchangeActivity,"申请成功",TipDialog.TYPE.SUCCESS).setOnDismissListener {
                            EventBus.getDefault().post("wenbantongrefresh")
                            finish()
                        }
                    } else {
                        DialogUtil.toast(this@WenBanTongApplyExchangeActivity,response.body()!!.msg)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                    WaitDialog.dismiss()
                }

            })
        }
    }

    fun setReason(str : String){
        applyexchange.visibility = View.VISIBLE
        pleasechoosereason.setTextColor(Color.parseColor("#ff66645f"))
        pleasechoosereason.setText(str)
    }

}