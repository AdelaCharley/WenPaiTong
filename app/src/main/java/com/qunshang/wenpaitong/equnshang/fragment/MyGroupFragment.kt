package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my_group.empty_layout
import kotlinx.android.synthetic.main.fragment_my_group.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.GroupListAdapter
import com.qunshang.wenpaitong.equnshang.data.MyGroupBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import java.lang.Exception

class MyGroupFragment(val status : Int) : MyBaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    var isDataLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isDataLoaded){
            loadData()
            isDataLoaded = true
        }
    }

    fun loadData(){
        ApiManager.getInstance().getApiAMallV3().loadGroupList(
            UserInfoViewModel.getUserId()
            ,status).enqueue(object : Callback<MyGroupBean>{
            override fun onResponse(call: Call<MyGroupBean>, response: Response<MyGroupBean>) {
                try {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        if (response.body()!!.data.size == 0){
                            empty_layout.visibility = View.VISIBLE
                            list.visibility = View.GONE
                            return
                        }
                        list.adapter = GroupListAdapter(requireContext(),response.body()!!.data)
                    }
                } catch ( e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<MyGroupBean>, t: Throwable) {

            }

        })
    }

}