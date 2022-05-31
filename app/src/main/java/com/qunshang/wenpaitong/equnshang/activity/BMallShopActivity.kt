package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle

import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_bmall_shop.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BMallProductAdapter
import com.qunshang.wenpaitong.equnshang.data.ABMallBannerBean
import com.qunshang.wenpaitong.equnshang.data.BMallProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class BMallShopActivity : BaseActivity() {

    lateinit var banner: Banner<ABMallBannerBean.DataBean, BannerImageAdapter<ABMallBannerBean.DataBean>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmall_shop)
        banner = findViewById(R.id.banner)
        search_layoout.setOnClickListener {
            val intent = Intent(this,BMallSearchActivity::class.java)
            startActivity(intent)
        }
        toolbar_back.setOnClickListener { finish() }
        mycaigoulist.setOnClickListener {
            val intent = Intent(this,BMallOrdersActivity::class.java)
            startActivity(intent)
        }
        ApiManager.getInstance().getApiMallTest().getBMallBanner().enqueue(object :
            Callback<ABMallBannerBean> {
            override fun onResponse(call: Call<ABMallBannerBean>, response: Response<ABMallBannerBean>) {

                if (response.body() == null){
                    return
                }

                val adapter : BannerImageAdapter<ABMallBannerBean.DataBean> = object : BannerImageAdapter<ABMallBannerBean.DataBean>(response.body()!!.data){
                    override fun onBindView(holder: BannerImageHolder?, data: ABMallBannerBean.DataBean?, position: Int, size: Int) {
                        Glide.with(holder!!.itemView.context)
                            .load(data!!.bannerUrl)
                            //.apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                            .into(holder.imageView)
                    }
                }
                banner
                    .setAdapter(adapter)
                    .addBannerLifecycleObserver(this@BMallShopActivity)
                    .setIndicator(CircleIndicator(this@BMallShopActivity))
            }

            override fun onFailure(call: Call<ABMallBannerBean>, t: Throwable) {

            }

        })

        ApiManager.getInstance().getApiMallTest().getBMallList().enqueue(object : Callback<BMallProductBean>{
            override fun onResponse(call: Call<BMallProductBean>, response: Response<BMallProductBean>) {
                if (response.body() == null){
                    return
                }
                //Log.i("zhangjuniii",response.body()!!.data.size.toString())
                val manager = GridLayoutManager(this@BMallShopActivity,2)
                list.layoutManager = manager
                list.adapter = BMallProductAdapter(
                    this@BMallShopActivity,
                    response.body()!!.data
                )
            }

            override fun onFailure(call: Call<BMallProductBean>, t: Throwable) {

            }

        })
        dooper.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","协议")
            intent.putExtra("url", "http://api.equnshang.cn/rule/bmallRule.html")
            startActivity(intent)
        }

    }
}