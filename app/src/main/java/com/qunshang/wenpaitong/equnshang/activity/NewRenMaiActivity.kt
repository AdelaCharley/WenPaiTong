package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_new_ren_mai.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.NewRenMaiPeopleBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.NewRenMaiFragment
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class NewRenMaiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeToGreyButTranslucent()
        setContentView(R.layout.activity_new_ren_mai)
        toolbar_back.setOnClickListener { finish() }
        appbarlayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (appBarLayout == null) {
                    return
                }
                val toolbarHeight = appBarLayout.totalScrollRange
                val dy = Math.abs(verticalOffset)
                if (dy <= toolbarHeight) {
                    val scale = dy.toFloat() / toolbarHeight
                    val alpha = scale * 255
                    toolbar.setBackgroundColor(Color.argb(alpha.toInt(), 255, 255, 255))
                    toolbar_title.setTextColor(Color.argb(alpha.toInt(), 0, 0, 0))

                }
            }
        })
        toolbar_right_image.setOnClickListener {
            val intent = Intent(this, SearchRenMaiActivity::class.java)
            startActivity(intent)
        }
        initView()
        loadPeopleCount()
    }

    fun loadPeopleCount(){
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadPeopleCount(UserInfoViewModel.getUserId()).enqueue(object : Callback<NewRenMaiPeopleBean>{
            override fun onResponse(
                call: Call<NewRenMaiPeopleBean>,
                response: Response<NewRenMaiPeopleBean>
            ) {
                if (response.body() == null) {
                    return
                }
                count_renshu.setText(response.body()!!.data.directPeopleNum.toString())
                label_all_renshu.setText("私域人脉：" + response.body()!!.data.allPeopleNum)
            }

            override fun onFailure(call: Call<NewRenMaiPeopleBean>, t: Throwable) {

            }
        })
    }

    fun initView(){
        val titles = ArrayList<String>()
        val fragments = ArrayList<Fragment>()
        titles.add("全部")
        /*titles.add("粉丝")
        titles.add("网店")
        titles.add("办事处")
        titles.add("联络处")*/
        fragments.add(NewRenMaiFragment(0))
        /*fragments.add(NewRenMaiFragment(1))
        fragments.add(NewRenMaiFragment(2))
        fragments.add(NewRenMaiFragment(3))
        fragments.add(NewRenMaiFragment(4))*/
        val pagerAdapter = BasePagerAdapter(supportFragmentManager, fragments, titles)
        viewpager.adapter = pagerAdapter
        viewpager.offscreenPageLimit = 5
        //tablayout.setupWithViewPager(viewpager)
    }

}