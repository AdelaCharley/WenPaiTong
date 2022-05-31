package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_merchant_qualifications.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.io.File

class MerchantQualificationsActivity : BaseActivity() {

    //var manfactureId = -999

    var images : ArrayList<String> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_qualifications)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("资质详情")
        //this.manfactureId = intent.getIntExtra("manfactureId",-999)
        this.images = intent.getStringArrayListExtra("images")
        StringUtils.log("manuidshi" + intent.getIntExtra("manfactureId",0))
        if (images == null){
            DialogUtil.toast(this,"出错了")
            return
        }
        adapter = ProductImageDetailAdapter(this, images)
        imageslist.adapter = adapter
        loadImageDetail(images!!)
        //imageslist.adapter = QualifilyAdapter(this,images)
        /*ApiManager.getInstance().getApiMallTest().loadManufactureQuantity(manfactureId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ManufactureQualifactionBean>{
                override fun onSubscribe(d: Disposable?) {

                }

                override fun onNext(t: ManufactureQualifactionBean?) {
                    Glide.with(this@MerchantQualificationsActivity).load(t?.data?.image).into(image)
                }

                override fun onError(e: Throwable?) {
                    DialogUtil.toast(this@MerchantQualificationsActivity,"获取数据出错")
                }

                override fun onComplete() {

                }

            })*/
    }

    lateinit var adapter : ProductImageDetailAdapter

    @SuppressLint("CheckResult")
    fun loadImageDetail(urls : ArrayList<String>){
        var count = 0
        for (i in urls.indices){
            Glide.with(this).load(urls.get(i)).downloadOnly(object : SimpleTarget<File?>() {
                override fun onResourceReady(resource: File, glideAnimation: Transition<in File?>?) {
                    count ++
                    if (count == urls.size){
                        if (this@MerchantQualificationsActivity::adapter.isInitialized){
                            adapter.refreshWithShow(true)
                        }
                    }
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                }
            })
        }
    }

}