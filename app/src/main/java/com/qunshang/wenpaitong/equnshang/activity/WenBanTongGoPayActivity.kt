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
import kotlinx.android.synthetic.main.activity_wen_ban_tong_go_pay.*
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

import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel

import com.qunshang.wenpaitong.equnshang.data.WenBanTongOrderDetailBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.lang.Exception

class WenBanTongGoPayActivity : BaseActivity() {
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
        setContentView(R.layout.activity_wen_ban_tong_go_pay)
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
            ApiManager.getInstance().getApiMallTest().payWenBanTong(
                "aliPay",
                intent.getStringExtra("orderId"),
                UserInfoViewModel.getUserId()
            ).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.body() == null){
                        return
                    }
                    val json = JSONObject(response.body()!!.string())
                    if (json.getInt("code") != 200){
                        DialogUtil.showWarnDialog(this@WenBanTongGoPayActivity,json.getString("msg"))
                        return
                    }
                    val url = json.getJSONObject("data").getString("aliPayUrl")
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } catch (e : Exception){
                        DialogUtil.toast(this@WenBanTongGoPayActivity,"请先安装支付宝")
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    StringUtils.log(t.message)
                }

            })
        } else if (mode.equals(wechat)){
            val req = WXLaunchMiniProgram.Req()
            req.userName = Constants.WENBANTONG_WECHAT_MINIPROGRAM_ID // 填小程序原始id
            val path = "/subpkgA/goods-pay/goods-pay?orderSn=" + intent.getStringExtra("orderId") + "&type=app"
            req.path = path
            //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            if (Constants.isRelease){
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
            } else {
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW// 可选打开 开发版，体验版和正式版
            }

            api.sendReq(req)
        }
    }

    var orderType :String ?= "amall"

    override fun onResume() {
        super.onResume()
        if(!isfirst){

            ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadOrderDetail(intent.getStringExtra("orderId")).enqueue(object : Callback<WenBanTongOrderDetailBean>{
                override fun onResponse(
                    call: Call<WenBanTongOrderDetailBean>,
                    response: Response<WenBanTongOrderDetailBean>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        val data = response.body()!!.data
                        if (data.order.orderStatus == 20){
                            val intent = Intent(this@WenBanTongGoPayActivity,WenBanTongPaySuccessActviity::class.java)
                            intent.putExtra("price",response.body()!!.data.order.payAmount)
                            intent.putExtra("orderSn",response.body()!!.data.order.orderSn)
                            startActivity(intent)
                            finish()
                            try {
                                val map = HashMap<String?,String?>()
                                map.put("userId",UserInfoViewModel.getUserId())
                                map.put("orderId",intent.getStringExtra("orderId"))
                                map.put("price",response.body()!!.data?.order?.payAmount)
                                if (Constants.isRelease){
                                    MobclickAgent.onEvent(this@WenBanTongGoPayActivity,"releasewenbantongpay",map)
                                }
                            } catch (e : Exception){
                                e.printStackTrace()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WenBanTongOrderDetailBean>, t: Throwable) {

                }

            })
        }
        isfirst = false
    }

    var isfirst = true

}