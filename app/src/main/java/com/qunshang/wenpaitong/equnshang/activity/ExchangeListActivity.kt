package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_discount_v2.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.ExchangeListFragment

class ExchangeListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_list)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "退换/售后"
        val titles = ArrayList<String>()
        titles.add("全部")
        titles.add("退款")
        titles.add("退货退款")
        titles.add("换货")
        //arrayOf("待使用","已使用","已失效")
        val list = ArrayList<Fragment>()
        list.add(ExchangeListFragment(null))
        list.add(ExchangeListFragment("10"))
        list.add(ExchangeListFragment("20"))
        list.add(ExchangeListFragment("30"))
        viewpager.adapter = BasePagerAdapter(supportFragmentManager,list, titles)
        viewpager.offscreenPageLimit = 4
        tabs.setupWithViewPager(viewpager)
    }
}