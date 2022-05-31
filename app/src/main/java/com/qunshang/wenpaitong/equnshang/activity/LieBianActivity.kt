package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.activity_lie_bian.webview
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel

class LieBianActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lie_bian)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("裂变")
        webview.settings.javaScriptEnabled = true
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
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
        webview.loadUrl(Constants.baseURL + "/equnshang/phone?phoneNumber=" + UserInfoViewModel.getUserInfo()!!.utel)
    }
}