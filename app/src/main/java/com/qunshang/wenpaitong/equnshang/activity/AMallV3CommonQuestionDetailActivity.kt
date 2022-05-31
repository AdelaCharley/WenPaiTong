package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_amall_v3_common_question_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R

class AMallV3CommonQuestionDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_v3_common_question_detail)
        toolbar_title.setText("问题详情")
        toolbar_back.setOnClickListener { finish() }
        titlestr.setText(intent.getStringExtra("title"))
        content.setText(intent.getStringExtra("content"))
    }
}