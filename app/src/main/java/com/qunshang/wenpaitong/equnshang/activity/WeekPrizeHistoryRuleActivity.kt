package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_week_task_rule.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants

class WeekPrizeHistoryRuleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_prize_history_rule)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("活动规则")
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.loadUrl(Constants.baseURL + "/rule/taskRule.html")
    }
}