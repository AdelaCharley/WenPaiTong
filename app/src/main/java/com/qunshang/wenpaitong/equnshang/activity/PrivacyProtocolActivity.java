package com.qunshang.wenpaitong.equnshang.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
;

import com.qunshang.wenpaitong.databinding.ActivityPrivacyProtocolBinding;
import com.qunshang.wenpaitong.equnshang.Constants;

/**
 * 隐私协议
 * create by 何姝霖
 */
public class PrivacyProtocolActivity extends BaseActivity {

    ActivityPrivacyProtocolBinding binding;

    String str = Constants.Companion.getBaseURL() + "/rule/privacy.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyProtocolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*ToolBarHelper helper = new ToolBarHelper(binding.toolbar.getRoot());
        helper.setTitle("新用户协议");*/
        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        binding.toolbar.toolbarTitle.setText("隐私政策");
        binding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.webview.loadUrl(str);
    }
}