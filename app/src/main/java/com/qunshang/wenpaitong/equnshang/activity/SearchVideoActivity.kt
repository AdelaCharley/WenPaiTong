package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View

import androidx.recyclerview.widget.GridLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_search_video.*
import kotlinx.android.synthetic.main.activity_search_video.input
import kotlinx.android.synthetic.main.activity_search_video.list
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.SearchVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class SearchVideoActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_video)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("搜索")
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()==KeyEvent.ACTION_UP) {
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
            if (keyCode == KeyEvent.KEYCODE_ENTER){

            } else if (keyCode == KeyEvent.KEYCODE_BACK){
                finish()
            }
            return@setOnKeyListener true
        }
        search.setOnClickListener {
            doSearch()
        }
    }

    fun doSearch(){
        CommonUtil.hideKeyboard(input)
        if (StringUtils.isEmpty(input.text.toString().trim())){
            DialogUtil.toast(this,"输入内容不可为空")
            return
        }
        ApiManager.getInstance().getApiVideoTest().loadSearchedVideoList(UserInfoViewModel.getUserId(),input.text.toString()).enqueue(object : Callback<RecommendVideoBean>{
            override fun onResponse(call: Call<RecommendVideoBean>, response: Response<RecommendVideoBean>) {
                if (response.body() != null){
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<RecommendVideoBean>, t: Throwable) {

            }

        })
    }

    fun initView(bean : RecommendVideoBean){
        if (bean.data.size == 0){
            layoutempty.visibility = View.VISIBLE
            list.visibility = View.GONE
            return
        } else {
            layoutempty.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
        val manager = GridLayoutManager(this,2)
        list.layoutManager = manager
        list.adapter = SearchVideoAdapter(this, bean.data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

}