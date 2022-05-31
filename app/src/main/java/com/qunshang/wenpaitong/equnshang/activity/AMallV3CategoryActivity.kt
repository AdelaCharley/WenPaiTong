package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.AMallV3CategoryFragment

class AMallV3CategoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_v3_category)
        supportFragmentManager.beginTransaction().add(R.id.container,AMallV3CategoryFragment()).commit()
    }
}