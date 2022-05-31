package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kongzue.dialog.v3.MessageDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_to_be_vip_or_partner.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VipImageBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.SubSamplImageUtil.Companion.loadLargeImage
import com.qunshang.wenpaitong.equnshang.view.BuyVipDialog
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV4

class ToBeVipOrPartnerActivity : BaseActivity() {

    var isVip = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_to_be_vip_or_partner)
        setSystemBarColor(R.color.tobevip)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(str : String){
        if ("vipbuyrefresh".equals(str)){
            initView()
        }
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        Glide.with(this).load(UserInfoViewModel.getUserInfo()?.headimage).into(userimage)
        username.setText(UserInfoViewModel.getUserInfo()?.uname)
        //identity.setText("当前身份为:" + getIdentity())
        if (UserInfoViewModel.getUserInfo() != null){
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                identity.visibility = View.VISIBLE
                identity.setText("会员过期时间:" + getIdentity() + "到期")
            } else {
                identity.visibility = View.GONE
            }
        }

        if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
            Glide.with(this).load(R.mipmap.manage_unvip).into(icon_vip)
        } else {
            Glide.with(this).load(R.mipmap.amallv3_buy_unvip).into(icon_vip)
        }
        loadImage()
        changeToVipView()
    }

    fun loadImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ApiManager.getInstance().getApiAMallV3().loadVipPartnerImage()
                .doOnError(object : Consumer<Throwable>{
                    override fun accept(t: Throwable) {

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<VipImageBean>{
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: VipImageBean) {
                        loadLargeImage(this@ToBeVipOrPartnerActivity, t.data.vip, detail)
                    }

                    override fun onError(e: Throwable) {
                        DialogUtil.toast(this@ToBeVipOrPartnerActivity,"出错了")
                    }

                    override fun onComplete() {

                    }
                })
        }
    }

    fun changeToVipView(){
        isVip = true
        root.setBackgroundColor(Color.parseColor("#E1C399"))
        tirenmai.setTextColor(Color.parseColor("#E1C399"))
        tirenmai.background = getDrawable(R.drawable.bg_myquanyi_tirenmai_vip)
        buynow.setText("￥100   立即购买")
        buynow.background = getDrawable(R.drawable.bg_myquanyi_lijigoumai_vip)

        label_label_kaitong.setText("开通会员代表接受")
        label_kaitong.setText("《会员服务协议》")
        label_kaitong.setTextColor(Color.parseColor("#E1C399"))
        label_kaitong.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","会员服务协议")
            intent.putExtra("url", Constants.baseURL + "/rule/vipRule.html")
            startActivity(intent)
        }
        toolbar_right.setOnClickListener {
            val intent = Intent(this,BuyHistoryActivity::class.java)
            startActivity(intent)
        }
        tirenmai.setOnClickListener {
            var productId = 0
            if (isVip) {
                productId = 1
            } else {
                productId = 2
            }
            val customPopup = BuyVipDialog(this, productId, ProductsDialogV4.price)
            XPopup.Builder(this)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .autoOpenSoftInput(false)
                .asCustom(customPopup)
                .show()
        }
        buynow.setOnClickListener {
            var productId = 0
            if (isVip) {
                productId = 1
                val json = JSONObject()
                json.put("userId", UserInfoViewModel.getUserId())
                json.put("productId", productId)
                //json.put("phone", UserInfoViewModel.getUserInfo()?.utel)
                json.put("number", "1")

                val mediaType: MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
                val stringBody = RequestBody.create(mediaType, json.toString())

                ApiManager.getInstance().getApiAMallV3().submitAMallV3FeeOrder(stringBody)
                    .enqueue(object :
                        Callback<BaseHttpBean<String>> {
                        override fun onResponse(
                            call: Call<BaseHttpBean<String>>,
                            response: Response<BaseHttpBean<String>>
                        ) {
                            val gson = Gson()
                            StringUtils.log("" + gson.toJson(response.body()))
                            if (response.body() != null) {
                                val gson = Gson()
                                Log.i(Constants.logtag, gson.toJson(response.body()))
                                if (response.body()!!.code == 403) {
                                    MessageDialog.show(
                                        this@ToBeVipOrPartnerActivity, "提示", "您还没有完成实名认证，是否要前去认证", "确定", "取消")
                                        .setOnOkButtonClickListener { baseDialog, v ->
                                            baseDialog.doDismiss()
                                            val intent = Intent(
                                                this@ToBeVipOrPartnerActivity,
                                                WenBanTongAuthActivity::class.java
                                            )
                                            startActivity(intent)
                                            return@setOnOkButtonClickListener true
                                        }
                                    return
                                }
                                if (response.body()?.code != 200) {
                                    DialogUtil.showWarnDialog(
                                        this@ToBeVipOrPartnerActivity,
                                        response.body()?.msg
                                    )
                                    return
                                }
                                val intent = Intent(
                                    this@ToBeVipOrPartnerActivity,
                                    GoPayActivityV2::class.java
                                )
                                intent.putExtra("price", ("100"))
                                val orderId: Int = response.body()!!.data!!.toInt()
                                intent.putExtra("orderId", orderId.toString())
                                intent.putExtra("orderType", "amall")
                                intent.putExtra("mode", "price")
                                startActivity(intent)
                            }

                        }

                        override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

                        }

                    })
                return@setOnClickListener
            } else {
                productId = 2
            }
            val customPopup = BuyVipDialog(this, productId, ProductsDialogV4.vipGroupPrice)
            XPopup.Builder(this)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .autoOpenSoftInput(false)
                .asCustom(customPopup)
                .show()
        }
    }

    fun getIdentity() : String{
        if (UserInfoViewModel.getUserInfo() != null){
            if (!StringUtils.isEmpty(UserInfoViewModel.getUserInfo()!!.overduetime)){
                val strs = UserInfoViewModel.getUserInfo()!!.overduetime.split(" ")
                if (strs.size > 0){
                    return strs[0]
                }
            }
            //return
        }
        identity.visibility = View.GONE
        return ""
    }

}