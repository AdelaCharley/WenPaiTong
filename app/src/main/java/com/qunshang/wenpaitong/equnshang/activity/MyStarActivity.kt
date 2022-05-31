package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.ProductStarFragment
import com.qunshang.wenpaitong.equnshang.fragment.StoreStarFragment
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import kotlinx.android.synthetic.main.activity_my_star.*
import kotlinx.android.synthetic.main.activity_product_detail.toolbar_back
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MyStarActivity : BaseActivity (){

    var positon = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_my_star)
        toolbar_back.setOnClickListener { finish() }
        initView()
    }

    fun initView(){
        toolbar_back.setOnClickListener { finish() }
        val titles = ArrayList<String>()
        titles.add("商品收藏")
        titles.add("店铺关注")
            //arrayOf("商品收藏", "店铺关注")
        val fragments = ArrayList<Fragment>()
        fragments.add(ProductStarFragment())
        fragments.add(StoreStarFragment())
        viewpager.adapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        tablayout.setupWithViewPager(viewpager)
        toolbar_right.setOnClickListener {
            val fragment = fragments.get(0) as ProductStarFragment
            fragment.makeBottomVisbleOrUnVisble()
        }
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 1){
                    toolbar_right.visibility = View.GONE
                }
                if (position == 0){
                    toolbar_right.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
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