package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_daily_lottery_ticket.*
import kotlinx.android.synthetic.main.activity_daily_lottery_ticket.clientrule
import kotlinx.android.synthetic.main.activity_daily_lottery_ticket.invitelist
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_title
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.InviteListAapter
import com.qunshang.wenpaitong.equnshang.adapter.ToDayLotteryActivityAdapter
import com.qunshang.wenpaitong.equnshang.adapter.TodayTicketAdapter
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import java.lang.Exception

class DailyLotteryTicketActivity : Fragment() {

    lateinit var banner: Banner<SwipeDataBean.DataBean, BannerImageAdapter<SwipeDataBean.DataBean>>

    var isImagesLoaded = false

    var isTicketsLoaded = false

    var isInviteListLoaded = false

    var isLotteryActivitysLoaded = false

    companion object{
        var count = 0
    }

    fun show(){
        if (isImagesLoaded && isTicketsLoaded && isInviteListLoaded && isLotteryActivitysLoaded){
            root.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_daily_lottery_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (TimeUtil.getDayOfWeek() == 4){
            return
        }
        toolbar_title.setText("今日抽奖")
        toolbar_right.setText("规则")
        toolbar_right.setOnClickListener {
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("title","活动规则")
            intent.putExtra("url","https://api.equnshang.cn/rule/lotteryRule.html")
            startActivity(intent)
        }
        banner = view.findViewById(R.id.banner)
        backtowatchvidfeo.setOnClickListener {
            requireActivity().finish()
        }
        text_share.setOnClickListener {
            val intent = Intent(requireContext(), ToDayLotteryShareActivity::class.java)
            startActivity(intent)
        }
        clienterbutton.setOnClickListener {
            DialogUtil.showCustomDialog(requireContext(),"task")
        }
        clientrule.setOnClickListener {
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("title","活动规则")
            intent.putExtra("url","https://api.equnshang.cn/rule/lotteryRule.html")
            startActivity(intent)
        }
        lasthistory.setOnClickListener {
            val intent = Intent(requireContext(),LastLotteryActivity::class.java)
            startActivity(intent)
        }
        /*loadImages()
        loadTickets()
        loadInviteList()
        loadLotteryActivity()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::adapter.isInitialized){
            adapter.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (TimeUtil.getDayOfWeek() == 4){
            return
        }
        if (isDataLoaded){
            return
        }
        loadImages()
        loadTickets()
        loadInviteList()
        loadLotteryActivity()
        isDataLoaded = true
    }

    var isDataLoaded = false

    fun loadLotteryActivity(){
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        activitys.layoutManager = manager
        ApiManager.getInstance().getApiLotteryTest().loadTodayLotteryBean(UserInfoViewModel.getUserId()).enqueue(object : Callback<ToDayLotteryActivityBean>{
            override fun onResponse(call: Call<ToDayLotteryActivityBean>, response: Response<ToDayLotteryActivityBean>) {
                try {
                    if (response.body() == null){
                        return
                    }
                    isLotteryActivitysLoaded = true
                    adapter = ToDayLotteryActivityAdapter(
                        this@DailyLotteryTicketActivity,
                        response.body()?.data)
                    activitys.adapter = adapter
                    show()
                } catch (e : Exception){

                }
            }

            override fun onFailure(call: Call<ToDayLotteryActivityBean>, t: Throwable) {

            }

        })
    }

    lateinit var adapter : ToDayLotteryActivityAdapter

    fun loadImages(){
        ApiManager.getInstance().getApiVideoTest().loadImages("todayLottery").enqueue(object : Callback<SwipeDataBean>{
            override fun onResponse(call: Call<SwipeDataBean>, response: Response<SwipeDataBean>) {
                try {
                    if (response.body() == null){
                        DialogUtil.toast(requireContext(),"出错了，请稍候再试")
                    }
                    isImagesLoaded = true
                    val bean = response.body()!!
                    val adapter : BannerImageAdapter<SwipeDataBean.DataBean> = object : BannerImageAdapter<SwipeDataBean.DataBean>(bean.data){
                        override fun onBindView(holder: BannerImageHolder?, data: SwipeDataBean.DataBean?, position: Int, size: Int) {
                            Glide.with(holder!!.itemView)
                                .load(data?.imageUrl)
                                //.apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                                .into(holder.imageView)
                        }
                    }
                    banner
                        .setAdapter(adapter)
                        .addBannerLifecycleObserver(this@DailyLotteryTicketActivity)
                        .setIndicator(CircleIndicator(requireContext()))
                    show()
                } catch (e : Exception){

                }
            }

            override fun onFailure(call: Call<SwipeDataBean>, t: Throwable) {

            }
        })
    }

    //获取可用的奖券数量
    fun getTicketsNumberAble(list : List<TodayTicketBean.DataBean>?){
        count = 0
        for (i in list!!.indices){
            var bean = list.get(i)
            if (bean.status == 0){
                count ++
            }
            //ToastUtil.toast(this,"奖券可用" + count)
        }
    }

    fun loadTickets(){

        val manager = GridLayoutManager(requireContext(),2)
        tickets.layoutManager = manager
        ApiManager.getInstance().getApiLotteryTest().loadTodayTickets(UserInfoViewModel.getUserId()).enqueue(object : Callback<TodayTicketBean>{
            override fun onResponse(call: Call<TodayTicketBean>, response: Response<TodayTicketBean>) {
                try {
                    if (response.body() == null){
                        return
                    }
                    isTicketsLoaded = true
                    if (response.body()!!.data.size != 0){
                        getTicketsNumberAble(response.body()?.data)
                        layout_no_ticket.visibility = View.GONE
                        tickets.adapter = TodayTicketAdapter(
                            requireContext(),
                            response.body()?.data
                        )
                    }
                    show()
                } catch (e : Exception){

                }
            }

            override fun onFailure(call: Call<TodayTicketBean>, t: Throwable) {

            }


        })

    }

    fun loadInviteList(){
        val manager = GridLayoutManager(requireContext(),5)
        invitelist.layoutManager = manager

        ApiManager.getInstance().getApiLotteryTest().loadInviteList(UserInfoViewModel.getUserId()).enqueue(object : Callback<ToDayInviteListBean>{
            override fun onResponse(call: Call<ToDayInviteListBean>, response: Response<ToDayInviteListBean>) {
                if (response.body() == null) {
                    return
                }
                try {
                    isInviteListLoaded = true
                    invitelist.adapter = InviteListAapter(
                        requireContext(),
                        response.body()!!.data
                    )
                    show()
                } catch (e : Exception){

                }

            }

            override fun onFailure(call: Call<ToDayInviteListBean>, t: Throwable) {

            }
        })
    }

}