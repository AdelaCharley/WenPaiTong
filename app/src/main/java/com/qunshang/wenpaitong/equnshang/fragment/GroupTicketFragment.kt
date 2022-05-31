package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_group_ticket.*
import kotlinx.android.synthetic.main.fragment_group_ticket.groupticketrule
import kotlinx.android.synthetic.main.fragment_group_ticket.label_groupticket_count
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.activity.GroupTicketRuleActivity
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class GroupTicketFragment : MyBaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        if (UserHelper.isLogin(requireContext())){
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2){
                label_groupticket_count.setText("10000工分兑换3群票")
            }
            ApiManager.getInstance().getApiPersonalTest().loadGroupTicketWorkPoint(UserInfoViewModel.getUserId()).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>{
                override fun onResponse(call: Call<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>, response: Response<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>) {
                    if (response.body() != null){
                        initView(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>, t: Throwable) {

                }

            })
        }

        groupticketrule.setOnClickListener { startActivity(
            Intent(requireActivity(),
                GroupTicketRuleActivity::class.java)
        ) }
    }

    fun initView(bean : com.qunshang.wenpaitong.equnshang.data.QunCoinBean){
        myticket.setText(bean.data.numberInfo.userQunCoin.toString())
        totalticketcount.setText(bean.data.numberInfo.currentNumber.toString())
        count_yesterday.setText(bean.data.numberInfo.yesterdayIncreaseNumber.toString())
        count_lastweek.setText(bean.data.numberInfo.lastWeekIncreaseNumber.toString())
        count_lastmonth.setText(bean.data.numberInfo.lastMonthIncreaseNumber.toString())
    }

    fun refresh(){
        init()
    }

}