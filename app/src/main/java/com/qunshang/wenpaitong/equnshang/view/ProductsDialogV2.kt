package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_products.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.AMallGoBuyActivity
import com.qunshang.wenpaitong.equnshang.adapter.SpecsDialogAdapter
import com.qunshang.wenpaitong.equnshang.data.ProductBean
import kotlin.collections.ArrayList

class ProductsDialogV2(context: Context, val type : String, val bean : ProductBean) : BottomPopupView(context) {

    companion object{
        //price为粉丝价、vipPrice为会员价、vipGroupPrice为会员拼团价
        val price = "price"

        val vipPrice = "vipPrice"

        val vipGroupPrice = "vipGroupPrice"
    }

    var count = 1

    override fun onCreate() {
        super.onCreate()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView()
    }

    fun initView(){
        close.setOnClickListener {
            dismiss()
        }
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        skus.layoutManager = linearLayoutManager

        val beanforadapter = ArrayList<ProductBean.DataBean.SkuList>()
        beanforadapter.addAll(bean.data.skuList)
        skus.adapter = SpecsDialogAdapter(context, beanforadapter)

        add.setOnClickListener {
            count++
            number.setText(count.toString())
        }
        cut.setOnClickListener {
            count --
            number.setText(count.toString())
        }
        buynow.setOnClickListener {
            goToBuy()
        }
        update(bean.data.skuList.get(0))
        product_name.setText(bean.data.productName)
    }

    fun goToBuy(){
        val intent = Intent(context, AMallGoBuyActivity::class.java)
        intent.putExtra("sku",selectedSku)
        intent.putExtra("count",count)
        intent.putExtra("data",bean)
        intent.putExtra("priceMode",type)
        context?.startActivity(intent)

        dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(data : ProductBean.DataBean.SkuList){
        Glide.with(this).load(data.image).into(image_spec)
        price.setText("￥" + getPrice(data))
        //group_price.setText("￥" + getPrice(data))
        selected.setText("已选" + data.value)
        storecount.setText("库存" + data.stock.toInt() + "件")
        selectedSku = data
        //bean.data.skuList.add(data)
    }

    lateinit var selectedSku : ProductBean.DataBean.SkuList

    fun getPrice(data : ProductBean.DataBean.SkuList) : Double{
        if (type.equals(ProductsDialogV2.price)){
            return data.price
        } else if (type.equals(vipGroupPrice)){
            return data.vipGroupPrice
        } else if(type.equals(vipPrice)){
            return data.vipPrice
        }
        return data.price
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_products
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}