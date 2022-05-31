package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.adapter.OrderAdapterV2
import com.qunshang.wenpaitong.equnshang.data.OrderBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VideoType
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import java.lang.Exception

class OrderFragmentV2(val ordertype : String) : MyBaseFragment() {

    val pageIndex = "1"

    val pageSize = "50"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_v2, container, false)
    }

    var isDataLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isDataLoaded){
            loadData()
            isDataLoaded = true
        }
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
        if ("refresh".equals(str)){
            loadData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        //loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiAMallV3().loadAMallV3OrderList(
            //"261"
            UserInfoViewModel.getUserId()
            ,ordertype,pageIndex,pageSize).enqueue(object :
            Callback<OrderBean> {
            override fun onResponse(call: Call<OrderBean>, response: Response<OrderBean>) {
                Log.i("zhangjuniii","你好啊" + response.body()?.data?.size + "-11-" + ordertype)
                try {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.data.size == 0){
                        empty_layout.visibility = View.VISIBLE
                        list.visibility = View.GONE
                        return
                    }
                    list.adapter = OrderAdapterV2(context, response.body()?.data)
                } catch ( e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<OrderBean>, t: Throwable) {
                Log.i("zhangjuniii","失败了")
            }

        })
    }

}