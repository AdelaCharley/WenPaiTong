package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_my_group.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_back
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.MyGroupFragment

class MyGroupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_group)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("我的拼团")

        val titles = ArrayList<String>()
        titles.add("全部")
        titles.add("拼团中")
        titles.add("拼团成功")
        titles.add("拼团失败")

        val fragments = ArrayList<Fragment>()
        fragments.add(MyGroupFragment(0))
        fragments.add(MyGroupFragment(10))
        fragments.add(MyGroupFragment(20))
        fragments.add(MyGroupFragment(40))
        viewpager.offscreenPageLimit = 4
        viewpager.adapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        tablayout.setupWithViewPager(viewpager)
    }
}