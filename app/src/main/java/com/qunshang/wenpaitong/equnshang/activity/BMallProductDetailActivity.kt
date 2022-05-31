package com.qunshang.wenpaitong.equnshang.activity
import android.content.Intent
import android.os.Bundle

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.lxj.xpopup.XPopup
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_bmall_product_detail.*
import kotlinx.android.synthetic.main.activity_bmall_product_detail.attributes
import kotlinx.android.synthetic.main.activity_bmall_product_detail.image_detail_list
import kotlinx.android.synthetic.main.activity_bmall_product_detail.image_shop
import kotlinx.android.synthetic.main.activity_bmall_product_detail.layout_manufacture
import kotlinx.android.synthetic.main.activity_bmall_product_detail.name_shop
import kotlinx.android.synthetic.main.activity_bmall_product_detail.origalprice
import kotlinx.android.synthetic.main.activity_bmall_product_detail.pregroupprice
import kotlinx.android.synthetic.main.activity_bmall_product_detail.product_name
import kotlinx.android.synthetic.main.activity_bmall_product_detail.sku_layout
import kotlinx.android.synthetic.main.activity_bmall_product_detail.spec_init_image
import kotlinx.android.synthetic.main.activity_bmall_product_detail.spec_layout
import kotlinx.android.synthetic.main.activity_bmall_product_detail.star
import kotlinx.android.synthetic.main.activity_bmall_product_detail.tablayout
import kotlinx.android.synthetic.main.activity_bmall_product_detail.toolbar_back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.data.ProductBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.view.*

class BMallProductDetailActivity : BaseActivity() {

    var productId = -999

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    lateinit var bean : ProductBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmall_product_detail)
        productId = intent.getIntExtra("productId",-999)
        if (productId == -999){
            return
        }
        toolbar_back.setOnClickListener { finish() }
        //DialogUtil.toast(this,"ProductID是" + productId)
        banner = findViewById(R.id.banner)
        initView()
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        val tab_product = tablayout.newTab().setText("商品")
        tab_product.view.setOnClickListener {  }
        val tab_detail = tablayout.newTab().setText("详情")
        tab_detail.view.setOnClickListener {  }
        tablayout.addTab(tab_product)
        tablayout.addTab(tab_detail)
        ApiManager.getInstance().getApiMallTest().loadBMallProductDetail(UserInfoViewModel.getUserId(),productId.toString()).enqueue(object : Callback<ProductBean>{
            override fun onResponse(call: Call<ProductBean>, response: Response<ProductBean>) {
                if (response.body() == null){
                    return
                }
                this@BMallProductDetailActivity.bean = response.body()!!
                displayViewByData(response.body())
            }

            override fun onFailure(call: Call<ProductBean>, t: Throwable) {

            }

        })
        attachScrollViewAndTabLayout()
    }

    fun displayViewByData(bean: ProductBean?){
        layout_manufacture.setOnClickListener {
            if (Constants.isNewPinHaoHuo){
                val intent1 = Intent(this, StoreDetailActivity::class.java)
                intent1.putExtra("manfactureId", bean!!.data.manufacture?.manufactureId)
                intent1.putExtra("type","bmall")
                this.startActivity(intent1)
            } else {
                val intent1 = Intent(this, StoreDetailActivity::class.java)
                intent1.putExtra("manfactureId", bean!!.data.manufacture?.manufactureId)
                intent1.putExtra("type","bmall")
                this.startActivity(intent1)
            }
        }
        sku_layout.setOnClickListener {
            showBuyDialog()
            //BMallProductsDialog(ProductsDialog.vipPrice,bean!!).show(supportFragmentManager,"")
        }

        this.bean = bean!!
        val adapter : BannerImageAdapter<String> = object : BannerImageAdapter<String>(bean!!.data!!.swiperList.imgList!!){
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

        val skuList = bean.data.skuList.get(0)
        pregroupprice.setText("￥" + skuList.price)

        startprice.setText(skuList.threshold)
        origalprice.setText("￥" + skuList.fanNormalPrice.toString())
        remainder.setText("剩余" + skuList.stock.toInt().toString() + "件")


        product_name.setText(bean.data.productName)

        contactcustomer.setOnClickListener {
            DialogUtil.showCustomDialog(this,"mall")
        }

        initSpec(bean.data.skuList)
        //Glide.with(this).load(skuList.image).into(spec_init_image)

        Glide.with(this).load(bean.data.manufacture.manufactureLogoUrl).into(image_shop)
        name_shop.setText(bean.data.manufacture.manufactureName)

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.isSmoothScrollbarEnabled = true
        image_detail_list.layoutManager = manager
        detailadapter = ProductImageDetailAdapter(this, bean.data.detailInfo)
        image_detail_list.adapter = detailadapter
        detailadapter.refreshWithShow(true)


        if(bean.data.isCollect != 0){
            star.setText("取消收藏")
        }
        star.setOnClickListener {
            ApiManager.getInstance().getApiMallTest().loadStarProduct(UserInfoViewModel.getUserId(), productId.toString()).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.StarProductBean>{
                override fun onResponse(call: Call<com.qunshang.wenpaitong.equnshang.data.StarProductBean>, response: Response<com.qunshang.wenpaitong.equnshang.data.StarProductBean>) {
                    val bean = response.body()
                    if (bean!!.data.equals("1")){
                        star.setText("取消收藏")
                    } else {
                        star.setText("收藏")
                    }
                }

                override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.StarProductBean>, t: Throwable) {

                }

            })
        }

        spec_layout.setOnClickListener {
            showAttrubuteList()
            /*Toast.makeText(this,"a好好", Toast.LENGTH_SHORT).show()
            SpecDialog(bean.data.attributeList).show(supportFragmentManager,"")*/
        }
        buynow.setOnClickListener {
            showBuyDialog()
        }
        attributes.setText(StringUtils.loadSpecStringTitles(bean.data.attributeList))
    }

    lateinit var detailadapter: ProductImageDetailAdapter

    fun showBuyDialog(){
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(BuyProductDialog(this,bean))
            .show()
    }

    fun showAttrubuteList(){
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(SpecDialogV2(this,bean.data.attributeList))
            .show()
    }

    fun initSpec(data : List<ProductBean.DataBean.SkuList>){
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        spec_init_image.layoutManager = manager
        spec_init_image.adapter =
            com.qunshang.wenpaitong.equnshang.adapter.ProductDetailSpecAdapter(this, data)
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