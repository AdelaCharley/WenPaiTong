package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.google.gson.Gson
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.dialog_identity_info.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
//import com.qunshang.wenpaitong.equnshang.activity.AuthActivity
import com.qunshang.wenpaitong.equnshang.activity.AuthInfoActivity
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class IDInfoDialog(context: Context,val bean: IdBean) : CenterPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.dialog_identity_info
    }

    override fun onCreate() {
        super.onCreate()
        myname.setText(bean.data.name)
        if (bean.data.sex == 1){
            mysex.setText("女")
        } else {
            mysex.setText("男")
        }
        myid.setText(bean.data.num)
        myaddress.setText(bean.data.address)
        mybirth.setText(bean.data.birth)
        mygroup.setText(bean.data.nationality)
        error.setOnClickListener {
            dismiss()
        }
        allright.setOnClickListener {
            dismiss()
            WaitDialog.show(context as AppCompatActivity,"正在加载")
            val bean = IdBean.IdSubmitBean()
            bean.userId = UserInfoViewModel.getUserId()
            bean.faceInfo = this.bean.data
            val gson = Gson()
            val jsonstr = gson.toJson(bean)

            val mediaType : MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
            val stringBody = RequestBody.create(mediaType,jsonstr)

            ApiManager.getInstance().getApiPersonalTest().submitIdentity(stringBody).enqueue(object : Callback<BaseHttpBean<Int>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<Int>>,
                    response: Response<BaseHttpBean<Int>>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        WaitDialog.dismiss()
                        TipDialog.show(context as AppCompatActivity, "绑定成功",TipDialog.TYPE.SUCCESS)
                        goAuthInfo()
                        return
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                }

            })
        }
    }

    fun goAuthInfo(){
        ApiManager.getInstance().getApiOld().identifyedBean(UserInfoViewModel.getUserId()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() == null){
                    return
                }
                val jsonstr = response.body()!!.string()
                val json = JSONObject(jsonstr)
                if (json.getInt("status") == 0){
                    val intent = Intent(context, AuthInfoActivity::class.java)
                    val gson = Gson()
                    val realInfo = gson.fromJson(json.getJSONObject("realInfo").toString(),
                        IdentityInfoBean::class.java)
                    intent.putExtra("info",realInfo)
                    context.startActivity(intent)
                    (context as AppCompatActivity).finish()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })
    }

}