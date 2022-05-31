package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_week_activitys.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.WeekInviteListAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import java.lang.Exception

class WeekActivitysActivity : Fragment (){

    var isStatusLoaded = false

    var isInviteListLoaded = false

    var isImagesLoaded = false

    var isWeekPrizeLoaded = false

    lateinit var banner: Banner<SwipeDataBean.DataBean, BannerImageAdapter<SwipeDataBean.DataBean>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_week_activitys)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_back.setOnClickListener { requireActivity().finish() }
        toolbar_title.setText("每周任务")
        banner = view.findViewById(R.id.banner)
        clienterbutton.setOnClickListener {
            DialogUtil.showCustomDialog(requireContext(),"task")
        }
        init()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_week_activitys, container, false)
    }

    fun show (){
        if (isStatusLoaded && isImagesLoaded && isWeekPrizeLoaded){
            rootview.visibility = View.VISIBLE
        }
    }

    fun init(){
        ApiManager.getInstance().getApiLotteryTest().getWeekStatus(UserInfoViewModel.getUserId()).enqueue(object : Callback<WeekVideoStatus>{
            override fun onResponse(call: Call<WeekVideoStatus>, response: Response<WeekVideoStatus>) {
                try {
                    if (response.body() == null){
                        DialogUtil.toast(requireContext(),"出错了")
                        return
                    }
                    isStatusLoaded = true
                    val bean = response.body()?.data!!
                    val gson = Gson()
                    val jsonstr = gson.toJson(response.body())
                    if (bean.status == 3){ //未看视频
                        invitelist.visibility = View.GONE
                        sharetofriendhelp.visibility = View.GONE
                        label_invite.visibility = View.GONE
                    } else if (bean.status == 1){//已完成任务
                        label_invite.visibility = View.GONE
                        label_weilingqu.visibility = View.GONE
                        label_back.visibility = View.GONE
                        loadInviteList()
                        sharetofriendhelp.visibility = View.GONE
                        finishedtask.visibility = View.VISIBLE
                    } else if (bean.status == 2){ //未领取任务
                        label_invite.visibility = View.GONE
                        sharetofriendhelp.visibility = View.GONE
                        //lolaebadInviteList()
                    } else {
                        label_weilingqu.visibility = View.GONE
                        label_back.visibility = View.GONE
                        loadInviteList()
                    }
                    show()
                } catch (e : Exception){
                    e.printStackTrace()
                }
                /*if (bean.status == 2){//未领取任务
                    isReceived = false
                    if (this@WeekLotteryVideoFragment::receive.isInitialized){
                        receive.visibility = View.VISIBLE
                    }
                    showReceiveTicketDialog()
                }
                ToastUtil.toast(context,"我获取了" + bean.status.toString())*/
            }

            override fun onFailure(call: Call<WeekVideoStatus>, t: Throwable) {

            }

        })
        label_back.setOnClickListener { requireActivity().finish() }
        loadImages()
        loadWeekPrize()
        label_ask.setOnClickListener { goToRuleActivity() }
        clientrule.setOnClickListener { goToRuleActivity() }
        lasthistory.setOnClickListener {
            val intent = Intent(requireContext(),WeekHistoryActivity::class.java)
            startActivity(intent)
        }
        sharetofriendhelp.setOnClickListener {
            val intent = Intent(requireContext(), WeekShareQRCodeActivity::class.java)
            startActivity(intent)
        }
    }

    fun goToRuleActivity(){
        val intent = Intent(requireContext(),WeekTaskRuleActivity::class.java)
        startActivity(intent)
    }

    fun loadWeekPrize(){
        ApiManager.getInstance().getApiLotteryTest().loadWeekPrize(UserInfoViewModel.getUserId()).enqueue(object : Callback<WeekPrizeBean>{
            override fun onResponse(call: Call<WeekPrizeBean>, response: Response<WeekPrizeBean>) {
                try {
                    if (response.body() == null){
                        return
                    }
                    isWeekPrizeLoaded = true
                    try {
                        prize_layout.setOnClickListener {
                            val intent = Intent(requireContext(), PrizeInfoDetailActivity::class.java)
                            intent.putExtra("prizeId", response.body()!!.data.prizeInfo.prizeId)
                            startActivity(intent)
                        }
                    } catch ( e : Exception){
                        e.printStackTrace()
                    }
                    this@WeekActivitysActivity.relationId = response.body()!!.data.taskUserRelationId
                    Glide.with(this@WeekActivitysActivity).load(response.body()!!.data.prizeInfo.imageUrl).into(prize_image)
                    prize_name.setText(response.body()!!.data.prizeInfo.name)
                    limitcount.setText("限量" + response.body()!!.data.prizeInfo.stock + "份")
                    progress.max = response.body()!!.data.prizeInfo.stock
                    progress.progress = response.body()!!.data.prizeInfo.used
                    receivecount.setText(response.body()!!.data.prizeInfo.used.toString() + "人已领取")
                    if (response.body()!!.data.prizeInfo.tags.size > 0){
                        tags_one.setText(response.body()!!.data.prizeInfo.tags.get(0))
                    }
                    if (response.body()!!.data.prizeInfo.tags.size > 1){
                        tags_two.setText(response.body()!!.data.prizeInfo.tags.get(1))
                    }
                    if (response.body()!!.data.prizeInfo.tags.size > 2){
                        tags_three.setText(response.body()!!.data.prizeInfo.tags.get(2))
                    }
                    if (response.body()!!.data.status == 1){//已经完成了任务，但是还没有领取
                        limitcount.visibility = View.INVISIBLE
                        progress.visibility = View.GONE
                        icon_gift.visibility = View.GONE
                        receivecount.visibility = View.GONE
                        receiveprize_now.visibility = View.VISIBLE
                        receiveprize_now.setOnClickListener {
                            val intent = Intent(requireContext(),ChooseAddressActivity::class.java)
                            startActivityForResult(intent,TYPE_RECEIVE_TICKET)
                        }
                    }
                    show()
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<WeekPrizeBean>, t: Throwable) {

            }

        })
    }

    private val TYPE_RECEIVE_TICKET = 569

    var relationId = "-99999"

    fun loadImages(){
        ApiManager.getInstance().getApiVideoTest().loadWeekImages().enqueue(object :
            Callback<SwipeDataBean> {
            override fun onResponse(call: Call<SwipeDataBean>, response: Response<SwipeDataBean>) {
                try {
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
                        .addBannerLifecycleObserver(this@WeekActivitysActivity)
                        .setIndicator(CircleIndicator(requireContext()))
                    show()
                } catch (e : Exception){
                    e.printStackTrace()
                }
                if (response.body() == null){
                    DialogUtil.toast(requireContext(),"出错了，请稍候再试")
                    return
                }
            }

            override fun onFailure(call: Call<SwipeDataBean>, t: Throwable) {

            }

        })
    }

    fun loadInviteList(){
        val manager = GridLayoutManager(requireContext(),5)
        invitelist.layoutManager = manager
        ApiManager.getInstance().getApiVideoTest().loadWeekInviteList(UserInfoViewModel.getUserId()).enqueue(object : Callback<WeekInviteListDataBean>{
            override fun onResponse(call: Call<WeekInviteListDataBean>, response: Response<WeekInviteListDataBean>) {
                try {
                    if (response.body() == null){
                        return
                    }

                    invitelist.adapter = WeekInviteListAdapter(
                        requireContext(),
                        response.body()?.data?.invitingList
                    )
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<WeekInviteListDataBean>, t: Throwable) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_RECEIVE_TICKET){
            if (resultCode == AddressAdapter.RESULT_CODE){
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean

                ApiManager.getInstance().getApiLotteryTest().bindAddress(UserInfoViewModel.getUserId(),address.id!!,1,relationId = relationId).enqueue(object : Callback<BindAddressBean>{
                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<BindAddressBean>,
                        response: Response<BindAddressBean>
                    ) {
                        try {
                            if (response.body() == null){
                                return
                            }
                            if (response.body()!!.code == 200){
                                limitcount.visibility = View.VISIBLE
                                progress.visibility = View.VISIBLE
                                icon_gift.visibility = View.VISIBLE
                                receivecount.visibility = View.VISIBLE
                                receiveprize_now.visibility = View.GONE
                                init()
                            } else {
                                DialogUtil.toast(requireContext(),"领取失败," + response.body()!!.msg)
                            }
                        } catch (e : Exception){
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<BindAddressBean>, t: Throwable) {

                    }

                })
            }
        }
    }

}