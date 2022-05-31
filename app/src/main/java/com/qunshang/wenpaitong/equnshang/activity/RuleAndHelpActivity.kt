package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rule_and_help.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants

class RuleAndHelpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule_and_help)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("帮助和说明")
        layout_servicerule.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","协议")
            intent.putExtra("url",Constants.baseURL + "/rule/amallRule.html")
            startActivity(intent)
        }
        layout_ruleillu.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","AMall（社群商城）规则说明")
            intent.putExtra("url",Constants.baseURL + "/rule/amallSuggestion.html")
            startActivity(intent)
        }
        layout_commonquestion.setOnClickListener {
            val intent = Intent(this,CommonQuestionActivity::class.java)
            startActivity(intent)
        }
    }
}