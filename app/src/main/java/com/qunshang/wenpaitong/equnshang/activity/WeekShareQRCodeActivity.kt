package com.qunshang.wenpaitong.equnshang.activity

import android.graphics.Bitmap

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.wasabeef.glide.transformations.CropSquareTransformation
import kotlinx.android.synthetic.main.activity_week_share_qrcode.bg
import kotlinx.android.synthetic.main.activity_week_share_qrcode.friendcircle
import kotlinx.android.synthetic.main.activity_week_share_qrcode.iv_share_qrcode
import kotlinx.android.synthetic.main.activity_week_share_qrcode.layout
import kotlinx.android.synthetic.main.activity_week_share_qrcode.wechat
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import uni.yzq.zxinglibrary.encode.CodeCreator

class WeekShareQRCodeActivity : BaseActivity() {

    var str = Constants.baseURL

    val TYPE_WECHAT_SESSION = -985

    val TYPE_WECHAT_FRIENDCIRCLE = -659

    //首页保存海报，mode为0，商品分享详情的mode是3，bmall分享是4，今日抽奖是1，每周任务是2

    fun initView(){
        bg.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (!isInited){
                    val bgwidth = bg.width  //获取背景图片的宽度
                    val layoutParams = iv_share_qrcode.layoutParams as ViewGroup.MarginLayoutParams //593  357
                    StringUtils.log("背景宽度是" + bgwidth)
                    val percent : Double = 193.0/494.0
                    layoutParams.width = (bgwidth * percent).toInt()
                    layoutParams.height = (bgwidth * percent).toInt()

                    val marginbottompercent = 133.0/193.0
                    layoutParams.setMargins(0,0,0,(marginbottompercent * layoutParams.width).toInt())
                    iv_share_qrcode.layoutParams = layoutParams

                    isInited = true
                    //checkIsInited()
                }
            }
        })
    }

    var isInited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_share_qrcode)
        toolbar_back.setOnClickListener { finish() }
        initView()
        val time = System.currentTimeMillis()
        val mode = 2
        val invitecode = UserInfoViewModel.getUserInfo()!!.invitecode
        toolbar_title.setText("分享")
        str = Constants.baseURL + "/web/register.html?time=" + time + "&&mode=" + mode + "&&invitecode=" + invitecode
        Glide.with(this).asBitmap().load(UserInfoViewModel.getUserInfo()!!.headimage)
            .transform(CropSquareTransformation())
            .into(object :
                SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmap = CodeCreator.createQRCode(str, 400, 400, resource)
                    iv_share_qrcode.setImageBitmap(bitmap)
                }
            })
        wechat.setOnClickListener { share(TYPE_WECHAT_SESSION) }
        friendcircle.setOnClickListener { share(TYPE_WECHAT_FRIENDCIRCLE) }
    }

    fun share(type : Int){
        val bitmapoflayout = BitmapUtils.getCacheBitmapFromView(layout)
        Observable
                .create(ObservableOnSubscribe<Bitmap?> { e ->
            Glide.with(this)
                .asBitmap()
                .load(bitmapoflayout)
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
        }).
                subscribeOn(AndroidSchedulers.mainThread())
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

                    val imgObj = WXImageObject(bitmapoflayout)
                    val msg = WXMediaMessage()
                    msg.mediaObject = imgObj
                    val data = BitmapUtils.bmpToByteArray(bitmapoflayout,false)
                    msg.thumbData = bytes
                    val req = SendMessageToWX.Req()
                    req.transaction = "req"
                    req.message = msg
                    if (type == TYPE_WECHAT_SESSION){
                        req.scene = SendMessageToWX.Req.WXSceneSession
                    } else if (type == TYPE_WECHAT_FRIENDCIRCLE){
                        req.scene = SendMessageToWX.Req.WXSceneTimeline
                    }

                    MainActivity.api.sendReq(req)
                }
            }, object : Consumer<Throwable?> {
                @Throws(java.lang.Exception::class)
                override fun accept(throwable: Throwable?) {
                    //DialogUtil.toast(this@WeekShareQRCodeActivity,"分享失败")
                }
            })
    }

}