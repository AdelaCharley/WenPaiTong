package com.qunshang.wenpaitong.equnshang.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.LoginWithPasswordFragment

class LoginWithPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_password)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("登录")
        supportFragmentManager.beginTransaction().add(R.id.container, LoginWithPasswordFragment()).commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    assert(v != null)
                    imm.hideSoftInputFromWindow(v?.getWindowToken(), 0)
                }
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }

    fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom: Int = top + v.getHeight()
            val right: Int = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

}