package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_a_mall_v3_my.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.OrderActivityV2
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.AMallV3OrderCountBean
import com.qunshang.wenpaitong.equnshang.data.AMallV3StarInfoBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class AMallV3MyFragment : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a_mall_v3_my, container, false)
    }

    fun goToOrderActivity(index : Int){
        val intent = Intent(requireContext(),OrderActivityV2::class.java)
        intent.putExtra("index",index)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (UserHelper.isLogin(requireContext())){
            if (UserInfoViewModel.getUserInfo() == null){
                return
            }
            Glide.with(requireContext()).load(UserInfoViewModel.getUserInfo()!!.headimage).into(userimage)
            username.setText(UserInfoViewModel.getUserInfo()!!.uname)

            var identity = ""

            if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                identity = "会员"
            } else {
                identity = "粉丝"
            }
            label_identity.setText(identity)


        }
        layout_mygroup.setOnClickListener {
            val intent = Intent(requireContext(),MyGroupActivity::class.java)
            startActivity(intent)
        }
        layout_star.setOnClickListener {
            val intent = Intent(requireContext(),MyStarActivityV2::class.java)
            startActivity(intent)
        }
        layout_history.setOnClickListener {
            val intent = Intent(requireContext(),BrowseHistoryActivityV2::class.java)
            startActivity(intent)
        }
        layout_discount_ticket.setOnClickListener {
            val intent = Intent(requireContext(),DiscountActivityV2::class.java)
            startActivity(intent)
        }
        layout_exchange.setOnClickListener {
            val intent = Intent(requireContext(),ExchangeListActivity::class.java)
            startActivity(intent)
        }
        allorders.setOnClickListener {
            val intent = Intent(requireContext(), OrderActivityV2::class.java)
            startActivity(intent)
        }
        layout_address.setOnClickListener {
            val intent = Intent(requireContext(),AddressActivityV2::class.java)
            intent.putExtra("isSelect",false)
            startActivity(intent)
        }
        gonow.setOnClickListener {
            val intent = Intent(requireContext(),ToBeVipOrPartnerActivity::class.java)
            startActivity(intent)
        }
        layout_waitforpay.setOnClickListener {
            goToOrderActivity(1)
        }
        layout_waitfordeliver.setOnClickListener {
            goToOrderActivity(3)
        }
        layout_waitforgroup.setOnClickListener {
            goToOrderActivity(2)
        }
        layout_waitforreceive.setOnClickListener {
            goToOrderActivity(4)
        }
        layout_alreadyover.setOnClickListener {
            goToOrderActivity(5)
        }
        layout_common.setOnClickListener {
            val intent = Intent(requireContext(),AMallV3CommonQuestionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        initOrderInfo()
        initStarInfo()
    }

    fun initOrderInfo(){
        ApiManager.getInstance().getApiAMallV3().loadAMallV3OrderCount(UserInfoViewModel.getUserId()).enqueue(object : Callback<AMallV3OrderCountBean>{
            override fun onResponse(
                call: Call<AMallV3OrderCountBean>,
                response: Response<AMallV3OrderCountBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    return
                }
                if ((response.body()!!.data.order.size == 4) and (response.body()!!.data.tool.size == 2)){

                    if (response.body()!!.data.order.get(0) != 0){
                        count_waitforpay.visibility = View.VISIBLE
                        count_waitforpay.setText(response.body()!!.data.order.get(0).toString())
                    } else {
                        count_waitforpay.visibility = View.GONE
                    }
                    if (response.body()!!.data.order.get(1) != 0){
                        count_waitforgroup?.visibility = View.VISIBLE
                        count_waitforgroup?.setText(response.body()!!.data.order.get(1).toString())
                    } else {
                        count_waitforgroup?.visibility = View.GONE
                    }
                    if (response.body()!!.data.order.get(2) != 0){
                        count_waitfordeliver.visibility = View.VISIBLE
                        count_waitfordeliver.setText(response.body()!!.data.order.get(2).toString())
                    } else {
                        count_waitfordeliver?.visibility = View.GONE
                    }
                    if (response.body()!!.data.order.get(3) != 0){
                        count_waitforget.visibility = View.VISIBLE
                        count_waitforget.setText(response.body()!!.data.order.get(3).toString())
                    } else {
                        count_waitforget?.visibility = View.GONE
                    }
                    if (response.body()!!.data.tool.get(0) != 0){
                        count_mygroup.visibility = View.VISIBLE
                        count_mygroup.setText(response.body()!!.data.tool.get(0).toString())
                    } else {
                        count_mygroup.visibility = View.GONE
                    }
                    if (response.body()!!.data.tool.get(1) != 0){
                        count_exchange.visibility = View.VISIBLE
                        count_exchange.setText(response.body()!!.data.tool.get(1).toString())
                    } else {
                        count_exchange.visibility = View.GONE
                    }

                }
            }

            override fun onFailure(call: Call<AMallV3OrderCountBean>, t: Throwable) {

            }

        })
    }

    fun initStarInfo(){
        ApiManager.getInstance().getApiAMallV3().loadMyStarInfoNumber(UserInfoViewModel.getUserId()).enqueue(object : Callback<AMallV3StarInfoBean>{
            override fun onResponse(
                call: Call<AMallV3StarInfoBean>,
                response: Response<AMallV3StarInfoBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    if (response.body()!!.data.size >= 3){
                        star_count.setText(response.body()!!.data.get(0).number.toString())
                        history_count.setText(response.body()!!.data.get(1).number.toString())
                        discount_count.setText(response.body()!!.data.get(2).number.toString())
                    }
                }
            }

            override fun onFailure(call: Call<AMallV3StarInfoBean>, t: Throwable) {

            }

        })

    }

}