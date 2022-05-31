package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.LotteryFragment

class LaiLingJiangActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeToDefaultButTranslucent()
        setContentView(R.layout.activity_lai_ling_jiang)
        supportFragmentManager.beginTransaction().add(R.id.container,LotteryFragment()).commit()
    }
}