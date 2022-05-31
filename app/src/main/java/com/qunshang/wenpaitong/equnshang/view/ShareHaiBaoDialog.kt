package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import kotlinx.android.synthetic.main.dialog_wenbantong_share_bottom.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.fragment.*
import com.qunshang.wenpaitong.equnshang.utils.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ShareHaiBaoDialog(val bean : WenBanTongProductBean,context: Context) : BottomPopupView(context) {

    fun createFile(){
        val activity = context as AppCompatActivity
        if (!PermissionUtil.checkStoragePermission(activity)){
            PermissionUtil.requireStoragePermission(activity)
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

        cancel?.setOnClickListener { dismiss() }
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val activity = context as AppCompatActivity
        val fragments = ArrayList<BaseShareFragment>()
        fragments.add(WenBanTongShare_OneFragment(bean))
        fragments.add(WenBanTongShare_TwoFragment(bean))
        fragments.add(WenBanTongShare_ThreeFragment(bean))
        //fragments.add(WenBanTongShare_FourFragment())
        fragments.add(WenBanTongShare_FiveFragment(bean))
        fragments.add(WenBanTongShare_SixFragment(bean))
        val titles = ArrayList<String>()
        titles.add("")
        titles.add("")
        titles.add("")
        //titles.add("")
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

            val PHOTO_DIR = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + UUID.randomUUID() + ".jpeg"

            val avaterFile = File(PHOTO_DIR)//设置文件名称

            if(avaterFile.exists()){
                avaterFile.delete()
            }

            createFile()

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
        return R.layout.dialog_share_haibao
    }

    override fun getMaxWidth(): Int {
        return (XPopupUtils.getWindowWidth(context) * 1)
    }

}