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
import kotlinx.android.synthetic.main.activity_receive_prize_to_flaunt.*
import kotlinx.android.synthetic.main.activity_receive_prize_to_flaunt.bg
import kotlinx.android.synthetic.main.activity_receive_prize_to_flaunt.friendcircle
import kotlinx.android.synthetic.main.activity_receive_prize_to_flaunt.layout
import kotlinx.android.synthetic.main.activity_receive_prize_to_flaunt.wechat
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import uni.yzq.zxinglibrary.encode.CodeCreator

class ReceivePrizeToFlauntActivity : BaseActivity() {

    var str = Constants.baseURL

    val TYPE_WECHAT_SESSION = -985

    val TYPE_WECHAT_FRIENDCIRCLE = -659

    fun initView(){
        bg.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (!isInited){
                    val bgwidth = bg.width  //???????????????????????????
                    val layoutParams = qrcode.layoutParams as ViewGroup.MarginLayoutParams
                    StringUtils.log("???????????????" + bgwidth)
                    val percent : Double = 144.0/465.0
                    layoutParams.width = (bgwidth * percent).toInt()
                    layoutParams.height = (bgwidth * percent).toInt()

                    val marginbottompercent = 95.0/144.0
                    layoutParams.setMargins(0,0,0,(marginbottompercent * layoutParams.width).toInt())
                    qrcode.layoutParams = layoutParams


                    val layoutParamsOfHead = userheadimage.layoutParams as ViewGroup.MarginLayoutParams
                    StringUtils.log("???????????????" + bgwidth)
                    val percentofuserhead : Double = 105.0/465.0
                    layoutParamsOfHead.width = (bgwidth * percentofuserhead).toInt()
                    layoutParamsOfHead.height = (bgwidth * percentofuserhead).toInt()

                    val margintoppercent = 35.0/105.0
                    layoutParamsOfHead.setMargins(0,(margintoppercent * layoutParamsOfHead.width).toInt(),0,0)
                    userheadimage.layoutParams = layoutParamsOfHead

                    val layoutParamsOfText = username.layoutParams as ViewGroup.MarginLayoutParams
                    val percentOfHead = 150.0/610.0
                    layoutParamsOfText.setMargins(0,(percentOfHead * bg.height).toInt(),0,0)
                    username.layoutParams = layoutParamsOfText
                    isInited = true
                }
            }
        })
    }

    var isInited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_prize_to_flaunt)
        toolbar_back.setOnClickListener { finish() }
        initView()
        val time = System.currentTimeMillis()
        val mode = 2
        val invitecode = UserInfoViewModel.getUserInfo()!!.invitecode
        str = Constants.baseURL + "/web/register.html?time=" + time + "&&mode=" + mode + "&&invitecode=" + invitecode
        toolbar_title.setText("??????")
        Glide.with(this).load(UserInfoViewModel.getUserInfo()!!.headimage).into(userheadimage)
        username.setText("????????????" + UserInfoViewModel.getUserInfo()!!.uname + "????????????")
        Glide.with(this).asBitmap().load(UserInfoViewModel.getUserInfo()!!.headimage)
            .transform(CropSquareTransformation())
            .into(object :
                SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmap = CodeCreator.createQRCode(str, 400, 400, resource)
                    qrcode.setImageBitmap(bitmap)
                }
            })
        wechat.setOnClickListener { share(TYPE_WECHAT_SESSION) }
        friendcircle.setOnClickListener { share(TYPE_WECHAT_FRIENDCIRCLE) }
    }

    fun share(type : Int){
        val bitmapoflayout = BitmapUtils.getCacheBitmapFromView(layout)
        Observable.create(ObservableOnSubscribe<Bitmap?> { e ->
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

                    val imgObj = WXImageObject(bitmapoflayout)
                    val msg = WXMediaMessage()
                    msg.mediaObject = imgObj
                    //val data = BitmapUtils.bmpToByteArray(bitmapoflayout,false)
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
                    DialogUtil.toast(this@ReceivePrizeToFlauntActivity,"????????????")
                }
            })
    }

}