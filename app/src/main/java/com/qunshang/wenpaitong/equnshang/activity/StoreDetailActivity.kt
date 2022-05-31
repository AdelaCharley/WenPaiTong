package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel.getUserId
import com.qunshang.wenpaitong.equnshang.fragment.ManufactureProductFragment
import com.qunshang.wenpaitong.equnshang.fragment.ManufactureVideoFragment
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_store_detail.*
import kotlinx.android.synthetic.main.activity_store_detail.concern
import kotlinx.android.synthetic.main.activity_store_detail.image_storer
import kotlinx.android.synthetic.main.activity_store_detail.store_name
import kotlinx.android.synthetic.main.activity_store_detail.tabs
import kotlinx.android.synthetic.main.activity_store_detail.viewpager
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_back
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.StoreDataBean
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class StoreDetailActivity : BaseActivity() {

    var manfactureId = -999

    var type : String? = "amall"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("主页")
        this.manfactureId = intent.getIntExtra("manfactureId",-999)
        //type = intent.getStringExtra("type")
        type = Constants.shopflag
        if (StringUtils.isEmpty(type)){
            type = "amall"
        }
        if (manfactureId == -999){
            DialogUtil.toast(this,"出错了")
            return
        }
        Log.i(Constants.logtag,"" + manfactureId)
        initView()
    }

    fun initView(){
        ApiManager.getInstance().getApiPersonalTest().loadManufactureInfo(manfactureId.toString(),type!!,UserInfoViewModel.getUserId()).enqueue(
            object : Callback<StoreDataBean>{
                override fun onResponse(
                    call: Call<StoreDataBean>,
                    response: Response<StoreDataBean>
                ) {
                    if (response.body() == null){
                        return
                    }
                    Glide.with(this@StoreDetailActivity).load(response.body()!!.data.manufactureLogoUrl).into(image_storer)
                    store_name.setText(response.body()!!.data.manufactureName)

                    val titles = ArrayList<String>()
                    val fragments = ArrayList<Fragment>()
                    titles.add("视频")
                    titles.add("商品")
                    fragments.add(ManufactureVideoFragment(response.body()!!.data.videos))
                    fragments.add(ManufactureProductFragment(response.body()!!.data.products,type!!))
                    val adapter = BasePagerAdapter(supportFragmentManager,fragments,titles)
                    viewpager.adapter = adapter
                    tabs.setupWithViewPager(viewpager)

                    if (response.body()!!.data.isFocus != 0){
                        concern.setText("取消关注")
                    }
                }

                override fun onFailure(call: Call<StoreDataBean>, t: Throwable) {

                }

            }
        )
        merchants.setOnClickListener {
            val intent = Intent(this,MerchantQualificationsActivity::class.java)
            intent.putExtra("manfactureId",manfactureId)
            startActivity(intent)
        }
        concern.setOnClickListener {
            ApiManager.getInstance().getApiPersonalTest().focusMan(getUserId(),manfactureId.toString())
                .enqueue(object : Callback<BaseHttpBean<Boolean>> {
                    override fun onResponse(
                        call: Call<BaseHttpBean<Boolean>>,
                        response: Response<BaseHttpBean<Boolean>>
                    ) {
                        if (response.body() == null){
                            return
                        }
                        if (response.body()!!.data!!) {
                            concern.setText("关注")
                        } else {
                            concern.setText("取消关注")
                        }
                        val followChangeBean = ManufactureChangeBean()
                        followChangeBean.setId(manfactureId)
                        followChangeBean.isFollow = response.body()!!.data!!
                        EventBus.getDefault().post(followChangeBean)
                    }

                    override fun onFailure(call: Call<BaseHttpBean<Boolean>>, t: Throwable) {}
                })
        }
        clienter.setOnClickListener {
            DialogUtil.showCustomDialog(this,"mall")
        }

    }

}