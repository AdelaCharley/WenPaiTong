package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View

import kotlinx.android.synthetic.main.activity_amall_v3_search.*
import kotlinx.android.synthetic.main.amallv3_search_top.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AMallV3SearchHistoryAdapter
import com.qunshang.wenpaitong.equnshang.db.DBUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class AMallV3SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_v3_search)
        initView()
    }

    fun initView(){
        list.adapter = AMallV3SearchHistoryAdapter(
            this,
            DBUtil.queryAMALLV3Data(this)
        )
        search.setOnClickListener { inserttoDBThenGo() }
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()== KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    inserttoDBThenGo()
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
        input.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0){
                    label_delete_text.visibility = View.INVISIBLE
                } else {
                    label_delete_text.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        back.setOnClickListener { finish() }
        delete.setOnClickListener {
            DBUtil.deteleAMALLV3All(this)
            list.adapter = AMallV3SearchHistoryAdapter(
                this,
                DBUtil.queryAMALLV3Data(this)
            )
        }
        label_delete_text.setOnClickListener {
            input.setText("")
        }
    }

    fun deleteOneThenUpdate(name : String){
        DBUtil.deleteAMALLV3Data(this,name)
        list.adapter = AMallV3SearchHistoryAdapter(
            this,
            DBUtil.queryAMALLV3Data(this)
        )
    }

    fun inserttoDBThenGo(){
        if (!StringUtils.isEmpty(input.text.toString())){
            DBUtil.insertAMallV3Data(this,input.text.toString())
            list.adapter = AMallV3SearchHistoryAdapter(
                this,
                DBUtil.queryAMALLV3Data(this)
            )
            val intent = Intent(this,AMallSearchResultV3Activity::class.java)
            intent.putExtra("keyword",input.text.toString())
            startActivity(intent)
        }
    }
}