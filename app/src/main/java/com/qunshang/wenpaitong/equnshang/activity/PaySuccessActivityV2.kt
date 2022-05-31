package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityPaySuccessV2Binding
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.PinTuanProgressAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.PinTuanDetailBean
import com.qunshang.wenpaitong.equnshang.data.UserInfo
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import java.util.*

class PaySuccessActivityV2 : BaseActivity (){

    lateinit var binding: ActivityPaySuccessV2Binding

    var malltype: String? = ""

    //var mode: String? = ""

    var orderGroupSn: String? = ""//成团号，注意这个在单独购买的时候是空值null

    var orderId = 0

    var price = ""              //订单花费的价格

    var isNBackOne = true
    var data: PinTuanDetailBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaySuccessV2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        orderId = intent.getIntExtra("orderId", 0)
        price = "订单金额： ￥" + intent.getStringExtra("price")              //订单花费的价格
        malltype = intent.getStringExtra("malltype")
        //mode = intent.getStringExtra("mode")
        orderGroupSn = intent.getStringExtra("orderGroupSn")
        setLayout()
    }

    private fun setLayout() {
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = ""
        binding.paySuccess.tvPrice.text = price
        if ("amall".equals(malltype)) {
            ApiManager.getInstance()
                .getApiAMallV3()
                .loadGroupDetail(UserInfoViewModel.getUserId(), orderGroupSn)
                .enqueue(object : Callback<BaseHttpBean<PinTuanDetailBean>> {
                    override fun onResponse(
                        call: Call<BaseHttpBean<PinTuanDetailBean>>,
                        response: Response<BaseHttpBean<PinTuanDetailBean>>
                    ) {
                        Log.d("heshulin", "psa-response: $response")
                        if (response.body()!!.code == 200) {
                            data = response.body()!!.data
                            if (data == null) {
                                setSingleBuyLayout()
                            } else {
                                setGroupBuyLayout()
                            }
                        } else {
                            Log.d("PaySuccessActivityV2", "response: $response")
                            return
                        }
                    }

                    override fun onFailure(
                        call: Call<BaseHttpBean<PinTuanDetailBean>>,
                        t: Throwable
                    ) {
                    }
                })
        }
    }

    private fun setSingleBuyLayout() {
        binding.layoutGroup.visibility = View.GONE
        binding.btnLeft.text = "查看订单"
        binding.btnRight.text = "返回首页"
        binding.btnLeft.setOnClickListener { seeOrderDetail() }
        binding.btnRight.setOnClickListener { goToMain() }
    }

    private fun setGroupBuyLayout() {
        loadData()
        binding.tvTitleInvite.tvTitle.text = "邀请好友"
        binding.btnLeft.text = "返回首页"
        binding.btnRight.text = "邀请好友拼团"
        binding.btnLeft.setOnClickListener { goToMain() }
        binding.btnRight.setOnClickListener { inviteFriend() }
    }

    private fun loadData() {
        isNBackOne = (data!!.groupInfo.isNBackOne != 0) && (data!!.userGroupInfo.isOwner != 0)
        setCountDownLayout(data!!.groupInfo.expireTime)
        setProgressLayout(data!!.groupInfo.userInfo)
        binding.tvHint.text = colorText()
        if (isNBackOne) {
            binding.rule.root.visibility = View.VISIBLE
            binding.rule.checkRule.setOnClickListener { toCheckRule() }
        }
    }

    private fun setCountDownLayout(time: String) {
        val task = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val timeArr = TimeUtil.getTimeRemainByDayString(time).split(":")
                    val layout = binding.countDownUp
                    layout.tvHh.text = timeArr[0]
                    layout.tvMm.text = timeArr[1]
                    layout.tvSs.text = timeArr[2]
                }
            }
        }
        val oneSecond: Long = 1000
        Timer().schedule(task, 0, oneSecond)
    }

    private fun colorText(): SpannableStringBuilder {
        var builder0 = SpannableStringBuilder("还差1人成团，赶快邀请好友加入吧！")
        val color0 = ForegroundColorSpan(Color.parseColor("#F3594F"))
        builder0.setSpan(color0, 2, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        if (!isNBackOne) {
            return builder0
        } else {
            builder0.append("\n")
        }
        val builder1 = SpannableStringBuilder("再额外邀请2人即可享受免单优惠")
        val color1 = ForegroundColorSpan(Color.parseColor("#F3594F"))
        builder1.setSpan(color1, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return builder0.append(builder1)
    }

    private fun setProgressLayout(userInfo: List<UserInfo>) {
        val layout = binding.progress
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        layout.layoutManager = manager
        layout.adapter = PinTuanProgressAdapter(isNBackOne, userInfo)
    }

    /*查看订单详情*/
    private fun seeOrderDetail() {
        val intent = Intent(this, OrderDetailActivityV2::class.java)
        intent.putExtra("id", orderId)
        startActivity(intent)
    }

    /*返回首页*/
    private fun goToMain() {
        val intent = Intent(this, AMallActivityV3::class.java)
        startActivity(intent)
    }

    /*邀请好友拼团*/
    private fun inviteFriend() {
        Observable.create(ObservableOnSubscribe<Bitmap?> { e ->
            Glide.with(this)
                .asBitmap()
                .load(data!!.product.skuPic)
                .centerCrop()
                .override(200, 200)
                .into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        e.onNext(resource)
                    }
                })
        }).subscribeOn(AndroidSchedulers.mainThread())
            .map(object : Function<Bitmap?, ByteArray> {
                override fun apply(bitmap: Bitmap?): ByteArray {
                    return BitmapUtils.bmpToByteArray(bitmap, 32)
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<ByteArray?> {
                @Throws(java.lang.Exception::class)
                override fun accept(bytes: ByteArray?) {
                    val webpage = WXWebpageObject()
                    if (Constants.isRelease){
                        webpage.webpageUrl = Constants.WECHAT_BASE_URL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    } else {
                        webpage.webpageUrl = Constants.WECHAT_BASE_URL + "/equnshangTest/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    }
                    //webpage.webpageUrl = Constants.WECHAT_BASE_URL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    //webpage.webpageUrl = Constants.baseURL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    val msg = WXMediaMessage(webpage)
                    msg.title =
                        "【仅剩" + /*data!!.groupInfo.groupSuccessLeftPeople*/ 1 + "个名额】，我" + data!!.product.productPrice + "元拼了个" + data!!.product.productName
                    msg.description = "我买了\"" + data!!.product.skuInfo + "\"规格"
                    msg.thumbData = bytes
                    val req = SendMessageToWX.Req()
                    req.transaction = "req"
                    req.message = msg
                    req.scene = SendMessageToWX.Req.WXSceneSession
                    MainActivity.api.sendReq(req)
                }
            }, object : Consumer<Throwable?> {
                @Throws(java.lang.Exception::class)
                override fun accept(throwable: Throwable?) {
                    DialogUtil.toast(this@PaySuccessActivityV2, "分享失败")
                }
            })
    }

    /*查看满返规则*/
    private fun toCheckRule() {
        val intent = Intent(this, FourBackRuleActivity::class.java)
        startActivity(intent)
    }
}