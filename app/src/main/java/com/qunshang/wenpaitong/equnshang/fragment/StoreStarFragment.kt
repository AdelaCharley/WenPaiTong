package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_store_star.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreStarFragment : MyBaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store_star, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiPersonalTest().loadFocusManufactuerList(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<com.qunshang.wenpaitong.equnshang.data.FocusManufactuerBean> {
            override fun onResponse(call: Call<com.qunshang.wenpaitong.equnshang.data.FocusManufactuerBean>, response: Response<com.qunshang.wenpaitong.equnshang.data.FocusManufactuerBean>) {
                if (response.body() == null){
                    return
                }
                list.adapter = com.qunshang.wenpaitong.equnshang.adapter.FocusManufactuerAdapter(
                    context,
                    response.body()?.data
                )
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.FocusManufactuerBean>, t: Throwable) {

            }

        })
    }

}