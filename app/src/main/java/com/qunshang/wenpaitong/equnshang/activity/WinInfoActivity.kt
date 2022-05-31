package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_win_info.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.data.AddressBean
import com.qunshang.wenpaitong.equnshang.data.BindAddressBean
import com.qunshang.wenpaitong.equnshang.data.PrizeInfoBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class WinInfoActivity : BaseActivity() {

    var activityId = -9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win_info)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("中奖信息")
        activityId = intent.getIntExtra("activityId",-9999)
        if (activityId == -9999){
            DialogUtil.toast(this,"出错了")
            return
        }
        init()
    }

    override fun onResume() {
        super.onResume()
        if (isfirst){
            isfirst = false
            return
        } else {
            init()
        }
    }

    var isfirst = true

    fun init(){
        ApiManager.getInstance().getApiVideoTest().loadPrizeInfo(UserInfoViewModel.getUserId(),activityId.toString()).enqueue(object : Callback<PrizeInfoBean>{
            override fun onResponse(call: Call<PrizeInfoBean>, response: Response<PrizeInfoBean>) {
                if (response.body() != null){
                    root.visibility = View.VISIBLE
                    this@WinInfoActivity.relationId = response.body()!!.data.numberInfo.activityNumberRelationId
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<PrizeInfoBean>, t: Throwable) {

            }

        })
    }

    fun initView(bean : PrizeInfoBean){
        Log.i(Constants.logtag,"WinInfostatus是" + bean.data.prizeInfo.prizeStatus)
        prize_number.setText(bean.data.numberInfo.number)
        val split = bean.data.numberInfo.winTime.split(" ")
        if (split.size == 2){
            time_day.setText(split[0])
            time_time.setText(split[1])
        }
        Glide.with(this).load(bean.data.prizeInfo.prizeImageUrl).into(prizeproducturl)
        productname.setText(bean.data.prizeInfo.prizeName)
        time.setText(bean.data.obtainInfo.obtainTime)

        flaunt.setOnClickListener {
            val intent = Intent(this,ReceivePrizeToFlauntActivity::class.java)
            startActivity(intent)
        }
        if (bean.data.prizeInfo.prizeStatus == 0){
            hideAddressLayout()
            experience.setText("立即兑奖")
        }
        if (bean.data.prizeInfo.prizeStatus == 1){
            experience.setText("礼品派发中")
            experience.isClickable = false
            showAddressLayout()

            contacter.setText(bean.data.obtainInfo.addressInfo.name + " " + bean.data.obtainInfo.addressInfo.phone)
            address.setText(bean.data.obtainInfo.addressInfo.address)

        } else if (bean.data.prizeInfo.prizeStatus == 2){
            experience.setText("发布心得拿工分")
            showAddressLayout()

            contacter.setText(bean.data.obtainInfo.addressInfo.name + " " + bean.data.obtainInfo.addressInfo.phone)
            address.setText(bean.data.obtainInfo.addressInfo.address)

        } else if (bean.data.prizeInfo.prizeStatus == 3){
            showAddressLayout()
            experience.setText("查看心得")

            contacter.setText(bean.data.obtainInfo.addressInfo.name + " " + bean.data.obtainInfo.addressInfo.phone)
            address.setText(bean.data.obtainInfo.addressInfo.address)

        }
        experience.setOnClickListener {
            if (bean.data.prizeInfo.prizeStatus == 0){
                val intent = Intent(this,ChooseAddressActivity::class.java)
                startActivityForResult(intent,TYPE_RECEIVE_TICKET)
                experience.isClickable = false
            }
            if (bean.data.prizeInfo.prizeStatus == 1){
                return@setOnClickListener
            }
            if (bean.data.prizeInfo.prizeStatus == 2){
                val intent = Intent(this,PublishExperienceActivity::class.java)
                //intent.putExtra("experienceid",bean.data.experienceId)
                intent.putExtra("prizeId",bean.data.prizeInfo.prizeId)
                intent.putExtra("type",0)
                intent.putExtra("relationId",bean.data.numberInfo.activityNumberRelationId)
                intent.putExtra("imageUrl",bean.data.prizeInfo.prizeImageUrl)
                intent.putExtra("winTime",bean.data.numberInfo.winTime)
                startActivity(intent)
                return@setOnClickListener
            }
            if (bean.data.prizeInfo.prizeStatus == 3){
                val intent = Intent(this,ExperienceActivity::class.java)
                intent.putExtra("experienceid",bean.data.experienceId)
                startActivity(intent)
            }
        }
    }

    fun hideAddressLayout(){
        label4.visibility = View.INVISIBLE
        label5.visibility = View.GONE
        label6.visibility = View.GONE
        time.visibility = View.GONE
        label7.visibility = View.GONE
        contacter.visibility = View.GONE
        address.visibility = View.GONE
    }

    fun showAddressLayout(){
        label4.visibility = View.VISIBLE
        label5.visibility = View.VISIBLE
        label6.visibility = View.VISIBLE
        time.visibility = View.VISIBLE
        label7.visibility = View.VISIBLE
        contacter.visibility = View.VISIBLE
        address.visibility = View.VISIBLE
    }

    var relationId : Long = -9999

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_RECEIVE_TICKET){
            if (resultCode == AddressAdapter.RESULT_CODE){
                //val id = data?.getStringExtra("addressId")
                val address = data?.getSerializableExtra("address") as AddressBean.DataBean
                ApiManager.getInstance().getApiLotteryTest().bindAddress(UserInfoViewModel.getUserId(),address.id!!,0,relationId = relationId.toString()).enqueue(object : Callback<BindAddressBean>{
                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<BindAddressBean>,
                        response: Response<BindAddressBean>
                    ) {
                        experience.isClickable = true
                        if (response.body() == null){
                            return
                        }
                        if (response.body()!!.code == 200){
                            DialogUtil.toast(this@WinInfoActivity,"领取成功")
                            /*val intent = Intent(this@WinInfoActivity, WinInfoActivity::class.java)
                            intent.putExtra("activityId", this@WinInfoActivity.activityId)
                            startActivity(intent)*/
                            init()
                        } else {
                            DialogUtil.toast(this@WinInfoActivity,"领取失败," + response.body()!!.msg)
                        }
                    }

                    override fun onFailure(call: Call<BindAddressBean>, t: Throwable) {

                    }

                })
            }
        }
    }

    private val TYPE_RECEIVE_TICKET = 569

}