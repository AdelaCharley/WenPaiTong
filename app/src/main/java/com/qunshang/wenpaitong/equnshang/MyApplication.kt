package com.qunshang.wenpaitong.equnshang

import android.app.Application
import android.content.Context
//import com.didichuxing.doraemonkit.DoraemonKit
import com.kongzue.dialog.util.DialogSettings
import com.liulishuo.filedownloader.FileDownloader
import com.umeng.commonsdk.UMConfigure
import xyz.doikki.videoplayer.ijk.IjkPlayerFactory
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager
import android.content.Intent
import cn.jpush.android.api.JPushInterface
import com.umeng.umcrash.UMCrash
import com.qunshang.wenpaitong.equnshang.activity.SplashActivity
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel

class MyApplication : Application() {

    companion object {
        val APP_STATUS_KILLED = 0 // 表示应用是被杀死后在启动的

        val APP_STATUS_NORMAL = 1 // 表示应用时正常的启动流程

        var APP_STATUS = APP_STATUS_KILLED // 记录App的启动状态

        var context: Context? = null

        fun getAppContext(): Context? {
            return context
        }

        fun reInitApp() {
            val intent = Intent(getAppContext(), SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            getAppContext()!!.startActivity(intent)
        }

    }



    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //DoraemonKit
        //DoraemonKit.install(this, "pId")

        //BlockCanary.install(this, AppBlockCanaryContext()).start()

        if (Constants.isRelease){
            Constants.baseURL = "https://api.equnshang.cn"
        } else {
            Constants.baseURL = "https://apitest.equnshang.com"
        }
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
            .setPlayerFactory(IjkPlayerFactory.create())
            .build())
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS
        DialogSettings.theme = DialogSettings.THEME.LIGHT
        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT
        FileDownloader.setup(this)
        UMConfigure.init(this, "61b6bd1be014255fcbaea975", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "")
        UMConfigure.setLogEnabled(false)
        UMCrash.registerUMCrashCallback {
            return@registerUMCrashCallback "当前用户的userID是" + UserInfoViewModel.getUserId()
        }
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}