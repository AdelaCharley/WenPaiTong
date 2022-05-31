package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_last_lottery.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.LastLotteryAdapter
import com.qunshang.wenpaitong.equnshang.data.LastLotteryResultBean
import com.qunshang.wenpaitong.equnshang.data.SwipeDataBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.util.*

/*
已中奖未领取奖品的选择收获地址.还没有写
 */

class LastLotteryActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_last_lottery)
        isEnd = false
        toolbar_title.setText("往期抽奖")
        toolbar_right.setText("规则")
        toolbar_back.setOnClickListener { finish() }
        toolbar_right.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","活动规则")
            intent.putExtra("url",Constants.baseURL + "/rule/lotteryRule.html")
            startActivity(intent)
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        adapter = LastLotteryAdapter(
            this@LastLotteryActivity)
        list.adapter = adapter
        initView()
    }

    lateinit var adapter: LastLotteryAdapter

    fun initView(){
        ApiManager.getInstance().getApiVideoTest().loadImages("pastLottery").enqueue(object : Callback<SwipeDataBean>{
            override fun onResponse(call: Call<SwipeDataBean>, response: Response<SwipeDataBean>) {
                if (response.body() != null){
                    adapter.addHeader(response.body())
                    //initBanner(response.body()?.data!!)
                } else {
                    Log.i("zhangjunii","null")
                }
            }

            override fun onFailure(call: Call<SwipeDataBean>, t: Throwable) {

            }

        })

        loadLotterys()
    }

    override fun onResume() {
        super.onResume()
        if (isfirst){
            isfirst = false
            return
        } else {
            loadLotterys()
        }
    }

    var isfirst = true

    fun loadLotterys(){
        ApiManager.getInstance().getApiVideoTest().loadLastLotteryResult(UserInfoViewModel.getUserId()).enqueue(object : Callback<LastLotteryResultBean>{
            override fun onResponse(call: Call<LastLotteryResultBean>, response: Response<LastLotteryResultBean>) {
                if (response.body() == null){
                    return
                }
                val gson = Gson()
                StringUtils.log(gson.toJson(response.body()!!))
                if (response.body()!!.data.size == 0){
                    list.visibility = View.GONE
                    layout_nodata.visibility = View.VISIBLE
                    return
                }
                adapter.add(response.body()!!.data)
            }

            override fun onFailure(call: Call<LastLotteryResultBean>, t: Throwable) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        isEnd = true
    }

    fun initBanner(images : List<SwipeDataBean.DataBean>){
        /*Log.i("zhangjunii",images.toString())
        val adapter : BannerImageAdapter<SwipeDataBean.DataBean> = object : BannerImageAdapter<SwipeDataBean.DataBean>(images){
            override fun onBindView(holder: BannerImageHolder?, data: SwipeDataBean.DataBean?, position: Int, size: Int) {
                Glide.with(holder!!.itemView)
                    .load(data?.imageUrl)
                    //.apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }
        banner
            .setAdapter(adapter)
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))*/
    }

    companion object {
        var isEnd = false
    }

}