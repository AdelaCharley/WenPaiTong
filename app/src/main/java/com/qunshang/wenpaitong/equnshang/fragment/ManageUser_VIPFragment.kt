package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_manage_user__v_i_p.*
import kotlinx.android.synthetic.main.fragment_manage_user__v_i_p.count_labela
import kotlinx.android.synthetic.main.fragment_manage_user__v_i_p.count_labelb
import kotlinx.android.synthetic.main.fragment_manage_user__v_i_p.lista
import kotlinx.android.synthetic.main.fragment_manage_user__v_i_p.listb
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.ManageContentAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserVipsBean
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ManageUser_VIPFragment : MyBaseFragment() {

    var vipsAPageIndex = 1

    var vipsBPageIndex = 1

    var vipsAPageSize = 20

    var vipsBPageSize = 20

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manage_user__v_i_p, container, false)
    }

    var isvipAAutoLoadMoreEnabled = true

    var isvipAloading = false

    private var lastvipAVisibleItemPosition = 0

    var isvipBAutoLoadMoreEnabled = true

    var isvipBloading = false

    private var lastvipBVisibleItemPosition = 0
    
    fun loadVipA(){
        ApiManager.getInstance().getApiPersonalTest().loadVipsData(userId =
        UserInfoViewModel.getUserId()
            ,pageIndex = vipsAPageIndex,pageSize = vipsAPageSize).enqueue(object :
            Callback<UserVipsBean> {
            override fun onResponse(call: Call<UserVipsBean>, response: Response<UserVipsBean>) {
                if (response.body() == null){
                    return
                }
                isvipAloading = false
                vipAAdapter.add(response.body()!!.data.levelA)
                this@ManageUser_VIPFragment.vipsAPageIndex++
                if (response.body()!!.data.levelA.size < vipsAPageSize){
                    isvipAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labela.setText(response.body()!!.data.countListLevelA.toString())
            }

            override fun onFailure(call: Call<UserVipsBean>, t: Throwable) {

            }

        })
    }
    
    fun loadVipB(){
        ApiManager.getInstance().getApiPersonalTest().loadVipsData(userId =
        UserInfoViewModel.getUserId()
            ,type = 1,pageIndex = vipsBPageIndex,pageSize = vipsBPageSize).enqueue(object :
            Callback<UserVipsBean> {
            override fun onResponse(call: Call<UserVipsBean>, response: Response<UserVipsBean>) {
                if (response.body() == null){
                    return
                }
                isvipBloading = false
                vipBAdapter.add(response.body()!!.data.levelB)
                this@ManageUser_VIPFragment.vipsBPageIndex++
                if (response.body()!!.data.levelB.size < vipsBPageSize){
                    isvipBAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labelb.setText(response.body()!!.data.countListLevelB.toString())
            }

            override fun onFailure(call: Call<UserVipsBean>, t: Throwable) {

            }

        })
    }

    lateinit var vipAAdapter: ManageContentAdapter

    lateinit var vipBAdapter: ManageContentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val managervipA = LinearLayoutManager(requireContext())
        managervipA.orientation = LinearLayoutManager.VERTICAL
        lista.layoutManager = managervipA
        lista.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isvipAAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managervipA.childCount
                    var totalItemCount = managervipA.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isvipAloading && lastvipAVisibleItemPosition == totalItemCount - 1) {
                        isvipAloading = true
                        loadVipA()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastvipAVisibleItemPosition = managervipA.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastvipAVisibleItemPosition)
            }
        })
        val managervipB = LinearLayoutManager(requireContext())
        managervipB.orientation = LinearLayoutManager.VERTICAL
        listb.layoutManager = managervipB
        listb.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isvipBAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managervipB.childCount
                    var totalItemCount = managervipB.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isvipBloading && lastvipBVisibleItemPosition == totalItemCount - 1) {
                        isvipBloading = true
                        loadVipB()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastvipBVisibleItemPosition = managervipB.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastvipBVisibleItemPosition)
            }
        })
        vipAAdapter = ManageContentAdapter(requireContext())
        vipBAdapter = ManageContentAdapter(requireContext())
        lista.adapter = vipAAdapter
        listb.adapter = vipBAdapter
        loadVipA()
        loadVipB()
        ApiManager.getInstance().getApiPersonalTest().loadVipsData(userId = UserInfoViewModel.getUserId(),type = -1).enqueue(object :
            Callback<UserVipsBean> {
            override fun onResponse(call: Call<UserVipsBean>, response: Response<UserVipsBean>) {
                if (response.body() == null){
                    return
                }
                count_labela.setText(response.body()!!.data.countListLevelA.toString())

                count_labelb.setText(response.body()!!.data.countListLevelB.toString())
            }

            override fun onFailure(call: Call<UserVipsBean>, t: Throwable) {

            }

        })
    }

}