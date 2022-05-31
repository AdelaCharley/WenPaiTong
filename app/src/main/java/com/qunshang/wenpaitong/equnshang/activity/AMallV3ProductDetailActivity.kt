package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.v3.MessageDialog
import com.lxj.xpopup.XPopup
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_amall_v3_product_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.AMallV3DetailShopAdapter
import com.qunshang.wenpaitong.equnshang.adapter.AMallV3ServiceLabelAdapter
import com.qunshang.wenpaitong.equnshang.adapter.ProductDetailSpecAdapterV2
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2
import com.qunshang.wenpaitong.equnshang.data.StarProductBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV4
import com.qunshang.wenpaitong.equnshang.view.ServiceDialog
import com.qunshang.wenpaitong.equnshang.view.SpecDialogV3
import java.io.File
import java.util.*

class AMallV3ProductDetailActivity : BaseActivity() {

    var productId = -10000

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    lateinit var bean : ProductBeanV2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!StringUtils.isEmpty(UserInfoViewModel.getUserId())){//如果用户登陆了，但是没有加载用户信息，则在此处加载用户信息
            KTUtil.loadPersonalInfo(this)
        }
        setContentView(R.layout.activity_amall_v3_product_detail)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "商品详情"
        toolbar_right_image.visibility = View.VISIBLE
        toolbar_right_image.setImageDrawable(resources.getDrawable(R.mipmap.nav_icon_send))
        banner = findViewById(R.id.banner)
        if(intent.getIntExtra("productId",-10000) == -10000){
            Toast.makeText(this,"出错了", Toast.LENGTH_SHORT).show()
            return
        } else {
            productId = intent.getIntExtra("productId",-10000)
            Log.i(Constants.logtag,"productId是" + productId)
        }
        loadData()
    }

    fun initView(){
        spec_init_image.setOnClickListener {
            if (!UserHelper.isLogin(this)) {
                startActivity(Intent(this, LoginActivity::class.java))
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2) {
                showSkuDialog(ProductsDialogV4.vipPrice)
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV4.price)
        }
        layout_fans_price.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV4.vipPrice)
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV4.price)
        }
        layout_nopintuan.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV4.vipPrice)
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV4.price)
        }
        layout_vip_price.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if ((productId == 1) or (productId == 2)){
                showSkuDialog(ProductsDialogV4.vipGroupPrice)
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV4.vipGroupPrice)
            } else {

                showBecomeVipDialog()
            }

        }
        layout_pintuanguize.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","拼团规则")
            intent.putExtra("url", Constants.baseURL + "/rule/amallGroupRule.html")
            startActivity(intent)
        }
        enterstore.setOnClickListener {
            val intent1 = Intent(this, StoreDetailActivityV2::class.java)
            intent1.putExtra("manfactureId", bean.data.manufacture?.manufactureId)
            this.startActivity(intent1)
        }
        layout_dianpu.setOnClickListener {
            val intent1 = Intent(this, StoreDetailActivityV2::class.java)
            intent1.putExtra("manfactureId", bean.data.manufacture?.manufactureId)
            this.startActivity(intent1)
        }
    }

    fun showBecomeVipDialog(){
        MessageDialog.show(this, "", "此价格为会员专属", "成为会员", "取消").setOnCancelButtonClickListener(object : OnDialogButtonClickListener {
                override fun onClick(baseDialog: BaseDialog?, v: View?): Boolean {
                    baseDialog?.doDismiss()
                    return true
                }
            }).setOnOkButtonClickListener { baseDialog, v ->
                val intent = Intent(this, ToBeVipOrPartnerActivity::class.java)
                intent.putExtra("productId", 1)
                startActivity(intent)
            baseDialog?.doDismiss()
                return@setOnOkButtonClickListener true
            }
    }

    fun showSkuDialog(type : String,orderGroupSn : String ?= null,ismemberforgroup : Boolean = false){
        XPopup.Builder(this)
            .moveUpToKeyboard(false)
            .enableDrag(true)
            .isDestroyOnDismiss(true)
            .asCustom(ProductsDialogV4(this,type,bean,orderGroupSn,ismemberforgroup))
            .show()
    }

    fun loadData(){
        ApiManager.getInstance().getApiAMallV3().loadProductDetail(UserInfoViewModel.getUserId(),productId.toString()).enqueue(object : Callback<ProductBeanV2> {
            override fun onResponse(call: Call<ProductBeanV2>, response: Response<ProductBeanV2>) {
                if (response.body() != null){
                    val bean = response.body()
                    Log.d("shulinr", "onResponse: $response")
                    if (bean?.code == 200){
                        initView()
                        displayViewByData(bean)//上面的二个是处理没有拼团的情况，如果是需要拼团的情况，则需要重新处理拼团的情况
                        loadPinTuanData()
                    } else {
                        DialogUtil.toast(this@AMallV3ProductDetailActivity,bean?.msg)
                    }
                }
            }

            override fun onFailure(call: Call<ProductBeanV2>, t: Throwable) {
            }

        })
    }

    fun loadPinTuanData(){
        if (!StringUtils.isEmpty(intent.getStringExtra("orderGroupSn"))){
            val expiredTime = intent.getStringExtra("expiredTime")
            val timeArr = TimeUtil.getTimeRemainByDayString(expiredTime)
            if ("00:00:00".equals(timeArr)){
                layout_fans_price.visibility = View.GONE
                layout_vip_price.visibility = View.GONE
                layout_pintuan_end.visibility = View.VISIBLE
                layout_pintuaning.visibility = View.GONE
                Glide.with(this).load(intent.getStringExtra("userMaster")).into(pintuan_end_user_image)
            } else {
                layout_fans_price.visibility = View.GONE
                layout_vip_price.visibility = View.GONE
                layout_pintuan_end.visibility = View.GONE
                layout_pintuaning.visibility = View.VISIBLE
                Glide.with(this).load(intent.getStringExtra("userMaster")).into(pintuaning_user_image)
                val task = object : TimerTask(){
                    override fun run() {
                        runOnUiThread {
                            val timeArr = TimeUtil.getTimeRemainByDayString(expiredTime)
                            label_pintuan_remaintime.setText("剩余" + timeArr)
                            if ("00:00:00".equals(timeArr)){
                                loadData()
                            }
                        }
                    }
                }
                val oneSecond: Long = 1000
                Timer().schedule(task, 0, oneSecond)
                layout_pintuaning.setOnClickListener {
                    showSkuDialog(ProductsDialogV4.vipGroupPrice,intent.getStringExtra("orderGroupSn"),true)
                }
            }
        }
        root.visibility = View.VISIBLE
    }

    fun initSpec(data : List<ProductBeanV2.DataBean.SkuList>){
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        spec_init_image.layoutManager = manager
        spec_init_image.adapter =
            ProductDetailSpecAdapterV2(this, data)
    }

    @SuppressLint("NewApi")
    fun displayViewByData(bean: ProductBeanV2?){
        this.bean = bean!!
        if (bean.data?.swiperList?.imgList?.size != 0){
            val adapter : BannerImageAdapter<String> = object : BannerImageAdapter<String>(bean.data!!.swiperList.imgList!!){
                override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
                    Glide.with(holder!!.itemView)
                        .load(data)
                        .into(holder.imageView)
                }
            }
            banner
                .setAdapter(adapter)
                .addBannerLifecycleObserver(this)
                .setIndicator(CircleIndicator(this))
        }
        val skuList = bean.data.skuList.get(0)

        if ( StringUtils.isEmpty(bean.data.productDesc)) {
            productdesc.visibility = View.GONE
        } else {
            productdesc.setText(bean.data.productDesc)
        }

        if (UserInfoViewModel.getUserInfo() != null){
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                fansprice.setText("" + skuList.vipGroupPrice)
                layout_benlai.visibility = View.VISIBLE
                benlaiprice.setText("" + skuList.marketPrice)
                bottom_vipprice.setText("￥" + skuList.vipPrice)
                label_nopintuan_price.setText("￥" + skuList.vipPrice)
            } else {
                fansprice.setText("" + skuList.vipGroupPrice)
                layout_benlai.visibility = View.GONE
                bottom_vipprice.setText("￥" + skuList.price)
                label_nopintuan_price.setText("￥" + skuList.price)
            }
        } else {//当用户未登录的时候
            fansprice.setText("" + skuList.vipGroupPrice)
            layout_benlai.visibility = View.GONE
            bottom_vipprice.setText("￥" + skuList.price)
            label_nopintuan_price.setText("￥" + skuList.price)
        }

        toolbar_right_image.setOnClickListener {
            val imageurl = bean.data?.swiperList!!.imgList.get(0)
            //val url = "/equnshang/culture/detail?productId=" + bean.data.productId
            val url = "/equnshang/Amall/detail?userId=" + UserInfoViewModel.getUserId() + "&productId=" + bean.data.productId
            val title = bean.data.productName
            val desc = "我刚发现一件好物，快来看看吧！"
            KTUtil.doWeChatShare(this,imageurl, url, title, desc)
        }

        bottom_groupprice.setText("￥" + skuList.vipGroupPrice)

        product_name.setText(bean.data.productName)

        initSpec(bean.data.skuList)

        if (StringUtils.isEmpty(bean.data.freight)){
            layout_express.visibility = View.GONE
        } else {
            feight.setText(bean.data.freight)
        }
        label_service.adapter = AMallV3ServiceLabelAdapter(this,bean.data.service)

        layout_baozhang.setOnClickListener {
            XPopup.Builder(this)
                .moveUpToKeyboard(false)
                .enableDrag(true)
                .isDestroyOnDismiss(true)
                .asCustom(ServiceDialog(this,bean.data.service))
                .show()
        }

        Glide.with(this).load(bean.data?.manufacture?.manufactureLogoUrl).into(image_shop)
        name_shop.setText(bean.data.manufacture.manufactureName)

        if (bean.data.manufacture.product.isEmpty()) {
            shopproducts.visibility = View.GONE
        } else {
            shopproducts.adapter = AMallV3DetailShopAdapter(this,bean.data.manufacture.product)
        }

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        image_detail_list.layoutManager = manager
        adapter = ProductImageDetailAdapter(this@AMallV3ProductDetailActivity, bean.data.detailInfo)
        image_detail_list.adapter = adapter
        loadImageDetail(bean.data.detailInfo)
        //image_detail_list.getRecycledViewPool().setMaxRecycledViews(0,0);
        layout_custom.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.data.servicePhone))
            startActivity(intent)
        }
        salecount.setText(bean?.data.sale.toString() + "件已售")
        if(bean.data.isCollect != 0){
            img_star.setImageDrawable(getDrawable(R.mipmap.amallv3_detail_star))
            text_star.setText("取消收藏")
            text_star.setTextColor(getColor(R.color.red))
        }
        layout_star.setOnClickListener {
            ApiManager.getInstance().getApiMallTest().loadStarProduct(UserInfoViewModel.getUserId(), productId.toString()).enqueue(object :
                Callback<StarProductBean> {
                @SuppressLint("NewApi")
                override fun onResponse(call: Call<StarProductBean>, response: Response<StarProductBean>) {
                    val bean = response.body()
                    if (bean!!.data.equals("1")){
                        img_star.setImageDrawable(getDrawable(R.mipmap.amallv3_detail_star))
                        text_star.setText("取消收藏")
                        text_star.setTextColor(getColor(R.color.red))
                    } else {
                        img_star.setImageDrawable(getDrawable(R.mipmap.amallv3_detail_unstar))
                        text_star.setText("收藏")
                        text_star.setTextColor(getColor(R.color.defgrey))
                    }
                }

                override fun onFailure(call: Call<StarProductBean>, t: Throwable) {

                }

            })
        }

        introduce.setText(bean.data.priceIntroduce)

        if (bean.data.fourBackOne == -1){//这个时候没有其他按钮，只有一个立即拼团按钮
            layout_fans_price.visibility = View.GONE
            layout_vip_price.visibility = View.GONE
            layout_pintuan_end.visibility = View.GONE
            layout_pintuaning.visibility = View.GONE
            layout_nopintuan.visibility = View.VISIBLE
            layout_label_pintuanyouhui.visibility = View.GONE
        }

        if (bean.data.fourBackOne != 1){
            layout_label_pintuanyouhui.visibility = View.GONE
        }

        spec_layout.setOnClickListener {
            showAttrubuteList()
        }
        attributes.setText(StringUtils.loadSpecStringTitlesV2(bean.data.attributeList))

    }

    lateinit var adapter : ProductImageDetailAdapter

    @SuppressLint("CheckResult")
    fun loadImageDetail(urls : List<String>){
        var count = 0
        for (i in urls.indices){
            Glide.with(this).load(urls.get(i)).downloadOnly(object : SimpleTarget<File?>() {
                override fun onResourceReady(resource: File, glideAnimation: Transition<in File?>?) {
                    count ++
                    if (count == urls.size){
                        if (this@AMallV3ProductDetailActivity::adapter.isInitialized){
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

    fun showAttrubuteList(){
        XPopup.Builder(this)
            .moveUpToKeyboard(false)
            .enableDrag(true)
            .isDestroyOnDismiss(true)
            .asCustom(SpecDialogV3(this,bean.data.attributeList))
            .show()
    }

}