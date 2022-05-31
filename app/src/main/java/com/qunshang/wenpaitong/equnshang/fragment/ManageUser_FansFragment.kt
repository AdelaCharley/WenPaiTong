package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_manage_user__fans.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.ManageContentAdapter
import com.qunshang.wenpaitong.equnshang.data.UserFansBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ManageUser_FansFragment : MyBaseFragment() {

    var fansAPageIndex = 1

    var fansBPageIndex = 1

    var fansAPageSize = 20

    var fanBPageSize = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_user__fans, container, false)
    }

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    var isFansBAutoLoadMoreEnabled = true

    var isFansBloading = false

    private var lastFansBVisibleItemPosition = 0

    fun loadFansA(){
        ApiManager.getInstance().getApiPersonalTest().loadFansData(userId =
        UserInfoViewModel.getUserId()
        ,pageIndex = fansAPageIndex,pageSize = fansAPageSize
        ).enqueue(object : Callback<UserFansBean>{
            override fun onResponse(call: Call<UserFansBean>, response: Response<UserFansBean>) {
                if (response.body() == null){
                    return
                }
                isFansAloading = false
                fansAAdapter.add(response.body()!!.data.levelA)
                this@ManageUser_FansFragment.fansAPageIndex++
                if (response.body()!!.data.levelA.size < fansAPageSize){
                    isFansAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labela.setText(response.body()!!.data.countListLevelA.toString())
            }

            override fun onFailure(call: Call<UserFansBean>, t: Throwable) {

            }

        })
    }

    fun loadFansB(){
        ApiManager.getInstance().getApiPersonalTest().loadFansData(userId =
        UserInfoViewModel.getUserId()
            ,type = 1,pageIndex = fansBPageIndex,pageSize = 20).enqueue(object : Callback<UserFansBean>{
            override fun onResponse(call: Call<UserFansBean>, response: Response<UserFansBean>) {
                if (response.body() == null){
                    return
                }
                isFansBloading = false
                fansBAdapter.add(response.body()!!.data.levelB)
                this@ManageUser_FansFragment.fansBPageIndex++
                if (response.body()!!.data.levelB.size < fanBPageSize){
                    isFansBAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labelb.setText(response.body()!!.data.countListLevelB.toString())
            }

            override fun onFailure(call: Call<UserFansBean>, t: Throwable) {

            }

        })
    }

    lateinit var fansAAdapter: ManageContentAdapter

    lateinit var fansBAdapter: ManageContentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val managerFansA = LinearLayoutManager(requireContext())
        managerFansA.orientation = LinearLayoutManager.VERTICAL
        lista.layoutManager = managerFansA
        lista.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFansAAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerFansA.childCount
                    var totalItemCount = managerFansA.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isFansAloading && lastFansAVisibleItemPosition == totalItemCount - 1) {
                        isFansAloading = true
                        loadFansA()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansAVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastFansAVisibleItemPosition)
            }
        })
        val managerFansB = LinearLayoutManager(requireContext())
        managerFansB.orientation = LinearLayoutManager.VERTICAL
        listb.layoutManager = managerFansB
        listb.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFansBAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerFansB.childCount
                    var totalItemCount = managerFansB.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isFansBloading && lastFansBVisibleItemPosition == totalItemCount - 1) {
                        isFansBloading = true
                        loadFansB()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansBVisibleItemPosition = managerFansB.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastFansBVisibleItemPosition)
            }
        })
        fansAAdapter = ManageContentAdapter(requireContext())
        fansBAdapter = ManageContentAdapter(requireContext())
        lista.adapter = fansAAdapter
        listb.adapter = fansBAdapter
        layout_fans_add.visibility = View.VISIBLE
        ApiManager.getInstance().getApiPersonalTest().loadFansData(userId =
        UserInfoViewModel.getUserId()
            ,type = -1).enqueue(object : Callback<UserFansBean>{
            override fun onResponse(call: Call<UserFansBean>, response: Response<UserFansBean>) {
                if (response.body() == null){
                    return
                }
                fans_add_count.setText(response.body()!!.data.more.toString())
            }

            override fun onFailure(call: Call<UserFansBean>, t: Throwable) {

            }

        })
        loadFansA()
        loadFansB()
    }

}