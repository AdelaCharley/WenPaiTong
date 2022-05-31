package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.VideoPrivateSettingActivity
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_edit_video.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.EditVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.EditVideoBean

class EditVideoFragment : MyBaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_video, container, false)
    }

    lateinit var adapter: EditVideoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        load()
    }

    fun load(){
        ApiManager.getInstance().getApiAddress().loadVideoBean(UserInfoViewModel.getUserId()).enqueue(object : Callback<EditVideoBean>{
            override fun onResponse(call: Call<EditVideoBean>, response: Response<EditVideoBean>) {
                if (response.body() == null){
                    return
                }
                adapter = EditVideoAdapter(
                    context,
                    response.body()!!.data,
                    this@EditVideoFragment
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<EditVideoBean>, t: Throwable) {

            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(str : String){
        if (!"refreshVideo".equals(str)){
            return
        }
        load()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(data : VideoPrivateSettingActivity.VideoPrivateData){
        if (data.code == 200){
            adapter.update(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}