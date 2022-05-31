package com.qunshang.wenpaitong.equnshang.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.VersionBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import java.io.File

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("关于")
        versionname.setText("android V " + CommonUtil.getVersionName(this))
        layout_protocal_pricacy.setOnClickListener {
            val intent = Intent(this,PrivacyProtocolActivity::class.java)
            startActivity(intent)
        }
        ApiManager.getInstance().getApiMainTest().checkVersionToUpdate(
            //236
            CommonUtil.getVersionCode(this)
        ).enqueue(object : Callback<VersionBean> {
            override fun onResponse(call: Call<VersionBean>, response: Response<VersionBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    TipDialog.show(this@AboutActivity,response.body()!!.msg, TipDialog.TYPE.ERROR)
                }
                layout_checkupdate.setOnClickListener {
                    if (response.body()!!.data.status == 1){
                        showAskDownLoadDialog(response.body()!!.data.url)
                    } else {
                        TipDialog.show(this@AboutActivity,"已是最新版本",TipDialog.TYPE.WARNING)
                    }
                }
                layoutguanwang.setOnClickListener {
                    val intent = Intent(this@AboutActivity,WebViewActivity::class.java)
                    intent.putExtra("title","群票")
                    intent.putExtra("url", "http://www.equnshang.com/")
                    startActivity(intent)
                }
                fuzhi.setOnClickListener {
                    val clipData = ClipData.newPlainText(null, mail.text.toString())
                    val cmd: ClipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cmd.setPrimaryClip(clipData)
                    DialogUtil.toast(this@AboutActivity,"复制成功")
                }

                layout_phone.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.text.toString()))
                    startActivity(intent)
                }
                layout_zizhi.setOnClickListener {
                    val intent = Intent(this@AboutActivity,WebViewActivity::class.java)
                    intent.putExtra("title","平台资质")
                    intent.putExtra("url", Constants.baseURL + "/wbt/qualification")
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<VersionBean>, t: Throwable) {

            }

        })
    }

    fun download(url : String){

        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + File.separator + System.currentTimeMillis() + ".apk"
        FileDownloader.getImpl().create(url)
            .setPath(path)
            .setListener(object : FileDownloadListener() {
                override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }
                override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {

                }

                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    WaitDialog.show(this@AboutActivity,"正在下载")
                }
                override fun blockComplete(task: BaseDownloadTask?) {

                }
                override fun retry(task: BaseDownloadTask?, ex: Throwable, retryingTimes: Int, soFarBytes: Int) {

                }

                override fun completed(task: BaseDownloadTask?) {
                    WaitDialog.dismiss()
                    CommonUtil.installAPK(path,this@AboutActivity)
                }
                override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }
                override fun error(task: BaseDownloadTask?, e: Throwable) {
                    //DialogUtil.toast(this@MainActivity,e.message)
                }
                override fun warn(task: BaseDownloadTask?) {

                }
            }).start()
    }

    fun showAskDownLoadDialog(url : String){
        MessageDialog.show(this, "更新", "发现新版本，是否更新", "确认", "取消")
            .setOkButton(OnDialogButtonClickListener() { baseDialog, v ->
                if (PermissionUtil.checkStoragePermission(this)){

                } else {
                    PermissionUtil.requireStoragePermission(this)
                    return@OnDialogButtonClickListener false
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val hasInstallPermission: Boolean = getPackageManager().canRequestPackageInstalls()
                        if (!hasInstallPermission) {
                            CommonUtil.startInstallPermissionSettingActivity(this)
                            return@OnDialogButtonClickListener false
                        }
                    }
                }

                baseDialog.doDismiss()
                download(url)
                //仅需要对需要处理的按钮进行操作
                //处理确定按钮事务
                false //可以通过 return 决定点击按钮是否默认自动关闭对话框
            })
    }

}