package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_manage_user__partner.*
import kotlinx.android.synthetic.main.fragment_manage_user__partner.count_labela
import kotlinx.android.synthetic.main.fragment_manage_user__partner.count_labelb
import kotlinx.android.synthetic.main.fragment_manage_user__partner.lista
import kotlinx.android.synthetic.main.fragment_manage_user__partner.listb
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.ManagePartnerContentAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserPartnerBean
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ManageUser_PartnerFragment : MyBaseFragment() {

    var partnersAPageIndex = 1

    var partnersBPageIndex = 1

    var partnersAPageSize = 20

    var partnersBPageSize = 20

    var partnersCPageIndex = 1

    var partnersCPageSize = 20

    var ispartnerAAutoLoadMoreEnabled = true

    var ispartnerAloading = false

    private var lastpartnerAVisibleItemPosition = 0

    var ispartnerCAutoLoadMoreEnabled = true

    var ispartnerCloading = false

    private var lastpartnerCVisibleItemPosition = 0

    var ispartnerBAutoLoadMoreEnabled = true

    var ispartnerBloading = false

    private var lastpartnerBVisibleItemPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_user__partner, container, false)
    }
    
    fun loadPartnerA(){
        ApiManager.getInstance().getApiPersonalTest().loadPartnerData(userId =
            UserInfoViewModel.getUserId()
        ,pageIndex = partnersAPageIndex,pageSize = partnersAPageSize).enqueue(object :
            Callback<UserPartnerBean> {
            override fun onResponse(call: Call<UserPartnerBean>, response: Response<UserPartnerBean>) {
                if (response.body() == null){
                    return
                }
                ispartnerAloading = false
                partnerAAdapter.add(response.body()!!.data.levelA)
                this@ManageUser_PartnerFragment.partnersAPageIndex++
                if (response.body()!!.data.levelA.size < partnersAPageSize){
                    ispartnerAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labela.setText(response.body()!!.data.countListLevelA.toString())
            }

            override fun onFailure(call: Call<UserPartnerBean>, t: Throwable) {

            }

        })
    }

    lateinit var partnerAAdapter: ManagePartnerContentAdapter

    lateinit var partnerBAdapter: ManagePartnerContentAdapter

    lateinit var partnerCAdapter: ManagePartnerContentAdapter

    fun loadPartnerB(){
        ApiManager.getInstance().getApiPersonalTest().loadPartnerData(userId =
            UserInfoViewModel.getUserId()
            ,type = 1,pageIndex = partnersBPageIndex,pageSize = partnersBPageSize).enqueue(object :
            Callback<UserPartnerBean> {
            override fun onResponse(call: Call<UserPartnerBean>, response: Response<UserPartnerBean>) {
                if (response.body() == null){
                    return
                }
                ispartnerBloading = false
                partnerBAdapter.add(response.body()!!.data.levelB)
                this@ManageUser_PartnerFragment.partnersBPageIndex++
                if (response.body()!!.data.levelB.size < partnersBPageSize){
                    ispartnerBAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labelb.setText(response.body()!!.data.countListLevelB.toString())
            }

            override fun onFailure(call: Call<UserPartnerBean>, t: Throwable) {

            }

        })
    }
    
    fun loadPartnerC(){
        ApiManager.getInstance().getApiPersonalTest().loadPartnerData(userId =
        UserInfoViewModel.getUserId()
            ,type = 3,pageIndex = partnersCPageIndex,pageSize = partnersCPageSize).enqueue(object :
            Callback<UserPartnerBean> {
            override fun onResponse(call: Call<UserPartnerBean>, response: Response<UserPartnerBean>) {
                if (response.body() == null){
                    return
                }
                ispartnerCloading = false
                partnerCAdapter.add(response.body()!!.data.levelC)
                this@ManageUser_PartnerFragment.partnersCPageIndex++
                if (response.body()!!.data.levelC.size < partnersCPageSize){
                    ispartnerCAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }
                count_labelc.setText(response.body()!!.data.countListLevelC.toString())
            }

            override fun onFailure(call: Call<UserPartnerBean>, t: Throwable) {

            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val managerpartnerA = LinearLayoutManager(requireContext())
        managerpartnerA.orientation = LinearLayoutManager.VERTICAL
        lista.layoutManager = managerpartnerA
        lista.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!ispartnerAAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerpartnerA.childCount
                    var totalItemCount = managerpartnerA.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!ispartnerAloading && lastpartnerAVisibleItemPosition == totalItemCount - 1) {
                        ispartnerAloading = true
                        loadPartnerA()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastpartnerAVisibleItemPosition = managerpartnerA.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastpartnerAVisibleItemPosition)
            }
        })
        val managerpartnerB = LinearLayoutManager(requireContext())
        managerpartnerB.orientation = LinearLayoutManager.VERTICAL
        listb.layoutManager = managerpartnerB
        listb.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!ispartnerBAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerpartnerB.childCount
                    var totalItemCount = managerpartnerB.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!ispartnerBloading && lastpartnerBVisibleItemPosition == totalItemCount - 1) {
                        ispartnerBloading = true
                        loadPartnerB()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastpartnerBVisibleItemPosition = managerpartnerB.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastpartnerBVisibleItemPosition)
            }
        })
        val managerpartnerC = LinearLayoutManager(requireContext())
        managerpartnerC.orientation = LinearLayoutManager.VERTICAL
        listc.layoutManager = managerpartnerC
        listc.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!ispartnerCAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerpartnerC.childCount
                    var totalItemCount = managerpartnerC.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!ispartnerCloading && lastpartnerCVisibleItemPosition == totalItemCount - 1) {
                        ispartnerCloading = true
                        loadPartnerC()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastpartnerCVisibleItemPosition = managerpartnerC.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastpartnerCVisibleItemPosition)
            }
        })
        partnerAAdapter = ManagePartnerContentAdapter(requireContext())
        partnerBAdapter = ManagePartnerContentAdapter(requireContext())
        partnerCAdapter = ManagePartnerContentAdapter(requireContext())
        lista.adapter = partnerAAdapter
        listb.adapter = partnerBAdapter
        listc.adapter = partnerCAdapter
        loadPartnerA()
        loadPartnerB()
        loadPartnerC()
        ApiManager.getInstance().getApiPersonalTest().loadPartnerData(userId = UserInfoViewModel.getUserId(),type = -1).enqueue(object :
            Callback<UserPartnerBean> {
            override fun onResponse(call: Call<UserPartnerBean>, response: Response<UserPartnerBean>) {
                if (response.body() == null){
                    return
                }
                count_labela.setText(response.body()!!.data.countListLevelA.toString())
                count_labelb.setText(response.body()!!.data.countListLevelB.toString())
                count_labelc.setText(response.body()!!.data.countListLevelC.toString())
            }

            override fun onFailure(call: Call<UserPartnerBean>, t: Throwable) {

            }

        })
        if (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 3 ){
            layout_partner_add.visibility = View.VISIBLE
            ApiManager.getInstance().getApiPersonalTest().loadPartnerData(userId =
            UserInfoViewModel.getUserId()
                ,type = -1).enqueue(object :
                Callback<UserPartnerBean> {
                override fun onResponse(call: Call<UserPartnerBean>, response: Response<UserPartnerBean>) {
                    if (response.body() == null){
                        return
                    }
                    partner_add_count.setText(response.body()!!.data.more.toString())
                }

                override fun onFailure(call: Call<UserPartnerBean>, t: Throwable) {

                }

            })
        } else {
            layout_partner_add.visibility = View.GONE
        }

    }

}