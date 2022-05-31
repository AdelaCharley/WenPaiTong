package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_vip_day.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.PrizeNumberListAdapter
import com.qunshang.wenpaitong.equnshang.adapter.VIPDayPrizesAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.lang.Exception
import java.util.*

class VipDayActivity(val acitivyId: Int) : Fragment() {

    //var acitivyId = -8

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_vip_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //acitivyId = intent.getIntExtra("activityId",-8)
    }

    fun init(){
        if (acitivyId <= 0){
            DialogUtil.toast(requireContext(),"会员日出错")
            return
        }
        StringUtils.log("VIP大一aciti" + acitivyId)
        toolbar_back.setOnClickListener { requireActivity().finish() }
        toolbar_title.setText("会员日")
        initBaseView()
        //getPrizeBudget()
        getPrizes(acitivyId)
        loadPrizeNumbers(acitivyId = acitivyId)
        if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
            getMyTicketNumber(acitivyId)
            layout_bg_tip_viprights?.visibility = View.GONE
        } else {
            root.visibility = View.VISIBLE
            layout_bg_ticket_number?.visibility = View.GONE
            layout_bg_tip_viprights?.visibility = View.VISIBLE
        }
        history.setOnClickListener {
            val intent = Intent(requireContext(),VipDayHistoryActivity::class.java)
            startActivity(intent)
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_vip_day)

    }

    fun initBaseView(){
        rule.setOnClickListener {
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("title","会员日规则")
            intent.putExtra("url", "https://api.equnshang.cn/rule/vipActivityRule.html")
            startActivity(intent)
        }
        clienter.setOnClickListener {
            DialogUtil.showCustomDialog(requireContext(),"vip")
        }
    }

    fun loadPrizeNumbers(acitivyId: Int){
        //周四6点之后，到周日结束的时间，获取数据之后进行显示.
        if (((TimeUtil.getDayOfWeek() == 4) and (TimeUtil.getCurrentHour() >=18)) or (TimeUtil.getDayOfWeek() > 4)){
            ApiManager.getInstance().getApiLotteryTest().loadPrizeNumbers(activityId = acitivyId).enqueue(object : Callback<PrizeNumberListBean>{
                override fun onResponse(
                    call: Call<PrizeNumberListBean>,
                    response: Response<PrizeNumberListBean>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code != 200){
                        return
                    }
                    try {
                        initPrizeNumberView(response.body()!!)
                    } catch (e : Exception){
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<PrizeNumberListBean>, t: Throwable) {

                }

            })
        }

    }

    fun initPrizeNumberView(bean: PrizeNumberListBean){
        for (currentbean in bean.data){
            if (currentbean?.level == 1){
                if (currentbean?.numberList.size == 0){
                    return
                }
                layout_bg_winnings?.visibility = View.VISIBLE
                number_yidengjiang?.setText(currentbean?.numberList.get(0).number)
            } else if (currentbean?.level == 2){
                list_erdengjiang.adapter = PrizeNumberListAdapter(requireContext(),currentbean.numberList)
            } else if (currentbean?.level == 3){
                list_sandengjiang.adapter = PrizeNumberListAdapter(requireContext(),currentbean.numberList)
            }
        }
    }

    val timer = Timer()

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    fun getMyTicketNumber(acitivyId: Int) {
        ApiManager.getInstance().getApiLotteryTest()
            .loadMyPrizeNumber(acitivyId, UserInfoViewModel.getUserId())
            .enqueue(object : Callback<MyTicketNumberBean> {
                override fun onResponse(
                    call: Call<MyTicketNumberBean>,
                    response: Response<MyTicketNumberBean>
                ) {
                    try {
                        if (response.body() == null) {
                            DialogUtil.toast(requireContext(),"数据加载出错")
                            return
                        }
                        root?.visibility = View.VISIBLE
                        val gson = Gson()
                        val str = gson.toJson(response.body())
                        Log.i("zhangjuniiis", str)
                        layout_bg_ticket_number?.visibility = View.VISIBLE
                        myticketnumber?.setText(response.body()!!.data.number)
                        when (response.body()!!.data.status) {
                            0 -> {
                                label_wait_time?.visibility = View.VISIBLE
                                remain_time?.visibility = View.VISIBLE

                                label_ticket_status?.visibility = View.GONE
                                do_label_status?.visibility = View.GONE

                                val timertask = object : TimerTask() {
                                    override fun run() {
                                        requireActivity().runOnUiThread {
                                            try {
                                                val remaintime = TimeUtil.getRemainTimeString(response.body()!!.data.time)
                                                remain_time?.setText(remaintime)
                                                if ("00:00:00".equals(remaintime)) {
                                                    timer.cancel()
                                                    //getMyTicketNumber(acitivyId)//重新刷新
                                                    label_wait_time?.visibility = View.GONE
                                                    remain_time?.visibility = View.GONE

                                                    label_ticket_status?.visibility = View.VISIBLE
                                                    do_label_status?.visibility = View.VISIBLE
                                                    label_ticket_status?.setText("您离中奖只差一步!")
                                                    do_label_status?.setText("下周再接再厉!")
                                                    do_label_status?.setTextColor(resources.getColor(R.color.grey))
                                                }
                                            } catch (e : Exception){

                                            }
                                        }
                                    }
                                }
                                try {
                                    timer.schedule(timertask, 0, 1000)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            1 -> {
                                label_wait_time.visibility = View.GONE
                                remain_time.visibility = View.GONE

                                label_ticket_status.visibility = View.VISIBLE
                                do_label_status.visibility =
                                    View.VISIBLE  //android:textColor="#ffc729f1"
                                label_ticket_status.setText("恭喜您获得" + response.body()!!.data.name)
                                do_label_status.setText("立即领取")
                                do_label_status.setOnClickListener {
                                    relationId = response.body()!!.data.id
                                    receiveTicket()
                                }
                            }
                            2 -> {
                                label_wait_time.visibility = View.GONE
                                remain_time.visibility = View.GONE

                                label_ticket_status.visibility = View.VISIBLE
                                do_label_status.visibility = View.VISIBLE
                                label_ticket_status.setText("恭喜您获得" + response.body()!!.data.name)
                                do_label_status.setTextColor(resources.getColor(R.color.grey))
                                do_label_status.setText("立即领取")

                            }
                            3 -> {
                                layout_bg_ticket_number.visibility = View.GONE
                            }
                        }
                    } catch (e : Exception){

                    }

                }

                override fun onFailure(call: Call<MyTicketNumberBean>, t: Throwable) {
                    try {
                        root.visibility = View.VISIBLE
                    } catch (e : Exception){

                    }
                }

            })
    }

    var relationId = "-99999"

    private val TYPE_RECEIVE_TICKET = 569

    //领取奖券
    fun receiveTicket(){
        val intent = Intent(requireContext(),ChooseAddressActivity::class.java)
        startActivityForResult(intent,TYPE_RECEIVE_TICKET)
    }

    /*fun getPrizeBudget(){
        ApiManager.getInstance().getApiLotteryTest().getVIPDayActivityId().enqueue(object : Callback<BaseHttpBean<Int>>{
            override fun onResponse(
                call: Call<BaseHttpBean<Int>>,
                response: Response<BaseHttpBean<Int>>
            ) {
                if (response.body() == null){
                    return
                }

            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

            }

        })
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_RECEIVE_TICKET){
            if (resultCode == AddressAdapter.RESULT_CODE){
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                try {
                    ApiManager.getInstance().getApiLotteryTest().bindAddress(UserInfoViewModel.getUserId(),address.id!!,2,relationId = relationId).enqueue(object : Callback<BindAddressBean>{
                        @SuppressLint("ResourceAsColor")
                        override fun onResponse(
                            call: Call<BindAddressBean>,
                            response: Response<BindAddressBean>
                        ) {
                            if (response.body() == null){
                                return
                            }
                            if (response.body()!!.code == 200){
                                DialogUtil.toast(requireContext(),"领取成功")
                                do_label_status.setTextColor(R.color.grey)
                                do_label_status.isClickable = false
                            } else {
                                DialogUtil.toast(requireContext(),"领取失败," + response.body()!!.msg)
                            }
                        }

                        override fun onFailure(call: Call<BindAddressBean>, t: Throwable) {

                        }

                    })
                } catch (e : Exception){
                    e.printStackTrace()
                }

            }
        }
    }

    fun getPrizes(acitivyId : Int){
        if (TimeUtil.getDayOfWeek() < 4){
            label_prize_list.setText("奖品清单预告")
        }
        try {
            ApiManager.getInstance().getApiLotteryTest().getPrizes(acitivyId).enqueue(object : Callback<VIPDayPrizeBudget>{
                override fun onResponse(
                    call: Call<VIPDayPrizeBudget>,
                    response: Response<VIPDayPrizeBudget>
                ) {
                    if (response.body() == null){
                        return
                    }
                    try {
                        if (context != null){
                            prizebudget.adapter = VIPDayPrizesAdapter(requireContext(),response.body()!!.data.prizeList)
                        }
                    } catch (e : Exception){
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<VIPDayPrizeBudget>, t: Throwable) {

                }

            })
        } catch (e : Exception){

        }
    }

}