package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import kotlinx.android.synthetic.main.activity_bind_inviter.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
class BindInviterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_inviter)
        toolbar_back.setOnClickListener {
            //finish()
        }
        toolbar_back.visibility = View.INVISIBLE
        toolbar_title.setText("填写推荐人")
        input.addTextChangedListener {
            loadBinderUserInfo()
        }
        next.setOnClickListener {
            if (StringUtils.isEmpty(input.text.toString())){
                MessageDialog.show(this, "", "确定不绑定联系人", "确认", "取消")
                    .setOkButton(OnDialogButtonClickListener { baseDialog, v ->

                        baseDialog.doDismiss()
                        goRefine()
                        //仅需要对需要处理的按钮进行操作
                        //处理确定按钮事务
                        false //可以通过 return 决定点击按钮是否默认自动关闭对话框
                    })
                //goRefine()
            } else {
                if (StringUtils.isRightPhone(input.text.toString())){
                    MessageDialog.show(this, "", "绑定联系人后不可更改", "确认", "取消")
                        .setOkButton(OnDialogButtonClickListener { baseDialog, v ->
                            ApiManager.getInstance().getApiPersonalTest().bindInviter(UserInfoViewModel.getUserId(),input.text.toString()).enqueue(object : Callback<BaseHttpBean<Int>>{
                                override fun onResponse(
                                    call: Call<BaseHttpBean<Int>>,
                                    response: Response<BaseHttpBean<Int>>
                                ) {
                                    if (response.body() != null){
                                        baseDialog.doDismiss()
                                        if (response.body()?.code == 200){
                                            goRefine()
                                        } else {
                                            DialogUtil.toast(this@BindInviterActivity,response.body()?.msg)
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                                }

                            })
                            return@OnDialogButtonClickListener true
                        })


                } else {
                    TipDialog.show(this,"请输入正确的手机号",TipDialog.TYPE.ERROR)
                    //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this,"请输入正确的手机号")
                }
            }
        }
    }

    fun loadBinderUserInfo(){
        if (StringUtils.isRightPhone(input.text.toString())){
            ApiManager.getInstance().getApiLoginTest().getReferee(input.text.toString()).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.body() == null){
                        DialogUtil.toast(this@BindInviterActivity,"出错了")
                        return
                    }
                    val jsonObject = JSONObject(response.body()!!.string())
                    if (jsonObject.getInt("code") != 200){
                        //ToastUtil.toast(this@BindInviterActivity,)
                            TipDialog.show(this@BindInviterActivity,jsonObject.getString("msg"),TipDialog.TYPE.ERROR)
                        return
                    }
                    /*if (response.body()!!.code != 200){
                        ToastUtil.toast(this@BindInviterActivity,response.body()!!.msg)
                        return
                    }*/
                    inviter_headimage.visibility = View.VISIBLE
                    inviter_username.visibility = View.VISIBLE
                    Glide.with(this@BindInviterActivity).load(jsonObject.getJSONObject("data").getString("headImageUrl")).into(inviter_headimage)
                    inviter_username.setText(jsonObject.getJSONObject("data").getString("uname"))
                    //next.setTextColor(getColor(R.color.blue))
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
        }
    }

    fun goRefine(){
        val intent: Intent = Intent(
            this,
            MainActivity::class.java
        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        //startActivity(Intent(this,RefineActivity::class.java))
    }

}