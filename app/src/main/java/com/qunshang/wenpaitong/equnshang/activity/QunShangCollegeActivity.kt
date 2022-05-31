package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.layout_toolbar.*

class QunShangCollegeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qun_shang_college)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("商学院")
        //content.setImage(ImageSource.resource(R.mipmap.qunshangcollege))
    }
}