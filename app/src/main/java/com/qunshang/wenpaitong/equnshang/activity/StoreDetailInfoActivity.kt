package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Color

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_store_detail_info.*
import kotlinx.android.synthetic.main.activity_store_detail_info.concern
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.ManufactureV2Bean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class StoreDetailInfoActivity : BaseActivity (){

    var bean : ManufactureV2Bean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarColor(R.color.qiangrey)
        setContentView(R.layout.activity_store_detail_info)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("")
        root_toolbar.setBackgroundColor(Color.parseColor("#f7f7f7"))
        bean = intent.getSerializableExtra("info") as ManufactureV2Bean
        StringUtils.log("manu" + bean?.data?.manufactureId)
        if (bean == null){
            return
        }
        initView()
    }

    fun initView(){
        if (bean == null){
            return
        }
        if (bean!!.data.isFocus != 0){
            concern.setText("取消关注")
            img_add.visibility = View.GONE
        }
        layout_concern.setOnClickListener {
            ApiManager.getInstance().getApiPersonalTest().focusMan(UserInfoViewModel.getUserId(),bean!!.data.manufactureId.toString())
                .enqueue(object : Callback<BaseHttpBean<Boolean>> {
                    override fun onResponse(
                        call: Call<BaseHttpBean<Boolean>>,
                        response: Response<BaseHttpBean<Boolean>>
                    ) {
                        if (response.body() == null){
                            return
                        }
                        if (response.body()!!.data!!) {
                            concern.setText("关注")
                            img_add.visibility = View.VISIBLE
                            this@StoreDetailInfoActivity.bean!!.data.isFocus = 0
                        } else {
                            concern.setText("取消关注")
                            img_add.visibility = View.GONE
                            this@StoreDetailInfoActivity.bean!!.data.isFocus = 200
                        }
                        val followChangeBean = ManufactureChangeBean()
                        followChangeBean.setId(bean!!.data.manufactureId)
                        followChangeBean.isFollow = response.body()!!.data!!
                        EventBus.getDefault().post(followChangeBean)
                    }

                    override fun onFailure(call: Call<BaseHttpBean<Boolean>>, t: Throwable) {}
                })
        }
        Glide.with(this).load(bean!!.data.manufactureLogoUrl).into(image_storer)
        name.setText(bean!!.data.manufactureName)
        total_count.setText("商品:" + bean!!.data.productCount.toString() + "件")
        opendate.setText(bean!!.data.openDate)
        location.setText(bean!!.data.location)
        phone.setText(bean!!.data.servicePhone)
        desc.setText(bean!!.data.desc)
        layout_zizhi.setOnClickListener {
            val intent = Intent(this,MerchantQualificationsActivity::class.java)
            intent.putStringArrayListExtra("images",bean!!.data.allBusinessLicence)
            StringUtils.log(bean!!.data.allBusinessLicence.size.toString() + "ge images")
            intent.putExtra("manfactureId",bean!!.data.manufactureId)
            startActivity(intent)
        }
    }

}