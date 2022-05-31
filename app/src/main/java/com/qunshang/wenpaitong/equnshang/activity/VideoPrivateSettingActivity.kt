package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_video_private_setting.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.EditVideoBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class VideoPrivateSettingActivity : BaseActivity() {

    lateinit var data : EditVideoBean.DataBean

    var pos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_video_private_setting)
        back.setOnClickListener { finish() }
        text_edit.setOnClickListener { finish() }
        data = intent.getSerializableExtra("data") as EditVideoBean.DataBean
        pos = intent.getIntExtra("position",-1)
        if ((data == null) or (!this::data.isInitialized)){
            return
        }
        if (pos == -1){
            return
        }
        initView()
    }

    fun initView(){
        Glide.with(this).load(data.video_poster_url).into(image)
        videoname.setText(data.video_desc)
        cancel.setOnClickListener {
            finish()
        }
        confirm.setOnClickListener {
            modify()
            /*if (("0".equals(data.is_private)) and !switch_btn.isChecked){//视频本事公开的，但是我要设置为私密
                modify()
            } else if ((!"0".equals(data.is_private)) and switch_btn.isChecked){//视频本是私密的，设置为公开
                modify()
            } else {
                DialogUtil.toast(this,"没有执行操作")
            }*/
        }
        if ("0".equals(data.is_private)){
            switch_btn.isChecked = true
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(def : String){

    }

    fun modify(){
        /*ApiManager.getInstance().getApiPersonalTest().alertPrivate(data.id).enqueue(object : Callback<BaseHttpBean<Int>>{
            override fun onResponse(
                call: Call<BaseHttpBean<Int>>,
                response: Response<BaseHttpBean<Int>>
            ) {
                if (response.body() == null){
                    return
                }
                DialogUtil.toast(this@VideoPrivateSettingActivity,response.body()!!.msg)

                val data = VideoPrivateData()
                data.code = 200
                data.position = pos
                EventBus.getDefault().post(data)
                finish()
            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

            }

        })*/
        var  isprivate = "0"
        if (switch_btn.isChecked){
            isprivate = "0"
        } else {
            isprivate = "1"
        }
        ApiManager.getInstance().getApiPersonalTest().editVideo(data.id,videoname.text.toString(),isprivate).enqueue(object : Callback<BaseHttpBean<Int>>{
            override fun onResponse(
                call: Call<BaseHttpBean<Int>>,
                response: Response<BaseHttpBean<Int>>
            ) {
                if (response.body() == null){
                    return
                }
                DialogUtil.toast(this@VideoPrivateSettingActivity,response.body()!!.msg)

                val data = VideoPrivateData()
                data.code = 200
                data.position = pos
                data.isprivate = isprivate
                data.desc = videoname.text.toString()
                EventBus.getDefault().post(data)
                finish()
            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    class VideoPrivateData{
        var position = -999
        var code = 200
        var isprivate = "0"
        var desc = ""
    }

}