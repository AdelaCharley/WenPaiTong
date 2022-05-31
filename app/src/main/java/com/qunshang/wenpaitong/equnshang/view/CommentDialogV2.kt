package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_comment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.adapter.CommentAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.CommentBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.*
import java.util.ArrayList

class CommentDialogV2(context: Context,val videoId: Int,val publishId : String) : BottomPopupView(context),View.OnClickListener {

    private val apiVideoTest = ApiManager.getInstance().getApiVideoTest()
    private var commentAdapter: CommentAdapter? = null

    private var datas = ArrayList<CommentBean.DataDTO>()    //加载的评论数据
    private var replyContent: String? = null                //自己写的评论内容

    override fun onCreate() {
        super.onCreate()
        //hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initView()
        createDialog()
    }

    override fun onShow() {
        super.onShow()
        setSoftInput()
        //SoftUtil.hideSoftInput(list)
    }

    override fun onClick(p0: View?) {

        if (p0!! == tv_comment_send) {
            replyContent = et_comment_enter.text.toString()
            if (!StringUtils.isEmpty(replyContent!!.trim())) {
                sendComment()
            } else {
                DialogUtil.toast(context,"输入内容为空")
            }
        } else if (p0 == dialog_comment_close) {
            this.dismiss()
        }
    }

    fun initView(){
        tv_dialog_comment_title.text = BaseVideoFragment.commentTitle + "条评论"
        tv_comment_send.setOnClickListener(this)
        dialog_comment_close.setOnClickListener(this)
    }

    private fun createDialog() {
        list!!.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentAdapter(context, datas)
        list!!.adapter = commentAdapter
        loadData()

    }

    fun setSoftInput(){
        val softInputUtil = SoftUtil()
        softInputUtil.attachSoftInput(rl_comment,object : SoftUtil.ISoftInputChanged{
            override fun onChanged(
                isSoftInputShow: Boolean,
                softInputHeight: Int,
                viewOffset: Int
            ) {
                if (isSoftInputShow) {
                    rl_comment.translationY = rl_comment.translationY - viewOffset
                } else {
                    et_comment_enter.clearFocus()
                    rl_comment.translationY = 0f
                }
            }

        })
    }

    /**
     * 上传评论信息
     */
    private fun sendComment() {
        if (!UserHelper.isLogin(context)){
            context.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        //Toast.makeText(context,"点击评论",Toast.LENGTH_SHORT).show()
        SoftUtil.hideSoftInput(windowDecorView)
        et_comment_enter.setText("")
        et_comment_enter.clearFocus()
        apiVideoTest.sendComment(UserInfoViewModel.getUserId() ,videoId, publishId = publishId, replyContent!!)
            .enqueue(object : Callback<BaseHttpBean<CommentBean.DataDTO>>{
                override fun onResponse(call: Call<BaseHttpBean<CommentBean.DataDTO>>, response: Response<BaseHttpBean<CommentBean.DataDTO>>) {
                    //StringUtils.log("成功")
                    if (response.body() == null){
                        return
                    }
                    if (response.body() != null){
                        onSendCommentSuccess(response.body()!!.data!!)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<CommentBean.DataDTO>>, t: Throwable) {
                    StringUtils.log(t.message)
                }
            })
    }

    fun onSendCommentSuccess(bean : CommentBean.DataDTO){
        commentAdapter?.addData(bean)
        if (VideoDataUtil.getVideoData() == null){

        } else if (VideoDataUtil.getVideoData()?.size == 0){

        } else {
            for (i in VideoDataUtil.getVideoData()!!.indices){
                if (VideoDataUtil.getVideoData()!!.get(i).videoId == videoId){
                    VideoDataUtil.getVideoData()!!.get(i).commentCount = VideoDataUtil.getVideoData()!!.get(i).commentCount + 1
                }
            }
        }
        StringUtils.log("评论钱的" + BaseVideoFragment.commentTitle.toInt())
        BaseVideoFragment.commentTitle = (BaseVideoFragment.commentTitle.toInt() + 1 ).toString()
        tv_dialog_comment_title.text = BaseVideoFragment.commentTitle + "条评论"
    }

    /**
     * 加载评论信息
     */
    private fun loadData() {
        apiVideoTest.loadComment(UserInfoViewModel.getUserId(),this.videoId).enqueue(object:
            Callback<CommentBean> {
            override fun onResponse(call: Call<CommentBean>, response: Response<CommentBean>) {
                if (response.body() == null){
                    return
                }
                val dataList = response.body()!!.data
                for (data in dataList) {
                    datas.add(data)
                }
                if (datas.size == 0){
                    nocomments.visibility = View.VISIBLE
                }
                commentAdapter?.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<CommentBean>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_comment
    }

}
