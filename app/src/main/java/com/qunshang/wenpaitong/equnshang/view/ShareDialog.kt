package com.qunshang.wenpaitong.equnshang.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.databinding.DialogShareBinding

/**
 * 首页点击【二维码】弹出的分享页面
 * create by libo
 * create on 2020-05-25
 * modifier 何姝霖
 * last-modified 2021-08-6
 */

class ShareDialog : BaseBottomSheetDialog() {
    private var binding: DialogShareBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogShareBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding?.top!!.toolbarBack.setOnClickListener { dismiss() }
        binding?.top!!.toolbarTitle.setText("邀请码")
    }


    override val height: Int
        get() = resources.displayMetrics.heightPixels

}