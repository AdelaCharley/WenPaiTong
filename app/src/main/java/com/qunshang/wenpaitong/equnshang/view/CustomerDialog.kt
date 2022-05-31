package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_customer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.CustomerBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class CustomerDialog (context: Context,val key : String) : CenterPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        ApiManager.getInstance().getApiMallTest().loadCustomInfo(key).enqueue(object : Callback<CustomerBean>{
            override fun onResponse(call: Call<CustomerBean>, response: Response<CustomerBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    val data = response.body()!!.data
                    videoname.setText(data.title)
                    desc.setText(data.content)
                    Glide.with(context).load(data.poster_url).into(image)
                }
            }

            override fun onFailure(call: Call<CustomerBean>, t: Throwable) {

            }

        })
        close.setOnClickListener {
            dismiss()
        }
        val imm: InputMethodManager ?=
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            if (rootView != null){
                imm.hideSoftInputFromWindow(rootView?.getWindowToken(), 0)
            }
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_customer
    }

}