package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.TipDialog
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_buy_product.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.R
//import com.qunshang.wenpaitong.equnshang.activity.BMallGoBuyActivity
import com.qunshang.wenpaitong.equnshang.adapter.BuyListSelectedSkuAdapter
import com.qunshang.wenpaitong.equnshang.adapter.SpecsDialogAdapter
import com.qunshang.wenpaitong.equnshang.data.ProductBean

class BuyProductDialog (context: Context, val bean : ProductBean) : BottomPopupView(context) {

    var count = 1

    var minCount = 0

    override fun onCreate() {
        super.onCreate()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
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
            if (count <= minCount){
                return@setOnClickListener
            }
            count --
            number.setText(count.toString())
        }
        buynow.setOnClickListener {
            goToBuy()
        }
        list.adapter = selectedSkuAdapter
        update(bean.data.skuList.get(0))
        product_name.setText(bean.data.productName)
        add_to_list.setOnClickListener {
            add()
        }
    }

    val selectedSkuAdapter = BuyListSelectedSkuAdapter(context)

    fun add(){
        //Log.i("counttest","" + count)
        //currentSku.buyCount = count
        selectedSkuAdapter.add(currentSku,count)
        //Log.i("counttest","" + count)
    }

    fun goToBuy(){
        /*if (selectedSkuAdapter.selectedSkus.size == 0){
            TipDialog.show(context as AppCompatActivity,"您还没有选择商品",TipDialog.TYPE.WARNING)
            return
        }
        val intent = Intent(context, BMallGoBuyActivity::class.java)
        intent.putExtra("skus",selectedSkuAdapter.selectedSkus)
        intent.putExtra("data",bean)
        context?.startActivity(intent)
        dismiss()*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(data : ProductBean.DataBean.SkuList){
        Glide.with(this).load(data.image).into(image_spec)
        price.setText("￥" + data.price)
        selected.setText("已选" + data.value)
        storecount.setText("库存" + data.stock.toInt() + "件")
        minCount = data.minQuantity
        count = minCount
        number.setText(minCount.toString())
        currentSku = data
    }



    lateinit var currentSku : ProductBean.DataBean.SkuList

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_buy_product
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}