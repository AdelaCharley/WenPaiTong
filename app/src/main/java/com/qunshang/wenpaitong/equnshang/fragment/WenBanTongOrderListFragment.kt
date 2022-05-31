package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_wen_ban_tong_order_list.*
import kotlinx.android.synthetic.main.layout_list_empty.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.WenBanTongOrderListAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WenBanTongOrderListBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.lang.Exception

class WenBanTongOrderListFragment(val status : String) : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wen_ban_tong_order_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(str : String){
        if ("wenbantongrefresh".equals(str)){
            pageIndex = 1
            adapter.remove()
            loadData()
        }
    }

    var pageIndex = 1

    val pageSize = 20

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    lateinit var adapter: WenBanTongOrderListAdapter

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
        adapter = WenBanTongOrderListAdapter(requireContext())
        list.adapter = adapter
        //loadData()
    }

    var isDataLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isDataLoaded){
            loadData()
            isDataLoaded = true
        }
    }

    fun loadData() {
        ApiManager.getInstance().getApiWenBanTong_ZhangJun()
            .loadOrderList(UserInfoViewModel.getUserId(),status, pageIndex, pageSize)
            .enqueue(object :
                Callback<WenBanTongOrderListBean> {
                override fun onResponse(
                    call: Call<WenBanTongOrderListBean>,
                    response: Response<WenBanTongOrderListBean>
                ) {
                    try {
                        Log.d("shulinr", "culture_news_list: $response")
                        if (response.body() == null) {
                            return
                        }
                        if (response.body()!!.code != 200) {
                            return
                        }
                        if (pageIndex == 1 && response.body()!!.data.size == 0){
                            empty.visibility = View.VISIBLE
                            list.visibility = View.GONE
                            image_status.setImageDrawable(requireContext().resources.getDrawable(R.mipmap.image_zanwudingdan))
                            text_status.setText("暂无订单")
                            return
                        }
                        isFansAloading = false
                        adapter.add(response.body()!!.data)
                        this@WenBanTongOrderListFragment.pageIndex++
                        if (response.body()!!.data.size < pageSize) {
                            isFansAAutoLoadMoreEnabled = false
                            StringUtils.log("停止了，这个被禁用了")
                        }
                    } catch (e : Exception){
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<WenBanTongOrderListBean>,
                    t: Throwable
                ) {

                }

            })
    }

}