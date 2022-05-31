package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_week_task_rule.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants

class WeekTaskRuleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_task_rule)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("活动规则")
        webview.getSettings().setJavaScriptEnabled(true)
        // 设置可以支持缩放
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true)
        //自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
        webview.getSettings().setLoadWithOverviewMode(true);

        webview.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

        }
        webview.loadUrl(Constants.baseURL + "/rule/taskRule.html")
    }
}