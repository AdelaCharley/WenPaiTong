package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_main_page_group_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.ArticleAdapter
import com.qunshang.wenpaitong.equnshang.data.GroupMainHomePageInfoBean
import xyz.doikki.videocontroller.StandardVideoController

class MainPageGroupDetailFragment(val gocid : Int,val name : String) : MyBaseFragment() {

    lateinit var adapter : ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isHidden
        return inflater.inflate(R.layout.fragment_main_page_group_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = StandardVideoController(requireContext())
        controller.addDefaultControlComponent("", false)
        videoView.setVideoController(controller) //设置控制器
        ApiManager.getInstance().getApiGroupMain().getGroupMainPageInfo(gocid).enqueue(object : Callback<GroupMainHomePageInfoBean>{
            override fun onResponse(call: Call<GroupMainHomePageInfoBean>, response: Response<GroupMainHomePageInfoBean>) {
                if (response.body() == null){
                    return
                }
                videoView.setUrl(response.body()!!.data.video)
                groupmainname.setText(name)
                content.setText(response.body()!!.data.desc)

                adapter = ArticleAdapter(
                    requireContext(),
                    response.body()!!.data.articleList
                )
                list.adapter = adapter
            }

            override fun onFailure(call: Call<GroupMainHomePageInfoBean>, t: Throwable) {
            }

        })
    }

    fun pause(){
        videoView?.pause()
    }

    override fun onPause() {
        super.onPause()
        videoView?.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.release()
    }

    fun isOnBack() : Boolean{
        return videoView.onBackPressed()
    }

}