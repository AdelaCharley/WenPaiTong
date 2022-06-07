package com.qunshang.wenpaitong.equnshang.activity

/*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_bmall_go_buy.*
import kotlinx.android.synthetic.main.activity_bmall_go_buy.address
import kotlinx.android.synthetic.main.activity_bmall_go_buy.btn_submit
import kotlinx.android.synthetic.main.activity_bmall_go_buy.currentnumber
import kotlinx.android.synthetic.main.activity_bmall_go_buy.dianpu_image
import kotlinx.android.synthetic.main.activity_bmall_go_buy.layout_defaultaddress
import kotlinx.android.synthetic.main.activity_bmall_go_buy.layout_newaddress
import kotlinx.android.synthetic.main.activity_bmall_go_buy.videoname
import kotlinx.android.synthetic.main.activity_bmall_go_buy.phone
import kotlinx.android.synthetic.main.activity_bmall_go_buy.remark
import kotlinx.android.synthetic.main.activity_bmall_go_buy.store_name
import kotlinx.android.synthetic.main.activity_bmall_go_buy.total_cost
import kotlinx.android.synthetic.main.activity_bmall_go_buy.total_price
import kotlinx.android.synthetic.main.activity_bmall_go_buy.work
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.BMallSubmitOrderAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class BMallGoBuyActivity : BaseActivity() {
    lateinit var bean : ProductBean

    lateinit var sku : List<ProductBean.DataBean.SkuList>

    var addressId = "-2000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmall_go_buy)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("确认订单")
        if (intent == null){
            DialogUtil.toast(this,"出错了")
        }
        bean = intent.getSerializableExtra("data") as ProductBean
        sku = intent.getSerializableExtra("skus") as List<ProductBean.DataBean.SkuList>
        initView()
    }

    private val TYPE_CHOOSE_ADDRESS = 569

    fun chooseAddress(){
        val intent = Intent(this,AddressActivityV2::class.java)
        startActivityForResult(intent,TYPE_CHOOSE_ADDRESS)
    }

    fun initView(){
        ApiManager.getInstance().getApiPersonalTest().loadDefaultAddress(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<DefaultAddressBean> {
            override fun onResponse(call: Call<DefaultAddressBean>, response: Response<DefaultAddressBean>) {
                if (response.body() != null){
                    if (response.body()!!.data != null){
                        if (response.body()!!.data!!.phone != null){
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
            }

            override fun onFailure(call: Call<DefaultAddressBean>, t: Throwable) {

            }

        })
        bMallSubmitOrderAdapter = BMallSubmitOrderAdapter(this,sku,bean)
        list.adapter = bMallSubmitOrderAdapter
        Glide.with(this).load(bean.data.manufacture.manufactureLogoUrl).into(dianpu_image)
        store_name.setText(bean.data.manufacture.manufactureName)

        btn_submit.setOnClickListener {
            submitOrder()
        }
        //updateTotalPrice()
        layout_newaddress.setOnClickListener { chooseAddress() }
        layout_defaultaddress.setOnClickListener { chooseAddress() }

        remark.addTextChangedListener {
            currentnumber.setText(remark.text.length.toString() + "/50")
        }
    }

    lateinit var bMallSubmitOrderAdapter: BMallSubmitOrderAdapter

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun submitOrder(){
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
        orderInfo.put("orderType","0")
        val product = JSONObject()
        product.put("productId",bean.data.productId)
        product.put("productName",bean.data.productName)
        val skuarray = JSONArray()
        for (sku in bMallSubmitOrderAdapter.getData()){
            val skuobject = JSONObject()
            skuobject.put("id",sku.id)
            skuobject.put("count",sku.buyCount)
            skuarray.put(skuobject)
        }

        product.put("skuList",skuarray)
        orderInfo.put("product",product)

        orderInfo.put("note",remark.text.toString())
        //orderInfo.put("priceMode",mode)
        json.put("orderInfo",orderInfo)

        Log.i("jsonstring",json.toString())

        val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
        val stringBody = RequestBody.create(mediaType,json.toString())

        ApiManager.getInstance().getApiMallTest().submitBMallOrder(stringBody).enqueue(object :
            Callback<BaseHttpBean<String>> {
            override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {
                if (response.body() != null){
                    if (response.body()!!.code != 200){
                        //DialogUtil.showWarnDialog(this@BMallGoBuyActivity,json.getString("msg"))以前写的，不知道为啥会这样
                        DialogUtil.showWarnDialog(this@BMallGoBuyActivity,response.body()?.msg)
                        return
                    }
                    val intent = Intent(this@BMallGoBuyActivity,GoPayActivity::class.java)
                    intent.putExtra("price",bMallSubmitOrderAdapter.totalPrice.toString())
                    intent.putExtra("orderType","bmall")
                    val orderId : String? = response.body()!!.data
                    intent.putExtra("orderId",orderId)
                    //Log.i("zhangjuniiifjdkf",orderId!!)
                    startActivity(intent)
                }

            }

            override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateTotalPrice(price : String){
        total_price.setText("￥" + price)
        total_cost.setText("￥" + price)
        work.setText("+" + price)
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
    }
}*/
