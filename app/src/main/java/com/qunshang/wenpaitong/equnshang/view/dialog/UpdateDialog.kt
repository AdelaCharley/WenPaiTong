package com.qunshang.wenpaitong.equnshang.view.dialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.lxj.xpopup.core.CenterPopupView
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.dialog_update.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.UpdateMessageAdapter
import com.qunshang.wenpaitong.equnshang.data.VersionBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.io.File

class UpdateDialog(context: Context,val bean : VersionBean) : CenterPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        val list = bean.data.updateFeature
        val messageAdapter = UpdateMessageAdapter(context,list)
        messagelist.adapter = messageAdapter
        confrimdownload.setOnClickListener {
            if (PermissionUtil.checkStoragePermission(context as Activity)){
                download(bean.data.url)
            } else {
                val rxPermissions = RxPermissions(context as AppCompatActivity)
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) {
                            download(bean.data.url)
                        } else {
                            DialogUtil.toast(context,"存储权限被拒绝，无法下载应用")
                            PermissionUtil.openPermissonInSetting(context as Activity)
                        }
                    }
            }
            false
        }
    }

    fun download(url : String){

        confrimdownload.visibility = View.GONE
        progressview.visibility = View.VISIBLE
        text_progress.visibility = View.VISIBLE

        val activity = context as AppCompatActivity

        val path = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + File.separator + System.currentTimeMillis() + ".apk"
        FileDownloader.getImpl()
            .create(url)
            .setPath(path)
            .setListener(object : FileDownloadListener() {
                override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }
                override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                    //DialogUtil.showUpdateDialog(activity)
                }

                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    //WaitDialog.dismiss(0)
                    StringUtils.log("当前进度是" + (100*soFarBytes).toString())
                    val currrent : Long = soFarBytes.toLong() * 100
                    val total = totalBytes.toLong()
                    update((currrent/total).toInt())
                    //DialogUtil.refreshUpdateDialog((currrent/total).toInt())
                    //DialogUtil.showUpdateDialog(this@MainActivity).update()
                    /*WaitDialog.show(this@MainActivity,"正在下载").setMessage("正在下载" + (currrent/total) + "%")
                        .refreshView()*/
                }
                override fun blockComplete(task: BaseDownloadTask?) {

                }
                override fun retry(task: BaseDownloadTask?, ex: Throwable, retryingTimes: Int, soFarBytes: Int) {

                }

                override fun completed(task: BaseDownloadTask?) {
                    //WaitDialog.dismiss()
                    DialogUtil.dismissDialog()
                    CommonUtil.installAPK(path,activity)
                }
                override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }
                override fun error(task: BaseDownloadTask?, e: Throwable?) {
                    //DialogUtil.toast(this@MainActivity,e.message)
                }
                override fun warn(task: BaseDownloadTask?) {

                }
            }).start()
    }


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_update
    }

    fun update(progress : Int){
        if (isShow){
            progressview?.progress = progress
            text_progress?.setText(progress.toString() + "%")
        }
    }

}