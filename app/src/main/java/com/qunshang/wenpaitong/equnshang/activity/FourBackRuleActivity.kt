package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R

class FourBackRuleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_four_back_rule)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("满四返一规则")
    }
}