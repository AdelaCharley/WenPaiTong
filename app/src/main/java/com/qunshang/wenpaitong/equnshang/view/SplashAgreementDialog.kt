package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.core.CenterPopupView
import org.greenrobot.eventbus.EventBus
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.activity.PrivacyProtocolActivity
import com.qunshang.wenpaitong.equnshang.activity.SplashActivity
import com.qunshang.wenpaitong.equnshang.activity.UserProtocolActivity

class SplashAgreementDialog(context: Context): CenterPopupView(context) {
    private lateinit var activity: SplashActivity
    private lateinit var tvContent: TextView
    private lateinit var tvAgree: TextView
    private lateinit var tvDisagree: TextView

    override fun onCreate() {
        super.onCreate()
        initView()
        colourContent()
        popupCustom()
    }

    private fun initView() {
        activity = context as SplashActivity
        tvContent = findViewById(R.id.tv_content)
        tvAgree = findViewById(R.id.tv_agree)
        tvDisagree = findViewById(R.id.tv_disagree)
    }

    private fun colourContent() {
        val spanStr = SpannableStringBuilder(resources.getString(R.string.agreement_p2))
        val color0 = ForegroundColorSpan(resources.getColor(R.color.blue))
        val color1 = ForegroundColorSpan(resources.getColor(R.color.blue))
        val toServe = object : ClickableSpan(){
            override fun onClick(widget: View) {
                context.startActivity(Intent(context, UserProtocolActivity::class.java))
            }
        }
        val toPrivacy = object : ClickableSpan(){
            override fun onClick(widget: View) {
                context.startActivity(Intent(context, PrivacyProtocolActivity::class.java))
            }
        }
        spanStr.apply{
            setSpan(toServe, 6, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(toPrivacy, 13, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(color0, 6, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(color1, 13, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        tvContent.text = spanStr
        tvContent.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun popupCustom() {
        val sp = context.getSharedPreferences("agreement", AppCompatActivity.MODE_PRIVATE)
        tvAgree.setOnClickListener {
            sp.edit().putBoolean("isAgree", true).apply()
            context.startActivity(Intent(context, MainActivity::class.java))
            activity.finish()
        }
        tvDisagree.setOnClickListener {
            sp.edit().putBoolean("isAgree", false).apply()
            activity.finish()
        }
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_splash_agreement

}