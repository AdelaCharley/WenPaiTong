package com.qunshang.wenpaitong.equnshang.fragment

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_qr_code_share.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import uni.yzq.zxinglibrary.encode.CodeCreator

class QrCodeShareFragment(val isShowShare : Boolean = false) : BaseShareFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_code_share, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (share != null){
            share?.visibility = View.VISIBLE
        }
    }

    override fun getRootBitmap(): Bitmap? {
        return BitmapUtils.getCacheBitmapFromView(layout)
    }

    fun initViewCommon(){
        bg.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (!isInited){
                    val bgwidth = bg.width  //获取背景图片的宽度
                    val layoutParams = iv_share_qrcode.layoutParams as ViewGroup.MarginLayoutParams //593  357
                    StringUtils.log("背景宽度是" + bgwidth)
                    val percent : Double = 350.0/593.0
                    layoutParams.width = (bgwidth * percent).toInt()
                    layoutParams.height = (bgwidth * percent).toInt()

                    val marginbottompercent = 201.0/350.0
                    layoutParams.setMargins(0,0,0,(marginbottompercent * layoutParams.width).toInt())
                    iv_share_qrcode.layoutParams = layoutParams


                    val time = System.currentTimeMillis()

                    val mode = 0
                    val invitecode = UserInfoViewModel.getUserInfo()!!.invitecode

                    val str = Constants.baseURL + "/web/register.html?time=" + time + "&&mode=" + mode + "&&invitecode=" + invitecode

                    Glide.with(requireContext()).asBitmap().load(UserInfoViewModel.getUserInfo()!!.headimage)
                        .transform(CropSquareTransformation())
                        .into(object :
                            SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val bitmap = CodeCreator.createQRCode(str, 400, 400, resource)
                                iv_share_qrcode.setImageBitmap(bitmap)
                                isQrCodeInited = true
                            }
                        })

                    isInited = true
                    //checkIsInited()
                }
            }
        })
    }

    var isInited = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewCommon()
        if (isShowShare){
            share?.visibility = View.GONE
        }
        share.setOnClickListener {
            share.visibility = View.GONE
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
                        val data = BitmapUtils.bmpToByteArray(bitmapoflayout,false)
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
                        DialogUtil.toast(requireContext(),"分享失败")
                    }
                })
        }
    }

}