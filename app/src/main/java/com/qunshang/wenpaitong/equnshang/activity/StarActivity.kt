package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.ActivityStarBinding
import com.qunshang.wenpaitong.equnshang.fragment.StarFragment

class StarActivity : BaseActivity() {
    lateinit var binding : ActivityStarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        binding.toolbar.toolbarTitle.text = "我的收藏"
        supportFragmentManager.beginTransaction().add(R.id.container, StarFragment()).commit()
    }
}