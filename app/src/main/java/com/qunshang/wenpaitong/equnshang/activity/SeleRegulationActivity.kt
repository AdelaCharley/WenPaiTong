package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.davemorrissey.labs.subscaleview.ImageSource
import kotlinx.android.synthetic.main.activity_sele_regulation.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class SeleRegulationActivity : BaseActivity() {

    var title : String? = ""

    var id = -999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sele_regulation)
        toolbar_back.setOnClickListener { finish() }
        title = intent.getStringExtra("title")
        id = intent.getIntExtra("id",-999)
        if (id == -999){
            return
        }
        toolbar_title.setText(title)
        content.setImage(ImageSource.resource(id))
        //toolbar_title.setText("群商内容发布自律公约")
        //content.setImage(ImageSource.resource(R.mipmap.selfregulationcontent))
    }
}