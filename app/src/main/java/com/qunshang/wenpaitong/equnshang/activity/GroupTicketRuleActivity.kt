package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_group_ticket_rule.webview
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants

class GroupTicketRuleActivity : BaseActivity() {

    val str = Constants.WECHAT_BASE_URL + "/rule/ruleTree.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_ticket_rule)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("规则")
        webview.getSettings().setJavaScriptEnabled(true)
        // 设置可以支持缩放
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true)
        //自适应屏幕
        webview.getSettings().setTextZoom(100)
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webview.loadUrl(str)
        webview.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //DialogUtil.toast(this@WebViewActivity,"啊啊啊啊" + url)
                return false
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

        }
    }
}