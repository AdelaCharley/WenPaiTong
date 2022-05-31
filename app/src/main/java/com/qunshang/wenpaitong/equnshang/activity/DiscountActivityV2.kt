package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_discount_v2.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.DiscountFragmentV2

class DiscountActivityV2 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount_v2)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "优惠券"
        val titles = ArrayList<String>()
        titles.add("待使用")
        titles.add("已使用")
        titles.add("已过期")
        //arrayOf("待使用","已使用","已失效")
        val list = ArrayList<Fragment>()
        list.add(DiscountFragmentV2("10"))
        list.add(DiscountFragmentV2("20"))
        list.add(DiscountFragmentV2("30"))
        viewpager.adapter = BasePagerAdapter(supportFragmentManager,list, titles)
        viewpager.offscreenPageLimit = 3
        tabs.setupWithViewPager(viewpager)
    }
}