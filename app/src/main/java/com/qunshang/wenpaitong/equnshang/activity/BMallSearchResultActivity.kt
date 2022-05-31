package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_bmall_search_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BMallProductAdapter
import com.qunshang.wenpaitong.equnshang.data.BMallProductBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class BMallSearchResultActivity : BaseActivity() {

    var keyword : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmall_search_result)
        this.keyword = intent.getStringExtra("keyword")
        if (StringUtils.isEmpty(keyword)){
            DialogUtil.toast(this,"出错了")
        }
        cancel.setOnClickListener { finish() }
        loadData()
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction() == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    this.keyword = input.text.toString()
                    loadData()
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

    fun loadData(){
        ApiManager.getInstance().getApiMallTest().loadBMallProducts(UserInfoViewModel.getUserId(),keyword!!,0).enqueue(object : Callback<BMallProductBean>{
            override fun onResponse(call: Call<BMallProductBean>, response: Response<BMallProductBean>) {
                if (response.body() == null){
                    return
                }
                list.adapter = BMallProductAdapter(this@BMallSearchResultActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<BMallProductBean>, t: Throwable) {

            }

        })
    }

}