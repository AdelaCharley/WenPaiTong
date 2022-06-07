package com.qunshang.wenpaitong.equnshang.activity

/*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.lljjcoder.style.citylist.Toast.ToastUtils
import kotlinx.android.synthetic.main.activity_after_sale_detail.*
import kotlinx.android.synthetic.main.item_return_goods.view.*
import kotlinx.android.synthetic.main.layout_after_sale_progress.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityAfterSaleDetailBinding
import com.qunshang.wenpaitong.equnshang.adapter.AfterSaleProgressAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class AfterSaleDetailActivity : BaseActivity() {

    var afterSaleSn : String ? = ""

    lateinit var binding: ActivityAfterSaleDetailBinding

    private var product: AfterSaleProduct ? = null
    var orderAfterSaleStatus : Int ?= 0
    var orderAfterSaleType : Int? = 0   //10-退款 20-退货退款 30-换货

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterSaleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.afterSaleSn = intent.getStringExtra("afterSaleSn")
        if (StringUtils.isEmpty(afterSaleSn)){
            afterSaleSn = ""
        }
        Log.i("heshulin", "asd-afterSaleSn: $afterSaleSn")
        loadData()
    }

    private fun loadData(){
        if (StringUtils.isEmpty(afterSaleSn)){
            return
        }
        ApiManager.getInstance()
            .getApiAMallV3()
            .loadAfterSaleDetail(afterSaleSn)
            .enqueue(object : Callback<BaseHttpBean<AfterSaleDetailBean>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<AfterSaleDetailBean>>,
                    response: Response<BaseHttpBean<AfterSaleDetailBean>>
                ) {
                    Log.d("shulinr", "asd_response: $response")

                    if (response.body() == null){
                        DialogUtil.toast(this@AfterSaleDetailActivity,"获取信息出错")
                        return
                    }

                    if (response.body()!!.code == 200) {
                        root.visibility = View.VISIBLE
                        val data = response.body()!!.data
                        product = data?.product
                        orderAfterSaleStatus = data?.orderAfterSaleStatus
                        orderAfterSaleType = data?.afterSaleInfo?.orderAfterSaleType
                        initLayout(data!!)
                    } else {
                        DialogUtil.toast(this@AfterSaleDetailActivity,response.body()!!.msg)
                        return
                    }
                }
                override fun onFailure(
                    call: Call<BaseHttpBean<AfterSaleDetailBean>>,
                    t: Throwable
                ) {}
            })
    }

    private fun initLayout(data: AfterSaleDetailBean) {
        binding.toolbar.toolbarBack.setOnClickListener{finish()}
        binding.toolbar.toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19F)
        setProgressLayout(data.statusText, data.errMsg)
        setProductInfoLayout()
        setReturnMoneyLayout(data.refundAmount, data.afterSaleInfo)
        when(orderAfterSaleType) {
            10 -> {
                binding.toolbar.toolbarTitle.text = "退款"
                binding.receiveInfo.root.visibility = View.GONE
                binding.returnGoods.root.visibility = View.GONE
            }
            20 -> {
                binding.toolbar.toolbarTitle.text = "退货退款"
                binding.receiveInfo.root.visibility = View.GONE
                setReturnGoodsLayout(data.manufactureAddressInfo, data.userSendInfo)
            }
            30 -> {
                binding.toolbar.toolbarTitle.text = "换货"
                setReceiveInfoLayout(data.userAddressInfo)
                setReturnGoodsLayout(data.manufactureAddressInfo, data.userSendInfo)
            }
        }
    }

    private fun setProgressLayout(statusText: String, errMsg: String) {
        val layout = binding.progress
        layout.tvTitle.text = statusText
        if (orderAfterSaleStatus != 0 && orderAfterSaleStatus != 250 && orderAfterSaleStatus != 350) {
            layout.layoutAudit.visibility = View.GONE
        } else {
            layout.layoutAudit.tv_content.text = errMsg
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        layout.recyclerView.layoutManager = manager
        layout.recyclerView.adapter = AfterSaleProgressAdapter(orderAfterSaleType!!, orderAfterSaleStatus!!)
    }

    private fun setReceiveInfoLayout(userAddressInfo: UserAddressInfo) {
        val layout = binding.receiveInfo
        layout.tvAddress.text = userAddressInfo.userAddressLocation
        layout.tvName.text = userAddressInfo.userAddressName
        layout.tvPhone.text = userAddressInfo.userAddressPhone
    }

    private fun setProductInfoLayout() {
        val layout = binding.productInfo
        layout.imgStoreIcon.visibility = View.GONE
        layout.tvStoreName.text = "商品信息"
        layout.tvStoreName.setPadding(20,0,0,0)
        layout.imgGoods.load(product!!.skuPic)
        layout.tvGoodsName.text = product!!.productName
        layout.tvGoodsSpec.text = "规格" + product!!.skuInfo
        layout.tvGoodsPrice.text = product!!.productSinglePrice.toString()
        layout.tvGoodsQuantity.text = "申请数量：" + product!!.productQuantity
    }

    private fun setReturnMoneyLayout(refundAmount: Double, afterSaleInfo: AfterSaleInfo) {
        val layout = binding.returnMoney
        if (orderAfterSaleType == 30) {
            layout.layoutReturnMoney.visibility = View.GONE
        } else {
            layout.tvPrice.text = refundAmount.toString()
        }
        layout.tvOrderNumberTitle.text = if (orderAfterSaleType == 30) "换货单号" else "退款单号"
        layout.tvOrderNumber.text = afterSaleInfo.afterSaleSn
        layout.tvApplyTime.text = afterSaleInfo.createTime
        layout.tvServiceType.text = when(orderAfterSaleType) {
            10 -> "仅退款"
            20 -> "退货退款"
            else -> "换货"
        }
        layout.tvReason.text = afterSaleInfo.refundReason
        layout.tvExplain.text = afterSaleInfo.refundExplain

        if (afterSaleInfo.refundPic == "") {
            layout.layoutImage.visibility = View.GONE
        } else {
            val imgUrlArr = afterSaleInfo.refundPic.split(",")
            val imgViewArr = arrayOf(layout.img1, layout.img2, layout.img3)
            for (i in imgUrlArr.indices) {
                imgViewArr[i].load(imgUrlArr[i])
            }
        }
    }

    private fun setReturnGoodsLayout(returnInfo: ExchangeBean.DataBean.VendorAddress?,
                                     userSendInfo: UserSendInfo) {
        val layout = binding.returnGoods
        if (orderAfterSaleStatus == 0 || orderAfterSaleStatus == 210 || orderAfterSaleStatus == 310) {
            binding.returnGoods.root.visibility = View.GONE
            return
        }

        layout.layoutReturn.tv_address.text = returnInfo!!.vendorAddressLocation
        layout.layoutReturn.tv_name.text = returnInfo.vendorAddressName
        layout.layoutReturn.tv_phone.text = returnInfo.vendorAddressPhone
        layout.btnCopy.setOnClickListener {
            //复制到剪贴板
            val str = "${returnInfo!!.vendorAddressLocation}," +
                    "${returnInfo.vendorAddressName}," +
                    "${returnInfo.vendorAddressPhone}"
            val clipData = ClipData.newPlainText(null, str)
            val cmd: ClipboardManager =
                this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmd.setPrimaryClip(clipData)
            ToastUtils.showShortToast(this, "已复制")
        }

        if (orderAfterSaleStatus == 220 || orderAfterSaleStatus == 320) {
            layout.layoutDelivery.visibility = View.GONE
            layout.btnDeliver.setOnClickListener { toSendExpress(returnInfo) }
        } else {
            layout.layoutDelivery.tv_send_method.text = userSendInfo.sendFunction
            layout.layoutDelivery.tv_express_company.text = userSendInfo.userExpressCompanyName
            layout.layoutDelivery.tv_express_sn.text = userSendInfo.userExpressSn
            layout.btnDeliver.visibility = View.GONE
        }
    }

    private fun toSendExpress(returnInfo: ExchangeBean.DataBean.VendorAddress) {
        val intent = Intent(this, AfterSaleSendExpressActivity::class.java)
        intent.putExtra("afterSaleSn", afterSaleSn)
        intent.putExtra("address", returnInfo)
        intent.putExtra("skuPic", product!!.skuPic)
        intent.putExtra("productName", product!!.productName)
        intent.putExtra("productSinglePrice", product!!.productSinglePrice)
        intent.putExtra("productQuantity", product!!.productQuantity)
        intent.putExtra("skuInfo", product!!.skuInfo)
        startActivity(intent)
    }
}*/
