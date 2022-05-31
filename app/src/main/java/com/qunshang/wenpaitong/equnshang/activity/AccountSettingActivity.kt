package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import com.bumptech.glide.Glide
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VersionBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import java.io.File

class AccountSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_account_setting)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("账户设置")
        initView()
    }

    fun initCheckUpdate(){
        try {
            ApiManager.getInstance().getApiMainTest().checkVersionToUpdate(
                //236
                CommonUtil.getVersionCode(this)
            ).enqueue(object : Callback<VersionBean> {
                override fun onResponse(call: Call<VersionBean>, response: Response<VersionBean>) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code != 200){
                        TipDialog.show(this@AccountSettingActivity,response.body()!!.msg, TipDialog.TYPE.ERROR)
                    }
                    layout_check_update.setOnClickListener {
                        if (response.body()!!.data.status == 1){
                            showAskDownLoadDialog(response.body()!!.data.url)
                        } else {
                            TipDialog.show(this@AccountSettingActivity,"已是最新版本", TipDialog.TYPE.WARNING)
                        }
                    }

                }

                override fun onFailure(call: Call<VersionBean>, t: Throwable) {

                }

            })
        } catch (e : Exception){
            e.printStackTrace()
        }
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
                    WaitDialog.show(this@AccountSettingActivity,"正在下载")
                }
                override fun blockComplete(task: BaseDownloadTask?) {

                }
                override fun retry(task: BaseDownloadTask?, ex: Throwable, retryingTimes: Int, soFarBytes: Int) {

                }

                override fun completed(task: BaseDownloadTask?) {
                    WaitDialog.dismiss()
                    CommonUtil.installAPK(path,this@AccountSettingActivity)
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

    fun initView() {
        initCheckUpdate()
        layout_address.setOnClickListener {
            if (Constants.isNewPinHaoHuo) {
                startActivity(Intent(this,
                                     AddressActivityV2::class.java))
            } else {
                startActivity(Intent(this,
                                     AddressActivity::class.java))
            }
        }
        logout.setOnClickListener {
            MessageDialog.show(this, "提示", "是否确定要退出", "确认", "取消")
                .setOkButton(OnDialogButtonClickListener() { baseDialog, v ->
                    baseDialog.doDismiss()
                    UserHelper.clear(this)
                    UserInfoViewModel.setUserId("")
                    UserInfoViewModel.setUserInfo(null)
                    Constants.isMainPageNeedToChangePage = true
                    Constants.pageNeedToBeChange = 0
                    EventBus.getDefault().post("loginsuccess")
                    finish()
                    /*val intent: Intent = Intent(
                            this@AccountSettingActivity,
                            MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)*/
                    return@OnDialogButtonClickListener true
                })
        }
        if (!com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(UserInfoViewModel.getUserInfo()?.headimage)) {
            Glide.with(this).load(UserInfoViewModel.getUserInfo()?.headimage).into(userimage)
        }
        if (!com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(UserInfoViewModel.getUserInfo()?.uname)) {
            username.setText(UserInfoViewModel.getUserInfo()?.uname)
        }

        layout_account_safety.setOnClickListener {
            val intent = Intent(this, AccountSaftyActivity::class.java)
                                startActivity(intent)
        }
        layout_legal.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","认购商城服务协议")
            intent.putExtra("url", "http://api.equnshang.cn/rule/cultureRule.html")
            startActivity(intent)
            return@setOnClickListener
            /*val intent = Intent(this,
                                SeleRegulationActivity::class.java)
            intent.putExtra("title", "自律公约")
            intent.putExtra("id", R.mipmap.selfregulationcontent)
            startActivity(intent)*/
        }
        layout_amallrule.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","兑换商城使用服务协议")
            intent.putExtra("url", "http://api.equnshang.cn/rule/amallRule.html")
            startActivity(intent)
        }
        user.setOnClickListener {
            val intent = Intent(this,
                AccountSaftyActivity::class.java)
            startActivity(intent)
        }
        layout_userprotocal.setOnClickListener {
            val intent = Intent(this,
                                WebViewActivity::class.java)
            intent.putExtra("title","协议")
            intent.putExtra("url", "http://api.equnshang.com/rule/userRule.html")
            startActivity(intent)
        }
        layout_secretprotocal.setOnClickListener {
            val intent = Intent(this,
                                PrivacyProtocolActivity::class.java)
            startActivity(intent)
        }
        layout_about.setOnClickListener {
            val intent = Intent(this,
                                AboutActivity::class.java)
            startActivity(intent)
        }
        layout_gexinghua?.setOnClickListener {
            val intent = Intent(this,PersonalPushSettingActivity::class.java)
            startActivity(intent)
            return@setOnClickListener
        }
        var identity = ""

        if (!(UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and !(UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3) {
                identity = "会员,主任"
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                identity = "会员,总裁"
            } else {
                identity = "会员,店主"
            }
            //identity = "会员,合伙人"
        }
        if (!(UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and (UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            identity = "会员"
        }
        if ((UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and !(UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3) {
                identity = "主任"
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                identity = "总裁"
            } else {
                identity = "店主"
            }
        }
        if ((UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and (UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            identity = "粉丝"
        }
        labelidentity.setText(identity)
        versionname.setText(CommonUtil.getVersionName(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(i: Int) {

    }

}