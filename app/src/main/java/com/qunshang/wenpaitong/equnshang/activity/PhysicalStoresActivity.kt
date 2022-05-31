package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.PhysicalStoresFragment

class PhysicalStoresActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physical_stores)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "实体店"
        loadData()
    }

    fun loadData(){
        supportFragmentManager.beginTransaction().add(R.id.container,PhysicalStoresFragment()).commit()
    }

}