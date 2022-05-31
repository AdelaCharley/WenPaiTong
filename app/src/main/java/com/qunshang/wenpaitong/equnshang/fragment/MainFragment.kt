package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.VideoType
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.vp_main
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import kotlin.collections.ArrayList

class MainFragment : BaseFragment(), View.OnClickListener {

    //private var qishitongfragment: QiShiTongVideoFragment = QiShiTongVideoFragment()   //企视通

    private var recommendFragment: SampleVideoFragment = SampleVideoFragment(VideoType.TYPE_RECOMMEND)            //视频播放页面

    private val fragments = ArrayList<BaseVideoFragment>()

    private var pagerAdapter: BasePagerAdapter? = null

    override fun setLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(str : String){
        if ("loginsuccess".equals(str)){
            pagerAdapter?.clearAll()
            setFragments()
        }
    }

    override fun init() {
        val  activity = requireActivity() as BaseActivity
        activity.changeToDefaultButTranslucent()
        //DialogUtil.toast(requireContext(),"啊喊")
        qishitong.setOnClickListener(this)
        groupmain.setOnClickListener(this)
        amall.setOnClickListener(this)
        //权证文版通
        quanzheng.setOnClickListener(this)
        //买卖景天的H5
        //应用，拼好货
        setFragments()
        getUnReadMessages()
    }

    fun getUnReadMessages(){
        if (StringUtils.isEmpty(UserInfoViewModel.getUserId())){
            return
        }
        ApiManager.getInstance().getApiMainTest().getUnReadMessageCount(UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>>{
            override fun onResponse(
                call: Call<BaseHttpBean<Int>>,
                response: Response<BaseHttpBean<Int>>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data != 0){
                    message_count.visibility = View.VISIBLE
                    message_count.setText(response.body()!!.data.toString())
                } else {
                    message_count.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

            }

        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden){
            fragments.get(curPage).pause()
        } else {
            if (curPage == 0){
                val  activity = requireActivity() as BaseActivity
                activity.changeToDefaultButTranslucent()
                //activity.setSystemBarColor(R.color.black)
                //activity.clearSystemBarColor()
                recommendFragment.start()
            } //else if (curPage == 1){
                //recommendFragment.start()
            //}

        }
    }

    private fun setFragments() {
        iv_main_inform.setOnClickListener(this)
        iv_main_search.setOnClickListener(this)
        recommendFragment = SampleVideoFragment(VideoType.TYPE_RECOMMEND)
        fragments.add(recommendFragment)
        val titles = ArrayList<String>()
        titles.add("推荐")
        titles.add("拼好货")
        pagerAdapter = BasePagerAdapter(childFragmentManager, fragments, titles)
        vp_main!!.adapter = null
        //vp_main?.notifyAll()

        vp_main!!.adapter = pagerAdapter
        vp_main.offscreenPageLimit = 2
        vp_main.setCurrentItem(0)
        vp_main!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                fragments.get(curPage).pause()
                curPage = position
                if (fragments.get(position).getIsDataLoaded()){
                    fragments.get(position).start()
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    companion object {
        var curPage = 0
    }

    val REQUEST_MESSAGE = 565

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MESSAGE){
            message_count.visibility = View.GONE
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            iv_main_inform -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                startActivityForResult(Intent(context,SystemInformActivity::class.java),REQUEST_MESSAGE)
                return
            }
            iv_main_search -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                startActivity(Intent(context,SearchVideoActivity::class.java))
            }
            qishitong -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                val intent = Intent(requireContext(),PhysicalStoresActivity::class.java)
                startActivity(intent)
            }
            quanzheng -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                val intent = Intent(requireContext(),
                    WenBanTongActivity::class.java)
                startActivity(intent)
            }
            groupmain -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                val intent = Intent(requireContext(),
                    WebViewActivity::class.java)
                intent.putExtra("title","文版通")
                intent.putExtra("url",Constants.baseURL + "/wbt/list")
                startActivity(intent)
            }
            amall -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                if (Constants.isNewPinHaoHuo){
                    val intent = Intent(requireContext(),
                        AMallActivityV3::class.java)
                    startActivity(intent)

                } else {
                    val intent = Intent(requireContext(),
                        AMallActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

}