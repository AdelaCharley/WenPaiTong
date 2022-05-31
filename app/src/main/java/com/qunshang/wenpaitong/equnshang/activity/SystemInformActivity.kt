package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.SystemInformFragment
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class SystemInformActivity : BaseActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_inform)
        supportFragmentManager.beginTransaction().add(R.id.container, SystemInformFragment()).commit()
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("系统通知")
        if (!StringUtils.isEmpty(UserInfoViewModel.getUserId())){
            ApiManager.getInstance().getApiMainTest().makeUnReadToRead(UserInfoViewModel.getUserId()).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
        }

    }
}