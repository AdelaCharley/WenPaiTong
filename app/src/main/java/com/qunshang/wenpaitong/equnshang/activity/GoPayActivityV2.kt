package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.view.View
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.v3.MessageDialog
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_go_pay.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.AliPayBean
import com.qunshang.wenpaitong.equnshang.data.MyBuyOrderBean
import com.qunshang.wenpaitong.equnshang.data.OrderDetailBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.KTUtil
import java.lang.Exception

class GoPayActivityV2 : BaseActivity() {

    var mode : String? = ali

    var paymode : String ?= ""

    companion object {
        val ali = "ali"
        val wechat = "wechat"
    }

    var ismemberforgroup = false

    lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_pay_v2)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        toolbar_back.setOnClickListener {
            MessageDialog.show(this,"","确认要放弃支付吗","继续付款","放弃")
                .setCancelButton(object : OnDialogButtonClickListener {
                    override fun onClick(baseDialog: BaseDialog?, v: View?): Boolean {
                        finish()
                        return false
                    }
                } )
        }
        ismemberforgroup = intent.getBooleanExtra("ismemberforgroup",false)
        this.orderType = intent.getStringExtra("orderType")
        paymode = intent.getStringExtra("mode")
        toolbar_title.text = "订单支付"
        price.text = "￥" + intent.getStringExtra("price")
        layout_wechatpay.setOnClickListener {
            mode = wechat
            img_wechat.setImageResource(R.mipmap.btn_login_select_true)
            img_ali.setImageResource(R.mipmap.btn_login_select_false)
        }
        layout_alipay.setOnClickListener {
            mode = ali
            img_wechat.setImageResource(R.mipmap.btn_login_select_false)
            img_ali.setImageResource(R.mipmap.btn_login_select_true)
        }
        confirmpay.setOnClickListener {
            confirmpay()
        }
        registerWeChat()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(str : String){}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun registerWeChat(){
        api = WXAPIFactory.createWXAPI(this, Constants.WENBANTONG_WECHAT_APPID)
    }

    fun confirmpay(){
        if (mode.equals(ali)){
            if (orderType.equals("amall")){
                ApiManager.getInstance().getApiMallTest().payAliAMall("aliPay",intent.getStringExtra("orderId"),
                    UserInfoViewModel.getUserId()).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.body() == null){
                            return
                        }
                        val json = JSONObject(response.body()!!.string())
                        if (json.getInt("code") != 200){
                            DialogUtil.showWarnDialog(this@GoPayActivityV2,json.getString("msg"))
                            return
                        }
                        val url = json.getJSONObject("data").getString("aliPayUrl")
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        } catch (e : Exception){
                            DialogUtil.toast(this@GoPayActivityV2,"请先安装支付宝")
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }

                })
            } else if (orderType.equals("bmall")){
                ApiManager.getInstance().getApiMallTest().payAliBMall("aliPay",intent.getStringExtra("orderId")).enqueue(object :
                    Callback<AliPayBean> {
                    override fun onResponse(call: Call<AliPayBean>, response: Response<AliPayBean>) {
                        if (response.body() == null){
                            DialogUtil.toast(this@GoPayActivityV2,"请求出现错误，无法请求数据")
                            return
                        }
                        if (response.body()!!.data == null){
                            DialogUtil.toast(this@GoPayActivityV2,response.body()!!.msg)
                            return
                        }
                        val url = response.body()!!.data.aliPayUrl
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        } catch (e : Exception){
                            DialogUtil.toast(this@GoPayActivityV2,"请先安装支付宝")
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<AliPayBean>, t: Throwable) {
                        DialogUtil.toast(this@GoPayActivityV2,"请求出现错误，" + t.message)
                    }

                })
            }
        } else if (mode.equals(wechat)){
            val req = WXLaunchMiniProgram.Req()
            req.userName = Constants.WENBANTONG_WECHAT_MINIPROGRAM_ID // 填小程序原始id
            val path = "/subpkgA/goods-pay/goods-pay?orderSn=" + intent.getStringExtra("orderId") + "&" + "type=" + "app" + "&isVip=vip"
            req.path = path
            //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            if (Constants.isRelease){
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
            } else {
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW// 可选打开 开发版，体验版和正式版
            }
            //req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
            api.sendReq(req)
        }
    }

    var orderType :String ?= "amall"

    override fun onResume() {
        super.onResume()
        if(!isfirst){
            if (orderType.equals("amall")){
                ApiManager.getInstance().getApiMallTest().loadOrderDetail(intent.getStringExtra("orderId").toString()).enqueue(object :
                    Callback<OrderDetailBean> {
                    override fun onResponse(call: Call<OrderDetailBean>, response: Response<OrderDetailBean>) {
                        if (response.body() != null){
                            if ((response.body()!!.data.orderStatus == 20) or (response.body()!!.data.orderStatus == 30) or
                                (response.body()!!.data.orderStatus == 40) or (response.body()!!.data.orderStatus == 50)){


                                    if (ismemberforgroup){
                                        val intent = Intent(this@GoPayActivityV2,ParticipateGroupActivity::class.java)
                                        startActivity(intent)
                                        return
                                    }
                                if ((response.body()!!.data.product.productId == 1) or (response.body()!!.data.product.productId == 2)){
                                    KTUtil.loadPersonalInfo(applicationContext)
                                    finish()
                                    return
                                }
                                EventBus.getDefault().post("refresh")
                                EventBus.getDefault().post("paysuccess")
                                val intent = Intent(this@GoPayActivityV2,PaySuccessActivityV2::class.java)
                                intent.putExtra("orderId",this@GoPayActivityV2.intent.getStringExtra("orderId")?.toInt())
                                intent.putExtra("price",this@GoPayActivityV2.intent.getStringExtra("price"))
                                intent.putExtra("orderGroupSn",response.body()!!.data.orderGroupSn)
                                intent.putExtra("malltype","amall")
                                intent.putExtra("mode",paymode)

                                try {
                                    val map = HashMap<String?,String?>()
                                    map.put("userId",UserInfoViewModel.getUserId())
                                    map.put("orderId",intent.getStringExtra("orderId"))
                                    map.put("price",intent.getStringExtra("price"))
                                    if (Constants.isRelease){
                                        MobclickAgent.onEvent(this@GoPayActivityV2,"releaseamallpay",map)
                                    }
                                } catch (e : Exception){
                                    e.printStackTrace()
                                }
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<OrderDetailBean>, t: Throwable) {
                    }

                })
            } else if (orderType.equals("bmall")){
                ApiManager.getInstance().getApiMallTest().loadMyBuyOrderDetailBean(intent.getStringExtra("orderId").toString()).enqueue(object :
                    Callback<MyBuyOrderBean> {
                    override fun onResponse(call: Call<MyBuyOrderBean>, response: Response<MyBuyOrderBean>) {
                        if (response.body() != null){
                            if ((response.body()!!.data.orderStatus == 20) or (response.body()!!.data.orderStatus == 30) or
                                (response.body()!!.data.orderStatus == 40) or (response.body()!!.data.orderStatus == 50)){
                                val intent = Intent(this@GoPayActivityV2,PaySuccessActivityV2::class.java)
                                intent.putExtra("orderId",this@GoPayActivityV2.intent.getStringExtra("orderId")?.toInt())
                                intent.putExtra("price",this@GoPayActivityV2.intent.getStringExtra("price"))
                                intent.putExtra("malltype","bmall")
                                intent.putExtra("mode",paymode)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<MyBuyOrderBean>, t: Throwable) {

                    }

                })
            }
        }
        isfirst = false
    }

    var isfirst = true

}