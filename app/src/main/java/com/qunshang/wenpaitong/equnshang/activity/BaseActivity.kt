package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.umeng.analytics.MobclickAgent
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.MyApplication
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import xyz.doikki.videoplayer.player.VideoViewManager

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkStatus()
        setSystemBarColor(R.color.white)
    }

    fun onCreate(savedInstanceState: Bundle?,isShowStatusBar : Boolean){
        super.onCreate(savedInstanceState)
        changeToDefaultButTranslucent()
    }

    fun checkStatus(){
        if (MyApplication.APP_STATUS != MyApplication.APP_STATUS_NORMAL) { // 非正常启动流程，直接从新初始化应用界面
            MyApplication.reInitApp()
            DialogUtil.toast(this,"应用被后台杀死，已重新启动应用")
            finish()
            return
        }
    }

    fun setSystemBarColor(color: Int) {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        getWindow().decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(color)
    }

    fun changeToDefaultButTranslucent(){
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        getWindow().decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = resources.getColor(R.color.transparent)
        window.navigationBarColor = resources.getColor(R.color.white)
    }

    fun changeToGreyButTranslucent(){
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        getWindow().decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.transparent)
        window.navigationBarColor = resources.getColor(R.color.white)
    }

    protected fun hideStatusBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init()
    }

    protected fun keepScreenOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


    }

    protected fun setExitAnimation(animId: Int) {
        overridePendingTransition(0, animId)
    }

    fun setFullScreen() {
        ImmersionBar.with(this).init()
    }

    protected fun getVideoViewManager(): VideoViewManager{
        return VideoViewManager.instance()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

}