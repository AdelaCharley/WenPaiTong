package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_auth_info.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.IdentityInfoBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel

class AuthInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_info)
        val authinfo : IdentityInfoBean? = intent.getSerializableExtra("info") as IdentityInfoBean
        if (authinfo == null){
            return
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("实名认证")
        Glide.with(this).load(UserInfoViewModel.getUserInfo()!!.headimage).into(image)
        videoname.setText("姓名:" + authinfo.realname)
        realname.setText(authinfo.realname)
        idnum.setText(authinfo.realidentity)
        address.setText(authinfo.realaddress)
        if (authinfo.realgender.equals("0")){
            sex.setText("男")
        } else {
            sex.setText("女")
        }
        birth.setText(authinfo.realbirth)
        nation.setText(authinfo.realnation)
    }
}