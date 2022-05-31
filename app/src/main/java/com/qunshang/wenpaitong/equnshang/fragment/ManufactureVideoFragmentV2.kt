package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_manufacture_video_v2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ManufactureVideoAdapterV2
import com.qunshang.wenpaitong.equnshang.data.QiShiTongVideoBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class ManufactureVideoFragmentV2(val manufactureId : Int) : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manufacture_video_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = GridLayoutManager(context,2)
        list.layoutManager = manager
        ApiManager.getInstance().getApiAMallV3().loadManufactureVideo(UserInfoViewModel.getUserId(),manufactureId).
            enqueue(object : Callback<QiShiTongVideoBean>{
                override fun onResponse(
                    call: Call<QiShiTongVideoBean>,
                    response: Response<QiShiTongVideoBean>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()?.code == 200){
                        if (response.body()?.data?.size == 0){
                            list.visibility = View.GONE
                            layoutempty.visibility = View.VISIBLE
                            return
                        }
                        list.adapter = ManufactureVideoAdapterV2(context, response.body()!!.data)
                    }

                }

                override fun onFailure(call: Call<QiShiTongVideoBean>, t: Throwable) {

                }

            })

    }

}