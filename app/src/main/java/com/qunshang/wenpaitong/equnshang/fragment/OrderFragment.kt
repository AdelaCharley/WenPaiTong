package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.OrderAdapter

class OrderFragment(val ordertype : String) : MyBaseFragment() {

    val pageIndex = "1"

    val pageSize = "50"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiMallTest().loadOrders(UserInfoViewModel.getUserId(),ordertype,pageIndex,pageSize).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.OrderBean>{
            override fun onResponse(call: Call<com.qunshang.wenpaitong.equnshang.data.OrderBean>, response: Response<com.qunshang.wenpaitong.equnshang.data.OrderBean>) {
                Log.i("zhangjuniii","你好啊" + response.body()?.data?.size + "-11-" + ordertype)
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    empty_layout.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    return
                }
                list.adapter =
                    OrderAdapter(context, response.body()?.data)
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.OrderBean>, t: Throwable) {
                Log.i("zhangjuniii","失败了")
            }

        })
    }

}