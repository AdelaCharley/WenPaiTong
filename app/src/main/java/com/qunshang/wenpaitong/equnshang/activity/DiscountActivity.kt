package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.DiscountFragment
import kotlinx.android.synthetic.main.activity_discount.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class DiscountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "优惠券"
        val titles = ArrayList<String>()
        titles.add("待使用")
        titles.add("已使用")
        titles.add("已失效")
            //arrayOf("待使用","已使用","已失效")
        val list = ArrayList<Fragment>()
        list.add(DiscountFragment("10"))
        list.add(DiscountFragment("20"))
        list.add(DiscountFragment("30"))
        viewpager.adapter = BasePagerAdapter(supportFragmentManager,list, titles)
        tabs.setupWithViewPager(viewpager)
    }

}