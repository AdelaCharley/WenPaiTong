package com.qunshang.wenpaitong.equnshang.activity
import android.content.Intent

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import kotlinx.android.synthetic.main.activity_amall_go_buy.*
import kotlinx.android.synthetic.main.activity_amall_go_buy.address
import kotlinx.android.synthetic.main.activity_amall_go_buy.btn_submit
import kotlinx.android.synthetic.main.activity_amall_go_buy.currentnumber
import kotlinx.android.synthetic.main.activity_amall_go_buy.dianpu_image
import kotlinx.android.synthetic.main.activity_amall_go_buy.layout_defaultaddress
import kotlinx.android.synthetic.main.activity_amall_go_buy.layout_newaddress
import kotlinx.android.synthetic.main.activity_amall_go_buy.videoname
import kotlinx.android.synthetic.main.activity_amall_go_buy.phone
import kotlinx.android.synthetic.main.activity_amall_go_buy.remark
import kotlinx.android.synthetic.main.activity_amall_go_buy.store_name
import kotlinx.android.synthetic.main.activity_amall_go_buy.total_cost
import kotlinx.android.synthetic.main.activity_amall_go_buy.total_price
import kotlinx.android.synthetic.main.activity_amall_go_buy.work
import kotlinx.android.synthetic.main.dialog_products_v3.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.*
import org.json.JSONArray
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.ChooseDiscountTicketAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.utils.MyNumUtils
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV3
import java.lang.Exception
import java.util.regex.Pattern

class AMallGoBuyActivity : BaseActivity() {

    lateinit var bean : ProductBean

    lateinit var sku : ProductBean.DataBean.SkuList

    var price : Double = 0.0

    var addressId = "-2000"

    var count = 0

    var mode : String ? = null

    var userCouponsId = -66

