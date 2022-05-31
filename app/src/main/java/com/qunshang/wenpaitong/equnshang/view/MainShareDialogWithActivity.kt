package com.qunshang.wenpaitong.equnshang.view

import android.content.Intent
import android.view.WindowManager
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_shares.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.QrCodeShareActivity
import com.qunshang.wenpaitong.equnshang.activity.VideoListActivity
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class MainShareDialogWithActivity(val activity: VideoListActivity) : BottomPopupView(activity) {

    override fun onCreate() {
        super.onCreate()
        StringUtils.log("我被调用了一次")
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        layout_wechat.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            activity.showWeChatShareDialog()
        }
        layout_friendcircle.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            activity.downloadtype = activity.TYPE_WECHAT
            activity.downloadVideo()
        }
        layout_qq.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            activity.showQQShareDialog()
        }
        layout_qqzone.setOnClickListener {
            CommonUtil.doCompleteTask(3)
            dismiss()
            activity.downloadtype = activity.TYPE_QQ
            activity.downloadVideo()
        }
        layout_saveimage.setOnClickListener {
            dismiss()
            val intent = Intent(context, QrCodeShareActivity::class.java)
            activity.startActivity(intent)
        }
        close.setOnClickListener { dismiss() }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_shares
    }

}