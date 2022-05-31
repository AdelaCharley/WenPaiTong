package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_week_prize_history.*
import kotlinx.android.synthetic.main.activity_week_prize_history.label_ask
import kotlinx.android.synthetic.main.activity_week_prize_history.label_rule
import kotlinx.android.synthetic.main.activity_week_prize_history.prize_image
import kotlinx.android.synthetic.main.activity_week_prize_history.prize_name
import kotlinx.android.synthetic.main.activity_week_prize_history.tags_one
import kotlinx.android.synthetic.main.activity_week_prize_history.tags_three
import kotlinx.android.synthetic.main.activity_week_prize_history.tags_two
import kotlinx.android.synthetic.main.activity_week_prize_history.toolbar_back
import kotlinx.android.synthetic.main.activity_week_prize_history.toolbar_title
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.WeekPrizeHistoryHelpAdapter
import com.qunshang.wenpaitong.equnshang.data.AddressBean
import com.qunshang.wenpaitong.equnshang.data.BindAddressBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WeekPrizeHistoryBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class WeekPrizeHistoryActivity : BaseActivity() {

    var taskUserRelationId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_prize_history)
        this.taskUserRelationId = intent.getStringExtra("taskUserRelationId")
        if (taskUserRelationId == null){
            return
        }
        Log.i("zhangjuniiifd",taskUserRelationId!!)
        if (StringUtils.isEmpty(taskUserRelationId)){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("我的任务礼品")
        init()
        label_ask.setOnClickListener { goToRuleActivity() }
        label_rule.setOnClickListener { goToRuleActivity() }
    }

    override fun onResume() {
        super.onResume()
        if (isfirst){
            isfirst = false
            return
        }
        init()
    }

    var isfirst = true

    fun init(){
        ApiManager.getInstance().getApiLotteryTest().loadFinishedTaskDetail(taskUserRelationId).enqueue(object : Callback<WeekPrizeHistoryBean>{
            override fun onResponse(call: Call<WeekPrizeHistoryBean>, response: Response<WeekPrizeHistoryBean>) {
                if (response.body() == null){
                    return
                }
                initView(response.body()!!.data)
            }

            override fun onFailure(call: Call<WeekPrizeHistoryBean>, t: Throwable) {

            }

        })
    }

    fun goToRuleActivity(){
        val intent = Intent(this,WeekPrizeHistoryRuleActivity::class.java)
        startActivity(intent)
    }

    private val TYPE_RECEIVE_TICKET = 569

    fun initView(bean : WeekPrizeHistoryBean.DataBean?){
        when(bean?.finishedTaskStatus){
            /*1	| [string]
                |
                已完成任务未领奖   //填写地址
            2	| [number]
                |
                已领奖待收货     //礼品派发中
            3	| [number]
                |
                已收货 填写心得
                        4	| [number]   //填写心得
                |
                填写完心得 查看心得*/   //查看心得
            1-> {
                receiveprize_now.setText("立即领取礼品")
                receiveprize_now.setOnClickListener {
                    val intent = Intent(this,ChooseAddressActivity::class.java)
                    startActivityForResult(intent,TYPE_RECEIVE_TICKET)
                }
            }

            2 -> {
                receiveprize_now.setText("礼品派发中")
                receiveprize_now.setOnClickListener {  }
            }

            3 -> {
                receiveprize_now.setText("填写心得")
                receiveprize_now.setOnClickListener {
                    val intent = Intent(this,PublishExperienceActivity::class.java)
                    //intent.putExtra("experienceid",bean.data.experienceId)
                    intent.putExtra("prizeId",bean?.prizeInfo?.prizeId)
                    intent.putExtra("type",1)
                    intent.putExtra("relationId",taskUserRelationId?.toLong())
                    intent.putExtra("imageUrl",bean?.prizeInfo?.imageUrl)
                    intent.putExtra("winTime",bean?.obtainInfo?.obtainTime)
                    startActivity(intent)
                }
            }

            4 -> {
                receiveprize_now.setText("查看心得")
                receiveprize_now.setOnClickListener {
                    val intent = Intent(this,ExperienceActivity::class.java)
                    intent.putExtra("experienceid",bean?.experienceId)
                    startActivity(intent)
                }
            }

        }
        list.adapter = WeekPrizeHistoryHelpAdapter(this,bean?.helpUserInfo)
        Glide.with(this).load(bean?.prizeInfo?.imageUrl).into(prize_image)
        prize_name.setText(bean?.prizeInfo?.name)
        if (bean?.prizeInfo?.tags != null){
            if (bean.prizeInfo!!.tags!!.size > 0){
                tags_one.setText(bean.prizeInfo.tags.get(0))
            }
            if (bean.prizeInfo.tags.size > 1){
                tags_two.setText(bean.prizeInfo.tags.get(1))
            }
            if (bean.prizeInfo.tags.size > 2){
                tags_three.setText(bean.prizeInfo.tags.get(2))
            }
        }

        if (bean?.finishedTaskStatus != 1){
            time.setText(bean?.obtainInfo?.obtainTime)
            contacter.setText(bean?.obtainInfo?.addressInfo?.name + " " + bean?.obtainInfo?.addressInfo?.phone)
            address.setText(bean?.obtainInfo?.addressInfo?.address)
            shareto.setOnClickListener {
                val intent = Intent(this,ReceivePrizeToFlauntActivity::class.java)
                startActivity(intent)
            }
        } else {
            layout_contactinfo.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_RECEIVE_TICKET){
            if (resultCode == AddressAdapter.RESULT_CODE){
                //val id = data?.getStringExtra("addressId")
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                ApiManager.getInstance().getApiLotteryTest().bindAddress(UserInfoViewModel.getUserId(),address.id!!,1,relationId = taskUserRelationId!!).enqueue(object : Callback<BindAddressBean>{
                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<BindAddressBean>,
                        response: Response<BindAddressBean>
                    ) {
                        if (response.body() == null){
                            return
                        }
                        val gson = Gson()
                        val jsonstr = gson.toJson(response.body())
                        Log.i("zjakgd",jsonstr)
                        if (response.body()!!.code == 200){
                            init()
                        } else {
                            DialogUtil.toast(this@WeekPrizeHistoryActivity,"领取失败," + response.body()!!.msg)
                        }
                    }

                    override fun onFailure(call: Call<BindAddressBean>, t: Throwable) {
                        //Log.i("zjakgd",t.message)
                    }

                })
            }
        }
    }

}