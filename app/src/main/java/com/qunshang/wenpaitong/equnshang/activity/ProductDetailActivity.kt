package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.v3.MessageDialog
import com.lxj.xpopup.XPopup
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.image_detail_list
import kotlinx.android.synthetic.main.activity_product_detail.image_shop
import kotlinx.android.synthetic.main.activity_product_detail.name_shop
import kotlinx.android.synthetic.main.activity_product_detail.pregroupprice
import kotlinx.android.synthetic.main.activity_product_detail.product_name
import kotlinx.android.synthetic.main.activity_product_detail.sku_layout
import kotlinx.android.synthetic.main.activity_product_detail.spec_init_image
import kotlinx.android.synthetic.main.activity_product_detail.spec_layout
import kotlinx.android.synthetic.main.activity_product_detail.star
import kotlinx.android.synthetic.main.activity_product_detail.tablayout
import kotlinx.android.synthetic.main.activity_product_detail.toolbar_back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.ProductBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiMall
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.view.SpecDialogV2
import com.google.android.material.tabs.TabLayout
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.ProductImageDetailAdapter
import com.qunshang.wenpaitong.equnshang.data.StarProductBeanOLD
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV3

class ProductDetailActivity : BaseActivity() {

    var productId = -10000

    lateinit var apiMall: ApiMall

    lateinit var banner: Banner<String,BannerImageAdapter<String>>

