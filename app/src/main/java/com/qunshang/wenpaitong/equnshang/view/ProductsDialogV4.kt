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
import kotlinx.android.synthetic.main.dialog_products_v4.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.AMallGoBuyActivityV2
import com.qunshang.wenpaitong.equnshang.adapter.SkuDialogAdapterV4
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2

class ProductsDialogV4(context: Context, val type : String, val bean : ProductBeanV2,
                       val orderGroupSn : String? = null,val isMemberForGroup : Boolean = false//是否是成员参与拼团
) : BottomPopupView(context) {

    companion object{
        //price为粉丝价、vipPrice为会员价、vipGroupPrice为会员拼团价
        val price = "price"

        val vipPrice = "vipPrice"

        val vipGroupPrice = "vipGroupPrice"

        val groupOwnerPrice = "groupOwnerPrice"

        var bean : ProductBeanV2? = null

        lateinit var words : Array<String?>

    }

    var count = 1

    override fun onCreate() {
        super.onCreate()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        ProductsDialogV4.bean = this.bean
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
        //product_name.setText(bean.data.productName)
    }

    //实际上是SkuAdapter，我不知道名词所以就这样写了.
    fun initSpecAdapter(){
        val skuDialogAdapterV4 = SkuDialogAdapterV4(context,bean.data.specList)
        skus.adapter = skuDialogAdapterV4
        ProductsDialogV4.words = arrayOfNulls(bean.data.specList.size)
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
        ProductsDialogV4.bean = null
        EventBus.getDefault().post("destory")
        EventBus.getDefault().unregister(this)
    }

    fun goToBuy(){
        if (selectedSku == null){
            TipDialog.show(context as AppCompatActivity,"请先选择规格", TipDialog.TYPE.WARNING)
            return
        }
        val intent = Intent(context, AMallGoBuyActivityV2::class.java)
        intent.putExtra("sku",selectedSku)
        intent.putExtra("count",count)
        intent.putExtra("data",bean)
        intent.putExtra("priceMode",type)
        intent.putExtra("orderGroupSn",orderGroupSn)
        intent.putExtra("ismemberforgroup",isMemberForGroup)
        context?.startActivity(intent)

        dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(data : ProductBeanV2.DataBean.SkuList){
        Glide.with(this).load(data.image).into(image_spec)
        price.setText("" + getPrice(data))
        //group_price.setText("￥" + getPrice(data))
        selected.setText("已选择：" + data.value)
        storecount.setText("库存" + data.stock.toInt() + "件")
        selectedSku = data
        //bean.data.skuList.add(data)
    }

    var selectedSku : ProductBeanV2.DataBean.SkuList? = null

    fun getPrice(data : ProductBeanV2.DataBean.SkuList) : Double{
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
        return R.layout.dialog_products_v4
    }

    override fun onDestroy() {
        super.onDestroy()
        ProductsDialogV4.bean = null
        EventBus.getDefault().unregister(this)
    }

}