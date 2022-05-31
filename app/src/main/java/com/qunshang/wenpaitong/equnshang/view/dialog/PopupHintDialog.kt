package com.qunshang.wenpaitong.equnshang.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.lxj.xpopup.core.CenterPopupView
import com.qunshang.wenpaitong.R

class PopupHintDialog(context: Context): CenterPopupView(context) {
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvSure: TextView

    private var title = ""
    private var content = ""

    constructor(context: Context, title: String, content: String): this(context) {
        this.title = title
        this.content = content
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_hint
    }

    @SuppressLint("WrongViewCast")
    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {
        tvTitle = findViewById(R.id.tv_title)
        tvContent = findViewById(R.id.tv_content)
        tvSure = findViewById(R.id.btn_sure)


        tvTitle.text = title
        tvContent.text = content
        tvSure.setOnClickListener { dismiss() }
    }
}