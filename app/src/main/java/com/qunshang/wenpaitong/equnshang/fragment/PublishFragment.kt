package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialog.v3.WaitDialog
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_publish.list
import retrofit2.Call
import retrofit2.Callback
import com.qunshang.wenpaitong.equnshang.adapter.PublishAdapter
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class PublishFragment(val userId : String): MyBaseFragment() {

    private var adapter: PublishAdapter? = null

    private var dataBeanList: ArrayList<RecommendVideoBean.DataBean>? = null   //视频播放列表

    private val apiVideoTest: ApiVideo = ApiManager.getInstance().getApiVideoTest()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_publish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val managerFansA = GridLayoutManager(requireContext(),2)
        list.layoutManager = managerFansA
        list.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                        loadVideo()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansAVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastFansAVisibleItemPosition)
            }
        })
        adapter = PublishAdapter(requireContext())
        list.adapter = adapter

        loadVideo()
    }

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    private fun loadVideo(){
        WaitDialog.show(requireActivity() as AppCompatActivity,"正在获取视频")
        val callback = object: Callback<RecommendVideoBean>{
            override fun onResponse(call: Call<RecommendVideoBean>, response: retrofit2.Response<RecommendVideoBean>) {
                WaitDialog.dismiss()
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data == null){
                    return
                }

                list.background = null

                isFansAloading = false
                dataBeanList?.addAll(response.body()!!.data)
                if (adapter != null){
                    adapter?.addData(response.body()!!.data)
                }
                pageIndex++
                if (response.body()!!.data.size < pageSize){
                    isFansAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }

            }

            override fun onFailure(call: Call<RecommendVideoBean>, t: Throwable) {
                WaitDialog.dismiss()
                t.message
            }
        }

        apiVideoTest.loadVideoList(UserInfoViewModel.getUserId(),pageIndex = pageIndex,pageSize = pageSize).enqueue(callback)

    }

    var pageIndex = 1

    val pageSize = 20

}