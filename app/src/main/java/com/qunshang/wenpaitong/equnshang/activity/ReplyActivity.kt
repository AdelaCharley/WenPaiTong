package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_reply.*
import kotlinx.android.synthetic.main.activity_reply.commentcontent
import kotlinx.android.synthetic.main.activity_reply.content
import kotlinx.android.synthetic.main.activity_reply.send
import kotlinx.android.synthetic.main.activity_reply.upcount
import kotlinx.android.synthetic.main.activity_reply.user_image
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ReplyActivity : BaseActivity() {

    lateinit var data : ExperienceBean.DataBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        data = intent.getSerializableExtra("data") as ExperienceBean.DataBean
        if (!this::data.isInitialized){
            DialogUtil.toast(this,"出错了")
            return
        }
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("回复")
        Glide.with(this).load(data.headImgSrc).into(user_image)
        username.setText(data.userName)
        content.setText(data.sendMsg)
        time.setText(data.createTime)
        if (data.getIsUp() != 0){
            up.setImageResource(R.mipmap.btn_main_up_true)
        }
        up.setOnClickListener {
            ApiManager.getInstance().getApiVideoTest().upComment(data.id.toString(),UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>>{
                override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        up.setImageResource(R.mipmap.btn_main_up_true)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                }

            })
        }
        loadReplys()
        upcount.setText(data.upNum.toString() + "个")

        send.setOnClickListener {
            if (com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(commentcontent.text.toString())){
                DialogUtil.toast(this,"请输入内容")
                return@setOnClickListener
            }


            ApiManager.getInstance().getApiVideoTest().
            commentExperienceOrComment(
                data.experienceId.toString(),
                data.id.toString(),
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
                    loadReplys()
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

    fun loadReplys(){
        ApiManager.getInstance().getApiVideoTest().loadReplys(data.id.toString(),UserInfoViewModel.getUserId(),"0").enqueue(object : Callback<ReplyBean>{
            override fun onResponse(call: Call<ReplyBean>, response: Response<ReplyBean>) {
                if (response.body() != null){
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ReplyBean>, t: Throwable) {

            }

        })
    }

    fun initView(bean : ReplyBean){
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        replys.layoutManager = manager
        replys.adapter = com.qunshang.wenpaitong.equnshang.adapter.ReplyAdapter(this, bean.data)
    }

}