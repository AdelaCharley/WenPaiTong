package com.qunshang.wenpaitong.equnshang.view

import android.text.TextUtils
import android.util.Log
import android.view.View

/**
 * create by libo
 * create on 2020-05-21
 * description
 */
object AutoLinkHerfManager {
    /**
     * 设置正文内容
     *
     * @param content
     */
    fun setContent(content: String?, autoLinkTextView: com.qunshang.wenpaitong.equnshang.view.AutoLinkTextView) {
        if (TextUtils.isEmpty(content)) return
        autoLinkTextView.visibility = View.VISIBLE
        autoLinkTextView.addAutoLinkMode(
            com.qunshang.wenpaitong.equnshang.view.AutoLinkMode.MODE_HASHTAG,
            com.qunshang.wenpaitong.equnshang.view.AutoLinkMode.MODE_MENTION,
            com.qunshang.wenpaitong.equnshang.view.AutoLinkMode.MODE_URL
        ) //设置需要富文本的模式
        autoLinkTextView.text = content
        autoLinkTextView.setAutoLinkOnClickListener { autoLinkMode: com.qunshang.wenpaitong.equnshang.view.AutoLinkMode?, matchedText: String ->
            when (autoLinkMode) {
                com.qunshang.wenpaitong.equnshang.view.AutoLinkMode.MODE_HASHTAG -> Log.i("minfo", "话题 $matchedText")
                com.qunshang.wenpaitong.equnshang.view.AutoLinkMode.MODE_MENTION -> Log.i("minfo", "at $matchedText")
            }
        }
    }
}