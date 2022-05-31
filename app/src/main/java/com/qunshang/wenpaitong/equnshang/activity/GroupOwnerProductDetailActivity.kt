package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.lxj.xpopup.XPopup
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_group_owner_product_detail.*
import kotlinx.android.synthetic.main.item_buy_product_selected_sku.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ProductDetailSpecAdapter
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.data.ProductBean
import com.qunshang.wenpaitong.equnshang.data.StarProductBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiMall
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV3
import com.qunshang.wenpaitong.equnshang.view.SpecDialogV2

class GroupOwnerProductDetailActivity : BaseActivity() {
    var productId = -10000

    lateinit var apiMall: ApiMall

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    lateinit var bean : ProductBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_owner_product_detail)
        banner = findViewById(R.id.banner)
        if(intent.getIntExtra("productId",-10000) == -10000){
            Toast.makeText(this,"出错了", Toast.LENGTH_SHORT).show()
            return
        } else {
            productId = intent.getIntExtra("productId",-10000)
            //Toast.makeText(this,"productId是" + productId, Toast.LENGTH_SHORT).show()
        }
        apiMall = ApiManager.getInstance().getApiMallTest()
        loadData()
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        val tab_product = tablayout.newTab().setText("商品")
        tab_product.view.setOnClickListener {  }
        val tab_detail = tablayout.newTab().setText("详情")
        tab_detail.view.setOnClickListener {  }
        tablayout.addTab(tab_product)
        tablayout.addTab(tab_detail)

        layout_vip_price.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV3.groupOwnerPrice)

        }
        layout_manufacture.setOnClickListener {
            val intent1 = Intent(this, StoreDetailActivityV2::class.java)
            intent1.putExtra("manfactureId", bean.data.manufacture?.manufactureId)
            this.startActivity(intent1)
        }
        layout_custom.setOnClickListener {
            DialogUtil.showCustomDialog(this,"mall")
        }
        attachScrollViewAndTabLayout()
    }

    fun showSkuDialog(type : String){
        //ProductsDialog(ProductsDialog.vipPrice,bean).show(supportFragmentManager,"")
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(ProductsDialogV3(this,type,bean))
            .show()
    }

    fun loadData(){
        apiMall.loadProductDetail(UserInfoViewModel.getUserId(),productId.toString()).enqueue(object :
            Callback<ProductBean> {
            override fun onResponse(call: Call<ProductBean>, response: Response<ProductBean>) {
                if (response.body() != null){
                    val bean = response.body()
                    if (bean?.code == 200){
                        initView()
                        displayViewByData(bean)
                    }
                    else {
                        DialogUtil.toast(this@GroupOwnerProductDetailActivity,bean?.msg)
                    }
                }
            }

            override fun onFailure(call: Call<ProductBean>, t: Throwable) {
            }

        })
    }

    fun initSpec(data : List<ProductBean.DataBean.SkuList>){
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        spec_init_image.layoutManager = manager
        spec_init_image.adapter =
            ProductDetailSpecAdapter(this, data)
    }

    fun displayViewByData(bean: ProductBean?){
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
        pregroupprice.setText("￥" + skuList.vipGroupPrice)
        label_total.setText("商品团购数量" + bean.data.vipGroupCard.total + "件")
        text_current.setText("已团" + bean.data.vipGroupCard.current + "件")
        group_progress.max = bean.data.vipGroupCard.total
        group_progress.progress = bean.data.vipGroupCard.current

        product_name.setText(bean.data.productName)

        fansprice.setText("￥" + skuList.groupOwnerPrice)

        initSpec(bean.data.skuList)
        //Glide.with(this).load(skuList.image).into(spec_init_image)

        Glide.with(this).load(bean.data.manufacture.manufactureLogoUrl).into(image_shop)
        name_shop.setText(bean.data.manufacture.manufactureName)

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.isSmoothScrollbarEnabled = true
        image_detail_list.layoutManager = manager
        image_detail_list.adapter =
            ProductImageDetailAdapter(this, bean.data.detailInfo)

        if(bean.data.isCollect != 0){
            star.setText("取消收藏")
        }
        star.setOnClickListener {
            ApiManager.getInstance().getApiMallTest().loadStarProduct(UserInfoViewModel.getUserId(), productId.toString()).enqueue(object :
                Callback<StarProductBean> {
                override fun onResponse(call: Call<StarProductBean>, response: Response<StarProductBean>) {
                    val bean = response.body()
                    if (bean!!.data.equals("1")){
                        star.setText("取消收藏")
                    } else {
                        star.setText("收藏")
                    }
                }

                override fun onFailure(call: Call<StarProductBean>, t: Throwable) {

                }

            })
        }

        spec_layout.setOnClickListener {
            showAttrubuteList()
        }
        attributes.setText(StringUtils.loadSpecStringTitles(bean.data.attributeList))
    }

    /*
    这个地方先不要注释掉，经过测试，发现了一下的问题，首先是item_spec字体错乱问题，经过观察发现是我的spec_name没写好，我写个bottomtobottomof parent就行，
         但是即便如此，DIalog也无法准确解析宽高，宽高设置为match_parent无效*/
    fun showAttrubuteList(){
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(SpecDialogV2(this,bean.data.attributeList))
            .show()
    }

    private var tabIndex = 0 //tablayout所处的下标

    private var scrollviewFlag = false //标记是否是scrollview在滑动

    fun attachScrollViewAndTabLayout(){
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (!scrollviewFlag) {
                    when (tab.position) {
                        0 -> scrollview.smoothScrollTo(0, banner.getTop(),1000)
                        1 -> scrollview.smoothScrollTo(0, layout_detail.getTop(),1000)
                    }
                }
                //用户点击tablayout时，标记不是scrollview主动滑动
                scrollviewFlag = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                scrollviewFlag = true
                tabIndex = tablayout.getSelectedTabPosition()
                if (scrollY < layout_detail.getTop()) {
                    if (tabIndex != 0) { //增加判断，如果滑动的区域是tableIndex=0对应的区域，则不改变tablayout的状态
                        tablayout.selectTab(tablayout.getTabAt(0))
                    }
                } else if (scrollY >= layout_detail.getTop()) {
                    if (tabIndex != 1) {
                        tablayout.selectTab(tablayout.getTabAt(1))
                    }
                }
                scrollviewFlag = false
            }
        })
    }
}