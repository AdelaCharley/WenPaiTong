package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_private_area.*
import kotlinx.android.synthetic.main.fragment_private_area.groupticketrule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.activity.GroupTicketRuleActivity

class PrivateAreaFragment : MyBaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_private_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        groupticketrule.setOnClickListener { startActivity(
            Intent(requireActivity(),
                GroupTicketRuleActivity::class.java)
        ) }
        ApiManager.getInstance().getApiMainTest().loadPriateAreaData(UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpListBean<Int>>{
            override fun onResponse(call: Call<BaseHttpListBean<Int>>, response: Response<BaseHttpListBean<Int>>) {
                if (response.body() == null){
                    return
                }
                val data = response.body()!!.data
                mygroupticket.setText(data?.get(0).toString())
                if (data?.size!! >= 3){
                    count_fans.setText(data.get(1).toString())
                    count_vip.setText(data.get(2).toString())
                    count_parter.setText(data.get(3).toString())
                }
            }

            override fun onFailure(call: Call<BaseHttpListBean<Int>>, t: Throwable) {

            }


        })
    }

    fun refresh(){
        init()
    }

}