    lateinit var bean : ProductBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        banner = findViewById(R.id.banner)
        if(intent.getIntExtra("productId",-10000) == -10000){
            Toast.makeText(this,"?????????", Toast.LENGTH_SHORT).show()
            return
        } else {
            productId = intent.getIntExtra("productId",-10000)
            Log.i(Constants.logtag,"productId???" + productId)
            //Toast.makeText(this,"productId???" + productId, Toast.LENGTH_SHORT).show()
        }
        apiMall = ApiManager.getInstance().getApiMallTest()
        loadData()
        handleVipOrPartner()
    }

    fun handleVipOrPartner(){
        if ((productId == 1) or (productId == 2)){
            bottom_vipprice.visibility = View.GONE
            label_vipprice.visibility = View.GONE
            takefee.visibility = View.VISIBLE

            layout_express.visibility = View.GONE
            layout_baozhang.visibility = View.GONE
            label3.visibility = View.GONE

            bottom_groupprice.visibility = View.GONE
            bottom_original.visibility = View.GONE
            nowfee.visibility = View.VISIBLE

            layout1.visibility = View.GONE
            benlaiprice.visibility = View.GONE
        }
        if (!UserHelper.isLogin(this)){
            return
        }
        if (productId == 1){
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                nowfee.setText("????????????")
            }
        }
        if (productId == 2){
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2){
                nowfee.setText("????????????")
            }
        }
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        val tab_product = tablayout.newTab().setText("??????")
        tab_product.view.setOnClickListener {  }
        val tab_detail = tablayout.newTab().setText("??????")
        tab_detail.view.setOnClickListener {  }
        tablayout.addTab(tab_product)
        tablayout.addTab(tab_detail)
        sku_layout.setOnClickListener {
            //showSkuDialog(ProductsDialogV3.price)
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV3.vipPrice)
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV3.price)
        }
        spec_init_image.setOnClickListener {
            //showSkuDialog(ProductsDialogV3.price)
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV3.vipPrice)
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV3.price)
        }
        layout_fans_price.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV3.vipPrice)
                return@setOnClickListener
            }
            showSkuDialog(ProductsDialogV3.price)
        }
        layout_vip_price.setOnClickListener {
            if (!UserHelper.isLogin(this)){
                startActivity(Intent(this,LoginActivity::class.java))
                return@setOnClickListener
            }
            if ((productId == 1) or (productId == 2)){
                showSkuDialog(ProductsDialogV3.vipGroupPrice)
                return@setOnClickListener
            }
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                showSkuDialog(ProductsDialogV3.vipGroupPrice)
            } else {

                showBecomeVipDialog()
            }

        }
        layout_manufacture.setOnClickListener {
            val intent1 = Intent(this, StoreDetailActivity::class.java)
            intent1.putExtra("manfactureId", bean.data.manufacture?.manufactureId)
            this.startActivity(intent1)
        }
        attachScrollViewAndTabLayout()
    }

    fun showBecomeVipDialog(){
        MessageDialog.show(this, "", "????????????????????????", "????????????", "??????")
            .setOnCancelButtonClickListener(object : OnDialogButtonClickListener{
                override fun onClick(baseDialog: BaseDialog?, v: View?): Boolean {
                    baseDialog?.doDismiss()
                    return true
                }

            })
            .setOnOkButtonClickListener { baseDialog, v ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("productId", 1)
                startActivity(intent)
                return@setOnOkButtonClickListener true
            }
            /*.setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                @Override
                public boolean onClick(BaseDialog baseDialog, View v) {
                    return true;                    //???????????????????????????????????????????????????????????????
                }
            })*/
    }

    fun showSkuDialog(type : String){
        //ProductsDialog(ProductsDialog.vipPrice,bean).show(supportFragmentManager,"")
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //????????????????????????????????????????????????????????????
            .enableDrag(true)
            .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
            //                        .isThreeDrag(true) //???????????????????????????????????????enableDrag(false)?????????
            .asCustom(ProductsDialogV3(this,type,bean))
            .show()
    }

    fun loadData(){
        apiMall.loadProductDetail(UserInfoViewModel.getUserId(),productId.toString()).enqueue(object : Callback<ProductBean>{
            override fun onResponse(call: Call<ProductBean>, response: Response<ProductBean>) {
                if (response.body() != null){
                    val bean = response.body()
                    if (bean?.code == 200){
                        initView()
                        displayViewByData(bean)
                    }
                    else {
                        DialogUtil.toast(this@ProductDetailActivity,bean?.msg)
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
            com.qunshang.wenpaitong.equnshang.adapter.ProductDetailSpecAdapter(this, data)
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
        pregroupprice.setText("???" + skuList.vipGroupPrice)

        label_total.setText("??????????????????" + bean.data.vipGroupCard.total + "???")
        text_current.setText("??????" + bean.data.vipGroupCard.current + "???")
        group_progress.max = bean.data.vipGroupCard.total
        group_progress.progress = bean.data.vipGroupCard.current
        groupnumber.setText("???" + bean.data.vipGroupCard.currentGroup + "???")

        if (UserInfoViewModel.getUserInfo() != null){
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                fansprice.setText("???" + skuList.vipPrice)
                layout_benlai.visibility = View.VISIBLE
                benlaiprice.setText("???" + skuList.price)
                bottom_vipprice.setText("???" + skuList.vipPrice)
            } else {
                fansprice.setText("???" + skuList.price)
                layout_benlai.visibility = View.GONE
                bottom_vipprice.setText("???" + skuList.price)
            }
        }
        //origalprice.setText("$" + skuList.price)

        //bottom_originalprice.setText("$" + skuList.price)
        bottom_groupprice.setText("???" + skuList.vipGroupPrice)

        product_name.setText(bean.data.productName)

        initSpec(bean.data.skuList)
        //Glide.with(this).load(skuList.image).into(spec_init_image)

        Glide.with(this).load(bean.data.manufacture.manufactureLogoUrl).into(image_shop)
        name_shop.setText(bean.data.manufacture.manufactureName)

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        //manager.isSmoothScrollbarEnabled = true
        image_detail_list.layoutManager = manager
        image_detail_list.adapter = ProductImageDetailAdapter(this, bean.data.detailInfo)

        contactcustomer.setOnClickListener {
            DialogUtil.showCustomDialog(this,"mall")
        }
        layout_custom.setOnClickListener {
            DialogUtil.showCustomDialog(this,"mall")
        }
        if(bean.data.isCollect != 0){
            star.setText("????????????")
            control_star.setText("????????????")
        }
        star.setOnClickListener {
            ApiManager.getInstance().getApiMallTest().loadStarProductOLD(UserInfoViewModel.getUserId(), productId.toString()).enqueue(object : Callback<StarProductBeanOLD>{
                override fun onResponse(call: Call<StarProductBeanOLD>, response: Response<StarProductBeanOLD>) {
                    if (response.body() == null){
                        return
                    }
                    val bean = response.body()
                    if (bean!!.data){
                        star.setText("????????????")
                        control_star.setText("????????????")
                    } else {
                        star.setText("??????")
                        control_star.setText("????????????")
                    }
                }

                override fun onFailure(call: Call<StarProductBeanOLD>, t: Throwable) {

                }

            })
        }
        control_star.setOnClickListener {
            ApiManager.getInstance().getApiMallTest().loadStarProductOLD(UserInfoViewModel.getUserId(), productId.toString()).enqueue(object : Callback<StarProductBeanOLD>{
                override fun onResponse(call: Call<StarProductBeanOLD>, response: Response<StarProductBeanOLD>) {
                    val bean = response.body()
                    if (bean!!.data){
                        star.setText("????????????")
                        control_star.setText("????????????")
                    } else {
                        star.setText("??????")
                        control_star.setText("????????????")
                    }
                }

                override fun onFailure(call: Call<StarProductBeanOLD>, t: Throwable) {

                }

            })
        }

        spec_layout.setOnClickListener {
            showAttrubuteList()
        }
        attributes.setText(StringUtils.loadSpecStringTitles(bean.data.attributeList))
        handleIsControlPrice(bean)
    }

    fun handleIsControlPrice(bean : ProductBean?){
        if (bean!!.data.getIsControlPrice() != 0){
            layout1.visibility = View.GONE
            control_layout1.visibility = View.VISIBLE

            control_label_total.setText("??????????????????" + bean.data.vipGroupCard.total + "???")
            control_text_current.setText("??????" + bean.data.vipGroupCard.current + "???")
            control_group_progress.max = bean.data.vipGroupCard.total
            control_group_progress.progress = bean.data.vipGroupCard.current
            control_groupnumber.setText("???" + bean.data.vipGroupCard.currentGroup + "???")
            if (bean.data.skuList.size == 0){
                return
            }
            val sku = bean.data.skuList.get(0)
            control_finalprice.text = "???" + sku.vipGroupPrice
            control_originalprice.text = "???" + sku.controlPrice
            butieprice.text = "???" + (sku.controlPrice - sku.vipGroupPrice)
            layout_control.visibility = View.VISIBLE
            layout_uncontrol.visibility = View.GONE
            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                bottom_control_finalprice.text = "???" + sku.vipPrice
                bottom_control_originalprice.text = "???" + sku.controlPrice
                bottom_butieprice.text = "???" + (sku.controlPrice - sku.vipPrice)
            } else {
                bottom_control_finalprice.text = "???" + sku.price
                bottom_control_equal.visibility = View.GONE
                bottom_control_cut.visibility = View.GONE
                bottom_control_butie.visibility = View.GONE
            }
        } else {
            if ((productId != 1) and (productId != 2)){
                layout1.visibility = View.VISIBLE
            }
            control_layout1.visibility = View.GONE
        }
    }

    /*
    ????????????????????????????????????????????????????????????????????????????????????item_spec????????????????????????????????????????????????spec_name?????????????????????bottomtobottomof parent?????????
         ?????????????????????DIalog?????????????????????????????????????????????match_parent??????*/
    fun showAttrubuteList(){
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //????????????????????????????????????????????????????????????
            .enableDrag(true)
            .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
            //                        .isThreeDrag(true) //???????????????????????????????????????enableDrag(false)?????????
            .asCustom(SpecDialogV2(this,bean.data.attributeList))
            .show()
    }

    private var tabIndex = 0 //tablayout???????????????

    private var scrollviewFlag = false //???????????????scrollview?????????

    fun attachScrollViewAndTabLayout(){
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (!scrollviewFlag) {
                    when (tab.position) {
                        0 -> scrollview.smoothScrollTo(0, banner.getTop(),1000)
                        1 -> scrollview.smoothScrollTo(0, layout_detail.getTop(),1000)
                    }
                }
                //????????????tablayout??????????????????scrollview????????????
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
                    if (tabIndex != 0) { //???????????????????????????????????????tableIndex=0??????????????????????????????tablayout?????????
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