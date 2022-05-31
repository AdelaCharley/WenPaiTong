package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_wenbantong_share_bottom.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongDownloadMaterialsActivity
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.KTUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class WenBanTongShareDialog(val bean : WenBanTongProductBean,context: Context) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        cancel?.setOnClickListener { dismiss() }
        layout1?.setOnClickListener {
            dismiss()
            val imageurl = bean.data?.swiperList?.get(0)
            val url = "/equnshang/culture/detail?productId=" + bean.data.productId
            val title = bean.data.productName
            val desc = "我刚发现一件好物，快来看看吧！"
            KTUtil.doWeChatShare(context,imageurl, url, title, desc)
            CommonUtil.doCompleteTask(3)
        }
        layout2.setOnClickListener {
            if (!UserHelper.isLogin(context)){
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                return@setOnClickListener
            }
            val activity = context as AppCompatActivity
            if (!PermissionUtil.checkStoragePermission(activity)){
                PermissionUtil.requireStoragePermission(activity)
                return@setOnClickListener
            }
            dismiss()
            val dialog = ShareHaiBaoDialog(bean,context)
            XPopup.Builder(context)
                .isViewMode(true)
                //.moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                //.enableDrag(false)
                //.isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                .asCustom(dialog)
                .show()
            CommonUtil.doCompleteTask(3)
            return@setOnClickListener
        }
        layout3.setOnClickListener {
            if (!UserHelper.isLogin(context)){
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                return@setOnClickListener
            }
            val activity = context as AppCompatActivity
            if (!PermissionUtil.checkStoragePermission(activity)){
                PermissionUtil.requireStoragePermission(activity)
                return@setOnClickListener
            }
            val intent = Intent(context,WenBanTongDownloadMaterialsActivity::class.java)
            intent.putExtra("data",bean)
            context.startActivity(intent)
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_wenbantong_share_bottom
    }

}