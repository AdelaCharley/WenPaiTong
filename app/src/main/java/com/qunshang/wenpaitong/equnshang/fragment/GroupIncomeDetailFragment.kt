package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_group_income_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupIncomeDetailFragment(val gocid : Int) : MyBaseFragment() {

    lateinit var adapter : com.qunshang.wenpaitong.equnshang.adapter.IncomeOfOneGroupMainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group_income_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiManager.getInstance().getApiGroupMain().getIncomeOfGroupMain(userId =
        UserInfoViewModel.getUserId(),gocId = gocid).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean>{
            override fun onResponse(
                call: Call<com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean>,
                response: Response<com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    layout.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    return
                }
                adapter = com.qunshang.wenpaitong.equnshang.adapter.IncomeOfOneGroupMainAdapter(
                    requireContext(),
                    response.body()!!.data
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean>, t: Throwable) {

            }

        })
    }

}