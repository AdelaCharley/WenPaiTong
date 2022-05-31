package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_new_ren_mai.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.NewRenMaiAdapter
import com.qunshang.wenpaitong.equnshang.data.NewRenMaiBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class NewRenMaiFragment(val type : Int? = null,val keyword : String ? = null) : Fragment() {

    var pageIndex = 1

    val pageSize = 20

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_ren_mai,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(s : String){}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        val managerFansA = LinearLayoutManager(requireContext())
        managerFansA.orientation = LinearLayoutManager.VERTICAL
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
        adapter = NewRenMaiAdapter(requireContext())
        list.adapter = adapter
        loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadNewRenMaiList(userId = UserInfoViewModel.getUserId(), keyword = keyword, type = type,pageIndex = pageIndex,pageSize = pageSize).enqueue(object :
            Callback<NewRenMaiBean> {
            override fun onResponse(
                call: Call<NewRenMaiBean>,
                response: Response<NewRenMaiBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    return
                }
                StringUtils.log("当前type是" + type + keyword)
                if ((pageIndex == 1) && (response.body()!!.data.size == 0)){
                    list?.visibility = View.GONE
                    layout_empty?.visibility = View.VISIBLE
                    EventBus.getDefault().post("newrenmainull")
                } else {
                    list?.visibility = View.VISIBLE
                    layout_empty?.visibility = View.GONE
                    EventBus.getDefault().post("newrenmainotnull")
                }
                isFansAloading = false
                adapter.add(response.body()!!.data)
                this@NewRenMaiFragment.pageIndex++
                if (response.body()!!.data.size < pageSize){
                    isFansAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
            }

            override fun onFailure(call: Call<NewRenMaiBean>, t: Throwable) {

            }

        })
    }

    lateinit var adapter: NewRenMaiAdapter
}