package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_prize_info_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.PrizeInfoPartamerAdapter
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.data.PrizeInfoDetailBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class PrizeInfoDetailActivity : BaseActivity() {

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    var prizeId = -999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_info_detail)
        prizeId = intent.getIntExtra("prizeId",-999)
        if (prizeId < 0){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("奖品")
        banner = findViewById(R.id.banner)
        ApiManager.getInstance().getApiLotteryTest().loadPrizeInfo(prizeId).enqueue(object : Callback<PrizeInfoDetailBean>{
            override fun onResponse(call: Call<PrizeInfoDetailBean>, response: Response<PrizeInfoDetailBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    DialogUtil.toast(this@PrizeInfoDetailActivity,response.body()!!.msg)
                    return
                }
                initView(response.body()!!)
            }

            override fun onFailure(call: Call<PrizeInfoDetailBean>, t: Throwable) {

            }

        })
    }

    fun initView(bean : PrizeInfoDetailBean){
        var images = filterList(bean.data.swiperImageUrl.split(",").toList())
        val adapter : BannerImageAdapter<String> = object : BannerImageAdapter<String>(images){
            override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
                Glide.with(holder!!.itemView)
                    .load(data)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }
        banner
            .setAdapter(adapter)
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))
        prize_price.setText("￥" + bean.data.marketPrice + "起")
        prize_name.setText(bean.data.description)
        list_attributes.adapter = PrizeInfoPartamerAdapter(this,bean.data.parameter)
        if (bean.data.experienceInfo == null){
            headimage.visibility = View.GONE
            username.visibility = View.GONE
            content.visibility = View.GONE
            experience_image.visibility = View.GONE
            noexperience.visibility = View.VISIBLE
        } else if (bean.data.experienceInfo.experienceId == 0){
            headimage.visibility = View.GONE
            username.visibility = View.GONE
            content.visibility = View.GONE
            Log.i(Constants.logtag,"啊啊啊为空啊")
            experience_image.visibility = View.GONE
            noexperience.visibility = View.VISIBLE
        } else {


            //也有可能这个新的不为空，但是experience为0

            Glide.with(this).load(bean.data.experienceInfo.headImageUrl).into(headimage)
            username.setText(bean.data.experienceInfo.userName)
            content.setText(bean.data.experienceInfo.content)
            Glide.with(this).load(bean.data.experienceInfo.imagUrl).into(experience_image)

            headimage.setOnClickListener {
                goExperienceDetail(bean.data.experienceInfo.experienceId)
            }
            username.setOnClickListener {
                goExperienceDetail(bean.data.experienceInfo.experienceId)
            }
            content.setOnClickListener {
                goExperienceDetail(bean.data.experienceInfo.experienceId)
            }
            experience_image.setOnClickListener {
                goExperienceDetail(bean.data.experienceInfo.experienceId)
            }
            layout_experience.setOnClickListener {
                val intent = Intent(this,AllExperienceActivity::class.java)
                intent.putExtra("prizeId",prizeId)
                startActivity(intent)
            }
        }
        var detailimages = filterList(bean.data.introduceImageUrl.split(",").toList())
        detailadapter = ProductImageDetailAdapter(this, detailimages)
        list_image_detail.adapter = detailadapter
        detailadapter.refreshWithShow(true)
    }

    lateinit var detailadapter: ProductImageDetailAdapter

    fun goExperienceDetail(experienceId : Int){
        val intent = Intent(this, ExperienceActivity::class.java)
        intent.putExtra("experienceid",experienceId)
        startActivity(intent)
    }

    fun filterList(list : List<String>) : List<String>{
        val newlist = ArrayList<String>()
        for ( i in list.indices){
            if (!com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(list.get(i))){
                newlist.add(list.get(i))
            }
        }
        return newlist
    }

}