package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_amall_v3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.ActivityAmallV3NewYearBinding
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class AMallActivityV3_NewYear : BaseActivity() {
    lateinit var binding : ActivityAmallV3NewYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val map = HashMap<String,String>()
        map.put("userId",UserInfoViewModel.getUserId())
        MobclickAgent.onEvent(applicationContext,"enteramall",map)
        Constants.shopflag = "amall"
        index = 0
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        binding = ActivityAmallV3NewYearBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeToGreyButTranslucent()
        initImages()
        initView()
    }

    override fun onResume() {
        super.onResume()
        initMessages()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(str : String) {
        if ("messagerefresh".equals(str)){
            initMessages()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun initMessages(){
        ApiManager.getInstance().getApiAMallV3().getMyMessages(UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>> {
            override fun onResponse(
                    call: Call<BaseHttpBean<Int>>,
                    response: Response<BaseHttpBean<Int>>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    if (response.body()!!.data!! > 0){
                        count_message.visibility = View.VISIBLE
                        count_message.setText(response.body()!!.data!!.toString())
                    } else {
                        count_message.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

            }

        })
    }

    val fragments = arrayOf(
            AMallV3MainFragmentNewYear(),
            AMallV3CategoryFragment(),
            AMallV3MessageFragment(),
            AMallV3MyFragment()
    )

    lateinit var linearlayouts : Array<View>

    lateinit var imageviews : Array<ImageView>

    lateinit var texts : Array<TextView>

    lateinit var imageDefault : Array<Int>

    lateinit var imagesSelected : Array<Int>//被选中时的状态

    //先初始化需要使用的图片
    fun initImages(){
        imageDefault = arrayOf(
                R.mipmap.amallv3_main_false_new_year,
                R.mipmap.amallv3_category_false_new_year,
                R.mipmap.amallv3_message_false_new_year,
                R.mipmap.amallv3_my_false_new_year
        )
        imagesSelected = arrayOf(
                R.mipmap.amallv3_main_true_new_year,
                R.mipmap.amallv3_category_true_new_year,
                R.mipmap.amallv3_message_true_new_year,
                R.mipmap.amallv3_my_true_new_year
        )
    }

    fun initView(){
        //首先初始化view的数据。
        linearlayouts = arrayOf(
                binding.layoutMain,
                binding.layoutCategory,
                binding.layoutMessage,
                binding.layoutMy
        )
        imageviews = arrayOf(
                binding.imgMain,
                binding.imgCategory,
                binding.imgMessage,
                binding.imgMy
        )
        texts = arrayOf(
                binding.textMain,
                binding.textCategory,
                binding.textMessage,
                binding.textMy
        )
        for (item in linearlayouts.indices) {
            linearlayouts[item].setOnClickListener(MainLayoutListener((item)))
        }

        supportFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
    }

    var index = 0

    inner class MainLayoutListener(val index : Int) : View.OnClickListener{

        override fun onClick(v: View?) {
            if (this@AMallActivityV3_NewYear.index != index){
                imageviews[this@AMallActivityV3_NewYear.index].setImageResource(imageDefault[this@AMallActivityV3_NewYear.index])
                texts[this@AMallActivityV3_NewYear.index].setTextColor(resources.getColor(R.color.black))

                this@AMallActivityV3_NewYear.index = index
                imageviews[this@AMallActivityV3_NewYear.index].setImageResource(imagesSelected[this@AMallActivityV3_NewYear.index])
                texts[this@AMallActivityV3_NewYear.index].setTextColor(resources.getColor(R.color.amallv3_true))

                hideAllFragments()
                if (fragments[index].isAdded){
                    supportFragmentManager.beginTransaction().show(fragments[index]).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
                }

            }
        }

    }

    fun hideAllFragments(){
        try {
            val transaction = supportFragmentManager.beginTransaction()
            for (i in fragments.indices){
                if (fragments[i].isAdded && !fragments[i].isHidden){
                    transaction.hide(fragments[i]).commit()
                }
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

}