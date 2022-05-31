package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_product_star.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ProductCollectionAdapterV2
import com.qunshang.wenpaitong.equnshang.data.AMallV3ProductStarBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class ProductStarFragmentV2 : MyBaseFragment() {
    lateinit var adapter: ProductCollectionAdapterV2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_star_v2, container, false)
    }

    fun loadData(){
        ApiManager.getInstance().getApiAMallV3().loadAMallV3ProductStarBean(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<AMallV3ProductStarBean> {
            override fun onResponse(call: Call<AMallV3ProductStarBean>, response: Response<AMallV3ProductStarBean>) {
                val data = response.body()?.data
                adapter = ProductCollectionAdapterV2(context, data,this@ProductStarFragmentV2)
                list.adapter = adapter
            }

            override fun onFailure(call: Call<AMallV3ProductStarBean>, t: Throwable) {

            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = linearLayoutManager
        loadData()
        /*button_unstar.setOnClickListener {
            makeBottomVisbleOrUnVisble()
            val list = StringUtils.analyseSetToString(adapter.selectedForDelete)
            ApiManager.getInstance().getApiPersonalTest().unstarProductCollection(list).enqueue(object :
                Callback<BaseHttpBean<String>> {
                override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {
                    val data = response.body()
                    if (data?.code == 200){
                        loadData()
                        //adapter.updateAfterRemove()
                    } else {
                        DialogUtil.toast(requireContext(),data?.msg)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

                }

            })
        }*/

    }

/*    fun makeBottomVisbleOrUnVisble(){
        if (bottom_layout.visibility == View.GONE){
            bottom_layout.visibility = View.VISIBLE
            EventBus.getDefault().post("user_product_checkbox")
        } else {
            bottom_layout.visibility = View.GONE
            EventBus.getDefault().post("user_product_checkbox")
        }
    }*/
}