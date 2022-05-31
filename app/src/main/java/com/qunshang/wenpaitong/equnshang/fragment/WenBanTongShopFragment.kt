package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_wen_ban_tong_shop.empty_layout
import kotlinx.android.synthetic.main.fragment_wen_ban_tong_shop.list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.CultureProductAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.CultureProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class WenBanTongShopFragment(val companyId : Int? = null,var isShowBanner : Boolean = true) : MyBaseFragment() {

    var pageIndex = 1

    val pageSize = 20

    lateinit var adapter: CultureProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wen_ban_tong_shop, container, false)
    }

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

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
                    //StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isFansAloading && lastFansAVisibleItemPosition == totalItemCount - 1) {
                        isFansAloading = true
                        loadData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansAVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                //StringUtils.log("最后可见" + lastFansAVisibleItemPosition)
            }
        })
        adapter = CultureProductAdapter(isShowBanner) //换Adapter，换成企业列表Adapter
        list.adapter = adapter
        loadData()
        //loadBanner()
    }

    /*fun loadBanner(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadBanner().enqueue(object : Callback<BaseHttpListBean<String>>{
            override fun onResponse(
                call: Call<BaseHttpListBean<String>>,
                response: Response<BaseHttpListBean<String>>
            ) {
                if (response.body() != null){
                    if (response.body()!!.code == 200){
                        adapter.setBanner(response.body()!!.data)
                    }
                }
            }

            override fun onFailure(call: Call<BaseHttpListBean<String>>, t: Throwable) {
                StringUtils.log(t.message)
            }

        })
    }*/

    fun loadData() {
        //换接口，换成获取企业列表的接口
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .loadProductList(companyId,pageIndex,pageSize)
            .enqueue(object :
            Callback<BaseHttpListBean<CultureProductBean>> {
            override fun onResponse(
                call: Call<BaseHttpListBean<CultureProductBean>>,
                response: Response<BaseHttpListBean<CultureProductBean>>
            ) {
                StringUtils.log("culture_product_list: $response")
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
                this@WenBanTongShopFragment.pageIndex++
                if (response.body()!!.data?.size!! < pageSize) {
                    isFansAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
            }

            override fun onFailure(call: Call<BaseHttpListBean<CultureProductBean>>, t: Throwable) {

            }

        })
    }


}