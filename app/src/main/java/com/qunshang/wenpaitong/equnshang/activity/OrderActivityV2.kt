package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_order_v2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.AddressBean
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.fragment.OrderFragmentV2
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class OrderActivityV2 : BaseActivity (){

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_v2)
        this.index = intent.getIntExtra("index",0)
        initView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    fun initView(){
        toolbar_right.setOnClickListener {
            val intent = Intent(this, AddressActivityV2::class.java)
            intent.putExtra("isSelect", false)
            startActivity(intent)
        }
        toolbar_back.setOnClickListener { finish() }
        val titles = ArrayList<String>()
        arrayOf("全部", "待支付","待成团","待发货","待收货","已完成","已关闭")
        titles.add("全部")
        titles.add("待付款")
        titles.add("待成团")
        titles.add("待发货")
        titles.add("待收货")
        titles.add("已完成")
        titles.add("已取消")
        titles.add("已关闭")
        //titles.add("已关闭")
        //titles.add("")
        val fragments = ArrayList<Fragment>()
        fragments.add(OrderFragmentV2("-1"))
        fragments.add(OrderFragmentV2("10"))
        fragments.add(OrderFragmentV2("20"))
        fragments.add(OrderFragmentV2("30"))
        fragments.add(OrderFragmentV2("40"))
        fragments.add(OrderFragmentV2("50"))
        fragments.add(OrderFragmentV2("70"))
        fragments.add(OrderFragmentV2("60"))
        //fragments.add(OrderFragmentV2("50"))
        //fragments.add(OrderFragment("60"))
        viewpager.offscreenPageLimit = 6
        viewpager.adapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        tablayout.setupWithViewPager(viewpager)
        viewpager.setCurrentItem(index)
        /*toolbar_right.setOnClickListener {
            Toast.makeText(this,"我的", Toast.LENGTH_SHORT).show()
            EventBus.getDefault().post("user_product_checkbox")
        }*/
    }

    companion object {

        var orderId = -999

        val TYPE_CHOOSE_ADDRESS = 200
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_CHOOSE_ADDRESS){
            if (resultCode == AddressAdapter.RESULT_CODE){
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                if (OrderActivity.orderId != -999){
                    ApiManager.getInstance().getApiMallTest().modifyAMallAddress(OrderActivity.orderId,address.id.toInt()).enqueue(object :
                        Callback<BaseHttpBean<Int>> {
                        override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {
                            if (response.body() == null){
                                return
                            }
                            if (response.body()!!.code == 200){
                                DialogUtil.showSuccessDialog(this@OrderActivityV2,"修改成功")
                            } else {
                                DialogUtil.showErrorDialog(this@OrderActivityV2,response.body()!!.msg)
                            }
                        }

                        override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {
                            DialogUtil.toast(this@OrderActivityV2,t.message)
                        }

                    })
                }
            }
        }
    }

}