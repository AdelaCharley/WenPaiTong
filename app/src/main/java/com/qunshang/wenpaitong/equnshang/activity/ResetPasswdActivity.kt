package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.qunshang.wenpaitong.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivitySetPasswdBinding
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VCodeBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 * 权证通 -> 个人中心 -> 密码设置
 * create by 何姝霖
 */

class ResetPasswdActivity: BaseActivity() {
    private lateinit var binding: ActivitySetPasswdBinding

    private lateinit var tvTitle: TextView
    private lateinit var tvHint: TextView   // 当前操作的提示信息（输入框上方）
    private lateinit var etCode: EditText   // 验证码输入框
    private lateinit var tvResend: TextView // 倒计时&重新获取验证码
    private lateinit var etPasswd: EditText // 密码输入框
    private lateinit var imgHide: ImageView // 密码输入框
//    private lateinit var tvError: TextView  // 操作错误时的提示信息（输入框下方）
    private lateinit var button: Button

    private var bizId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPasswdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.toolbarBack.setOnClickListener { finish() }
        binding.toolbar.toolbarTitle.text = ""

        bindView()
        toSendResetSms()
        inputPasswd()
    }

    private fun bindView(){
        tvTitle = binding.tvTitle
        tvHint = binding.tvHint
        etCode = binding.etCode
        tvResend = binding.tvResend
        etPasswd = binding.etPasswd
        imgHide = binding.imgHide
//        tvError = binding.tvError
        button = binding.button
    }

    /**
     * step1:发送信息，获取验证码
     */
    private fun toSendResetSms() {
        val uTel = UserInfoViewModel.getUserInfo()?.utel
        ApiManager.getInstance()
            .getApiWenbantongHeshulinOld()
            .getLoginVCode(uTel)
            .enqueue(object: Callback<VCodeBean> {
                override fun onResponse(
                    call: Call<VCodeBean>,
                    response: Response<VCodeBean>
                ) {
                    Log.d(TAG, "onResponse-toSendResetSms: ${response.body()}")
                    bizId = response.body()?.BizId ?: ""
                    object: CountDownTimer(MAX_COUNT_DOWN, ONE_SECOND) {
                        override fun onTick(millisUntilFinished: Long) {
                            tvResend.text = (millisUntilFinished / ONE_SECOND).toString()
                            tvResend.isClickable = false
                        }

                        override fun onFinish() {
                            tvResend.text = "重新获取"
                            tvResend.isClickable = true
                            tvResend.setOnClickListener { toSendResetSms() }
                        }
                    }.start()
                }

                override fun onFailure(call: Call<VCodeBean>, t: Throwable) {
                }

            })
    }


    /**
     * step2: 输入密码
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun inputPasswd() {
        var isPasswdVisible = false
        button.isActivated = false
        button.isEnabled = false

        button.setOnClickListener {
            toAssureNewPasswd()
        }

        /*密码长度限制：错误提示 + 按钮可用性*/
        etPasswd.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val len = s?.length ?: 0
                val isAvailablePasswd = len in PASSWD_MIN_LENGTH..PASSWD_MAX_LENGTH
                button.isActivated = isAvailablePasswd
                button.isEnabled = isAvailablePasswd
//                when (len) {
//                    in 1..PASSWD_MIN_LENGTH -> {
//                        tvError.text = ERROR_PASSWD_TOO_SHORT
//                        tvError.visibility = View.VISIBLE
//                    }
//                    PASSWD_MAX_LENGTH       -> {
//                        tvError.text = ERROR_PASSWD_TOO_LONG
//                        tvError.visibility = View.VISIBLE
//                    }
//                    else                    -> {
//                        tvError.visibility = View.GONE
//                    }
//                }
            }

        })

        /*密码显隐*/
        etPasswd.transformationMethod = PasswordTransformationMethod.getInstance()
        imgHide.setOnClickListener {
            isPasswdVisible = !isPasswdVisible
            if (isPasswdVisible) {
                imgHide.setImageDrawable(this.getDrawable(R.mipmap.image_mima_visible))
                etPasswd.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                imgHide.setImageDrawable(this.getDrawable(R.mipmap.image_yanjing_yincang))
                etPasswd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    /**
     * step3: 确认新登录密码
     */
    private fun toAssureNewPasswd() {
        val context = this
        val userId = UserHelper.getCurrentLoginUser(this)
        val checkCode = etCode.text.toString()
        val newPasswd = etPasswd.text.toString()
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .modifyLoginPassword(userId, bizId, checkCode, newPasswd)
            .enqueue(object: Callback<BaseHttpBean<String>> {
                override fun onResponse(call: Call<BaseHttpBean<String>>,
                                        response: Response<BaseHttpBean<String>>) {
                    Log.d(TAG, "onResponse-toAssureNewPasswd: ${response.body()}")
                    if (response.body()?.code == 200) {
                        finish()
                    } else {
                        DialogUtil.toast(context, response.body()?.msg)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                }
            })

    }

    companion object {
        private const val TAG = "shu-ResetPasswd"

        private const val PASSWD_MIN_LENGTH = 6
        private const val PASSWD_MAX_LENGTH = 12
        private const val ONE_SECOND: Long = 1000
        private const val MAX_COUNT_DOWN = 60 * ONE_SECOND

        private const val ERROR_PASSWD_TOO_SHORT = "密码太短了"
        private const val ERROR_PASSWD_TOO_LONG = "密码已达到最大长度"

    }

}