package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.AMallFragment
import kotlinx.android.synthetic.main.activity_amall_v2.*
import com.qunshang.wenpaitong.equnshang.Constants

class AMallActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_v2)
        Constants.shopflag = "amall"
        toolbar_back.setOnClickListener { finish() }
        val fragment = AMallFragment()
        fragment.setHideTopView(true)
        ruleandhelp.setOnClickListener { startActivity(Intent(this,RuleAndHelpActivity::class.java)) }
        supportFragmentManager.beginTransaction().add(R.id.container,fragment).commit()
    }
}