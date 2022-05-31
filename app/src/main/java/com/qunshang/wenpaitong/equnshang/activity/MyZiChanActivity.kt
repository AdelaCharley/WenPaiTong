package com.qunshang.wenpaitong.equnshang.activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_my_zi_chan.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.*

class MyZiChanActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarColor(R.color.bg_layout_personal)
        setContentView(R.layout.activity_my_zi_chan)
        toolbar_title.setText("我的资产")
        toolbar_back.setOnClickListener { finish() }
        root_toolbar.setBackgroundColor(Color.parseColor("#F7F7F7"))
        val titles = ArrayList<String>()
        val fragments = ArrayList<Fragment>()
        titles.add("我的工分")
        titles.add("我的群票")
        titles.add("我的佣金")
        titles.add("我的版通")
        fragments.add(NewGongFenFragment())
        fragments.add(NewQunPiaoFragment())
        fragments.add(MyCommissionFragment())
        fragments.add(MyWBTCreditFragment())
        val pagerAdapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 4
        tablayout.setupWithViewPager(viewpager)
        val page = intent.getIntExtra("page",0)
        viewpager.setCurrentItem(page)
    }
}