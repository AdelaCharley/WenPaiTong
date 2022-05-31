package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.activity_change_user_name.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ChangeUserNameActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_name)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("修改昵称")
        toolbar_right.setText("保存")
        toolbar_right.setTextColor(resources.getColor(R.color.toolbarright_save))
        input_mynickname.setText(UserInfoViewModel.getUserInfo()?.uname)
        currentnumber.setText(input_mynickname.text.toString().length.toString() + "/6")
        input_mynickname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                currentnumber.setText(input_mynickname.text.toString().length.toString() + "/6")
            }

        })
        toolbar_right.setOnClickListener {
            WaitDialog.show(this,"正在修改")
            ApiManager.getInstance().getApiPersonalTest().updatePersonalData(
                UserInfoViewModel.getUserId(),
                input_mynickname.text.toString()!!,
                UserInfoViewModel.getUserInfo()!!.gender.toString(),
                //UserInfoViewModel.getUserInfo()!!.birthday,
                //UserInfoViewModel.getUserInfo()!!.tags,
                UserInfoViewModel.getUserInfo()!!.headimage)
                .enqueue(object: Callback<UserMsgBean> {
                    override fun onResponse(call: Call<UserMsgBean>,
                                            response: Response<UserMsgBean>
                    ) {
                        WaitDialog.dismiss()
                        Log.i("zhangjuniii", UserInfoViewModel.getUserInfo()!!.headimage)
                        if (response.body() == null) {
                            return
                            //ToastUtil.toast(this@UserInfoActivity,"" + response.body()!!.data.uname)
                        }
                        if (response.body()!!.code == 200) {
                            DialogUtil.toast(this@ChangeUserNameActivity,"修改成功")
                            //username.setText(input_mynickname.text.toString())
                            UserInfoViewModel.getUserInfo()?.uname = input_mynickname.text.toString()
                            finish()
                        } else {
                            DialogUtil.toast(this@ChangeUserNameActivity,"修改失败")
                        }
                    }

                    override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                        WaitDialog.dismiss()
                    }

                })
        }
    }
}