package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.fragment.QrCodeShareFragment

class QrCodeShareActivity : BaseActivity() {

    fun initViewByCommon(){
        setContentView(R.layout.activity_qr_code_share)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "邀请码"
        supportFragmentManager.beginTransaction().add(R.id.container,QrCodeShareFragment()).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init()
        initViewByCommon()
        //initViewByActivity(null)
    }
}