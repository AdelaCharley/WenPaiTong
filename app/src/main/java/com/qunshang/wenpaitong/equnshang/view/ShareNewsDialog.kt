package com.qunshang.wenpaitong.equnshang.view

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.dialog_share_news.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.CollegeNewsShareBean
import com.qunshang.wenpaitong.equnshang.fragment.*
import com.qunshang.wenpaitong.equnshang.utils.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ShareNewsDialog(val bean : CollegeNewsShareBean,context: Context) : BottomPopupView(context) {

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
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val activity = context as AppCompatActivity
        val fragments = ArrayList<BaseShareFragment>()
        fragments.add(CollegeShare_OneFragment(bean))
        fragments.add(CollegeShare_TwoFragment(bean))
        fragments.add(CollegeShare_ThreeFragment(bean))
        val titles = ArrayList<String>()
        titles.add("")
        titles.add("")
        titles.add("")
        val pagerAdapter = BasePagerAdapter(activity.supportFragmentManager, fragments, titles)
        viewPager.pageMargin = CommonUtil.dp2px(context,15)
        viewPager.offscreenPageLimit = 8
        viewPager.adapter = pagerAdapter
        layout1.setOnClickListener {
            KTUtil.doWeChatShareImage(context,fragments.get(viewPager.currentItem).rootBitmap)
        }
        layout2.setOnClickListener {
            KTUtil.doWeChatShareImage(context,fragments.get(viewPager.currentItem).rootBitmap,2)
        }
        layout3.setOnClickListener {
            val activity = context as AppCompatActivity
            if (!PermissionUtil.checkStoragePermission(activity)){
                PermissionUtil.requireStoragePermission(activity)
                return@setOnClickListener
            }

            val PHOTO_DIR = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + System.currentTimeMillis() + ".jpeg"

            val avaterFile = File(PHOTO_DIR)//设置文件名称

            if(avaterFile.exists()){
                avaterFile.delete()
            }
            try {
                avaterFile.createNewFile()
                val fos = FileOutputStream(avaterFile)
                fragments.get(viewPager.currentItem).rootBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                dismiss()
                FileUtils.saveImageToLocal(context,avaterFile)//通知相册
                DialogUtil.showSuccessDialog(context,"下载完成")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_share_news
    }

    override fun getMaxWidth(): Int {
        return (XPopupUtils.getWindowWidth(context) * 1)
    }

}