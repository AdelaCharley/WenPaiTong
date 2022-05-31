package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.ActivityUpBinding
import com.qunshang.wenpaitong.equnshang.fragment.UpFragment

class UpActivity : BaseActivity() {

    lateinit var binding : ActivityUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        binding.toolbar.toolbarTitle.text = "我的点赞"
        supportFragmentManager.beginTransaction().add(R.id.container, UpFragment()).commit()
    }
}