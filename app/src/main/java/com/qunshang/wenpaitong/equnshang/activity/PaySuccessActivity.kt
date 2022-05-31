package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pay_success.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R

class PaySuccessActivity : BaseActivity() {

    var malltype : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_success)
        val orderId = intent.getIntExtra("orderId",0)
        val price = intent.getStringExtra("price")
        malltype = intent.getStringExtra("malltype")
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("支付成功")
        payprice.setText("￥" + price.toString())
        if ("amall".equals(malltype)){
            seeorder.setOnClickListener {
                val intent = Intent(this,OrderDetailActivity::class.java)
                intent.putExtra("id",orderId)
                startActivity(intent)
            }
        } else {
            seeorder.setOnClickListener {
                val intent = Intent(this, MyBuyOrderDetailActivity::class.java)
                intent.putExtra("id", orderId)
                startActivity(intent)
            }
        }

        backshop.setOnClickListener {
            val intent = Intent(this,AMallActivity::class.java)
            startActivity(intent)
        }
    }
}