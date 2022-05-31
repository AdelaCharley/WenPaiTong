package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.ActivityConcernBinding
import com.qunshang.wenpaitong.equnshang.fragment.ConcernFragment

class ConcernActivity : BaseActivity() {
    lateinit var binding : ActivityConcernBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConcernBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ToolBarHelper helper = new ToolBarHelper(binding.toolbar.getRoot());
        helper.setTitle("新用户协议");*/
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        binding.toolbar.toolbarTitle.text = "我的关注"
        supportFragmentManager.beginTransaction().add(R.id.container,ConcernFragment()).commit()
    }
}