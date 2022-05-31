package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.FragmentMyWbtCreditBinding
import com.qunshang.wenpaitong.equnshang.adapter.MyWBTCreditAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.WBTCreditBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class MyWBTCreditFragment: Fragment() {
    private val TAG = "shu-MyWBTCredit"
    private lateinit var binding: FragmentMyWbtCreditBinding
    private lateinit var userId: String
    private lateinit var list: RecyclerView
    private lateinit var adapter: MyWBTCreditAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMyWbtCreditBinding.inflate(inflater, container, false)
        userId = UserHelper.getCurrentLoginUser(requireContext())
        list = binding.list
        adapter = MyWBTCreditAdapter(requireContext())
        initLayout()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (isDataLoaded){
            return
        }
        getWBTCredit()
        getWBTCreditLog()
        isDataLoaded = true
    }

    var isDataLoaded = false

    private fun getWBTCredit() {
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .getWBTCredit(userId)
            .enqueue(object: Callback<BaseHttpBean<String>> {
                override fun onResponse(call: Call<BaseHttpBean<String>>,
                                        response: Response<BaseHttpBean<String>>) {
                    Log.d(TAG, "onResponse: $response")
                    if (response.isSuccessful) {
                        response.body()!!.data?.let { adapter.setCreditAmount(it) }
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {}
            })
    }


    private var pageIndex = 1
    private var pageSize = 20
    private fun getWBTCreditLog() {
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .getWBTCreditLog(userId, pageIndex, pageSize)
            .enqueue(object: Callback<BaseHttpListBean<WBTCreditBean>> {
                override fun onResponse(call: Call<BaseHttpListBean<WBTCreditBean>>,
                                        response: Response<BaseHttpListBean<WBTCreditBean>>) {
                    Log.d(TAG, "onResponse: $response")
                    if (response.isSuccessful) {
                        if (response.body()?.data == null){
                            return
                        }
                        adapter.add(response.body()!!.data!!)
                        pageIndex++
                        isLoadMoreEnable = response.body()!!.data?.size!! >= pageSize
                    }
                }

                override fun onFailure(call: Call<BaseHttpListBean<WBTCreditBean>>, t: Throwable) {}
            })
    }

    private var isLoadMoreEnable = true
    private var isLoading = false
    private var lastVisibleItemPosition = 0
    private fun initLayout() {
        val layoutManager = LinearLayoutManager(requireContext())
        list.layoutManager = layoutManager
        list.adapter = adapter
        list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isLoadMoreEnable) return
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading
                    && lastVisibleItemPosition == layoutManager.itemCount - 1) {
                    isLoading = true
                    getWBTCreditLog()
                }
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
        })
    }

}