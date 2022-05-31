package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.util.Log
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_signin.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.MyPhysicalStoreBean

class SignInDialog(context: Context,val bean : MyPhysicalStoreBean) : CenterPopupView(context)  {

    override fun onCreate() {
        super.onCreate()
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        Glide.with(context).load(bean.data.vendorDetail.vendorHeadImg).into(headimage)
        videoname.setText(bean.data.vendorDetail.vendorName)
        Log.i(Constants.logtag,bean.data.shareUrl + "equnshangscan" + bean.data.vendorDetail.vendorId)
        val bitmap = uni.yzq.zxinglibrary.encode.CodeCreator.createQRCode(bean.data.shareUrl + "equnshangscan" + bean.data.vendorDetail.vendorId, 400, 400, null)
        qrimage.setImageBitmap(bitmap)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_signin
    }

}