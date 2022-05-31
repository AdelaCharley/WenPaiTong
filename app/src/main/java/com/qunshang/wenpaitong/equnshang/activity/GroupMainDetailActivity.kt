package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.GroupDetailShopFragment
import com.qunshang.wenpaitong.equnshang.fragment.GroupIncomeDetailFragment
import com.qunshang.wenpaitong.equnshang.fragment.MainPageGroupDetailFragment
import kotlinx.android.synthetic.main.activity_group_main_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class GroupMainDetailActivity : BaseActivity() {

    var id = -999

    var name : String? = ""

    lateinit var mainpage : MainPageGroupDetailFragment

    lateinit var income : GroupIncomeDetailFragment

    lateinit var shopf : GroupDetailShopFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main_detail)
        id = intent.getIntExtra("id",-999)
        name = intent.getStringExtra("name")
        if (id == -999){
            DialogUtil.toast(this,"出错了")
            return
        }
        if (com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(name)){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText(name)
        initView()
        showMain()
    }

    fun hideAllFragments(){
        val transaction = supportFragmentManager.beginTransaction()
        if(this::mainpage.isInitialized){
            transaction.hide(mainpage)
        }
        if(this::income.isInitialized){
            transaction.hide(income)
        }
        if(this::shopf.isInitialized) {
            transaction.hide(shopf)
        }
        transaction.commit();
    }

    fun showMain(){
        main.setTextColor(resources.getColor(R.color.blue))
        money.setImageResource(R.mipmap.group_money_unselected)
        shop.setTextColor(resources.getColor(R.color.grey))
        hideAllFragments()

        val transaction = getSupportFragmentManager().beginTransaction()
        if(!this::mainpage.isInitialized){
            mainpage = MainPageGroupDetailFragment(id,name!!)
            transaction.add(R.id.container,mainpage)
        }
        hideAllFragments()
        transaction.show(mainpage)
        transaction.commit()
    }

    fun showMoney(){
        if (this::mainpage.isInitialized){
            mainpage.pause()
        }
        main.setTextColor(resources.getColor(R.color.grey))
        money.setImageResource(R.mipmap.group_money_selected)
        shop.setTextColor(resources.getColor(R.color.grey))
        hideAllFragments()

        val transaction = getSupportFragmentManager().beginTransaction()
        if(!this::income.isInitialized){
            income = GroupIncomeDetailFragment(id)
            transaction.add(R.id.container,income)
        }
        hideAllFragments()
        transaction.show(income)
        transaction.commit()
    }

    fun showShop(){
        if (this::mainpage.isInitialized){
            mainpage.pause()
        }
        main.setTextColor(resources.getColor(R.color.grey))
        money.setImageResource(R.mipmap.group_money_unselected)
        shop.setTextColor(resources.getColor(R.color.blue))
        hideAllFragments()

        val transaction = getSupportFragmentManager().beginTransaction()
        if(!this::shopf.isInitialized){
            shopf = GroupDetailShopFragment(id)
            transaction.add(R.id.container,shopf)
        }
        hideAllFragments()
        transaction.show(shopf)
        transaction.commit()
    }

    fun initView(){
        main.setOnClickListener {
            showMain()
        }
        money.setOnClickListener {
            showMoney()
        }
        shop.setOnClickListener {
            showShop()
        }
    }

    override fun onBackPressed() {
        if (!mainpage.isOnBack()){
            super.onBackPressed()
        }
    }

}