package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.activity_amall_go_buy_v2.*
import kotlinx.android.synthetic.main.dialog_buy_vip_partner.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.GoPayActivityV2
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.MyNumUtils
import java.util.regex.Pattern

class BuyVipDialog(context: Context,val productId : Int,val mode : String) : CenterPopupView(context){

    lateinit var beanV2: ProductBeanV2

    override fun onCreate() {
        super.onCreate()
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        if (mode.equals(ProductsDialogV4.price) or mode.equals(ProductsDialogV4.vipPrice)){
            label2.visibility = View.VISIBLE
            phone.visibility = View.VISIBLE
            buynow.setText("立即赠送")
        } else {
            label2.visibility = View.GONE
            phone.visibility = View.GONE
            buynow.setText("立即购买")
        }
        if (productId == 1){
            label.visibility = View.GONE
            layout.visibility = View.GONE
            line.visibility = View.GONE
        }
        ApiManager.getInstance().getApiAMallV3().loadProductDetail(UserInfoViewModel.getUserId(),productId.toString()).enqueue(object :
            Callback<ProductBeanV2> {
            override fun onResponse(call: Call<ProductBeanV2>, response: Response<ProductBeanV2>) {
                if (response.body() != null){
                    val bean = response.body()
                    this@BuyVipDialog.beanV2 = bean!!
                    if (bean.code == 200){
                        initView()
                    } else {
                        DialogUtil.toast(context, bean.msg)
                    }
                }
            }

            override fun onFailure(call: Call<ProductBeanV2>, t: Throwable) {

            }

        })

        val imm: InputMethodManager?=
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            if (rootView != null){
                imm.hideSoftInputFromWindow(rootView?.getWindowToken(), 0)
            }
        }
    }

    var currentCount = 1

    fun initView(){
        add.setOnClickListener {
            currentCount++
            count.setText(currentCount.toString())
        }
        cut.setOnClickListener {
            if (currentCount <= 1){
                return@setOnClickListener
            }
            currentCount --
            count.setText(currentCount.toString())
        }
        buynow.setOnClickListener {

            if (!checkPhone()){
                return@setOnClickListener
            }

            if (!this::beanV2.isInitialized){
                return@setOnClickListener
            }

            val json = JSONObject()
            json.put("userId", UserInfoViewModel.getUserId())
            json.put("productId",productId)
            json.put("phone",phone.text.toString())
            json.put("number",count.text.toString())

            val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
            val stringBody = RequestBody.create(mediaType,json.toString())

            ApiManager.getInstance().getApiAMallV3().submitAMallV3FeeOrder(stringBody).enqueue(object :
                Callback<BaseHttpBean<String>> {
                override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {

                    if (response.body() != null){
                        val gson = Gson()
                        Log.i(Constants.logtag,gson.toJson(response.body()))
                        /*if (response.body()!!.code == 403){
                            MessageDialog.show(context as AppCompatActivity,"提示","您还没有完成实名认证，是否要前去认证","确定","取消")
                                .setOnOkButtonClickListener { baseDialog, v ->
                                    baseDialog.doDismiss()
                                    val intent = Intent(context, AuthActivity::class.java)
                                    context.startActivity(intent)
                                    return@setOnOkButtonClickListener true
                                }
                            return
                        }*/
                        if (response.body()?.code != 200){
                            dismiss()
                            DialogUtil.showWarnDialog(context,response.body()?.msg)
                            return
                        }
                        dismiss()
                        val intent = Intent(context, GoPayActivityV2::class.java)
                        intent.putExtra("price",(MyNumUtils.remain(count.text.toString().toInt() * beanV2.data.skuList.get(0).price)).toString())
                        val orderId : Int = response.body()!!.data!!.toInt()
                        intent.putExtra("orderId",orderId.toString())
                        intent.putExtra("orderType","amall")
                        intent.putExtra("mode",mode)
                        context.startActivity(intent)
                    }

                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

                }

            })
            return@setOnClickListener
        }
    }

    fun checkPhone() : Boolean {

        if (mode.equals(ProductsDialogV4.vipGroupPrice)){
            return true
        }

        val regexPhone = "^1[3-9]\\d{9}$"
        val p = Pattern.compile(regexPhone)
        val m = p.matcher(phone.text.toString())

        return if (!m.matches()) {
            phoneisright.visibility = View.VISIBLE
            phone.background = context.getDrawable(R.drawable.bg_dialog_buy_vip_partner_errorphone)
            false
        } else {
            phoneisright.visibility = View.GONE
            phone.background = context.getDrawable(R.drawable.bg_dialog_buy_vip_partner_defphone)
            true
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_buy_vip_partner
    }

}