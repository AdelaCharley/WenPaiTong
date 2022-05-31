package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_amall_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.ABMallBannerBean
import com.qunshang.wenpaitong.equnshang.data.AMallProductBeanV2
import com.qunshang.wenpaitong.equnshang.data.CategoryBean

class AmallProductFragment(val categoryId : Int) : MyBaseFragment() {

    var isBannerDataLoaded = false

    var isProductLoaded = false

    var isCategoryLoaded = false

    var pageIndex = 1

    val pageSize = 100

    lateinit var banner: Banner<ABMallBannerBean.DataBean, BannerImageAdapter<ABMallBannerBean.DataBean>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_amall_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banner = view.findViewById(R.id.banner)
    }

    override fun onResume() {
        super.onResume()
        if (!isBannerDataLoaded or !isProductLoaded or !isCategoryLoaded){
            init()
        }
    }

    fun init(){
        initBanner()
        //initCategoryAndBrand()
        //initProducts()
    }

    fun initCategoryAndBrand(){
        if (categoryId != 0){
            ApiManager.getInstance().getApiMallTest().loadCategroy(categoryId,pageIndex,pageSize).enqueue(object : Callback<CategoryBean>{
                override fun onResponse(call: Call<CategoryBean>, response: Response<CategoryBean>) {
                    if (response.body() == null){
                        return
                    }
                    category.visibility = View.VISIBLE
                    layout_brand.visibility = View.VISIBLE

                    val categorymanager = GridLayoutManager(context,3)
                    category.layoutManager = categorymanager
                    category.adapter = com.qunshang.wenpaitong.equnshang.adapter.CategoryAdapter(
                        context,
                        response.body()!!.data.category
                    )

                    if (response.body()!!.data.brand.size != 0){
                        val brandmanager = GridLayoutManager(context,3)
                        brands.layoutManager = brandmanager
                        brands.adapter = com.qunshang.wenpaitong.equnshang.adapter.BrandAdapter(
                            context,
                            response.body()!!.data.brand
                        )
                    } else {
                        layout_brand.visibility = View.GONE
                    }
                    isCategoryLoaded = true
                    initProducts()
                }

                override fun onFailure(call: Call<CategoryBean>, t: Throwable) {

                }

            })
        } else {
            category.visibility = View.GONE
            layout_brand.visibility = View.GONE
            isCategoryLoaded = true
            initProducts()
        }
    }

    fun initBanner(){
        if (categoryId == 0){
            //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(context,"我是0")
            ApiManager.getInstance().getApiMallTest().loadAMallBanner().enqueue(object : Callback<ABMallBannerBean>{
                override fun onResponse(
                    call: Call<ABMallBannerBean>,
                    response: Response<ABMallBannerBean>
                ) {

                    if (response.body() == null){
                        return
                    }

                    val adapter : BannerImageAdapter<ABMallBannerBean.DataBean> = object : BannerImageAdapter<ABMallBannerBean.DataBean>(response.body()!!.data){
                        override fun onBindView(holder: BannerImageHolder?, data: ABMallBannerBean.DataBean?, position: Int, size: Int) {
                            Glide.with(holder!!.itemView.context)
                                .load(data!!.bannerUrl)
                                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                                .into(holder.imageView)
                        }
                    }
                    banner
                        .setAdapter(adapter)
                        .addBannerLifecycleObserver(activity)
                        .setIndicator(CircleIndicator(context))
                    isBannerDataLoaded = true
                    initCategoryAndBrand()
                }

                override fun onFailure(call: Call<ABMallBannerBean>, t: Throwable) {

                }

            })
        } else {
            isBannerDataLoaded = true
            banner.visibility = View.GONE
            initCategoryAndBrand()
        }
    }

    fun initProducts(){
        ApiManager.getInstance().getApiMallTest().loadProduct(categoryId,pageIndex,pageSize).enqueue(object : Callback<AMallProductBeanV2>{
            override fun onResponse(call: Call<AMallProductBeanV2>, response: Response<AMallProductBeanV2>) {
                if (response.body() == null){
                    return
                }

                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                products.layoutManager = manager

                val adapter =
                    com.qunshang.wenpaitong.equnshang.adapter.AMallAdapterV2(context, response.body()!!.data)
                products.adapter = adapter

                isProductLoaded = true

            }

            override fun onFailure(call: Call<AMallProductBeanV2>, t: Throwable) {

            }

        })
    }

}