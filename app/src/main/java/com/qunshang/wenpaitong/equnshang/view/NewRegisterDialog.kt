package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_new_people.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.WebViewActivity
import com.qunshang.wenpaitong.equnshang.data.NewcomerGiftInfoBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import java.util.*

class NewRegisterDialog(context: Context,val newer : NewcomerGiftInfoBean?) : CenterPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        initView()
        if (newer?.product != null){
            Glide.with(context).load(newer?.product.mainUrl).into(image)
            name.setText(newer?.product.title)
            subtitle.setText(newer?.product.subTitle)
            price.setText(newer?.product.price)
            val timerTask = object : TimerTask() {
                override fun run() {
                    val timeRemain = TimeUtil.getTimeRemainByDayString(newer.endTime)
                    val activity = context as AppCompatActivity
                    activity.runOnUiThread {
                        if (timeRemain == "00:00:00") {
                            //layoutMid.newUserGift.visibility = View.GONE
                            cancel()
                        } else {
                            remaintime.setText(timeRemain)
                            /*layoutMid.newUserGift.visibility = View.VISIBLE
                            layoutMid.tvCountDown.text = "倒计时：$timeRemain"*/
                        }
                    }
                }
            }
            Timer().schedule(timerTask, 0, 1000)
        }
        bg.setOnClickListener {
            dismiss()
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("title","新人礼")
            intent.putExtra("url", Constants.baseURL + "/equnshang/newcomer?userId=" + UserInfoViewModel.getUserId())
            context.startActivity(intent)
        }
        close.setOnClickListener { dismiss() }
    }

    fun initView(){
        bg.viewTreeObserver.addOnGlobalLayoutListener{
            if (isInited){
                return@addOnGlobalLayoutListener
            }
            val width = bg.width
            val height = bg.height

            val topofremaintime = height * (195.0 / 715.0)
            val leftofremaintime = width * (190.0 / 532.0)
            StringUtils.log("height是" + height + "hihei" + topofremaintime)
            val layoutparamsofremaintext = remaintime.layoutParams as MarginLayoutParams
            layoutparamsofremaintext.setMargins(leftofremaintime.toInt(),topofremaintime.toInt(),0,0)
            remaintime.layoutParams = layoutparamsofremaintext

            val leftofimage = width *(32.0 / 408.0)
            val bottomofimage = height * (100.0/501.0)
            val layoutparamsofimage = image.layoutParams as MarginLayoutParams
            layoutparamsofimage.setMargins(leftofimage.toInt(),0,0,bottomofimage.toInt())
            image.layoutParams = layoutparamsofimage

            isInited = true
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_new_people
    }

    var isInited = false

}