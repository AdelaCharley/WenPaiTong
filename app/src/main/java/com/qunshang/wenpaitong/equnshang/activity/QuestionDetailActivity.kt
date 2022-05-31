package com.qunshang.wenpaitong.equnshang.activity


import android.os.Bundle
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class QuestionDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("")
        val titlestr = intent.getStringExtra("title")
        val contentstr = intent.getStringExtra("content")
        if ((StringUtils.isEmpty(titlestr)) or StringUtils.isEmpty(contentstr)){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_title.setText(titlestr)
        titles.setText(titlestr)
        contents.setText(contentstr)
    }
}