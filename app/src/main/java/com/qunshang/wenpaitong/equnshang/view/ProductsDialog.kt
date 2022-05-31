package com.qunshang.wenpaitong.equnshang.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.AMallGoBuyActivity
import kotlinx.android.synthetic.main.dialog_products.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ProductsDialog(val type : String,val bean : com.qunshang.wenpaitong.equnshang.data.ProductBean) : BaseBottomSheetDialog() {

    companion object{
        //price为粉丝价、vipPrice为会员价、vipGroupPrice为会员拼团价

        val price = "price"

        val vipPrice = "vipPrice"

        val vipGroupPrice = "vipGroupPrice"
    }

    var count = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        return inflater.inflate(R.layout.dialog_products,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){
        close.setOnClickListener {
            dismiss()
        }
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        skus.layoutManager = linearLayoutManager

        val beanforadapter = ArrayList<com.qunshang.wenpaitong.equnshang.data.ProductBean.DataBean.SkuList>()
        beanforadapter.addAll(bean.data.skuList)
        skus.adapter = com.qunshang.wenpaitong.equnshang.adapter.SpecsDialogAdapter(context, beanforadapter)

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
    }

    fun goToBuy(){
        val intent = Intent(context,AMallGoBuyActivity::class.java)
        intent.putExtra("count",count)
        intent.putExtra("data",bean)
        intent.putExtra("priceMode",type)
        context?.startActivity(intent)

        dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(data : com.qunshang.wenpaitong.equnshang.data.ProductBean.DataBean.SkuList){
        //DialogUtil.toast(context,"你好啊，我更新了事件")
        Glide.with(this).load(data.image).into(image_spec)
        price.setText("$" + data.vipGroupPrice.toString())
        //group_price.setText(data.vipGroupPrice.toString())
        selected.setText("已选" + data.value)
        storecount.setText("库存" + data.stock.toString())
        bean.data.skuList.removeAll (bean.data.skuList)
        bean.data.skuList.add(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }


    override val height: Int
        get() = dp2px(requireContext(),400.0f)

}