    var isHaveDiscountTicket = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_go_buy)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("确认订单")
        if (intent == null){
            DialogUtil.toast(this,"出错了")
        }
        bean = intent.getSerializableExtra("data") as ProductBean
        sku = intent.getSerializableExtra("sku") as ProductBean.DataBean.SkuList
        count = intent.getIntExtra("count",0)
        mode = intent.getStringExtra("priceMode")
        initView()
    }

    private val TYPE_CHOOSE_ADDRESS = 569

    private val TYPE_CHOOSE_TICKET = 985

    fun chooseAddress(){
        val intent = Intent(this,AddressActivity::class.java)
        startActivityForResult(intent,TYPE_CHOOSE_ADDRESS)
    }

    fun initView(){
        ApiManager.getInstance().getApiPersonalTest().loadDefaultAddress(UserInfoViewModel.getUserId()).enqueue(object : Callback<DefaultAddressBean>{
            override fun onResponse(call: Call<DefaultAddressBean>, response: Response<DefaultAddressBean>) {
                if (response.body() != null){
                    if (response.body()!!.data.phone != null){
                        layout_newaddress.visibility = View.GONE
                        layout_defaultaddress.visibility = View.VISIBLE

                        val bean = response.body()?.data!!
                        videoname.setText(bean.name)
                        phone.setText(bean.phone)
                        address.setText(bean.site)
                        addressId = bean.id
                    }
                }
            }

            override fun onFailure(call: Call<DefaultAddressBean>, t: Throwable) {

            }

        })

        //if (mode.equals(""))

        number.setText(count.toString())
        bigprice.setText("￥" + getPrice())

        Glide.with(this).load(bean.data.manufacture.manufactureLogoUrl).into(dianpu_image)
        store_name.setText(bean.data.manufacture.manufactureName)
        product_name.setText(bean.data.productName)

        sku_image.setText(sku.value)
        Glide.with(this).load(sku.image).into(store_image)
        this.price = getPrice().toDouble()
        add.setOnClickListener {
            if ((bean.data.productId == 1) or (bean.data.productId == 2)){//会员和合伙人写死为1
                number.setText("1")
                return@setOnClickListener
            }
            count = (number.text.toString().toInt() + 1)
            number.setText(count.toString())
            updateTotalPrice()
        }
        cut.setOnClickListener {
            if ((bean.data.productId == 1) or (bean.data.productId == 2)){
                number.setText("1")
                return@setOnClickListener
            }
            if ((number.text.toString().toInt()) <= 1) {
                return@setOnClickListener
            }
            count = (number.text.toString().toInt() - 1)
            number.setText(count.toString())
            updateTotalPrice()
        }
        btn_submit.setOnClickListener {
            submitOrder()
        }
        updateTotalPrice()
        layout_newaddress.setOnClickListener { chooseAddress() }
        layout_defaultaddress.setOnClickListener { chooseAddress() }
        layout_discount.setOnClickListener {
            val intent = Intent(this,ChooseDiscountTicketActivity::class.java)
            intent.putExtra("productId",bean.data.productId)
            intent.putExtra("price",MyNumUtils.remain(number.text.toString().toInt() * price))
            startActivityForResult(intent,TYPE_CHOOSE_TICKET)
        }

        if (bean.data.getIsControlPrice() != 0){
            layout_lijian.visibility = View.GONE
            layout_pingtaibutie.visibility = View.VISIBLE
            butie.setText("-" + (sku.controlPrice - price))
            bigprice.setText("￥" + sku.controlPrice)//如果不是控价商品，bigprice是多少,discountfee也是多少，如果不是，就不一样了
            total_price.setText("￥" + sku.controlPrice)
        }

        if (mode.equals(ProductsDialogV3.vipGroupPrice)){
            discountprice.visibility = View.VISIBLE
            group_progress.visibility = View.VISIBLE
            label_yushoutuangoujia.visibility = View.VISIBLE
            discountprice.setText("￥" + price)  //注意这里的discountprice哦
            group_progress.max = bean.data.vipGroupCard.total
            group_progress.progress = bean.data.vipGroupCard.current

            ApiManager.getInstance().getApiMallTest().loadProductDiscountTicket(UserInfoViewModel.getUserId(),bean.data.productId,3).enqueue(object : Callback<DiscountTicketBean>{
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
                    } else if (bean.useType.equals("product") and (this@AMallGoBuyActivity.bean.data.productId == bean.relationId)){
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
            discountprice.visibility = View.GONE
            group_progress.visibility = View.GONE
            label_yushoutuangoujia.visibility = View.GONE
            layout_discount.isClickable = false//只有拼团才可以选择使用优惠券，如果不是拼团，不可以选择使用优惠券
        }

        if ((bean.data.productId == 1) or ((bean.data.productId == 2))){
            if (mode.equals(ProductsDialogV3.price) or mode.equals(ProductsDialogV3.vipPrice)){
                layout_phone.visibility = View.VISIBLE
                layout_confirm_phone.visibility = View.VISIBLE
            } else if (mode.equals(ProductsDialogV3.vipGroupPrice)) {
                layout_contactPhone.visibility = View.VISIBLE
            }
        }

        remark.addTextChangedListener {
            currentnumber.setText(remark.text.length.toString() + "/50")
        }
    }

    var discountcost = 0.0

    fun getPrice() : String{
        if (mode.equals(ProductsDialogV3.price)){
            return sku.price.toString()
        } else if (mode.equals(ProductsDialogV3.vipGroupPrice)){
            return sku.vipGroupPrice.toString()
        } else if (mode.equals(ProductsDialogV3.groupOwnerPrice)){
            return sku.groupOwnerPrice.toString()
        }
        return sku.vipPrice.toString()
    }

    fun submitOrder(){
        if (addressId.equals("-2000")){
            DialogUtil.showWarnDialog(this,"请添加收货地址")
            return
        }
        val json = JSONObject()
        json.put("userId",UserInfoViewModel.getUserId())
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
        orderInfo.put("priceMode",mode)
        if ((bean.data.productId == 1) or ((bean.data.productId == 2))){
            orderInfo.put("priceMode",ProductsDialogV3.price)
        } else {
            orderInfo.put("priceMode",mode)
        }
        if (this.userCouponsId != -66){
            orderInfo.put("userCouponsId",userCouponsId)
        }
        if ((bean.data.productId == 1) or ((bean.data.productId == 2))){
            if (mode.equals(ProductsDialogV3.price) or mode.equals(ProductsDialogV3.vipPrice)){
                if (isRightPhone(payphone.text.toString())){
                    if (!payphone.text.toString().equals(confirm_phone.text.toString())){
                        showWarnDialog("两次输入的手机号不一致")
                        return
                    } else {
                        orderInfo.put("phone",payphone.text.toString())
                    }
                } else {
                    showWarnDialog("输入的手机号格式不对")
                    return
                }
            } else if (mode.equals(ProductsDialogV3.vipGroupPrice)) {
                if (isRightPhone(contactPhone.text.toString())){
                    orderInfo.put("contactPhone",contactPhone.text.toString())
                }
            }
        }
        json.put("orderInfo",orderInfo)

        val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
        val stringBody = RequestBody.create(mediaType,json.toString())

        ApiManager.getInstance().getApiMallTest().submitAMallOrder(stringBody).enqueue(object : Callback<BaseHttpBean<Int>>{
            override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {

                if (response.body() != null){
                    if (response.body()!!.code == 403){
                        MessageDialog.show(this@AMallGoBuyActivity,"提示","您还没有完成实名认证，是否要前去认证","确定","取消")
                            .setOnOkButtonClickListener { baseDialog, v ->
                                baseDialog.doDismiss()
                                val intent = Intent(this@AMallGoBuyActivity,WenBanTongAuthActivity::class.java)
                                startActivity(intent)
                                return@setOnOkButtonClickListener true
                            }
                        return
                    }
                    if (response.body()?.code != 200){
                        DialogUtil.showWarnDialog(this@AMallGoBuyActivity,response.body()?.msg)
                        return
                    }
                    val intent = Intent(this@AMallGoBuyActivity,GoPayActivity::class.java)
                    intent.putExtra("price",(MyNumUtils.remain(number.text.toString().toInt() * price - discountcost)).toString())
                    val orderId : Int ? = response.body()!!.data
                    intent.putExtra("orderId",orderId.toString())
                    intent.putExtra("orderType","amall")
                    startActivity(intent)
                }

            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

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

            totalprice = MyNumUtils.remain(number.text.toString().toInt() * price + discountmoney)
            if (bean.data.getIsControlPrice() != 0){
                totalprice = sku.controlPrice * number.text.toString().toInt() + discountmoney

                var totalbutie = number.text.toString().toInt() * (sku.controlPrice - price)
                butie.setText("-" + totalbutie)
            }
            total_price.setText("￥" + (totalprice - discountmoney).toString())
            total_cost.setText("￥" + (MyNumUtils.remain(number.text.toString().toInt() * price + discount.text.toString().toDouble())).toString())

            if (bean.data.productId == 1){
                work.setText("+" + 100)
            } else if (bean.data.productId == 2){
                work.setText("+" + 30000)
            } else {
                work.setText("+" + (MyNumUtils.remain(number.text.toString().toInt() * price + discount.text.toString().toDouble())).toInt())
            }
        } else {

            totalprice = MyNumUtils.remain(number.text.toString().toInt() * price)

            if (bean.data.getIsControlPrice() != 0){
                totalprice = sku.controlPrice * number.text.toString().toInt()

                var totalbutie = number.text.toString().toInt() * (sku.controlPrice - price)
                butie.setText("-" + totalbutie)
            }
            total_price.setText("￥" + (totalprice).toString())
            total_cost.setText("￥" + (MyNumUtils.remain(number.text.toString().toInt() * price)).toString())
            if (bean.data.productId == 1){
                work.setText("+" + 100)
            } else if (bean.data.productId == 2){
                work.setText("+" + 30000)
            } else {
                work.setText("+" + (MyNumUtils.remain(number.text.toString().toInt() * price)).toInt())
            }
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

    private fun isRightPhone(str : String): Boolean {
        if (StringUtils.isEmpty(str)) {
            //showWarnDialog("请输入手机号")
            //ToastUtil.toast(RegisterActivity.this,"请输入手机号");
            return false
        }
        val regexPhone = "^1[3-9]\\d{9}$"
        val p = Pattern.compile(regexPhone)
        val m = p.matcher(str)
        return if (!m.matches()) {
            showWarnDialog("手机号不正确")
            //ToastUtil.toast(RegisterActivity.this,"手机号不正确");
            false
        } else {
            true
        }
    }

}