package com.qunshang.wenpaitong.equnshang.view

import android.content.Intent
import android.view.WindowManager
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_shares.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.QrCodeShareActivity
import com.qunshang.wenpaitong.equnshang.fragment.SampleVideoFragment
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class MainShareDialog(val fragment: SampleVideoFragment) : BottomPopupView(fragment.requireContext()) {

    override fun onCreate() {
        super.onCreate()
        StringUtils.log("我被调用了一次")
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        layout_wechat.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            fragment.showWeChatShareDialog()
        }
        layout_friendcircle.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            fragment.downloadtype = fragment?.TYPE_WECHAT
            fragment.downloadVideo()
        }
        layout_qq.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            fragment.showQQShareDialog()
        }
        layout_qqzone.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            fragment.downloadtype = fragment.TYPE_QQ
            fragment.downloadVideo()
        }
        layout_saveimage.setOnClickListener {
            dismiss()
            val intent = Intent(context, QrCodeShareActivity::class.java)
            fragment.startActivity(intent)
        }
        close.setOnClickListener { dismiss() }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_shares
    }

}