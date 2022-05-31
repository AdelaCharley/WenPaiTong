package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiMain
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.adapter.SystemInformAdapter
import com.qunshang.wenpaitong.equnshang.data.SystemInformBean
import kotlinx.android.synthetic.main.fragment_system_inform.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * 系统通知页面
 * create by 何姝霖
 */
//这个先别删
class SystemInformFragment : MyBaseFragment() {
    private val TAG = "TEST_SystemInformFragment"
    private lateinit var systemInformAdapter: SystemInformAdapter
    private var systemInform: List<SystemInformBean>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_system_inform, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadSystemInform()
    }

    private fun loadSystemInform(){
        val apiMain: ApiMain = ApiManager.getInstance().getApiMainTest()
        apiMain.loadSystemInform(UserInfoViewModel.getUserId()).enqueue(object: Callback<BaseHttpListBean<SystemInformBean>>{
            override fun onResponse(call: Call<BaseHttpListBean<SystemInformBean>>,
                                    response: Response<BaseHttpListBean<SystemInformBean>>) {
                systemInform = response.body()!!.data
                if (systemInform == null){
                    return
                }
                systemInformAdapter = SystemInformAdapter(context!!, systemInform!!)
                rv_system_inform_list!!.layoutManager = LinearLayoutManager(context)
                rv_system_inform_list.adapter = systemInformAdapter
            }
            override fun onFailure(call: Call<BaseHttpListBean<SystemInformBean>>, t: Throwable) {
                Log.e(TAG!!, "onFailure: ", t)
            }
        })
    }
}