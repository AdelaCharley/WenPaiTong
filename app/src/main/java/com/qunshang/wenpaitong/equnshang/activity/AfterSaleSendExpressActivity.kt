package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import coil.load
import kotlinx.android.synthetic.main.activity_after_sale_send_express.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityAfterSaleSendExpressBinding
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.ExchangeBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class AfterSaleSendExpressActivity : BaseActivity() {

    lateinit var binding: ActivityAfterSaleSendExpressBinding

    var afterSaleSn : String ? = ""
    var expressCompanyCode : String ? = ""
    var expressSn : String ? = ""

    var address : ExchangeBean.DataBean.VendorAddress ? = null//地址信息
    var skuPic : String ? = null            //图片
    var productName : String ?= null        //商品名
    var productSinglePrice : Double = 0.0   //价格
    var productQuantity = 0                 //数量
    var skuInfo : String ? = null           //规格

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterSaleSendExpressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        initData()
        setLayout()

        if (StringUtils.isEmpty(afterSaleSn)){
            afterSaleSn = ""
            return
        }
        if (address == null){
            return
        }

        toSubmitDelivery()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(str : String){}

    private fun initData(){
        this.afterSaleSn = intent.getStringExtra("afterSaleSn")
        this.address = intent.getSerializableExtra("address") as ExchangeBean.DataBean.VendorAddress?
        this.skuPic = intent.getStringExtra("skuPic")
        this.productName = intent.getStringExtra("productName")
        this.productSinglePrice = intent.getDoubleExtra("productSinglePrice",0.0)
        this.productQuantity = intent.getIntExtra("productQuantity",0)
        this.skuInfo = intent.getStringExtra("skuInfo")
    }

    private fun setLayout() {
        binding.toolbar.toolbarTitle.text = "买家发货"
        binding.toolbar.toolbarBack.setOnClickListener { finish() }

        binding.layoutAddress.addresssite.text = address!!.vendorAddressLocation
        binding.layoutAddress.name.text = address!!.vendorAddressName
        binding.layoutAddress.phone.text = address!!.vendorAddressPhone
        binding.orderInfo.imgGoods.load(skuPic)
        binding.orderInfo.tvGoodsName.text = productName
        binding.orderInfo.tvGoodsSize.text = "规格：" + skuInfo
        binding.orderInfo.tvGoodsPrice.text = productSinglePrice.toString()
        binding.orderInfo.tvGoodsQuantity.text = "X " + productQuantity

        binding.chooseExpress.setOnClickListener {
            startActivityForResult(Intent(this, ExpressCompanyActivity::class.java),123)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 321) {
            binding.tvExpressCompany.text = data!!.getSerializableExtra("companyName").toString()
            expressCompanyCode= data!!.getSerializableExtra("companyCode").toString()
        }
    }

    private fun toSubmitDelivery() {
        binding.submit.setOnClickListener {
            expressSn = binding.tvExpressSn.text.toString()
            if (expressCompanyCode == "") {
                DialogUtil.toast(this,"请选择快递公司")
            }else  if (expressSn == "") {
                DialogUtil.toast(this,"请填写快递单号")
            } else{
                toSendProduct()
            }
        }
    }

    private fun toSendProduct(){
        ApiManager.getInstance()
            .getApiAMallV3()
            .sendProduct(afterSaleSn!!, expressCompanyCode!!, expressSn!!)
            .enqueue(object : Callback<BaseHttpBean<String>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<String>>,
                    response: Response<BaseHttpBean<String>>
                ) {
                    if (response.isSuccessful){
                        EventBus.getDefault().post("refresh")
                        if (response.body()!!.data == "success"){
                            finish()
                        }
                    } else {
                        Log.d("AfterSaleSendExpressAct", "onResponse: ")
                        return
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                }

            })
    }

}