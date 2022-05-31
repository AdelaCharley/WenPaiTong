package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.v3.MessageDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_physical_stores.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.MyPhysicalStoreBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VendorModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.amap.api.maps.MapsInitializer

import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.VendorDetailBean

class MyPhysicalStoresActivity : BaseActivity() {

    var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_physical_stores)
        changeToGreyButTranslucent()
        toolbar_back.setOnClickListener { finish() }
        loadData()
        MapsInitializer.updatePrivacyShow(this,true,true)
        MapsInitializer.updatePrivacyAgree(this,true)
    }

    fun initView( bean : MyPhysicalStoreBean){
        layout_store_manage.setOnClickListener {
            if (isOpen == 0){
                showOpenDialog()
                return@setOnClickListener
            }
            val intent = Intent(this,OpenStoreActivity::class.java)
            startActivity(intent)
        }
        layout_manage_image.setOnClickListener {
            if (isOpen == 0){
                showOpenDialog()
                return@setOnClickListener
            }
            if (status == 10){
                showAuditingDialog()
                return@setOnClickListener
            }
            val intent = Intent(this,ManageStoreAlbumActivity::class.java)
            startActivity(intent)
        }
        layout_manage_video.setOnClickListener {
            if (isOpen == 0){
                showOpenDialog()
                return@setOnClickListener
            }
            if (status == 10){
                showAuditingDialog()
                return@setOnClickListener
            }
            val intent = Intent(this,ManageStoreVideoActivity::class.java)
            startActivity(intent)
        }
        if (status == 10){
            physical_share.setOnClickListener {
                DialogUtil.showWarnDialog(this,"店铺正在审核中，请耐心等待")
            }
        }
        if (status == 20){
            if (!StringUtils.isEmpty(bean.data.vendorDetail.vendorHeadImg)){
                Glide.with(this).load(bean.data.vendorDetail.vendorHeadImg).into(physical_icon)
            }
            physical_share.setOnClickListener { showShareDialog() }
            todayusercount.setText(bean.data.analyzeData.todayScanUserCount.toString())
            weekusercount.setText(bean.data.analyzeData.weekScanUserCount.toString())
            totalusercount.setText(bean.data.analyzeData.totalScanUserCount.toString())
            imagecount.setText(bean.data.analyzeData.vendorPictureCount.toString())
            videocount.setText(bean.data.analyzeData.vendorVideoCount.toString())
        }
    }

    fun showShareDialog(){
        if (!this::myPhysicalStoreBean.isInitialized){
            return
        }
        DialogUtil.showSharePhysicalDialog(this,myPhysicalStoreBean)
    }

    fun doShare(index : Int){
        val bitmapoflayout = BitmapUtils.getCacheBitmapFromView(physical_icon)
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
        })
            .subscribeOn(AndroidSchedulers.mainThread())
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
                    webpage.webpageUrl = Constants.baseURL + "/equnshang/O2O/index?vendorId=" + VendorModel.getVendorId()
                    val msg = WXMediaMessage(webpage)
                    msg.title = "刚刚在群票看到一个不错的店铺，快来看看~"
                    msg.description = "刚刚在群票看到一个不错的店铺，快来看看~"
                    val thumbBmp =
                        bitmapoflayout
                    msg.thumbData = bytes
                    val req = SendMessageToWX.Req()
                    req.transaction = "req"
                    req.message = msg
                    if (index == 0){
                        req.scene = SendMessageToWX.Req.WXSceneSession
                    } else if (index == 1){
                        req.scene = SendMessageToWX.Req.WXSceneTimeline
                    }
                    MainActivity.api.sendReq(req)
                }
            }, object : Consumer<Throwable?> {
                @Throws(java.lang.Exception::class)
                override fun accept(throwable: Throwable?) {
                    DialogUtil.toast(this@MyPhysicalStoresActivity,"分享失败")
                }
            })
    }

    var isOpen = 0

    fun loadVendroDetail(){
        ApiManager.getInstance().getApiStore().loadVendorDetailData(VendorModel.getVendorId()).enqueue(object :
            retrofit2.Callback<VendorDetailBean> {
            override fun onResponse(
                call: Call<VendorDetailBean>,
                response: Response<VendorDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    DialogUtil.toast(this@MyPhysicalStoresActivity,response.body()!!.msg)
                    return
                }
                this@MyPhysicalStoresActivity.vendordetail = response.body()!!
            }

            override fun onFailure(call: Call<VendorDetailBean>, t: Throwable) {

            }

        })
    }

    var vendordetail : VendorDetailBean ? = null

    fun loadData(){
        ApiManager.getInstance().getApiStore().loadStoreInfo(UserInfoViewModel.getUserId()).enqueue(object : Callback<MyPhysicalStoreBean>{
            override fun onResponse(
                call: Call<MyPhysicalStoreBean>,
                response: Response<MyPhysicalStoreBean>
            ) {
                if (response.body() == null){
                    return
                }
                this@MyPhysicalStoresActivity.myPhysicalStoreBean = response.body()!!
                isOpen = response.body()!!.data.vendorDetail.isOpen
                if (isOpen == 0){
                    showOpenDialog()
                }
                this@MyPhysicalStoresActivity.status = response.body()!!.data.vendorDetail.status

                VendorModel.setVendorId(response.body()!!.data.vendorDetail.vendorId)
                if (status == 20){
                    loadVendroDetail()
                }
                initView(response.body()!!)
            }

            override fun onFailure(call: Call<MyPhysicalStoreBean>, t: Throwable) {

            }
        })
    }

    lateinit var myPhysicalStoreBean: MyPhysicalStoreBean

    fun showAuditingDialog(){
        DialogUtil.showWarnDialog(this,"店铺正在审核中，请耐心等待")
    }

    fun showOpenDialog(){
        MessageDialog.show(this, "提示", "您还还没有开店，是否要去开店", "确认", "取消")
            .setOkButton(OnDialogButtonClickListener() { baseDialog, v ->
                val intent = Intent(this,OpenStoreActivity::class.java)
                startActivity(intent)
                baseDialog.doDismiss()
                return@OnDialogButtonClickListener true
            })
    }

}