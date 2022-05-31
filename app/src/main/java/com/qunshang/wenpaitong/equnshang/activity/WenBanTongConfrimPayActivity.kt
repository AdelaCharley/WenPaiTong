package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_wen_ban_tong_confrim_pay.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.data.WenBanTongZiTiBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.MyNumUtils
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongConfrimPayActivity : BaseActivity() {

    var count = 1

    lateinit var wenBantongProductBean : WenBanTongProductBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wen_ban_tong_confrim_pay)
        setSystemBarColor(R.color.wenbantong_confrimpay_bg)
        wenBantongProductBean = intent.getSerializableExtra("product") as WenBanTongProductBean
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("确认订单")
        top.background = null
        loadZiTi()
        initView()
    }

    fun initView(){
        //zitiaddress.setText(wenBantongProductBean.data.companyAddress) //隐藏自提点，到时候可以再加上去
        Glide.with(this).load(wenBantongProductBean.data.manufacture.companyAvatar).into(label_img)
        Glide.with(this).load(wenBantongProductBean.data.productPosterUrl).into(product_image)
        shopname.setText(wenBantongProductBean.data.manufacture.companyName)
        product_single_price.setText(wenBantongProductBean.data.price.toString())
        if (wenBantongProductBean.data.discount != null){
            product_single_price.setText(wenBantongProductBean.data.discount.productDiscountPriceStr)
        }
        wenbantongcount.setText(wenBantongProductBean.data.tag)
        //wenbantongcount.setText(wenBantongProductBean.data.price.toString() + "文版通")
        product_name.setText(wenBantongProductBean.data.productName)
        updatePrice()
        add.setOnClickListener {
            count++
            number.setText(count.toString())
            updatePrice()
        }

        cut.setOnClickListener {
            if (count <= 1){
                return@setOnClickListener
            }
            count --
            number.setText(count.toString())
            updatePrice()
        }
        paynow.setOnClickListener {
            val json = JSONObject()
            json.put("userId", UserInfoViewModel.getUserId())
            json.put("productId",wenBantongProductBean.data.productId)
            json.put("productCount",count)

            val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
            val stringBody = RequestBody.create(mediaType,json.toString())

            ApiManager.getInstance().getApiWenBanTong_ZhangJun().generateWenBanTongOrder(stringBody).enqueue(object :
                Callback<BaseHttpBean<String>> {
                override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {

                    if (response.body() != null){
                        val gson = Gson()
                        Log.i(Constants.logtag,gson.toJson(response.body()))
                        if (response.body()?.code != 200){
                            DialogUtil.showWarnDialog(this@WenBanTongConfrimPayActivity,response.body()?.msg)
                            return
                        }
                        val intent = Intent(this@WenBanTongConfrimPayActivity, WenBanTongGoPayActivity::class.java)
                        if (wenBantongProductBean.data.discount != null){
                            intent.putExtra("price", MyNumUtils.makeDoubleToString((number.text.toString().toInt() * wenBantongProductBean.data.discount.productDiscountPrice.toDouble())))
                        } else {
                            intent.putExtra("price", MyNumUtils.makeDoubleToString((number.text.toString().toInt() * wenBantongProductBean.data.realPrice.toDouble())))
                        }
                                    //MyNumUtils.remain(count * wenBantongProductBean.data.realPrice.toDouble())).toString()
                                    //MyNumUtils.makeDoubleToString(number.text.toString().toInt() * wenBantongProductBean.data.realPrice.toDouble())
                            //MyNumUtils.remain().toString())
                        val orderId : String = response.body()!!.data!!
                        intent.putExtra("orderId",orderId)
                        intent.putExtra("orderType","amall")
                        startActivity(intent)
                    }

                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

                }

            })
            return@setOnClickListener
        }
    }

    fun updatePrice(){
        if (wenBantongProductBean.data.discount != null){
            total_price.setText(
                MyNumUtils.makeDoubleToString((number.text.toString().toInt() * wenBantongProductBean.data.discount.productDiscountPrice.toDouble()))
                //MyNumUtils.remain().toString()
            )
            return
        }
        total_price.setText(
            MyNumUtils.makeDoubleToString((number.text.toString().toInt() * wenBantongProductBean.data.realPrice.toDouble()))
            //MyNumUtils.remain().toString()
        )
    }

    override fun onResume() {
        super.onResume()
    }

    fun loadZiTi(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadZiTi(UserInfoViewModel.getUserId()).enqueue(object : Callback<WenBanTongZiTiBean>{
            override fun onResponse(
                call: Call<WenBanTongZiTiBean>,
                response: Response<WenBanTongZiTiBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    if (StringUtils.isEmpty(response.body()!!.data.realName)){
                        zitiinfo.visibility = View.GONE
                        text_wanshan_inform.visibility = View.VISIBLE
                        text_wanshan_inform.setOnClickListener {
                            wanshanInfo()
                        }
                    } else {
                        zitiinfo.visibility = View.VISIBLE
                        zitiinfo.setText(response.body()!!.data.realName + "   " +  response.body()!!.data.phone)
                        text_wanshan_inform.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<WenBanTongZiTiBean>, t: Throwable) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1333){
            if (resultCode == 500){
                loadZiTi()
            }
        }
    }

    fun wanshanInfo(){
        val intent = Intent(this,WenBanTongAuthActivity::class.java)
        startActivityForResult(intent,1333)
    }

}