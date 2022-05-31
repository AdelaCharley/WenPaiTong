package com.qunshang.wenpaitong.equnshang.activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.*
import kotlinx.android.synthetic.main.activity_personal_homepage.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityPersonalHomepageBinding
import com.qunshang.wenpaitong.equnshang.adapter.HomepageNavBean
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.DoConcernBean
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiManager.Companion.getInstance
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

/**
 *"我的主页 "页面
 * create by 何姝霖
 */
class PersonalHomepageActivity : BaseActivity() {

    lateinit var binding : ActivityPersonalHomepageBinding

    var userId : String? = UserInfoViewModel.getUserId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalHomepageBinding.inflate(layoutInflater)
        changeToGreyButTranslucent()
        setContentView(binding.root)
        binding.toolbarBack.setOnClickListener { finish() }
        this.userId = intent.getStringExtra("userId")
        if (StringUtils.isEmpty(userId)){
            userId = UserInfoViewModel.getUserId()
        }
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        initView()
        initListener()
    }

    private fun initView(){
        ApiManager.getInstance().getApiMainTest()
            .loadPersonalInfo(userId!!).enqueue(object :
                Callback<UserMsgBean> {
                override fun onResponse(call: Call<UserMsgBean>, response: Response<UserMsgBean>) {
                    if (response.body() == null){
                        return
                    }
                    val userInfo = response.body()!!.data
                    val image = userInfo.headimage
                    Glide.with(this@PersonalHomepageActivity).load(image).into(person_image)
                    person_name.setText(userInfo.uname)
                    loadNav()

                    val titles = ArrayList<String>()
                    titles.add("发布")
                    titles.add("商品")
                    val fragments = ArrayList<Fragment>()
                    fragments.add(PublishFragment(userId!!))
                    val adapter = BasePagerAdapter(supportFragmentManager,fragments,titles)
                    binding.container.adapter = adapter
                    //binding.tabs.setupWithViewPager(binding.container)
                }
                override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {

                }
            })
        val ivFoucs = intent.getIntExtra("ivFocus",-9)
        if (ivFoucs == -9){//自己进入自己 的页面的时候 隐藏关注按钮
            follow.visibility = View.GONE
        }
        if (ivFoucs > 0){
            follow.visibility = View.VISIBLE
            follow.setText("取消关注")
        }
        if (ivFoucs == 0){
            follow.visibility = View.VISIBLE
            follow.setText("关注")
        }
        follow.setOnClickListener {
            getInstance().getApiPersonalTest()
                .loadDoConcern(UserInfoViewModel.getUserId(), this.userId!!)
                .enqueue(object : Callback<DoConcernBean?> {
                    override fun onResponse(
                        call: Call<DoConcernBean?>,
                        response: Response<DoConcernBean?>
                    ) {
                        if (response != null) {
                            val body = response.body()
                            val isconcerned = body!!.isData
                            if (!isconcerned) {
                                follow.setText("取消关注")
                            } else {
                                follow.setText("关注")
                            }
                            val followChangeBean = FollowChangeBean()
                            followChangeBean.id = userId!!.toInt()
                            followChangeBean.isFollow = isconcerned
                            EventBus.getDefault().post(followChangeBean)
                        }
                    }

                    override fun onFailure(call: Call<DoConcernBean?>, t: Throwable) {}
                })
        }
    }

    fun loadNav(){
        ApiManager.getInstance().getApiPersonalTest().loadHomepageNav(userId!!,userId!!).enqueue(object: Callback<BaseHttpBean<HomepageNavBean>>{
            override fun onResponse(call: Call<BaseHttpBean<HomepageNavBean>>, response: Response<BaseHttpBean<HomepageNavBean>>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    layout.visibility = View.VISIBLE
                    val behavior = response.body()!!.data!!.behavior
                    if (behavior.size >= 4){
                        text_count_concern.setText(behavior.get(0).number.toString())
                        text_concern.setText(behavior.get(0).behaviorTxt)
                        text_count_up.setText(behavior.get(1).number.toString())
                        text_up.setText(behavior.get(1).behaviorTxt)
                        text_count_like.setText(behavior.get(2).number.toString())
                        text_like.setText(behavior.get(2).behaviorTxt)
                        text_count_comment.setText(behavior.get(3).number.toString())
                        text_comment.setText(behavior.get(3).behaviorTxt)
                    } else {
                        Toast.makeText(this@PersonalHomepageActivity,"系统出现错误",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    DialogUtil.showWarnDialog(this@PersonalHomepageActivity,response.body()!!.msg)
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<HomepageNavBean>>, t: Throwable) {

            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(def : String){}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun initListener(){
        layout_concern.setOnClickListener {
            if (!UserInfoViewModel.getUserId().equals(userId)){
                return@setOnClickListener
            }
            val intent = Intent(this,ConcernActivity::class.java)
            startActivity(intent)
            //showConcernFragment()
        }
        layout_up.setOnClickListener {
            if (!UserInfoViewModel.getUserId().equals(userId)){
                return@setOnClickListener
            }
            val intent = Intent(this,UpActivity::class.java)
            startActivity(intent)
            //showUpFragment()
        }
        layout_like.setOnClickListener {
            if (!UserInfoViewModel.getUserId().equals(userId)){
                return@setOnClickListener
            }
            val intent = Intent(this,StarActivity::class.java)
            startActivity(intent)
            //showLikeFragment()
        }
        layout_comment.setOnClickListener {
            if (!UserInfoViewModel.getUserId().equals(userId)){
                return@setOnClickListener
            }
            val intent = Intent(this,CommentActivity::class.java)
            startActivity(intent)
            //showCommentFragment()
        }
    }

}