package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.SearchAMallActivity
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_amall.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.FragmentAmallBinding

class AMallFragment : BaseVideoFragment() {

    var isHideTop = false

    lateinit var binding : FragmentAmallBinding
    override fun setLayoutId(): Int {
        return R.layout.fragment_amall
    }

    override fun init() {
        binding.searchLayoout.setOnClickListener {
            startActivity(Intent(context, SearchAMallActivity::class.java))
        }
        //initView()
        loadData()
        if (isHideTop){
            hideTop()
        }
    }

    override fun playCurVideo(position: Int) {

    }

    fun setHideTopView(boolean: Boolean){
        isHideTop = boolean
    }

    fun hideTop(){
        top.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAmallBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!isDataLoaded){
            init()
        }
    }

    fun loadData(){

        ApiManager.getInstance().getApiMallTest().loadAllCategory().enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.CategoryBean>{
            override fun onResponse(call : Call<com.qunshang.wenpaitong.equnshang.data.CategoryBean>, response: Response<com.qunshang.wenpaitong.equnshang.data.CategoryBean>) {
                if (response.body() == null){
                    return
                }
                initView(response.body()!!)
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.CategoryBean>, t: Throwable) {

            }
        })
    }

    fun initView(bean : com.qunshang.wenpaitong.equnshang.data.CategoryBean){
        val titles = ArrayList<String>()
        val fragments = ArrayList<AmallProductFragment>()
        for (i in bean.data.category.indices){
            titles.add(bean.data.category.get(i).categoryName)
            fragments.add(AmallProductFragment(bean.data.category.get(i).categoryId))
        }

        binding.viewpager.offscreenPageLimit = bean.data.category.size
        val adapter = BasePagerAdapter(childFragmentManager,fragments, titles)
        binding.viewpager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewpager)
        isDataLoaded = true
    }

}