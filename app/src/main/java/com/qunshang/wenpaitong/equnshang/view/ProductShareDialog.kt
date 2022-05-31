package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lxj.xpopup.core.BottomPopupView
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
import kotlinx.android.synthetic.main.dialog_product_share.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ProductShareDialog(context: Context,val bean : ProductBeanV2) : BottomPopupView(context) {

    val TYPE_WECHAT_SESSION = -985

    val TYPE_WECHAT_FRIENDCIRCLE = -659

    override fun onCreate() {
        super.onCreate()
        Glide.with(context).load(bean.data.skuList.get(0).image).into(image)
        name.setText(bean.data.productName)
        price.setText("￥" + bean.data.skuList.get(0).vipGroupPrice)
        var time = System.currentTimeMillis()
        var mode = 3
        var invitecode = UserInfoViewModel.getUserInfo()!!.invitecode
        val str = Constants.baseURL + "/web/register.html?time=" + time + "&&mode=" + mode + "&&invitecode=" + invitecode
        Glide.with(this).asBitmap().load(UserInfoViewModel.getUserInfo()!!.headimage)
            .transform(CropSquareTransformation())
            .into(object :
                SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmap = uni.yzq.zxinglibrary.encode.CodeCreator.createQRCode(str, 400, 400, resource)
                    iv_share_qrcode.setImageBitmap(bitmap)
                }
            })
        wechat.setOnClickListener { share(TYPE_WECHAT_SESSION) }
        friendcircle.setOnClickListener { share(TYPE_WECHAT_FRIENDCIRCLE) }
    }

    fun share(type : Int){
        val bitmapoflayout = BitmapUtils.getCacheBitmapFromView(root)
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
                    //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this@ReceivePrizeToFlauntActivity,"分享完毕")
                }
            }, object : Consumer<Throwable?> {
                @Throws(java.lang.Exception::class)
                override fun accept(throwable: Throwable?) {
                    DialogUtil.toast(context,"分享失败")
                }
            })
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_product_share
    }


}