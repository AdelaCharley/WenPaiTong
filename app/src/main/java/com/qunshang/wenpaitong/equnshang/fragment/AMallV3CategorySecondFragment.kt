package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_a_mall_v3_category_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AMallV3CategorySecondAdapter
import com.qunshang.wenpaitong.equnshang.data.AMallV3CategoryFirstBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class AMallV3CategorySecondFragment(val categoryId : Int) : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a_mall_v3_category_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiManager.getInstance().getApiAMallV3().loadFirstCategorys(categoryId = categoryId).enqueue(object :
            Callback<AMallV3CategoryFirstBean> {
            override fun onResponse(
                call: Call<AMallV3CategoryFirstBean>,
                response: Response<AMallV3CategoryFirstBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    categorys.adapter = AMallV3CategorySecondAdapter(requireContext(),response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AMallV3CategoryFirstBean>, t: Throwable) {

            }

        })
    }

}