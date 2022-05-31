package com.qunshang.wenpaitong.equnshang.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialog.v3.CustomDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.activity.ToBeVipOrPartnerActivity
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean
import com.qunshang.wenpaitong.equnshang.interfaces.OnClickDetailListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.wxapi.WXEntryActivity

class KTUtil {

    companion object {

        fun filterList(list : List<String>) : List<String>{
            val newlist = ArrayList<String>()
            for ( i in list.indices){
                if (!StringUtils.isEmpty(list.get(i))){
                    newlist.add(list.get(i))
                }
            }
            return newlist
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        fun def(str : String){}

        fun loadPersonalInfo(context: Context) {
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this)
            }
            var defaultUserId = UserInfoViewModel.getUserId()

            val localUserId = UserHelper.getCurrentLoginUser(context)
            if (!StringUtils.isEmpty(localUserId)){
                defaultUserId = localUserId
            }

            if (!StringUtils.isEmpty(defaultUserId)){

                UserInfoViewModel.setUserId(defaultUserId)
                StringUtils.log("当前已将userId设置为" + UserInfoViewModel.getUserId())
                ApiManager.getInstance().getApiMainTest()
                    .loadPersonalInfo(UserInfoViewModel.getUserId()).enqueue(object :
                        Callback<UserMsgBean> {
                        override fun onResponse(
                            call: Call<UserMsgBean>,
                            response: Response<UserMsgBean>
                        ) {
                            if (response.body() == null){
                                return
                            }
                            val userInfo = response.body()!!.data
                            UserInfoViewModel.setUserInfo(userInfo)
                            EventBus.getDefault().post("vipbuyrefresh")
                        }
                        override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                        }
                    })
            }
        }

        fun doWeChatShareImage(context: Context,bitmap : Bitmap,type : Int = 1){
            Observable.create(ObservableOnSubscribe<Bitmap?> { e ->
                Glide.with(context)
                    .asBitmap()
                    .load(bitmap)
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
                .map(object : Function<Bitmap?, ByteArray>{
                    override fun apply(bitmap: Bitmap?): ByteArray {
                        return BitmapUtils.bmpToByteArray(bitmap, 32)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<ByteArray?> {
                    @Throws(java.lang.Exception::class)
                    override fun accept(bytes: ByteArray?) {

                        val imgObj = WXImageObject(bitmap)
                        val msg = WXMediaMessage()
                        msg.mediaObject = imgObj
                        val data = BitmapUtils.bmpToByteArray(bitmap,false)
                        msg.thumbData = bytes
                        val req = SendMessageToWX.Req()
                        req.transaction = "req"
                        req.message = msg
                        req.scene = SendMessageToWX.Req.WXSceneSession
                        if (type == 2){
                            req.scene = SendMessageToWX.Req.WXSceneTimeline
                        }
                        MainActivity.api.sendReq(req)
                    }
                }, object : Consumer<Throwable?> {
                    @Throws(java.lang.Exception::class)
                    override fun accept(throwable: Throwable?) {
                        DialogUtil.toast(context,"分享失败")
                    }
                })
        }

        fun doWeChatShare(context : Context,imageurl : String?,url : String?//相对URL，不包括BaseURL
                          ,title : String?,desc : String?,isIncludeBaseURL : Boolean = false){
            Observable.create(ObservableOnSubscribe <Bitmap?> { e ->
                Glide.with(context)
                    .asBitmap()
                    .load(imageurl)
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
                        if (isIncludeBaseURL){
                            webpage.webpageUrl = url
                        } else {
                            webpage.webpageUrl = Constants.baseURL + url
                        }

                        val msg = WXMediaMessage(webpage)
                        msg.title = title
                        msg.description = desc
                        msg.thumbData = bytes
                        val req = SendMessageToWX.Req()
                        req.transaction = "req"
                        req.message = msg
                        req.scene = SendMessageToWX.Req.WXSceneSession
                        MainActivity.api.sendReq(req)
                    }
                }, object : Consumer<Throwable?> {
                    @Throws(Exception::class)
                    override fun accept(throwable: Throwable?) {
                        DialogUtil.toast(context,"分享失败")
                    }
                })
        }

        fun showGoVipDayDialog(activity: AppCompatActivity,listener : OnClickDetailListener){
            CustomDialog.show(
                activity, R.layout.dialog_partner
            ) { dialog, v -> //dialog.doDismiss();
                var image = v.findViewById<ImageView>(R.id.bg)
                val knowdetail = v.findViewById<ImageView>(R.id.knowdetail)
                if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                    image.setImageDrawable(activity.getDrawable(R.mipmap.vipday_vip))
                    knowdetail.setOnClickListener {
                        dialog.doDismiss()
                        /*val intent = Intent(activity, VipDayActivity::class.java)
                        intent.putExtra("activityId", LotteryFragment.activityId)
                        activity.startActivity(intent)*/
                        listener.onClick()
                    }
                } else {
                    image.setImageDrawable(activity.getDrawable(R.mipmap.vipday_fan))
                    knowdetail.setOnClickListener {
                        dialog.doDismiss()
                        val intent = Intent(activity, ToBeVipOrPartnerActivity::class.java)
                        activity.startActivity(intent)
                    }
                }
            }.setCancelable(false)
        }

        fun doShare(context : Context,url : String,orderGroupSn : String,price : Double,productName : String,sku : String){
            Observable.create(ObservableOnSubscribe <Bitmap?> { e ->
                Glide.with(context)
                    .asBitmap()
                    .load(url)
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
                        //webpage.webpageUrl = Constants.baseURL + "/equnshang/Amall/groupDetail?orderGroupSn=" + orderGroupSn + "&userId=" + UserInfoViewModel.getUserId()
                        val msg = WXMediaMessage(webpage)
                        msg.title = "我" + price + "元拼了个" + productName
                        msg.description = "我买了\"" + sku + "\"规格"
                        msg.thumbData = bytes
                        val req = SendMessageToWX.Req()
                        req.transaction = "req"
                        req.message = msg
                        req.scene = SendMessageToWX.Req.WXSceneSession
                        if (WXEntryActivity.api != null){
                            WXEntryActivity.api.sendReq(req)
                        }
                    }
                }, object : Consumer<Throwable?> {
                    @Throws(java.lang.Exception::class)
                    override fun accept(throwable: Throwable?) {
                        StringUtils.log(throwable?.message)
                        DialogUtil.toast(context,"分享失败")
                    }
                })
        }

    }

}