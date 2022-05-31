package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choose_discount_ticket.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ChooseDiscountTicketAdapter
import com.qunshang.wenpaitong.equnshang.data.DiscountTicketBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ChooseDiscountTicketActivity : BaseActivity() {

    var productId = -99

    var price = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_choose_discount_ticket)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("优惠券")
        this.productId = intent.getIntExtra("productId",-99)
        this.price = intent.getDoubleExtra("price",0.0)
        if (productId == -99){
            DialogUtil.toast(this,"出错了")
            return
        }
        cancel.setOnClickListener {
            val intent = Intent()
            val bean = DiscountTicketBean.DataBean()
            bean.price = 0.0
            bean.userCouponsId = -66
            intent.putExtra("data",bean)
            setResult(ChooseDiscountTicketAdapter.TYPE_CHOOSE_TICKET_RESULT,intent)
            finish()
        }
        ApiManager.getInstance().getApiMallTest().loadProductDiscountTicket(UserInfoViewModel.getUserId(),productId,3).enqueue(object : Callback<DiscountTicketBean>{
            override fun onResponse(
                call: Call<DiscountTicketBean>,
                response: Response<DiscountTicketBean>
            ) {
                if (response.body() == null){
                    return
                }
                adapter = ChooseDiscountTicketAdapter(this@ChooseDiscountTicketActivity,response.body()!!.data,price)
                list.adapter = adapter
            }

            override fun onFailure(call: Call<DiscountTicketBean>, t: Throwable) {
                //Log.i(Constants.logtag,t.message)
            }

        })
        confirm.setOnClickListener {
            if (this::adapter.isInitialized){
                if (adapter.selectedTicket == null){
                    //DialogUtil.toast(this,"没有可选的优惠券")
                    return@setOnClickListener
                }
                if (adapter.selectedTicket.useType.equals("all")){
                } else if (adapter.selectedTicket.useType.equals("product") and (productId == adapter.selectedTicket.relationId)){
                } else {
                    DialogUtil.toast(this,"当前优惠券不可用")
                    return@setOnClickListener
                }
                val intent = Intent()
                intent.putExtra("data",adapter.selectedTicket)
                setResult(ChooseDiscountTicketAdapter.TYPE_CHOOSE_TICKET_RESULT,intent)
                finish()
            }
        }
    }

    lateinit var adapter : ChooseDiscountTicketAdapter

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)//这个地方使用ThreadMode.Main是无法刷新UI的，刷新UI需要使用MAIN_ORDERED
    fun def(bean : DiscountTicketBean.DataBean){
        discountfee.setText("店铺优惠￥" + bean.price.toString())
        //DialogUtil.toast(this,"我接受到了" + bean.price)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}