package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_store_detail_v2.*
import kotlinx.android.synthetic.main.activity_store_detail_v2.input
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.ManufactureV2Bean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean
import com.qunshang.wenpaitong.equnshang.fragment.ManufactureProductFragmentV2
import com.qunshang.wenpaitong.equnshang.fragment.ManufactureVideoFragmentV2
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import android.graphics.Typeface
import android.util.TypedValue
import androidx.viewpager.widget.ViewPager

class StoreDetailActivityV2 : BaseActivity() {

    var manfactureId = -999

    var type : String? = "amall"

    lateinit var bean: ManufactureV2Bean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail_v2)
        toolbar_back.setOnClickListener { finish() }
        changeToDefaultButTranslucent()
        this.manfactureId = intent.getIntExtra("manfactureId",-999)
        type = intent.getStringExtra("type")
        if (StringUtils.isEmpty(type)){
            type = "amall"
        }
        if (manfactureId == -999){
            DialogUtil.toast(this,"出错了")
            return
        }
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()== KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    if (!StringUtils.isEmpty(input.text.toString())){
                        val intent = Intent(this,AMallSearchResultV3Activity::class.java)
                        intent.putExtra("keyword",input.text.toString())
                        intent.putExtra("manufacturerId",manfactureId)
                        startActivity(intent)
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DEL){
                    if (input.text.length != 0){
                        input.text.delete(input.text.length - 1, input.text.length)
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish()
                }
            }

            return@setOnKeyListener true
        }
        Log.i(Constants.logtag,"" + manfactureId)
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    var isFirst = true

    fun initView(){
        ApiManager.getInstance().getApiAMallV3().loadManufactureInfo(manfactureId,UserInfoViewModel.getUserId()).enqueue(
            object : Callback<ManufactureV2Bean> {
                override fun onResponse(
                    call: Call<ManufactureV2Bean>,
                    response: Response<ManufactureV2Bean>
                ) {
                    if (response.body() == null){
                        return
                    }

                    this@StoreDetailActivityV2.bean = response.body()!!

                    Glide.with(this@StoreDetailActivityV2).load(response.body()!!.data.manufactureLogoUrl).into(image_storer)
                    store_name.setText(response.body()!!.data.manufactureName)
                    salecount.setText("总售 " + response.body()!!.data.totalSale + "件")
                    if (response.body()!!.data.isFocus != 0){
                        img_add.setImageDrawable(resources.getDrawable(R.mipmap.image_store_detail_star))
                    } else {
                        img_add.setImageDrawable(resources.getDrawable(R.mipmap.image_store_detail_unstar))
                    }

                    if (!isFirst){
                        return
                    }

                    val titles = ArrayList<String>()
                    val fragments = ArrayList<Fragment>()
                    titles.add("商品")
                    titles.add("视频")
                    fragments.add(ManufactureProductFragmentV2(manfactureId))
                    fragments.add(ManufactureVideoFragmentV2(manfactureId))
                    viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {

                        }

                        override fun onPageSelected(position: Int) {
                            if (position == 0){
                                text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                                    17.toFloat()
                                )
                                text1.setTypeface(Typeface.DEFAULT_BOLD)
                                text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                                    15.toFloat()
                                )
                            }
                            if (position == 1){
                                text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                                    15.toFloat()
                                )
                                text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                                    17.toFloat()
                                )
                                text2.setTypeface(Typeface.DEFAULT_BOLD);
                            }
                        }

                        override fun onPageScrollStateChanged(state: Int) {

                        }

                    })
                    val adapter = BasePagerAdapter(supportFragmentManager,fragments,titles)
                    viewpager.adapter = adapter
                    text1.setOnClickListener {
                        viewpager.setCurrentItem(0)
                    }
                    text2.setOnClickListener {
                        viewpager.setCurrentItem(1)
                    }

                    image_storer.setOnClickListener {
                        val intent = Intent(this@StoreDetailActivityV2,StoreDetailInfoActivity::class.java)
                        intent.putExtra("info",this@StoreDetailActivityV2.bean)
                        startActivity(intent)
                    }
                    isFirst = false
                }

                override fun onFailure(call: Call<ManufactureV2Bean>, t: Throwable) {

                }

            }
        )
        layout_concern.setOnClickListener {
            ApiManager.getInstance().getApiPersonalTest().focusMan(UserInfoViewModel.getUserId(),manfactureId.toString())
                .enqueue(object : Callback<BaseHttpBean<Boolean>> {
                    override fun onResponse(
                        call: Call<BaseHttpBean<Boolean>>,
                        response: Response<BaseHttpBean<Boolean>>
                    ) {
                        if (response.body() == null){
                            return
                        }
                        if (response.body()!!.data!!) {
                            img_add.setImageDrawable(resources.getDrawable(R.mipmap.image_store_detail_unstar))
                            this@StoreDetailActivityV2.bean.data.isFocus = 0
                        } else {
                            img_add.setImageDrawable(resources.getDrawable(R.mipmap.image_store_detail_star))
                            this@StoreDetailActivityV2.bean .data.isFocus = 200
                        }
                        val followChangeBean = ManufactureChangeBean()
                        followChangeBean.setId(manfactureId)
                        followChangeBean.isFollow = response.body()!!.data!!
                        EventBus.getDefault().post(followChangeBean)
                    }

                    override fun onFailure(call: Call<BaseHttpBean<Boolean>>, t: Throwable) {}
                })
        }
    }

}