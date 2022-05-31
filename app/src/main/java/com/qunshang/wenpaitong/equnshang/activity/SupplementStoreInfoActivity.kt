package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_supplement_store_info.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R

class SupplementStoreInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplement_store_info)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("店铺简介")
        submit.setOnClickListener {
            val intent = Intent()
            intent.putExtra("info",info.text.toString())
            setResult(OpenStoreActivity.INFO_RESULT_CODE,intent)
            finish()
        }
    }
}