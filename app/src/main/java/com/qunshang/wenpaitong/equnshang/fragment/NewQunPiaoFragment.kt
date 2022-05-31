package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_new_qun_piao_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.WebViewActivity
import com.qunshang.wenpaitong.equnshang.data.QunCoinBeanV2
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class NewQunPiaoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_qun_piao_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (isDataLoaded){
            return
        }
        init()
        isDataLoaded = true
    }

    var isDataLoaded = false

    fun init(){
        if (UserHelper.isLogin(requireContext())){
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2){
                //labelgr.setText("10000工分兑换3群票")
            }
            ApiManager.getInstance().getApiPersonalTest().loadGroupTicketWorkPointV2(UserInfoViewModel.getUserId()).enqueue(object :
                Callback<QunCoinBeanV2> {
                override fun onResponse(call: Call<QunCoinBeanV2>, response: Response<QunCoinBeanV2>) {
                    if (response.body() != null){
                        initView(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<QunCoinBeanV2>, t: Throwable) {

                }

            })
        }

        /*groupticketrule.setOnClickListener { startActivity(
            Intent(requireActivity(),
                GroupTicketRuleActivity::class.java)
        ) }*/
    }

    fun initView(bean : QunCoinBeanV2){
        /*image_wenhao.setOnClickListener {
            if (layout1.visibility == View.VISIBLE){
                layout1.visibility = View.GONE
            } else {
                layout1.visibility = View.VISIBLE
            }
        }*/
        myticket?.setText("" + bean.data.userQunCoin.currentNumber)
        totalticketcount?.setText(bean.data.totalQunCoin)
        count_yesterday?.setText(bean.data.userQunCoin.yesterdayIncreaseNumber)
        count_lastweek?.setText(bean.data.userQunCoin.lastWeekIncreaseNumber)
        count_lastmonth?.setText(bean.data.userQunCoin.lastMonthIncreaseNumber.toString())
        rule.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("title","工分奖励及使用规则")
            intent.putExtra("url", Constants.baseURL + "/rule/ruleTree.html")
            startActivity(intent)
        }
    }

    fun refresh(){
        init()
    }


}