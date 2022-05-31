package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_bmall_search.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class BMallSearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmall_search)
        cancel.setOnClickListener { finish() }
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    doSearch()
                } else if (keyCode == KeyEvent.KEYCODE_DEL){
                    if (input.text.length != 0){
                        input.text.delete(input.text.length - 1, input.text.length)
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish()
                }
            }

            return@setOnKeyListener true
        }
    }

    fun doSearch(){
        if (StringUtils.isEmpty(input.text.toString())){
            DialogUtil.toast(this,"输入关键字为空")
            return
        }
        val intent = Intent(this,BMallSearchResultActivity::class.java)
        intent.putExtra("keyword",input.text.toString())
        startActivity(intent)
    }

}