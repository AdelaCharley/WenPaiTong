package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_wen_ban_tong_order_list.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.WenBanTongOrderListFragment

class WenBanTongOrderListActivity : BaseActivity (){

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wen_ban_tong_order_list)
        this.index = intent.getIntExtra("index",0)
        initView()
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        val titles = ArrayList<String>()
        titles.add("全部")
        titles.add("已支付")
        titles.add("已提货")
        //titles.add("已退款")
        val fragments = ArrayList<Fragment>()
        fragments.add(WenBanTongOrderListFragment("0"))
        fragments.add(WenBanTongOrderListFragment("20"))
        fragments.add(WenBanTongOrderListFragment("30"))
        //fragments.add(WenBanTongOrderListFragment("40"))
        viewpager.offscreenPageLimit = 4
        viewpager.adapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        tablayout.setupWithViewPager(viewpager)
        viewpager.setCurrentItem(index)
    }
}