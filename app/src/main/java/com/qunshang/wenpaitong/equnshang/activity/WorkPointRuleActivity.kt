package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.layout_toolbar.*

class WorkPointRuleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_point_rule)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("规则")
        //content.setImage(ImageSource.resource(R.mipmap.workpointrule))
    }
}