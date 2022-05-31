package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_group_detail_shop.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.GroupMainDetailShopAdapter
import com.qunshang.wenpaitong.equnshang.data.GroupMainProductBean

class GroupDetailShopFragment(val gocid : Int) : MyBaseFragment() {

    lateinit var adapter : GroupMainDetailShopAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group_detail_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiManager.getInstance().getApiGroupMain().loadGroupMainProductList(gocid).enqueue(object : Callback<GroupMainProductBean>{
            override fun onResponse(call: Call<GroupMainProductBean>, response: Response<GroupMainProductBean>) {
                if (response.body() == null){
                    return
                }
                adapter = GroupMainDetailShopAdapter(
                    requireContext(),
                    response.body()!!.data
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<GroupMainProductBean>, t: Throwable) {

            }

        })
    }

}