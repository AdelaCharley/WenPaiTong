package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.QiShiTongVideoFragment
import kotlinx.android.synthetic.main.activity_procure.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

//BMall
class ProcureActivity : BaseActivity() {

    companion object {
        var qishitongtype = 1
    }

    lateinit var fragment: QiShiTongVideoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_procure)
        changeToDefaultButTranslucent()
        Constants.shopflag = "bmall"
        qishitongtype = 2
        layout.setOnClickListener {
            /*val intent = Intent(this,BMallShopActivity::class.java)
            startActivity(intent)*/
        }
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_back.setImageDrawable(getDrawable(R.mipmap.arrow_left_white))
        toolbar_title.setText("拼采购")
        toolbar_title.setTextColor(resources.getColor(R.color.white))
        top.setBackgroundColor(resources.getColor(R.color.black))
        fragment = QiShiTongVideoFragment()
        supportFragmentManager.beginTransaction().add(R.id.container,fragment).commit()
        if (UserHelper.getIsFirstEnterProduce(this)){
            click_to_enter_procureshop.visibility = View.VISIBLE
            iknow.visibility = View.VISIBLE
            layout.isClickable = false
            iknow.setOnClickListener {
                UserHelper.setIsFirstEnterProduce(this,"11111")
                click_to_enter_procureshop.visibility = View.GONE
                iknow.visibility = View.GONE
                layout.isClickable = true
            }
        } else {
            click_to_enter_procureshop.visibility = View.GONE
            iknow.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        qishitongtype = 1
    }

    override fun onResume() {
        super.onResume()
        if (this::fragment.isInitialized){
            fragment.start()
        }
    }

}