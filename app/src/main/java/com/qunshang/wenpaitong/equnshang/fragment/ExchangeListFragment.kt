package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_exchange_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.ExchangeAdapter
import com.qunshang.wenpaitong.equnshang.data.ExchangeBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import java.lang.Exception

class ExchangeListFragment(val status : String?) : MyBaseFragment() {

    var currentIndex = 1;
    val pageSize = 100;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exchange_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
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

    fun loadData(){
        ApiManager.getInstance().getApiAMallV3().loadAMallV3ExchangeList(UserInfoViewModel.getUserId(),status,currentIndex,pageSize).enqueue(object :
            Callback<ExchangeBean> {
            override fun onResponse(call: Call<ExchangeBean>, response: Response<ExchangeBean>) {
                try {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.data.size == 0){
                        empty_layout.visibility = View.VISIBLE
                        list.visibility = View.GONE
                        return
                    }
                    list.adapter = ExchangeAdapter(context, response.body()!!.data)
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ExchangeBean>, t: Throwable) {

            }

        })
    }

}