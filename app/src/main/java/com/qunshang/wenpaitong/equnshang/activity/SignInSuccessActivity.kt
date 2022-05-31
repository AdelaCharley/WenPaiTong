package com.qunshang.wenpaitong.equnshang.activity


import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in_success.*
import com.qunshang.wenpaitong.R

class SignInSuccessActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_success)
        close.setOnClickListener { finish() }
        confirm.setOnClickListener { finish() }
    }
}