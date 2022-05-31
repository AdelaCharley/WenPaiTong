package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_express_detail_v2.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.ExpressMsgAdapterV2
import com.qunshang.wenpaitong.equnshang.data.ExpressBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ExpressDetailActivityV2 : BaseActivity() {

    var orderId : Int = -999

    var type = "amall"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_express_detail_v2)
        if (intent.getIntExtra("orderId",-999) == -999 && StringUtils.isEmpty(intent.getStringExtra("orderSn"))){
            DialogUtil.toast(this,"出错了")
            return
        } else {
            orderId = intent.getIntExtra("orderId",-999)
        }
        Log.i(Constants.logtag,orderId.toString() + "")
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
            ApiManager.getInstance().getApiMallTest().loadAMallExpress(orderId.toString()).enqueue(object :
                Callback<ExpressBean> {
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
            ApiManager.getInstance().getApiMallTest().loadBMallExpress(orderId.toString()).enqueue(object :
                Callback<ExpressBean> {
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
        } else if (type.equals("wenbantong")){
            //var orderSn = "20220107354101535"
            var orderSn = intent.getStringExtra("orderSn")
            StringUtils.log(orderSn)
            ApiManager.getInstance().getApiMallTest().loadWenBanTongExpress(orderSn!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<ExpressBean>{
                    override fun accept(t: ExpressBean) {
                        if (t.code == 200){
                            initView(t)
                        }
                    }
                },object : Consumer<Throwable>{
                    override fun accept(t: Throwable) {
                        DialogUtil.toast(this@ExpressDetailActivityV2,"查询出错")
                    }

                })
        }

    }

    fun initView(bean : ExpressBean){
        view.visibility = View.VISIBLE
        Glide.with(this).load(bean.data.express.expressLogo).into(image)
        expresscompany.setText("物流公司：" + bean.data.express.expressName)
        expressnumber.setText("运单号码：" + bean.data.express.expressSN)
        expressphone.setText("物流电话：" + bean.data.express.expressPhone)
        list.adapter = ExpressMsgAdapterV2(this, bean.data.expressMsg)
    }

}