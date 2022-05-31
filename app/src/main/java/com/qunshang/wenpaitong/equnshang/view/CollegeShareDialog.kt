package com.qunshang.wenpaitong.equnshang.view

import android.Manifest
import android.content.Context
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.dialog_wenbantong_share_bottom.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.CollegeNewsShareBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.*
import java.io.File
import java.lang.Exception
import java.util.*

class CollegeShareDialog(val bean : CollegeNewsShareBean, context: Context,val url : String?,val shareimage : String?,val bigtitle : String?,val subtitle : String?) : BottomPopupView(context) {

    fun createFile(){

        val activity = context as AppCompatActivity

        if (!PermissionUtil.checkStoragePermission(activity)){
            //PermissionUtil.requireStoragePermission(activity)
            val rxPermissions = RxPermissions(activity)
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        createFile()
                    } else {
                        DialogUtil.toast(activity,"存储权限被拒绝，请打开权限")
                        PermissionUtil.openPermissonInSetting(activity)
                    }
                }
            return
        }
        val PHOTO_DIR = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + UUID.randomUUID() + ".jpeg"

        val avaterFile = File(PHOTO_DIR)//设置文件名称
        try {
            val fileparent = File(avaterFile.parent)
            StringUtils.log(fileparent.absolutePath)
            if (fileparent.exists()){
                StringUtils.log("已存在了加济k")
            } else {
                if (avaterFile.mkdirs()){
                    StringUtils.log("创建陈宫")
                } else {
                    StringUtils.log("穿件失败")
                }
            }

        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onCreate() {
        super.onCreate()
        createFile()
        cancel?.setOnClickListener { dismiss() }
        layout1?.setOnClickListener {
            dismiss()
            /*val imageurl = bean.data?.swiperList?.get(0)
            val url = "/equnshang/culture/detail?productId=" + bean.data.productId
            val title = bean.data.productName
            val desc = "我刚发现一件好物，快来看看吧！"
            KTUtil.doWeChatShare(context,imageurl, url, title, desc)
            CommonUtil.doCompleteTask(3)*/
            if (!StringUtils.isEmpty(UserInfoViewModel.getUserId())){
                CommonUtil.doCompleteTask(3)
                val newUrl = url + "&isBrowser=true"
                KTUtil.doWeChatShare(context,shareimage,newUrl,bigtitle,subtitle,true)
            }
        }
        layout2.setOnClickListener {
            val activity = context as AppCompatActivity
            if (!PermissionUtil.checkStoragePermission(activity)){
                PermissionUtil.requireStoragePermission(activity)
                return@setOnClickListener
            }
            dismiss()
            val dialog = ShareNewsDialog(bean,context)
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
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_college_share_bottom
    }

}