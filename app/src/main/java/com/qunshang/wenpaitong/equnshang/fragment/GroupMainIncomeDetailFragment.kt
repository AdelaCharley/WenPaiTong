package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_group_main_income_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupMainIncomeDetailFragment(val productId : Int,val state : Int) : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_main_income_detail, container, false)
    }

    lateinit var adapter : com.qunshang.wenpaitong.equnshang.adapter.IncomeDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiManager.getInstance().getApiGroupMain().loadIncomeDetail(userId = UserInfoViewModel.getUserId(),productId = productId,state = state).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.IncomeDetailBean>{
            override fun onResponse(
                call: Call<com.qunshang.wenpaitong.equnshang.data.IncomeDetailBean>,
                response: Response<com.qunshang.wenpaitong.equnshang.data.IncomeDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                adapter = com.qunshang.wenpaitong.equnshang.adapter.IncomeDetailAdapter(
                    requireContext(),
                    response.body()!!.data
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.IncomeDetailBean>, t: Throwable) {

            }

        })
    }

}