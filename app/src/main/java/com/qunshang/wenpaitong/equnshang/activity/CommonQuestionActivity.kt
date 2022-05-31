package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_common_question.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.CommonQuestionAdapter
import com.qunshang.wenpaitong.equnshang.data.AMallQuestionBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class CommonQuestionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_question)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("常见问题")
        ApiManager.getInstance().getApiMallTest().loadAMallQuestions().enqueue(object : Callback<AMallQuestionBean>{
            override fun onResponse(call: Call<AMallQuestionBean>, response: Response<AMallQuestionBean>) {
                if (response.body() == null){
                    return
                }
                questions.adapter = CommonQuestionAdapter(this@CommonQuestionActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<AMallQuestionBean>, t: Throwable) {

            }

        })
    }
}