package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lxj.xpopup.XPopup
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_wen_ban_tong_product_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.WenBanTongShareDialog
import com.qunshang.wenpaitong.equnshang.view.WenBanTongSpecDialog
import java.io.File
import java.util.*

class WenBanTongProductDetailActivity : BaseActivity (){

    var productId = -10000

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    lateinit var bean : WenBanTongProductBean

    fun init(){
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        toolbar_back.setOnClickListener { finish() }
        banner = findViewById(R.id.banner)
        if(intent.getIntExtra("productId",-10000) == -10000){
            Toast.makeText(this,"出错了", Toast.LENGTH_SHORT).show()
            return
        } else {
            productId = intent.getIntExtra("productId",-10000)
            Log.i(Constants.logtag,"productId是" + productId)
        }
        loadData()
        initLayout1()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wen_ban_tong_product_detail)
        changeToGreyButTranslucent()
        init()
    }

    /*override fun onResume() {
        super.onResume()
        init()
        StringUtils.log("啊啊啊刷新了")
    }*/

    var isInited = false

    var current = 0

    val maxcount = 3

    fun initLayout1(){
        layout1.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (current >= maxcount){
                    return
                }
                StringUtils.log("我执行了")
                val bgwidth = layout1.height  //获取背景图片的宽度
                StringUtils.log("我的款" + bgwidth)
                val layoutParams = productname.layoutParams as ViewGroup.MarginLayoutParams //593  357

                val margintoppercent = 75.0/200.0
                layoutParams.setMargins(layoutParams.leftMargin,(margintoppercent * bgwidth).toInt(),layoutParams.rightMargin,0)
                StringUtils.log("fjdkj" + (margintoppercent * bgwidth).toInt())
                productname.layoutParams = layoutParams

                val layoutParams1 = label_discount.layoutParams as ViewGroup.MarginLayoutParams //593  357

                val marginbottompercent = 25.0/200.0
                val margintoppercentoflabeldiscount = 8.5/200.0
                layoutParams1.setMargins(0,(margintoppercentoflabeldiscount*bgwidth).toInt(),0,(marginbottompercent * bgwidth).toInt())
                label_discount.layoutParams = layoutParams1

                isInited = true
                //checkIsInited()
                current ++
                if (!isInited){

                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(str : String){}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun initView(){
        layout_nopintuan.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
        }
    }

    fun loadData(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadProductDetail(productId.toString()).enqueue(object :
            Callback<WenBanTongProductBean> {
            override fun onResponse(call: Call<WenBanTongProductBean>, response: Response<WenBanTongProductBean>) {
                if (response.body() != null){
                    val bean = response.body()
                    Log.d("shulinr", "onResponse: $response")
                    if (bean?.code == 200){
                        root.visibility = View.VISIBLE
                        initView()
                        displayViewByData(bean)//上面的二个是处理没有拼团的情况，如果是需要拼团的情况，则需要重新处理拼团的情况
                    } else {
                        DialogUtil.toast(this@WenBanTongProductDetailActivity,bean?.msg)
                    }
                }
            }

            override fun onFailure(call: Call<WenBanTongProductBean>, t: Throwable) {
                StringUtils.log("啊啊啊房钱错误" + t.message)
            }

        })
    }

    @SuppressLint("NewApi")
    fun displayViewByData(bean: WenBanTongProductBean?){
        if (bean == null){
            return
        }
        this.bean = bean!!
        if (bean.data?.swiperList?.size != 0){
            val adapter : BannerImageAdapter<String> = object : BannerImageAdapter<String>(bean.data!!.swiperList){
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

        if ( StringUtils.isEmpty(bean.data.productDesc)) {
            productdesc.visibility = View.GONE
        } else {
            productdesc.setText(bean.data.productDesc)
        }
        val str = "JFK大家\n"
        toolbar_share.setOnClickListener {
            val dialog = WenBanTongShareDialog(bean,this)
            XPopup.Builder(this)
                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                .enableDrag(true)
                .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                .asCustom(dialog)
                .show()
            return@setOnClickListener
            val customPopup = WenBanTongShareDialog(bean,this)
            XPopup.Builder(this)
                .isViewMode(true)
                .autoOpenSoftInput(false)
                .asCustom(customPopup)
                .show()
            return@setOnClickListener
            val imageurl = bean.data?.swiperList?.get(0)
            val url = "/equnshang/culture/detail?productId=" + bean.data.productId
            val title = bean.data.productName
            val desc = "我刚发现一件好物，快来看看吧！"
            KTUtil.doWeChatShare(this,imageurl, url, title, desc)
            CommonUtil.doCompleteTask(3)
        }

        evprice.setText(bean.data.evaluationPrice.toString())

        price.setText(bean.data.price.toString())
        label_wenbantong_new_price.setText("￥" + bean.data.price.toString())

        stock.setText("库存:" + bean.data.stock.toString())
        label_new_wenbantong_stock.setText(bean.data.initStock.toString() + "份")

        product_name.setText(bean.data.productName)
        productname.setText(bean.data.productName)

        progress.progress = bean.data.process.toInt()
        text_progress.setText(bean.data.process + "%")

        category.setText(bean.data.packetName)
        alreadysale.setText("已售" + bean.data.sale + "元")

        originalprice.setText("￥ " + bean.data.evaluationPrice)

        layout_nopintuan.setOnClickListener {
            if (!UserHelper.isLogin(this)) {
                startActivity(Intent(this, LoginActivity::class.java))
                return@setOnClickListener
            }
            val intent = Intent(this,WenBanTongConfrimPayActivity::class.java)
            intent.putExtra("product",bean)
            startActivity(intent)
        }

        name_shop.setOnClickListener {
            val intent = Intent(this,WenBanTongCompanyDetailActivity::class.java)
            intent.putExtra("companyId",bean.data.manufacture.companyId)
            startActivity(intent)
        }

        pricetag.setText(bean.data.tag)
        labelnew_wenbantong_count.setText(bean.data.tag)

        //pricetag.setText(bean.data.price + "文版通")

        when(bean.data.status){
            "10" ->{
                layout_nopintuan.visibility = View.GONE
                layout_dengdai_kaishou.visibility = View.VISIBLE
                kaishoutime.setText(bean.data.startTime
                    //.split(" ").get(0)
                        + "开售")
                /*buynow.setText("将于" + bean.data.startTime
                    //.split(" ").get(0)
                        + "开售")
                buynow.setPadding(CommonUtil.dp2px(this,15),CommonUtil.dp2px(this,5),CommonUtil.dp2px(this,15),CommonUtil.dp2px(this,5))
                buynow.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14f)
                layout_nopintuan.background = getDrawable(R.drawable.background_wenbantong_buynow_cannotbuy)
                layout_nopintuan.isClickable = fals*/
            }
            "20" -> {

            }
            "30" -> {
                buynow.setText("已结束")
                layout_nopintuan.background = getDrawable(R.drawable.background_wenbantong_buynow_cannotbuy)
                layout_nopintuan.isClickable = false
            }
        }

        if (bean.data.stock <= 0){
            buynow.setText("已售罄")
            layout_nopintuan.background = getDrawable(R.drawable.background_wenbantong_buynow_cannotbuy)
            layout_nopintuan.isClickable = false
        }

        if (StringUtils.isEmpty(bean.data.freight)){
            layout_express.visibility = View.GONE
        } else {
            feight.setText(bean.data.freight)
        }
        Glide.with(this).load(bean.data.manufacture.companyAvatar).into(image_shop)
        name_shop.setText(bean.data.manufacture.companyName)
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        image_detail_list.layoutManager = manager
        //image_detail_list.adapter = ProductImageDetailAdapter(this, bean.data.detailInfo)
        loadImageDetail(bean.data.detailInfo)
        /*layout_custom.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.data.servicePhone))
            startActivity(intent)
        }*/
        spec_layout.setOnClickListener {
            showAttrubuteList()
        }

        attributes.setText(StringUtils.loadWenBanTongSpecStringTitlesV2(bean.data.attributeList))

        val layoutParams = labelnew_wenbantong_count.layoutParams as ViewGroup.MarginLayoutParams

        if (bean.data.discount != null){
            discount.visibility = View.VISIBLE
            discount.setText(bean.data.discount.discountStr)

            label_discount.visibility = View.VISIBLE
            label_discount.setText(bean.data.discount.discountStr)

            price.setText(bean.data.discount.productDiscountPriceStr)
            label_wenbantong_new_price.setText("￥ " + bean.data.discount.productDiscountPriceStr)
            layout_discount.visibility = View.VISIBLE
            text_discount_tag.setText(bean.data.discount.discountStr)
            text_discount_remaintime.setText(bean.data.discount.endTime)
            evprice.setText(bean.data.price)
            val timer = Timer()
            label2.setText("原价 ￥")
            val task = object : TimerTask(){
                override fun run() {
                    if (StringUtils.isEmpty(TimeUtil.getTimeRemainByDayString(bean.data.discount.endTime))){
                        return
                    }
                    runOnUiThread {
                        text_discount_remaintime.setText("剩余时间 : " + TimeUtil.getTimeRemainByDayStringWithDay(bean.data.discount.endTime))
                    }
                }
            }
            try {
                timer.schedule(task,0,1000)
            } catch (e : Exception){
                e.printStackTrace()
            }

            label_discount.setText(bean.data.discount?.discountStr)
            label_discount.width = CommonUtil.dp2px(this,57)

            layoutParams.setMargins(CommonUtil.dp2px(this,5),layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin)

        } else {
            label_discount.setText("")
            label_discount.width = 0
            layoutParams.setMargins(CommonUtil.dp2px(this,0),layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin)
        }
        Glide.with(this).load(bean.data.purchaseTip).into(image1)
        Glide.with(this).load(bean.data.priceTip).into(image2)

        zixunkefu.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.data.servicePhone))
            startActivity(intent)
        }
    }

    lateinit var adapter: ProductImageDetailAdapter

    @SuppressLint("CheckResult")
    fun loadImageDetail(urls : List<String>){
        var count = 0
        for (i in urls.indices){
            Glide.with(this).load(urls.get(i)).downloadOnly(object : SimpleTarget<File?>() {
                override fun onResourceReady(resource: File, glideAnimation: Transition<in File?>?) {
                    count ++
                    if (count == urls.size){
                        adapter = ProductImageDetailAdapter(this@WenBanTongProductDetailActivity, urls)
                        image_detail_list.adapter = adapter
                        adapter.refreshWithShow(true)

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
            .asCustom(WenBanTongSpecDialog(this,bean.data.attributeList))
            .show()
    }

}