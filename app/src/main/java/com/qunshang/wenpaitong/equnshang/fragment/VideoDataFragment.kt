package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_video_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.VideoContentDataBean

class VideoDataFragment : MyBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiManager.getInstance().getApiAddress().loadVideoDataContent(UserInfoViewModel.getUserId()).enqueue(object : Callback<VideoContentDataBean>{
            override fun onResponse(call: Call<VideoContentDataBean>, response: Response<VideoContentDataBean>) {
                if (response.body() == null){
                    return
                }
                val manager = LinearLayoutManager(context)
                manager.orientation = LinearLayoutManager.VERTICAL
                list.layoutManager = manager
                list.adapter = com.qunshang.wenpaitong.equnshang.adapter.VideoDataContentAdapter(
                    context,
                    response.body()!!.data
                )
            }

            override fun onFailure(call: Call<VideoContentDataBean>, t: Throwable) {

            }

        })
    }

}