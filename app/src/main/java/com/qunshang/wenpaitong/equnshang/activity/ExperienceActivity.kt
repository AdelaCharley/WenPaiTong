package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_experience.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.ExperienceAdapter
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ExperienceActivity : BaseActivity() {

    var experienceId = -9999

    lateinit var banner: Banner<String, BannerImageAdapter<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("心得分享")
        banner = findViewById(R.id.image_url)
        experienceId = intent.getIntExtra("experienceid",-9999)
        if (experienceId == -9999){
            DialogUtil.toast(this,"出错了")
            return
        } else {
            //DialogUtil.toast(this,experienceId.toString())
        }

        ApiManager.getInstance().getApiVideoTest().loadExperienceInfo(experienceId.toString(),UserInfoViewModel.getUserId()).enqueue(object : Callback<ExperienceInfoBean>{
            override fun onResponse(call: Call<ExperienceInfoBean>, response: Response<ExperienceInfoBean>) {
                if(response.body() != null){
                    initInfo(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ExperienceInfoBean>, t: Throwable) {

            }

        })
        loadExperienceComment()

        initView()

    }

    fun loadExperienceComment(){
        ApiManager.getInstance().getApiVideoTest().loadExperience(experienceId.toString(),"0",UserInfoViewModel.getUserId()).enqueue(object : Callback<ExperienceBean>{
            override fun onResponse(call: Call<ExperienceBean>, response: Response<ExperienceBean>) {
                if (response.body() != null){
                    val manager = LinearLayoutManager(this@ExperienceActivity)
                    manager.orientation = LinearLayoutManager.VERTICAL
                    comments.layoutManager = manager
                    adapter = ExperienceAdapter(this@ExperienceActivity, response.body()?.data!!)
                    comments.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ExperienceBean>, t: Throwable) {

            }

        })
    }

    fun initView(){
        send.setOnClickListener {
            if (StringUtils.isEmpty(commentcontent.text.toString())) {
                DialogUtil.toast(this, "请输入内容")
                return@setOnClickListener
            }

            ApiManager.getInstance().getApiVideoTest().
            commentExperienceOrComment(
                experienceId.toString(),
                "-1",
                commentcontent.text.toString(),UserInfoViewModel.getUserId()).enqueue(object : Callback<SubmitCommentForExperienceBean>{
                override fun onResponse(
                    call: Call<SubmitCommentForExperienceBean>,
                    response: Response<SubmitCommentForExperienceBean>
                ) {

                    if (response.body() == null){
                        return
                    }
                    commentcontent.setText("")
                    commentcontent.clearFocus()
                    loadExperienceComment()
                    /*val data = response.body()!!.data
                    val bean = ExperienceBean.DataBean()
                    bean.id = data.id
                    bean.sendMsg = data.content
                    bean.createTime = data.createTime
                    bean.experienceId = data.experienceId.toInt()
                    adapter.add(bean)*/

                }

                override fun onFailure(call: Call<SubmitCommentForExperienceBean>, t: Throwable) {

                }


            })
        }
    }

    lateinit var adapter : ExperienceAdapter

    fun filterList(list : List<String>) : List<String>{
        val newlist = ArrayList<String>()
        for ( i in list.indices){
            if (!StringUtils.isEmpty(list.get(i))){
                newlist.add(list.get(i))
            }
        }
        return newlist
    }

    fun initInfo(bean : ExperienceInfoBean){
        Glide.with(this).load(bean.data.headImage).into(user_image)
        videoname.setText(bean.data.name)

        if (!StringUtils.isEmpty(bean.data.image_url)){
            val split = filterList(bean.data.image_url.split(",").toList())
            val adapter : BannerImageAdapter<String> = object : BannerImageAdapter<String>(split){
                override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
                    Glide.with(holder!!.itemView)
                        .load(data)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                        .into(holder.imageView)
                }
            }
            banner
                .setAdapter(adapter)
                .addBannerLifecycleObserver(this)
                .setIndicator(CircleIndicator(this))
        }

        if (bean.data.getIsUp() != 0){
            iv_zan.setImageResource(R.mipmap.btn_main_up_true)
        }
        iv_zan.setOnClickListener {
            ApiManager.getInstance().getApiVideoTest().upExperience(experienceId,UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>>{
                override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        iv_zan.setImageResource(R.mipmap.btn_main_up_true)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                }

            })
        }

        //Glide.with(this).load(bean.data.image_url).into(image_url)
        Glide.with(this).load(bean.data.introduce_image_url).into(product_image_url)
        productname.setText(bean.data.description)
        date.setText(bean.data.win_time)
        title_.setText(bean.data.prizeName)
        content.setText(bean.data.content)
        browsecount.setText(bean.data.browse_num.toString() + "浏览")
        upcount.setText(bean.data.up_num.toString())
        commentcount.setText(bean.data.comment_num.toString())
    }

}