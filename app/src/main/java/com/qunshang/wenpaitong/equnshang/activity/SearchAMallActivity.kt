package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.view.KeyEvent
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.activity_search_amall.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.adapter.SearchHistoryAdapter

class SearchAMallActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_amall)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("搜索")
        initView()
    }

    fun initView(){
        val flowLayoutManager = com.qunshang.wenpaitong.equnshang.view.AutoLineFeedLayoutManager()
//设置每一个item间距
        list.addItemDecoration(
            com.qunshang.wenpaitong.equnshang.view.SpaceItemDecoration(
                com.qunshang.wenpaitong.equnshang.utils.CommonUtil.dp2px(
                    this,
                    10
                )
            )
        );
        list.setLayoutManager(flowLayoutManager)
        //list.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
        list.adapter = SearchHistoryAdapter(
            this,
            com.qunshang.wenpaitong.equnshang.db.DBUtil.queryData(this)
        )
        cancel.setOnClickListener {
            finish()
        }
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()==KeyEvent.ACTION_UP) {
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
        delete.setOnClickListener {
            com.qunshang.wenpaitong.equnshang.db.DBUtil.deteleAll(this)
            list.adapter = SearchHistoryAdapter(
                this,
                com.qunshang.wenpaitong.equnshang.db.DBUtil.queryData(this)
            )
        }
    }



    fun inserttoDBThenGo(){
        if (!com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(input.text.toString())){
            com.qunshang.wenpaitong.equnshang.db.DBUtil.insertData(this,input.text.toString())
            list.adapter = SearchHistoryAdapter(
                this,
                com.qunshang.wenpaitong.equnshang.db.DBUtil.queryData(this)
            )
            val intent = Intent(this,SearchResultActivity::class.java)
            intent.putExtra("keyword",input.text.toString())
            startActivity(intent)
        }
    }

}