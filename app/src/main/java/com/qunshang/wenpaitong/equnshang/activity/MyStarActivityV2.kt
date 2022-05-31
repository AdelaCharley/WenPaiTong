package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_my_star_v2.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.ProductStarFragmentV2
import com.qunshang.wenpaitong.equnshang.fragment.StoreStarFragmentV2

class MyStarActivityV2 : BaseActivity() {

    var positon = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_my_star_v2)
        toolbar_back.setOnClickListener { finish() }
        initView()
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("我的收藏")
        val titles = ArrayList<String>()
        titles.add("商品")
        titles.add("店铺")
        //arrayOf("商品收藏", "店铺关注")
        val fragments = ArrayList<Fragment>()
        fragments.add(ProductStarFragmentV2())
        fragments.add(StoreStarFragmentV2())
        viewpager.adapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        tablayout.setupWithViewPager(viewpager)
        positon = intent.getIntExtra("position",0)
        viewpager.setCurrentItem(positon)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun default(string: String){

    }
}