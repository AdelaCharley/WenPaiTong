package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_wen_ban_tong_ask.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.CultureNewsAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.CultureNewsBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongAskFragment(val companyId : Int ? = null) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wen_ban_tong_ask, container, false)
    }

    var pageIndex = 1

    val pageSize = 20

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    lateinit var adapter: CultureNewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val managerFansA = LinearLayoutManager(requireContext())
        managerFansA.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = managerFansA
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFansAAutoLoadMoreEnabled) {
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerFansA.childCount
                    var totalItemCount = managerFansA.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isFansAloading && lastFansAVisibleItemPosition == totalItemCount - 1) {
                        isFansAloading = true
                        loadData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansAVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastFansAVisibleItemPosition)
            }
        })
        adapter = CultureNewsAdapter()
        list.adapter = adapter
        loadData()
    }

    fun loadData() {
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .loadNewsList(companyId, pageIndex, pageSize)
            .enqueue(object :
                Callback<BaseHttpListBean<CultureNewsBean>> {
                override fun onResponse(
                    call: Call<BaseHttpListBean<CultureNewsBean>>,
                    response: Response<BaseHttpListBean<CultureNewsBean>>
                ) {
                    Log.d("shulinr", "culture_news_list: $response")
                    if (response.body() == null) {
                        return
                    }
                    if (response.body()!!.code != 200) {
                        return
                    }
                    if (pageIndex == 1 && response.body()!!.data?.size == 0){
                        list.visibility = View.INVISIBLE
                        empty_layout.visibility = View.VISIBLE
                    }
                    isFansAloading = false
                    adapter.add(response.body()!!.data!!)
                    this@WenBanTongAskFragment.pageIndex++
                    if (response.body()!!.data?.size!! < pageSize) {
                        isFansAAutoLoadMoreEnabled = false
                        StringUtils.log("停止了，这个被禁用了")
                    }
                }

                override fun onFailure(
                    call: Call<BaseHttpListBean<CultureNewsBean>>,
                    t: Throwable
                ) {

                }

            })
    }


}