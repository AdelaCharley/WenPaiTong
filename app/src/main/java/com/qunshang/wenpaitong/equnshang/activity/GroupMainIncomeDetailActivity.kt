package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.fragment.GroupMainIncomeDetailFragment
import kotlinx.android.synthetic.main.activity_group_main_income_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean

class GroupMainIncomeDetailActivity : BaseActivity() {

    lateinit var data : IncomeOfOneGroupMainBean.DataBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main_income_detail)
        this.data = intent.getSerializableExtra("data") as IncomeOfOneGroupMainBean.DataBean
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("文版通收入")
        if ((this.data == null) or (!this::data.isInitialized)){
            return
        }
        Glide.with(this).load(data.productImage).into(image)
        videoname.setText(data.productName)
        total.setText(this.data.productIncome.toString())
        val titles = ArrayList<String>()
        titles.add("待结算")
        titles.add("已结算")
        val fragments = ArrayList<Fragment>()
        fragments.add(GroupMainIncomeDetailFragment(data.productId,10))
        fragments.add(GroupMainIncomeDetailFragment(data.productId,20))
        val adapter = BasePagerAdapter(supportFragmentManager,fragments,titles)
        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)
    }
}