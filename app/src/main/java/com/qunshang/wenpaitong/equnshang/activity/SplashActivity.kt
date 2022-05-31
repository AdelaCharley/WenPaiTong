package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.MyApplication
import com.qunshang.wenpaitong.equnshang.view.SplashAgreementDialog

/**
 * create by libo
 * create on 2020/5/19
 * description App启动页面
 */
class SplashActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MyApplication.APP_STATUS = MyApplication.APP_STATUS_NORMAL
        Constants.isOpened = true

        super.onCreate(savedInstanceState)
        // App正常的启动，设置App的启动状态为正常启动
        setContentView(R.layout.activity_splash)
        setFullScreen()
        //changeToGreyButTranslucent()
//        colourContent(this)
        object: CountDownTimer(500, 500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                toMainActivity()
            }
        }.start()
    }

    private fun popupAgree() {
        val custom = SplashAgreementDialog(this)
        val popup = XPopup.Builder(this)
        popup.popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
            .autoOpenSoftInput(false)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .asCustom(custom)
            .show()
    }

    /*private fun toMainActivity() {
        val sp = getSharedPreferences("agreement", MODE_PRIVATE)
        val isAgree = sp.getBoolean("isAgree", false)
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }*/

    private fun toMainActivity() {
        val sp = getSharedPreferences("agreement", MODE_PRIVATE)
        val isAgree = sp.getBoolean("isAgree", false)
        if (isAgree) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        } else {
            popupAgree()
        }
    }

}


