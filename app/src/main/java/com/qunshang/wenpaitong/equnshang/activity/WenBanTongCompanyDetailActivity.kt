package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Color

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_wen_ban_tong_company_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.WenBanTongCompanyDetailBean
import com.qunshang.wenpaitong.equnshang.fragment.WenBanTongAskFragment
import com.qunshang.wenpaitong.equnshang.fragment.WenBanTongShopFragment
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongCompanyDetailActivity : BaseActivity() {

    var companyId = 0

    lateinit var askFragment : WenBanTongAskFragment

    lateinit var shopFragment: WenBanTongShopFragment

    var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wen_ban_tong_company_detail)
        changeToGreyButTranslucent()
        this.companyId = intent.getIntExtra("companyId",0)
        askFragment = WenBanTongAskFragment(companyId)
        shopFragment = WenBanTongShopFragment(companyId,false)
        toolbar_back.setOnClickListener { finish() }
        initView()
    }

    fun initView(){
        layout1.setOnClickListener {
            showAsk()
        }
        layout2.setOnClickListener {
            showShop()
        }
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadWenBanTongCompanyDetailInfo(companyId).enqueue(object : Callback<WenBanTongCompanyDetailBean>{
            override fun onResponse(
                call: Call<WenBanTongCompanyDetailBean>,
                response: Response<WenBanTongCompanyDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                root.visibility = View.VISIBLE
                val data = response.body()!!.data
                Glide.with(this@WenBanTongCompanyDetailActivity).load(data.companyPoster).into(bg)
                Glide.with(this@WenBanTongCompanyDetailActivity).load(data.companyLogo).into(logo)
                name.setText(data.companyName)
                phone.setText(data.companyPhone)
                desc.setText(data.companyDesc)
                layout4.setOnClickListener {
                    val intent = Intent(this@WenBanTongCompanyDetailActivity,MerchantQualificationsActivity::class.java)
                    intent.putStringArrayListExtra("images",data.companyLicence)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<WenBanTongCompanyDetailBean>, t: Throwable) {

            }

        })
        showAsk()
    }


    fun showAsk(){
        if (index != 0){
            index = 0
            text1.setTextColor(Color.parseColor("#ffa98f60"))
            view1.visibility = View.GONE
            text2.setTextColor(resources.getColor(R.color.text_grey_heavy))
            view2.visibility = View.GONE
            if (shopFragment.isAdded && !shopFragment.isHidden) {
                supportFragmentManager.beginTransaction().hide(shopFragment).commit()
            }
            if (askFragment.isAdded){
                supportFragmentManager.beginTransaction().show(askFragment).commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.container,askFragment).commit()
            }
        }
    }

    fun showShop() {
        if (index != 1){
            index = 1
            text2.setTextColor(Color.parseColor("#ffa98f60"))
            view2.visibility = View.GONE
            text1.setTextColor(resources.getColor(R.color.text_grey_heavy))
            view1.visibility = View.GONE
            if (askFragment.isAdded && !askFragment.isHidden) {
                supportFragmentManager.beginTransaction().hide(askFragment).commit()
            }
            if (shopFragment.isAdded){
                supportFragmentManager.beginTransaction().show(shopFragment).commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.container,shopFragment).commit()
            }
        }
    }

}