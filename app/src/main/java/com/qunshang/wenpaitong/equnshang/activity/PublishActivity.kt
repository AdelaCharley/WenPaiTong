package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.ActivityPublishBinding
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.PublishFragment

class PublishActivity : BaseActivity() {

    lateinit var binding : ActivityPublishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ToolBarHelper helper = new ToolBarHelper(binding.toolbar.getRoot());
        helper.setTitle("新用户协议");*/
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        binding.toolbar.toolbarTitle.text = "我的发布"
        supportFragmentManager.beginTransaction().add(R.id.container, PublishFragment(UserInfoViewModel.getUserId())).commit()
    }
}