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
import coil.load
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
import kotlinx.android.synthetic.main.activity_pin_tuan_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityPinTuanDetailBinding
import com.qunshang.wenpaitong.databinding.ItemCountDownBinding
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.PinTuanProgressAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import java.util.*

class PinTuanDetailActivity : BaseActivity() {
    lateinit var binding: ActivityPinTuanDetailBinding
    private var orderGroupSn : String? = null

    val STATUS_CONTINUE = 10
    val STATUS_SUCCESS = 20
//    val STATUS_FREE = 30
    val STATUS_FAIL = 40

    var status = 0
    var is4Back1 = true
    var isEndFree = false    //免单倒计时结束?

    lateinit var dataBean : PinTuanDetailBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinTuanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderGroupSn = intent.getStringExtra("orderGroupSn")
        Log.i(Constants.logtag, orderGroupSn!! )

        loadData()

    }

    private fun loadData() {
        ApiManager.getInstance()
            .getApiAMallV3()
            .loadGroupDetail(UserInfoViewModel.getUserId(), orderGroupSn)
            .enqueue(object : Callback<BaseHttpBean<PinTuanDetailBean>> {
                override fun onResponse(
                    call: Call<BaseHttpBean<PinTuanDetailBean>>,
                    response: Response<BaseHttpBean<PinTuanDetailBean>>
                ) {
                    Log.d("shulinr", "ptd-response: $response")
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        if (response.body()!!.data == null){
                            return
                        }
                        dataBean = response.body()!!.data!!
                        status = dataBean.groupInfo.status
                        is4Back1 = (dataBean.groupInfo.isNBackOne != 0) && (dataBean.userGroupInfo.isOwner != 0)
                        initLayout()
                    } else {
                        Log.d("PinTuanDetailActivity", "response: $response")
                        return
                    }
                }
                override fun onFailure(call: Call<BaseHttpBean<PinTuanDetailBean>>, t: Throwable) {
                }

            })
    }

    private fun initLayout() {
        binding.toolbar.toolbarTitle.text = "拼团详情"
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        if (is4Back1) {
            binding.rule.checkRule.visibility = View.GONE
        } else {
            binding.rule.root.visibility = View.GONE
        }
        binding.btnOrderDetail.setOnClickListener {
            if (!this::dataBean.isInitialized){
                return@setOnClickListener
            }
            val intent = Intent(this, OrderDetailActivityV2::class.java)
            intent.putExtra("id",dataBean.orderId)
            startActivity(intent)
        }
        setSubTitleLayout()
        setCountDownLayout(dataBean.groupInfo.expireTime)
        setProgressLayout(dataBean.groupInfo.userInfo)
        setInviteLayout(dataBean.groupInfo.cashSuccessLeftPeople)
        setProductInfoLayout(dataBean.product)
        root.visibility = View.VISIBLE
    }

    private fun doShare(){
        Observable.create(ObservableOnSubscribe <Bitmap?> { e ->
            Glide.with(this)
                .asBitmap()
                .load(dataBean.product.skuPic)
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
                    webpage.webpageUrl = "http://api.equnshang.cn" + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    //webpage.webpageUrl = Constants.baseURL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    val msg = WXMediaMessage(webpage)
                    msg.title = "【仅剩" +
                            /*dataBean.groupInfo.groupSuccessLeftPeople*/ 1 +
                            "个名额】，我" + dataBean.product.productPrice + "元拼了个" + dataBean.product.productName
                    msg.description = "我买了\"" + dataBean.product.skuInfo + "\"规格"
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
                    DialogUtil.toast(this@PinTuanDetailActivity,"分享失败")
                }
            })
    }

    private fun setSubTitleLayout() {
        if (!is4Back1 || status == STATUS_CONTINUE) {
            binding.tvContent.visibility = View.GONE
        }
        when (status) {
            STATUS_CONTINUE -> { binding.state.tvTitle.text = "拼团中"
            }
            STATUS_FAIL -> { binding.state.tvTitle.text = "拼团失败"
                binding.tvContent.text = "原路退还您的金额"
            }
            else -> { binding.state.tvTitle.text = "拼团成功"
                binding.tvContent.text = "商家将尽快为您发货"
            }
        }
    }

    private fun setCountDownLayout(time: String) {
        var timeStr = TimeUtil.getTimeRemainByDayString(time)

        if (status == STATUS_CONTINUE) {
            binding.countDownUp.root.visibility = View.VISIBLE
        } else if (status == STATUS_SUCCESS && is4Back1) {
            if (timeStr == "00:00:00") {
                isEndFree = true
                return
            } else {
                binding.countDownUnder.root.visibility = View.VISIBLE
            }
        }

        val task = object : TimerTask(){
            override fun run() {
                runOnUiThread {
                    val timeArr = TimeUtil.getTimeRemainByDayString(time).split(":")
                    when (status) {
                        STATUS_CONTINUE -> { setTime(binding.countDownUp, "剩余:", timeArr) }
                        STATUS_SUCCESS -> { setTime(binding.countDownUnder, "距离4人免单剩余:", timeArr) }
                        else -> { }
                    }
                }
            }
        }
        val oneSecond: Long = 1000
        Timer().schedule(task, 0, oneSecond)
    }

    private fun setTime(layout: ItemCountDownBinding, title: String, timeArr: List<String>) {
        layout.tvTitle.text = title
        layout.tvHh.text = timeArr[0]
        layout.tvMm.text = timeArr[1]
        layout.tvSs.text = timeArr[2]
    }

    private fun setProgressLayout(userInfo: List<UserInfo>) {
        val layout = binding.progress
        if (!is4Back1){
            layout.line.visibility = View.GONE
            if (status == STATUS_SUCCESS) {
                binding.hintSuccess.visibility = View.VISIBLE
                binding.tvHintContent.visibility = View.VISIBLE
                binding.tvHintContent.text = "商家将尽快为您发货"
            } else if (status == STATUS_FAIL) {
                binding.tvHintContent.visibility = View.VISIBLE
                binding.tvHintContent.text = "原路退还您的金额"
            }
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        layout.recyclerView.layoutManager = manager
        layout.recyclerView.adapter = PinTuanProgressAdapter(is4Back1,userInfo)
    }

    private fun setInviteLayout(cashPeople: Int) {
        if ((status == STATUS_CONTINUE || (status == STATUS_SUCCESS && is4Back1)) && !isEndFree) {
            binding.invite.root.visibility = View.VISIBLE
            binding.invite.tvContent.text = colorText(cashPeople)
            binding.invite.btnInvite.setOnClickListener {
                if (this::dataBean.isInitialized){
                    doShare()
                }
            }
        }
    }

    private fun colorText(cashPeople: Int): SpannableStringBuilder {
        var builder0 = SpannableStringBuilder("")
        if(status == STATUS_CONTINUE) {
            builder0 = SpannableStringBuilder("还差1人成团，赶快邀请好友加入吧！")
            val color0 = ForegroundColorSpan(Color.parseColor("#F3594F"))
            builder0.setSpan(color0, 2, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            if (!is4Back1) {
                return builder0
            } else {
                builder0.append("\n")
            }
        }
        val builder1 = SpannableStringBuilder("再额外邀请" + cashPeople + "人即可享受免单优惠")
        val color1 = ForegroundColorSpan(Color.parseColor("#F3594F"))
        builder1.setSpan(color1, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return builder0.append(builder1)
    }

    private fun setProductInfoLayout(product: Product) {
        val layout = binding.productInfo
        layout.tvGoodsQuantity.visibility = View.GONE
        layout.tvStoreName.text = product.manufactureName
        layout.imgStoreIcon.load(product.manufactureHeadImg)
        layout.tvGoodsName.text = product.productName
        layout.imgGoods.load(product.skuPic)
        layout.tvGoodsSpec.text = "规格：${product.skuInfo}"
        layout.tvGoodsPrice.text = product.productPrice.toString()
    }

}