package com.qunshang.wenpaitong.equnshang.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
;

import android.view.View;
import android.webkit.WebSettings;

import com.qunshang.wenpaitong.databinding.ActivityUserProtocolBinding;

/**
 * 用户协议
 * create by 何姝霖
 */
public class UserProtocolActivity extends BaseActivity {

    ActivityUserProtocolBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProtocolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbar.toolbarTitle.setText("协议");
        binding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.webview.loadUrl("http://api.equnshang.com/rule/userRule.html");
    }
}