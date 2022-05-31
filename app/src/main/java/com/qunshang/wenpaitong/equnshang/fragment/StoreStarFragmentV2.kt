package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_store_star.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.FocusManufactuerAdapterV2
import com.qunshang.wenpaitong.equnshang.data.FocusManufactuerBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class StoreStarFragmentV2 : MyBaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store_star_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        val divider = DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.view_divide_cccccc))
        list.addItemDecoration(divider)
        list.layoutManager = manager
        loadData()
    }

    fun loadData(){
        ApiManager.getInstance().getApiPersonalTest().loadFocusManufactuerList(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<FocusManufactuerBean> {
            override fun onResponse(call: Call<FocusManufactuerBean>, response: Response<FocusManufactuerBean>) {
                if (response.body() == null){
                    return
                }
                list.adapter = FocusManufactuerAdapterV2(
                    context,
                    response.body()?.data
                )
            }

            override fun onFailure(call: Call<FocusManufactuerBean>, t: Throwable) {

            }

        })
    }
}