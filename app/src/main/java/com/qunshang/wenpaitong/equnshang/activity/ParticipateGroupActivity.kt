package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup

import kotlinx.android.synthetic.main.activity_participate_group.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.ParticipateGroupAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV4
import java.util.*

class ParticipateGroupActivity : BaseActivity (){

    private var orderGroupSn : String? = null

    lateinit var dataBean : PinTuanDetailBean

    lateinit var productBean : ProductBeanV2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participate_group)
        toolbar_title.setText("拼好货")
        //toolbar_back.setOnClickListener { finish() }
        if (Constants.isOpened){
            toolbar_back.setOnClickListener {
                finish()
            }
        } else {
            toolbar_back.setOnClickListener {
                val intent = Intent(this,SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        orderGroupSn = intent.getStringExtra("orderGroupSn")
        loadData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        loadData()
    }

    fun showSkuDialog(type : String,orderGroupSn : String ?= null){
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(ProductsDialogV4(this,type,productBean,orderGroupSn,true))
            .show()
    }

    fun loadProductInfo(productId : Int){
        ApiManager.getInstance().getApiAMallV3().loadProductDetail(UserInfoViewModel.getUserId(),productId.toString()).enqueue(object : Callback<ProductBeanV2> {
            override fun onResponse(call: Call<ProductBeanV2>, response: Response<ProductBeanV2>) {
                if (response.body() != null){
                    layout_root.visibility = View.VISIBLE
                    val bean = response.body()
                    if (bean?.code == 200){
                        this@ParticipateGroupActivity.productBean = response.body()!!

                        Glide.with(applicationContext).load(dataBean.product.skuPic).into(img_goods)
                        tv_goods_name.setText(dataBean.product.productName)
                        tv_goods_spec.setText("规格:" + dataBean.product.skuInfo)
                        tv_goods_price.setText(dataBean.product.productPrice.toString())
                        mastersku.setText("拼主所选规格为" + dataBean.product.skuInfo)
                        remaincount.setText(dataBean.groupInfo.groupSuccessLeftPeople.toString())
                        val timeArr = TimeUtil.getTimeRemainByDayString(dataBean.groupInfo.expireTime)

                        //已拼团

                        if (dataBean.groupInfo.userInfo.size >= 2){
                            layout_remain.visibility = View.GONE
                        }

                        if ((dataBean.userGroupInfo.isOwner == 0) or (dataBean.groupInfo.isNBackOne == 0)){
                            label_rule.visibility = View.GONE
                            image_step.visibility = View.GONE
                        }

                        if ((dataBean.userGroupInfo.isOwner == 0) and (dataBean.userGroupInfo.joinGroup == 1)){//如果不是团长，但是已经拼过团
                            layout_success.visibility = View.VISIBLE
                            layout.visibility = View.GONE
                            mastersku.visibility = View.GONE
                            layout_remain.visibility = View.GONE
                            label_rule.visibility = View.GONE
                            image_step.visibility = View.GONE
                            doparti.setText("回到首页")
                            doparti.setOnClickListener {
                                val intent = Intent(this@ParticipateGroupActivity,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        //拼团结束了
                        else if (("00:00:00".equals(timeArr)) or
                            (dataBean.groupInfo.status == 40) or
                            ((dataBean.groupInfo.isNBackOne == 1) and (dataBean.groupInfo.status == 30)) or
                            ((dataBean.groupInfo.isNBackOne == 0) and (dataBean.groupInfo.status == 20))
                        ){//加一个判断，如果团已经满了，比如2人团已经有两个人了，4人团已经有四个人二楼，
                            //status为40也是结束   ,isNBackOne=1并且 status是30也是拼团已结束            isNBackOne是0并且status 为20也是偏团结束了
                            doparti.setText("拼团已结束")
                            doparti.background = getDrawable(R.drawable.bg_amallv3_participate_group_end)
                            doparti.isClickable = false
                            layout_remain.visibility = View.GONE
                        }

                        //拼团进行中
                        else {

                            if (dataBean.userGroupInfo.isOwner == 1){
                                doparti.setText("去邀请")
                                doparti.setOnClickListener {
                                    KTUtil.doShare(this@ParticipateGroupActivity,dataBean.product.skuPic,dataBean.groupInfo.orderGroupSn,
                                        dataBean.product.productPrice,dataBean.product.productName,dataBean.product.skuInfo)
                                }
                            } else {
                                doparti.setText("参与" + dataBean.groupInfo.userInfo.get(0).name + "的拼团")
                                layout.setOnClickListener {
                                    val intent = Intent(this@ParticipateGroupActivity, AMallV3ProductDetailActivity::class.java)
                                    intent.putExtra("productId",dataBean.product.productId)

                                    intent.putExtra("orderGroupSn",orderGroupSn)
                                    intent.putExtra("expiredTime",dataBean.groupInfo.expireTime)
                                    intent.putExtra("userMaster",dataBean.groupInfo.userInfo.get(0).headImage)

                                    startActivity(intent)
                                }
                                doparti.setOnClickListener {
                                    showSkuDialog(ProductsDialogV4.vipGroupPrice,orderGroupSn = dataBean.groupInfo.orderGroupSn)
                                }
                            }

                            val task = object : TimerTask(){
                                override fun run() {
                                    runOnUiThread {
                                        val timeArr = TimeUtil.getTimeRemainByDayString(dataBean.groupInfo.expireTime)
                                        remaintime.setText(timeArr)
                                        if (("00:00:00").equals(timeArr)){
                                            try {
                                                cancel()
                                            } catch (e : Exception){
                                                e.printStackTrace()
                                            }
                                            loadData()
                                        }
                                    }
                                }
                            }
                            val oneSecond: Long = 1000
                            Timer().schedule(task, 0, oneSecond)

                        }
                        list.adapter = ParticipateGroupAdapter(this@ParticipateGroupActivity,dataBean.groupInfo.userInfo)
                    }
                    else {
                        DialogUtil.toast(this@ParticipateGroupActivity,bean?.msg)
                    }
                }
            }

            override fun onFailure(call: Call<ProductBeanV2>, t: Throwable) {
            }

        })
    }

    fun loadData(){
        ApiManager.getInstance()
            .getApiAMallV3()
            .loadGroupDetail(UserInfoViewModel.getUserId(), orderGroupSn)
            .enqueue(object : Callback<BaseHttpBean<PinTuanDetailBean>> {
                override fun onResponse(
                    call: Call<BaseHttpBean<PinTuanDetailBean>>,
                    response: Response<BaseHttpBean<PinTuanDetailBean>>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        this@ParticipateGroupActivity.dataBean = response.body()!!.data!!
                        loadProductInfo(dataBean.product.productId)
                    }
                }
                override fun onFailure(call: Call<BaseHttpBean<PinTuanDetailBean>>, t: Throwable) {
                }

            })
    }

}