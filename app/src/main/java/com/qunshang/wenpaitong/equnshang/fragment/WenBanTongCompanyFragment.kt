package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.FragmentWenBanTongCompanyBinding
import com.qunshang.wenpaitong.equnshang.adapter.CultureCompanyAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.CultureCompanyBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongCompanyFragment : MyBaseFragment() {

    lateinit var binding: FragmentWenBanTongCompanyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWenBanTongCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    var pageIndex = 1

    val pageSize = 20

    var isAutoLoadMoreEnabled = true

    var isloading = false

    private var lastVisibleItemPosition = 0

    lateinit var adapter: CultureCompanyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val managerFansA = LinearLayoutManager(requireContext())
        managerFansA.orientation = LinearLayoutManager.VERTICAL
        binding.list.layoutManager = managerFansA
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isAutoLoadMoreEnabled) {
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerFansA.childCount
                    var totalItemCount = managerFansA.itemCount
                    Log.d("shulinr", "当前的totalItemCOunt: $totalItemCount")
                    if (!isloading && lastVisibleItemPosition == totalItemCount - 1) {
                        isloading = true
                        loadData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                StringUtils.log("最后可见:$lastVisibleItemPosition")
            }
        })
        adapter = CultureCompanyAdapter()//换Adapter，换成企业列表Adapter
        binding.list.adapter = adapter
        loadData()
    }

    fun loadData() {
        //换接口，换成获取企业列表的接口
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .loadCompanyList(pageIndex = pageIndex, pageSize = pageSize).enqueue(object :
                Callback<BaseHttpListBean<CultureCompanyBean>> {
                override fun onResponse(
                    call: Call<BaseHttpListBean<CultureCompanyBean>>,
                    response: Response<BaseHttpListBean<CultureCompanyBean>>
                ) {
                    Log.d("shulinr", "culture_company_list: $response")
                    if (response.body() == null) {
                        return
                    }
                    if (response.body()!!.code != 200) {
                        return
                    }
                    isloading = false
                    pageIndex++
                    val data = response.body()!!.data
                    adapter.add(data!!)
                    if (data.size < pageSize) {
                        isAutoLoadMoreEnabled = false
                        Log.d("shulinr", "停止了，这个被禁用了")
                    }
                }

                override fun onFailure(
                    call: Call<BaseHttpListBean<CultureCompanyBean>>,
                    t: Throwable
                ) {

                }

            })
    }
}