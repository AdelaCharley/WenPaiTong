package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.MessageDialog
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import com.kongzue.dialog.v3.TipDialog
import kotlinx.android.synthetic.main.activity_amall_go_buy_v2.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.ChooseDiscountTicketAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.MyNumUtils
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV4
import java.lang.Exception
import java.util.regex.Pattern

class AMallGoBuyActivityV2 : BaseActivity() {

    lateinit var bean : ProductBeanV2

    lateinit var sku : ProductBeanV2.DataBean.SkuList

    var price : Double = 0.0

    var addressId = "-2000"

    var count = 0

    var mode : String ? = null

    var userCouponsId = -66

    var isHaveDiscountTicket = false

    var ismemberforgroup = false

    var feightmoney : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_go_buy_v2)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("确认订单")
        if (intent == null){
            DialogUtil.toast(this,"出错了")
        }
        ismemberforgroup = intent.getBooleanExtra("ismemberforgroup",false)
        bean = intent.getSerializableExtra("data") as ProductBeanV2
        sku = intent.getSerializableExtra("sku") as ProductBeanV2.DataBean.SkuList
        count = intent.getIntExtra("count",0)
        mode = intent.getStringExtra("priceMode")
        initView()
    }

    fun loadExpressMoney(){
        ApiManager.getInstance().getApiAMallV3().loadExpressMoney(sku.id,addressId,count).enqueue(object : Callback<FreightMoneyBean>{
            override fun onResponse(
                call: Call<FreightMoneyBean>,
                response: Response<FreightMoneyBean>
            ) {
                if (response.body() == null){
                    return
                }
                val responseBody = response.body()!!
                if (responseBody.code != 200){
                    freightmoney.setText(responseBody.msg)
                    feightmoney = 0.00
                    updateTotalPrice()
                } else {
                    freightmoney.setText("￥" + responseBody?.data?.freightAmount)
                    feightmoney = responseBody?.data?.freightAmount!!
                    updateTotalPrice()
                }
            }

            override fun onFailure(call: Call<FreightMoneyBean>, t: Throwable) {

            }

        })
    }

    private val TYPE_CHOOSE_ADDRESS = 569

    private val TYPE_CHOOSE_TICKET = 985

    fun chooseAddress(){
        val intent = Intent(this,AddressActivityV2::class.java)
        startActivityForResult(intent,TYPE_CHOOSE_ADDRESS)
    }

    fun initView(){
        ApiManager.getInstance().getApiPersonalTest().loadDefaultAddress(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<DefaultAddressBean> {
            override fun onResponse(call: Call<DefaultAddressBean>, response: Response<DefaultAddressBean>) {
                if (response.body() != null){
                    if (response.body()!!.data == null){
                        return
                    }
                    if (!StringUtils.isEmpty(response.body()!!.data?.phone)){
                        layout_newaddress.visibility = View.GONE
                        layout_defaultaddress.visibility = View.VISIBLE

                        val bean = response.body()?.data!!
                        videoname.setText(bean.name)
                        phone.setText(bean.phone)
                        address.setText(bean.site)
                        addressId = bean.id
                        btn_submit.setBackgroundResource(R.drawable.bg_amallv3_submit_useable)
                        loadExpressMoney()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultAddressBean>, t: Throwable) {

            }

        })

        number.setText(count.toString())
        bigprice.setText("￥" + getPrice())

        Glide.with(this).load(bean.data.manufacture.manufactureLogoUrl).into(dianpu_image)
        store_name.setText(bean.data.manufacture.manufactureName)
        product_name.setText(bean.data.productName)

        sku_image.setText("规格：" + sku.value)
        Glide.with(this).load(sku.image).into(store_image)
        this.price = getPrice().toDouble()
        btn_submit.setOnClickListener {
            submitOrder()
        }
        updateTotalPrice()
        layout_newaddress.setOnClickListener { chooseAddress() }
        layout_defaultaddress.setOnClickListener { chooseAddress() }
        layout_discount.setOnClickListener {
            val intent = Intent(this,ChooseDiscountTicketActivity::class.java)
            intent.putExtra("productId",bean.data.productId)
            intent.putExtra("price", MyNumUtils.remain(number.text.toString().toInt() * price))
            startActivityForResult(intent,TYPE_CHOOSE_TICKET)
        }
        if (mode.equals(ProductsDialogV4.vipGroupPrice)){
            ApiManager.getInstance().getApiMallTest().loadProductDiscountTicket(UserInfoViewModel.getUserId(),bean.data.productId,3).enqueue(object :
                Callback<DiscountTicketBean> {
                override fun onResponse(
                    call: Call<DiscountTicketBean>,
                    response: Response<DiscountTicketBean>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.data.size == 0){
                        return
                    }
                    val bean = response.body()!!.data.get(0)
                    if (bean.useType.equals("all")){
                        userCouponsId = bean.userCouponsId
                        isHaveDiscountTicket = true
                        discount.setText("-" + response.body()!!.data.get(0).price)
                        discountcost = response.body()!!.data.get(0).price
                        updateTotalPrice()
                    } else if (bean.useType.equals("product") and (this@AMallGoBuyActivityV2.bean.data.productId == bean.relationId)){
                        userCouponsId = bean.userCouponsId
                        isHaveDiscountTicket = true
                        discount.setText("-" + response.body()!!.data.get(0).price)
                        discountcost = response.body()!!.data.get(0).price
                        updateTotalPrice()
                    }
                }

                override fun onFailure(call: Call<DiscountTicketBean>, t: Throwable) {

                }

            })
        } else {
            layout_discount.isClickable = false//只有拼团才可以选择使用优惠券，如果不是拼团，不可以选择使用优惠券
        }
        detail_hint.text = "共${count}件 已优惠${discountcost}元"
        remark.addTextChangedListener {
            currentnumber.setText(remark.text.length.toString() + "/50")
        }
    }

    var discountcost = 0.0

    fun getPrice() : String{
        if (mode.equals(ProductsDialogV4.price)){
            return sku.price.toString()
        } else if (mode.equals(ProductsDialogV4.vipGroupPrice)){
            return sku.vipGroupPrice.toString()
        } else if (mode.equals(ProductsDialogV4.groupOwnerPrice)){
            return sku.groupOwnerPrice.toString()
        }
        return sku.vipPrice.toString()
    }

    fun getPayMode(mode : String?) : String {
        if (mode.equals(ProductsDialogV4.price)){
            return "alone"
        }
        if (mode.equals(ProductsDialogV4.vipPrice)){
            return "alone"
        }
        if (mode.equals(ProductsDialogV4.vipGroupPrice)){
            return "group"
        }
        if (mode.equals(ProductsDialogV4.groupOwnerPrice)){
            return "groupOwner"
        }
        return "alone"
    }

    fun submitFeeOrder(){
        val json = JSONObject()
        json.put("userId", UserInfoViewModel.getUserId())
        json.put("productId",bean.data.productId)
        json.put("phone",intent.getStringExtra("phone"))

        val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
        val stringBody = RequestBody.create(mediaType,json.toString())

        ApiManager.getInstance().getApiAMallV3().submitAMallV3FeeOrder(stringBody).enqueue(object :
            Callback<BaseHttpBean<String>> {
            override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {

                if (response.body() != null){
                    val gson = Gson()
                    Log.i(Constants.logtag,gson.toJson(response.body()))
                    if (response.body()!!.code == 403){
                        MessageDialog.show(this@AMallGoBuyActivityV2,"提示","您还没有完成实名认证，是否要前去认证","确定","取消")
                            .setOnOkButtonClickListener { baseDialog, v ->
                                baseDialog.doDismiss()
                                val intent = Intent(this@AMallGoBuyActivityV2,WenBanTongAuthActivity::class.java)
                                startActivity(intent)
                                return@setOnOkButtonClickListener true
                            }
                        return
                    }
                    if (response.body()?.code != 200){
                        DialogUtil.showWarnDialog(this@AMallGoBuyActivityV2,response.body()?.msg)
                        return
                    }
                    val intent = Intent(this@AMallGoBuyActivityV2,GoPayActivityV2::class.java)
                    intent.putExtra("price",(MyNumUtils.remain(number.text.toString().toInt() * price - discountcost)).toString())
                    val orderId : Int? = response.body()!!.data?.toInt()
                    intent.putExtra("orderId",orderId.toString())
                    intent.putExtra("orderType","amall")
                    intent.putExtra("mode",mode)
                    startActivity(intent)
                }

            }

            override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

            }

        })
    }

    fun submitOrder(){
        StringUtils.log("啊啊" + "fjdkjfdk")
        if ((bean.data.productId == 1) or ((bean.data.productId == 2))){
            submitFeeOrder()
            return
        }

        if (addressId.equals("-2000")){
            DialogUtil.showWarnDialog(this,"请添加收货地址")
            return
        }
        val json = JSONObject()
        json.put("userId", UserInfoViewModel.getUserId())
        json.put("addressId",addressId)

        val orderInfo = JSONObject()
        val manufacture = JSONObject()
        manufacture.put("manufactureId",bean.data.manufacture.manufactureId)
        orderInfo.put("manufacture",manufacture)

        val sku = JSONObject()
        sku.put("id",this.sku.id)
        sku.put("count",count)

        val product = JSONObject()
        product.put("productId",bean.data.productId)
        product.put("productName",bean.data.productName)
        val skuarray = JSONArray()
        skuarray.put(sku)
        product.put("skuList",skuarray)
        orderInfo.put("product",product)

        orderInfo.put("note",remark.text.toString())
        orderInfo.put("priceMode",getPayMode(mode))

        if (!StringUtils.isEmpty(intent.getStringExtra("orderGroupSn"))){
            orderInfo.put("orderGroupSn",intent.getStringExtra("orderGroupSn"))
        }

        if (this.userCouponsId != -66){
            orderInfo.put("userCouponsId",userCouponsId)
        }
        if ((bean.data.productId == 1) or ((bean.data.productId == 2))){
            if (mode.equals(ProductsDialogV4.price) or mode.equals(ProductsDialogV4.vipPrice)){
                if (isRightPhone(intent.getStringExtra("phone"))){
                    orderInfo.put("phone",intent.getStringExtra("phone"))
                } else {
                    showWarnDialog("输入的手机号格式不对")
                    return
                }
            } else if (mode.equals(ProductsDialogV4.vipGroupPrice)) {
                if (isRightPhone(intent.getStringExtra("phone"))){
                    orderInfo.put("contactPhone",intent.getStringExtra("phone"))
                }
            }
        }
        json.put("orderInfo",orderInfo)

        val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
        val stringBody = RequestBody.create(mediaType,json.toString())
        StringUtils.log("请我")
        ApiManager.getInstance().getApiAMallV3().submitAMallV3Order(stringBody).enqueue(object :
            Callback<BaseHttpBean<Int>> {
            override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {

                if (response.body() != null){
                    StringUtils.log("code是" + response.body()!!.code)
                    if (response.body()!!.code == 403){
                        MessageDialog.show(this@AMallGoBuyActivityV2,"提示","您还没有完成实名认证，是否要前去认证","确定","取消")
                            .setOnOkButtonClickListener { baseDialog, v ->
                                baseDialog.doDismiss()
                                val intent = Intent(this@AMallGoBuyActivityV2,WenBanTongAuthActivity::class.java)
                                startActivity(intent)
                                return@setOnOkButtonClickListener true
                            }
                        return
                    }
                    if (response.body()?.code != 200){
                        DialogUtil.showWarnDialog(this@AMallGoBuyActivityV2,response.body()?.msg)
                        return
                    }
                    val intent = Intent(this@AMallGoBuyActivityV2,GoPayActivityV2::class.java)
                    intent.putExtra("price",(MyNumUtils.remain(number.text.toString().toInt() * price - discountcost + feightmoney)).toString())
                    val orderId : Int? = response.body()!!.data
                    intent.putExtra("orderId",orderId.toString())
                    intent.putExtra("orderType","amall")
                    intent.putExtra("mode",mode)
                    intent.putExtra("ismemberforgroup",ismemberforgroup)
                    startActivity(intent)
                } else {
                    StringUtils.log("为空啊Zgjdkfjk ")
                }

            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {
                StringUtils.log(t.message)
            }

        })
    }

    fun updateTotalPrice(){
        if (isHaveDiscountTicket){
            var discountmoney = 0.0 // 优惠券优惠金额
            try {
                discountmoney = discount.text.toString().toDouble()
            } catch (e : Exception){
                discountmoney = 0.0
            }

            totalprice = MyNumUtils.remain(number.text.toString().toInt() * price + discountmoney + feightmoney)
            total_cost.text = "￥" + (totalprice + discountmoney).toString()
            subtotal_price.text = (totalprice + discountmoney).toString()
        } else {
            totalprice = MyNumUtils.remain(number.text.toString().toInt() * price + feightmoney)
            total_cost.text = "￥$totalprice"
            subtotal_price.text = totalprice.toString()
        }

    }

    var totalprice = 0.0

    //告诉程序没有选择优惠券
    fun tellUnselected(){
        isHaveDiscountTicket = false
        discount.setText("没有选择优惠券")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_CHOOSE_ADDRESS){
            if (resultCode == AddressAdapter.RESULT_CODE){
                layout_newaddress.visibility = View.GONE
                layout_defaultaddress.visibility = View.VISIBLE
                val addressdata = data?.getSerializableExtra("address") as AddressBean.DataBean
                this.addressId = addressdata.id
                videoname.setText(addressdata.name)
                phone.setText(addressdata.phone)
                address.setText(addressdata.site)
                btn_submit.setBackgroundResource(R.drawable.bg_amallv3_submit_useable)
                loadExpressMoney()
            }
        }
        if (requestCode == TYPE_CHOOSE_TICKET){
            if (resultCode == ChooseDiscountTicketAdapter.TYPE_CHOOSE_TICKET_RESULT){
                val ticketbean : DiscountTicketBean.DataBean = data!!.getSerializableExtra("data") as DiscountTicketBean.DataBean
                userCouponsId = ticketbean.userCouponsId
                discount.setText("-" + ticketbean.price)
                discountcost = ticketbean.price
                if (userCouponsId == -66){
                    tellUnselected()
                } else {
                    isHaveDiscountTicket = true
                }
                updateTotalPrice()
            }
        }
    }

    fun showWarnDialog(str: String?) {
        TipDialog.show(this, str, TipDialog.TYPE.WARNING)
    }

    private fun isRightPhone(str : String?): Boolean {
        if (StringUtils.isEmpty(str)) {
            return false
        }
        val regexPhone = "^1[3-9]\\d{9}$"
        val p = Pattern.compile(regexPhone)
        val m = p.matcher(str)
        return if (!m.matches()) {
            showWarnDialog("手机号不正确")
            false
        } else {
            true
        }
    }

}