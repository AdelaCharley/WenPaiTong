package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_apply_log_out_failed.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R

class ApplyLogOutFailedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_log_out_failed)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("申请注销账号")
        reason.setText(intent.getStringExtra("reason"))
        iknow.setOnClickListener { finish() }
    }
}