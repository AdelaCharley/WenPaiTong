package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_express_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.ExpressBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ExpressDetailActivity : BaseActivity() {

    var orderId : Int = -999

    var type = "amall"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_express_detail)
        if (intent.getIntExtra("orderId",-999) == -999){
            DialogUtil.toast(this,"出错了")
            return
        } else {
            orderId = intent.getIntExtra("orderId",-999)
            //DialogUtil.toast(this,"orderid" + orderId)
        }
        val type : String? = intent.getStringExtra("type")
        if (!StringUtils.isEmpty(type)){
            this.type = "amall"
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("物流信息")
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        if (type.equals("amall")){
            ApiManager.getInstance().getApiMallTest().loadAMallExpress(orderId.toString()).enqueue(object : Callback<ExpressBean>{
                override fun onResponse(call: Call<ExpressBean>, response: Response<ExpressBean>) {
                    if (response.body() != null){
                        val bean = response.body()!!
                        if (response.body()!!.code == 200){
                            initView(bean)
                        }
                    }
                }

                override fun onFailure(call: Call<ExpressBean>, t: Throwable) {

                }

            })
        } else if (type.equals("bmall")){
            ApiManager.getInstance().getApiMallTest().loadBMallExpress(orderId.toString()).enqueue(object : Callback<ExpressBean>{
                override fun onResponse(call: Call<ExpressBean>, response: Response<ExpressBean>) {
                    if (response.body() != null){
                        val bean = response.body()!!
                        if (response.body()!!.code == 200){
                            initView(bean)
                        }
                    }
                }

                override fun onFailure(call: Call<ExpressBean>, t: Throwable) {

                }

            })
        }

    }

    fun initView(bean : ExpressBean){
        Glide.with(this).load(bean.data.product.productImage).into(image_product)
        name_product.setText(bean.data.product.productName)
        expressprovider.setText(bean.data.express.expressName)
        expressid.setText("运单编号" + bean.data.express.expressSN)
        list.adapter = com.qunshang.wenpaitong.equnshang.adapter.ExpressMsgAdapter(this, bean.data.expressMsg)
    }

}