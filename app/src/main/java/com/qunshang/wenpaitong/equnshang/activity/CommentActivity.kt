package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle

import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.ActivityCommentBinding
import com.qunshang.wenpaitong.equnshang.fragment.CommentFragment

class CommentActivity : BaseActivity() {

    lateinit var binding : ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ToolBarHelper helper = new ToolBarHelper(binding.toolbar.getRoot());
        helper.setTitle("新用户协议");*/
        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        binding.toolbar.toolbarTitle.text = "我的评论"
        supportFragmentManager.beginTransaction().add(R.id.container, CommentFragment()).commit()
    }
}