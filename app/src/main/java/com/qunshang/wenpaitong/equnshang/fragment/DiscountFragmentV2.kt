package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_discount_v2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.DiscountAdapterV2
import com.qunshang.wenpaitong.equnshang.data.DiscountTicketBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class DiscountFragmentV2(val status : String) : MyBaseFragment() {

    var currentIndex = 1;
    val pageSize = 50;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discount_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiPersonalTest().loadDiscountTickert(UserInfoViewModel.getUserId(),status,currentIndex,pageSize).enqueue(object :
            Callback<DiscountTicketBean> {
            override fun onResponse(call: Call<DiscountTicketBean>, response: Response<DiscountTicketBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    empty_layout.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    return
                }
                list.adapter = DiscountAdapterV2(context, response.body()!!.data)
            }

            override fun onFailure(call: Call<DiscountTicketBean>, t: Throwable) {

            }

        })
    }

}