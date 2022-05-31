package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.TipDialog
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_products_v3.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.AMallGoBuyActivity
import com.qunshang.wenpaitong.equnshang.adapter.SkuDialogAdapterV3
import com.qunshang.wenpaitong.equnshang.data.ProductBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ProductsDialogV3(context: Context, val type : String, val bean : ProductBean) : BottomPopupView(context) {

    companion object{
        //price为粉丝价、vipPrice为会员价、vipGroupPrice为会员拼团价
        val price = "price"

        val vipPrice = "vipPrice"

        val vipGroupPrice = "vipGroupPrice"

        val groupOwnerPrice = "groupOwnerPrice"

        var bean : ProductBean? = null

        lateinit var words : Array<String?>

    }

    var count = 1

    override fun onCreate() {
        super.onCreate()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        ProductsDialogV3.bean = this.bean
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView()
    }

    fun initView(){
        close.setOnClickListener {
            dismiss()
        }
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        skus.layoutManager = linearLayoutManager


        /*val beanforadapter = ArrayList<ProductBean.DataBean.SkuList>()
        beanforadapter.addAll(bean.data.skuList)
        specs.adapter = SpecsDialogAdapter(context, beanforadapter)*/
        initSpecAdapter()

        add.setOnClickListener {
            if ((bean.data.productId == 1) or (bean.data.productId == 2)){
                number.setText("1")
                return@setOnClickListener
            }
            count++
            number.setText(count.toString())
        }
        cut.setOnClickListener {
            if ((bean.data.productId == 1) or (bean.data.productId == 2)){
                number.setText("1")
                return@setOnClickListener
            }
            if (count <= 1){
                return@setOnClickListener
            }
            count --
            number.setText(count.toString())
        }
        buynow.setOnClickListener {
            goToBuy()
        }
        //update(bean.data.skuList.get(0))
        product_name.setText(bean.data.productName)
    }

    //实际上是SkuAdapter，我不知道名词所以就这样写了.
    fun initSpecAdapter(){
        val skuDialogAdapterV3 = SkuDialogAdapterV3(context,bean.data.specList)
        skus.adapter = skuDialogAdapterV3
        ProductsDialogV3.words = arrayOfNulls(bean.data.specList.size)
        skus.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver
        .OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if (!isInited){
                    EventBus.getDefault().post("init")
                    isInited = true
                }
            }
        })
    }

    var isInited = false

    override fun onDismiss() {
        super.onDismiss()
        ProductsDialogV3.bean = null
        EventBus.getDefault().post("destory")
        EventBus.getDefault().unregister(this)
    }

    fun goToBuy(){
        if (selectedSku == null){
            TipDialog.show(context as AppCompatActivity,"请先选择规格",TipDialog.TYPE.WARNING)
            return
        }
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
        selected.setText("已选:" + data.value)
        storecount.setText("库存:" + data.stock.toInt() + "件")
        selectedSku = data
        //bean.data.skuList.add(data)
    }

    var selectedSku : ProductBean.DataBean.SkuList? = null

    fun getPrice(data : ProductBean.DataBean.SkuList) : Double{
        if (type.equals(ProductsDialogV2.price)){
            return data.price
        } else if (type.equals(vipGroupPrice)){
            return data.vipGroupPrice
        } else if(type.equals(vipPrice)){
            return data.vipPrice
        } else if (type.equals(groupOwnerPrice)){
            return data.groupOwnerPrice
        }
        return data.price
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_products_v3
    }

    override fun onDestroy() {
        super.onDestroy()
        ProductsDialogV3.bean = null
        DialogUtil.toast(context,"我已经销毁了")
        EventBus.getDefault().unregister(this)
    }

}