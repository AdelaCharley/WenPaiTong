package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wen_ban_tong_pay_success_actviity.*
import kotlinx.android.synthetic.main.layout_pay_success.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants

class WenBanTongPaySuccessActviity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wen_ban_tong_pay_success_actviity)
        pay_success_image.setImageDrawable(resources.getDrawable(R.mipmap.wenbantong_paysuccess))
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("")
        tv_price.setText("订单金额：￥" + intent.getStringExtra("price"))
        btn_left.setOnClickListener {
            val intent = Intent(this, WenBanTongOrderDetailActivity::class.java)
            intent.putExtra("orderSn", getIntent().getStringExtra("orderSn"))
            startActivity(intent)
        }
        btn_right.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            Constants.isMainPageNeedToChangePage = true
            Constants.pageNeedToBeChange = 1
            startActivity(intent)
        }
    }
}