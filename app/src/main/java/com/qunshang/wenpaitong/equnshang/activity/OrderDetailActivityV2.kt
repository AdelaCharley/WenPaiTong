package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Bitmap

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialog.v3.MessageDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_detail_v2.*
import kotlinx.android.synthetic.main.item_group_list.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.OrderDetailBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiManager.Companion.getInstance
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import java.lang.Exception
import java.util.*

/*
class OrderDetailActivityV2 : BaseActivity() {

    var orderId = -999

    lateinit var orderDetailBean: OrderDetailBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_v2)
        orderId = intent.getIntExtra("id",-999)
        if (orderId == -999){
            DialogUtil.toast(this,"出错了")
            return
        }
        StringUtils.log("orderId是" + orderId)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("订单详情")
        init()
    }

    fun init(){
        if (this::timer.isInitialized){
            timer.cancel()
        }
        ApiManager.getInstance().getApiMallTest().loadOrderDetail(orderId.toString()).enqueue(object :
            Callback<OrderDetailBean> {
            override fun onResponse(call: Call<OrderDetailBean>, response: Response<OrderDetailBean>) {
                if (response.body() != null){
                    if (response.body()!!.code != 200){
                        return
                    }
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<OrderDetailBean>, t: Throwable) {

            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(str : String){}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (this::timer.isInitialized){
            timer.cancel()
        }
    }

    lateinit var timer : Timer

    fun initView(bean : OrderDetailBean){
        this.orderDetailBean = bean
        layout_operations.visibility = View.VISIBLE
        status_content.setText(bean.data.statusDesc)
        when (bean.data.orderStatus){
            10 -> {
                seegroupdetail.setVisibility(View.GONE)
                gotoinvite.setVisibility(View.GONE)
                applyexchangemoney.setVisibility(View.GONE)
                seeexpress.setVisibility(View.GONE)
                confrim.setVisibility(View.GONE)
                applyshouhou.setVisibility(View.GONE)
                buyagain.setVisibility(View.GONE)
                cancelorder.setVisibility(View.VISIBLE)
                gopay.setVisibility(View.VISIBLE)

                order_status.setText("待支付")
                timer = Timer()
                val task = object : TimerTask(){
                    override fun run() {
                        if (StringUtils.isEmpty(TimeUtil.getTimeRemainByDayString(bean.data.expiredTime))){
                            return
                        }
                        runOnUiThread {
                            status_content.setText("剩余" + TimeUtil.getTimeRemainByDayString(bean.data.expiredTime))
                        }
                    }
                }
                try {
                    timer.schedule(task,0,1000)
                } catch (e : Exception){
                    e.printStackTrace()
                }
                status_subcontent.visibility = View.GONE
            }
            20 -> {
                seegroupdetail.setVisibility(View.VISIBLE)
                gotoinvite.setVisibility(View.VISIBLE)
                applyexchangemoney.setVisibility(View.GONE)
                seeexpress.setVisibility(View.GONE)
                confrim.setVisibility(View.GONE)
                applyshouhou.setVisibility(View.GONE)
                buyagain.setVisibility(View.GONE)
                cancelorder.setVisibility(View.GONE)
                gopay.setVisibility(View.GONE)
                order_status.setText("待成团")
                status_subcontent.visibility = View.VISIBLE
                status_subcontent.setText("分享给小伙伴来拼单吧~")
            }
            30 -> {
                seegroupdetail.setVisibility(View.GONE)
                gotoinvite.setVisibility(View.GONE)
                applyexchangemoney.setVisibility(View.VISIBLE)
                seeexpress.setVisibility(View.GONE)
                confrim.setVisibility(View.GONE)
                applyshouhou.setVisibility(View.GONE)
                buyagain.setVisibility(View.GONE)
                cancelorder.setVisibility(View.GONE)
                gopay.setVisibility(View.GONE)
                order_status.setText("待发货")
                //status_content.setText("您的订单已经支付完成，等待商家发货")
            }
            40 -> {
                seegroupdetail.setVisibility(View.GONE)
                gotoinvite.setVisibility(View.GONE)
                applyexchangemoney.setVisibility(View.GONE)
                seeexpress.setVisibility(View.VISIBLE)
                confrim.setVisibility(View.VISIBLE)
                applyshouhou.setVisibility(View.GONE)
                buyagain.setVisibility(View.GONE)
                cancelorder.setVisibility(View.GONE)
                gopay.setVisibility(View.GONE)
                order_status.setText("待收货")
                //status_content.setText("您的订单已发货，等待送达")
            }
            50 -> {
                seegroupdetail.setVisibility(View.GONE)
                gotoinvite.setVisibility(View.GONE)
                applyexchangemoney.setVisibility(View.GONE)
                seeexpress.setVisibility(View.GONE)
                confrim.setVisibility(View.GONE)
                applyshouhou.setVisibility(View.VISIBLE)
                buyagain.setVisibility(View.GONE)
                cancelorder.setVisibility(View.GONE)
                gopay.setVisibility(View.GONE)
                order_status.setText("已完成")
                //status_content.setText("交易成功")
            }
            60 -> {
                seegroupdetail.setVisibility(View.GONE)
                gotoinvite.setVisibility(View.GONE)
                applyexchangemoney.setVisibility(View.GONE)
                seeexpress.setVisibility(View.GONE)
                confrim.setVisibility(View.GONE)
                applyshouhou.setVisibility(View.GONE)
                buyagain.setVisibility(View.VISIBLE)
                cancelorder.setVisibility(View.GONE)
                gopay.setVisibility(View.GONE)
                order_status.setText("已关闭")
                //status_content.setText("订单已经关闭")
            }
            70 -> {
                seegroupdetail.setVisibility(View.GONE)
                gotoinvite.setVisibility(View.GONE)
                applyexchangemoney.setVisibility(View.GONE)
                seeexpress.setVisibility(View.GONE)
                confrim.setVisibility(View.GONE)
                applyshouhou.setVisibility(View.GONE)
                buyagain.setVisibility(View.VISIBLE)
                cancelorder.setVisibility(View.GONE)
                gopay.setVisibility(View.GONE)
                order_status.setText("已取消")
                //status_content.setText("订单超时未支付")
            }
        }

        layout_product.setOnClickListener {
            val intent = Intent(this, AMallV3ProductDetailActivity::class.java)
            intent.putExtra("productId",bean.data.product.productId)
            startActivity(intent)
        }

        if (!StringUtils.isEmpty(bean.data.paymentTime)){
            layout_paytime.visibility = View.VISIBLE
            paytime.setText(bean.data.paymentTime)
        } else {
            layout_paytime.visibility = View.GONE
        }

        videoname.setText(bean.data.address.addressName)
        phone.setText(bean.data.address.addressPhone)
        address.setText(bean.data.address.addressSite)
        Glide.with(this).load(bean.data.manufacture.manufactureHeadImgUrl).into(store_image)
        storename.setText(bean.data.manufacture.manufactureName)
        Glide.with(this).load(bean.data.product.skuList.posterUrl).into(image_store)
        product_name.setText(bean.data.product.productName)
        product_spec.setText(bean.data.product.skuList.value)
        product_count.setText("x " + bean.data.product.skuList.number)
        product_price.setText("" + bean.data.product.skuList.price.toString())
        orderid.setText(bean.data.orderSn)
        time.setText(bean.data.createTime)

        productprice.setText("￥" + bean.data.product.skuList.number.toInt() * bean.data.product.skuList.price)

        discount.setText("-" + bean.data.orderDiscountAmount)
        coupon.setText("-" + "￥" + bean.data.orderCouponAmount)
        credit.setText("+" + bean.data.credit)
        if (bean.data.orderFreightAmount != 0.0){
            orderfright.setText("￥" + bean.data.orderFreightAmount.toString())
        } else {
            orderfright.setText("免运费")
        }
        remark.setText(bean.data.note)
        realprice.setText("￥" + bean.data.orderPayAmount.toString())
        realprice_hint.text = "共${bean.data.product.skuList.number}件 已优惠${bean.data.orderCouponAmount}元"
        addListenersToBottomButtons()
        //realprice.setText("￥" + bean.data.product.skuList.number.toInt() * bean.data.product.skuList.price)
        root.visibility = View.VISIBLE
    }
    
    fun addListenersToBottomButtons(){
        seegroupdetail.setOnClickListener(View.OnClickListener { seeGroupDetail() })
        gotoinvite.setOnClickListener(View.OnClickListener { goToInvite() })
        if (!StringUtils.isEmpty(orderDetailBean.data.afterSaleSn)){
            applyexchangemoney.setText("退款详情")
        }
        applyexchangemoney.setOnClickListener(View.OnClickListener {
            applyExchangeMoney(

            )
        })
        seeexpress.setOnClickListener(View.OnClickListener { seeExpress() })
        confrim.setOnClickListener(View.OnClickListener { confirm() })
        if (!StringUtils.isEmpty(orderDetailBean.data.afterSaleSn)){
            applyshouhou.setText("售后详情")
        }
        applyshouhou.setOnClickListener(View.OnClickListener { applyShouHou() })
        buyagain.setOnClickListener(View.OnClickListener { buyAgain() })
        cancelorder.setOnClickListener(View.OnClickListener { cancelOrder() })
        gopay.setOnClickListener(View.OnClickListener { goPay() })
    }

    fun goPay() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        val intent = Intent(this, GoPayActivityV2::class.java)
        intent.putExtra("price", orderDetailBean.data.orderPayAmount.toString())
        intent.putExtra("orderId", orderDetailBean.data.orderId.toString())
        intent.putExtra("orderType", "amall")
        startActivity(intent)
    }

    fun cancelOrder() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        MessageDialog.show(this, "", "确认取消订单？", "取消订单", "再想想")
            .setOkButton { baseDialog, v ->
                getInstance().getApiMallTest()
                    .cancelOrder(orderDetailBean.data.orderId.toString())
                    .enqueue(object : Callback<BaseHttpBean<Int>> {
                        override fun onResponse(
                            call: Call<BaseHttpBean<Int>>,
                            response: Response<BaseHttpBean<Int>>
                        ) {
                            if (response.body() != null) {
                                if (response.body()!!.code == 200) {
                                    Toast.makeText(this@OrderDetailActivityV2, "取消订单成功", Toast.LENGTH_SHORT).show()
                                    EventBus.getDefault().post("refresh")
                                    init()
                                } else {
                                    Toast.makeText(
                                        this@OrderDetailActivityV2,
                                        "取消失败" + response.body()!!.msg,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<BaseHttpBean<Int>>,
                            t: Throwable
                        ) {
                        }
                    })
                false
            }
    }

    fun buyAgain() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        val intent = Intent(this, AMallV3ProductDetailActivity::class.java)
        intent.putExtra("productId", orderDetailBean.data.product.productId)
        startActivity(intent)
    }

    fun applyShouHou() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        if (!StringUtils.isEmpty(orderDetailBean.data.afterSaleSn)){
            val intent = Intent(this, AfterSaleDetailActivity::class.java)
            intent.putExtra("afterSaleSn", orderDetailBean.data.getAfterSaleSn())
            startActivity(intent)
            return
        }
        val intent = Intent(this, ApplyAfterSaleActivity::class.java)
        intent.putExtra("orderId", orderDetailBean.data.orderId)
        startActivity(intent)
    }

    fun confirm() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        MessageDialog.show(this, "", "确定已收到货？", "确认", "取消")
            .setOkButton { baseDialog, v ->
                getInstance().getApiMallTest()
                    .confirmReceipt(orderDetailBean.data.orderId.toString())
                    .enqueue(object : Callback<BaseHttpBean<Int>> {
                        override fun onResponse(
                            call: Call<BaseHttpBean<Int>>,
                            response: Response<BaseHttpBean<Int>>
                        ) {
                            baseDialog.doDismiss()
                            if (response.body() != null) {
                                if (response.body()!!.code == 200) {
                                    Toast.makeText(
                                        this@OrderDetailActivityV2,
                                        "确认收货成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    init()
                                    EventBus.getDefault().post("refresh")
                                } else {
                                    Toast.makeText(
                                        this@OrderDetailActivityV2,
                                        "确认收货失败" + response.body()!!.msg,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<BaseHttpBean<Int>>,
                            t: Throwable
                        ) {
                        }
                    })
                true
            }
    }

    fun seeExpress() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        val intent = Intent(this, ExpressDetailActivityV2::class.java)
        intent.putExtra("orderId",orderDetailBean.data.orderId)
        intent.putExtra("type", "amall")
        startActivity(intent)
    }

    fun applyExchangeMoney() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        if (!StringUtils.isEmpty(orderDetailBean.data.afterSaleSn)){
            val intent = Intent(this, AfterSaleDetailActivity::class.java)
            intent.putExtra("afterSaleSn", orderDetailBean.data.afterSaleSn)
            startActivity(intent)
            return
        }
        val intent = Intent(this, DoApplyAfterSaleActivity::class.java)
        intent.putExtra("orderId", orderId)
        intent.putExtra("type", 10)
        startActivity(intent)
    }

    fun seeGroupDetail() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        val intent = Intent(this, PinTuanDetailActivity::class.java)
        intent.putExtra("orderGroupSn",orderDetailBean.data.orderGroupSn)
        startActivity(intent)
    }

    fun goToInvite() {
        if (!this::orderDetailBean.isInitialized){
            return
        }
        Observable.create(ObservableOnSubscribe <Bitmap?> { e ->
            Glide.with(this)
                .asBitmap()
                .load(orderDetailBean.data.product.skuList.posterUrl)
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
                        webpage.webpageUrl = Constants.WECHAT_BASE_URL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderDetailBean.data.orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    } else {
                        webpage.webpageUrl = Constants.WECHAT_BASE_URL + "/equnshangTest/Amall/groupDetail?orderGroupSn=" + orderDetailBean.data.orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    }
                    //webpage.webpageUrl = Constants.WECHAT_BASE_URL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderDetailBean.data.orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                    val msg = WXMediaMessage(webpage)
                    msg.title = "我" + orderDetailBean.data.product.skuList.price + "元拼了个" + orderDetailBean.data.product.productName
                    msg.description = "我买了\"" + orderDetailBean.data.product.skuList.value + "\"规格"
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
                    DialogUtil.toast(this@OrderDetailActivityV2,"分享失败")
                }
            })
    }

}*/
