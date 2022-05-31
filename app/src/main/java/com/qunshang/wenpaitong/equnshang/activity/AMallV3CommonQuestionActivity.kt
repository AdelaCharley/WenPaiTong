package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_amall_v3_common_question.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R

class AMallV3CommonQuestionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_v3_common_question)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("常见问题")
        text1.setOnClickListener {
            val intent = Intent(this,AMallV3CommonQuestionDetailActivity::class.java)
            intent.putExtra("title","拼团有效期是多久？")
            intent.putExtra("content","拼团时间期限为开团后24小时，统一以商品的开团结束时间为准。")
            startActivity(intent)
        }
        text2.setOnClickListener {
            val intent = Intent(this,AMallV3CommonQuestionDetailActivity::class.java)
            intent.putExtra("title","什么情况下会拼团失败？")
            intent.putExtra("content","1.超过成团有效期24小时，未达成相应参团人数的要求，则该团失败。\n\n2.在团有效期24小时内，商品已提前售罄，若还未拼团成功，则该团失败。\n\n" +
                    "3.高峰期间，同时支付的人过多，团人数有限制，超出该团人数限制的部分用户则会拼团失败。" )
            startActivity(intent)
        }
        text3.setOnClickListener {
            val intent = Intent(this,AMallV3CommonQuestionDetailActivity::class.java)
            intent.putExtra("title","拼团失败后我的钱款去向何处？")
            intent.putExtra("content","拼团失败的订单，系统会在1-2个工作日内处理退款，系统处理后1-10个工作日内原路退回至原支付账户中。")
            startActivity(intent)
        }
    }
}