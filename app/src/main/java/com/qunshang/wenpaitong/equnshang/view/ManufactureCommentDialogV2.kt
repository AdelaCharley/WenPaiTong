package com.qunshang.wenpaitong.equnshang.view
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_comment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.CommentBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import java.util.*

class ManufactureCommentDialogV2(context: Context, val videoId: Int,val publishId : String) : BottomPopupView(context), View.OnClickListener {

    private val apiVideoTest = ApiManager.getInstance().getApiVideoTest()
    private var commentAdapter: com.qunshang.wenpaitong.equnshang.adapter.ManufactureCommentAdapter? = null

    private var datas = ArrayList<CommentBean.DataDTO>()    //加载的评论数据
    private var replyContent: String? = null                //自己写的评论内容

    val TAG = "TEST_CommentDialog"

    override fun onClick(v: View?) {
        if (v!! == tv_comment_send){
            replyContent = et_comment_enter.text.toString()
            if (!StringUtils.isEmpty(replyContent!!.trim())) {
                sendComment()
            } else {
                DialogUtil.toast(context,"输入内容为空")
            }
        } else if (v == dialog_comment_close){
            this.dismiss()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_comment
    }

    override fun onCreate() {
        super.onCreate()
        hostWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initView()
        createDialog()
    }

    fun initView(){
        //isCancelable = true
        tv_dialog_comment_title.text = BaseVideoFragment.commentTitle + "条评论"
        tv_comment_send.setOnClickListener(this)
        dialog_comment_close.setOnClickListener(this)
    }

    private fun createDialog() {
        list!!.layoutManager = LinearLayoutManager(context)
        commentAdapter =
            com.qunshang.wenpaitong.equnshang.adapter.ManufactureCommentAdapter(
                context,
                datas
            )
        list!!.adapter = commentAdapter
        loadData()
    }

    private fun loadData() {
        apiVideoTest.loadManufactureComments(UserInfoViewModel.getUserId(),this.videoId).enqueue(object:
            Callback<CommentBean> {
            override fun onResponse(call: Call<CommentBean>, response: retrofit2.Response<CommentBean>) {
                if (response.body() == null){
                    return
                }
                val dataList = response.body()!!.data
                for (data in dataList) {
                    datas.add(data)
                }
                commentAdapter!!.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<CommentBean>, t: Throwable) {
                t.printStackTrace()
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
        //Toast.makeText(context,"点击评论", Toast.LENGTH_SHORT).show()
        apiVideoTest.sendManufactureComment(UserInfoViewModel.getUserId() ,videoId, publishId = publishId, replyContent!!)
            .enqueue(object : Callback<BaseHttpBean<CommentBean.DataDTO>>{
                override fun onResponse(call: Call<BaseHttpBean<CommentBean.DataDTO>>, response: Response<BaseHttpBean<CommentBean.DataDTO>>) {
                    //Log.i(TAG, "onResponse: " + response.body()?.msg)
                    if (response.body() != null){
                        onSendCommentSuccess(response.body()!!.data!!)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<CommentBean.DataDTO>>, t: Throwable) {
                    //Log.i(TAG, "onResponse: " + "error eo" + t.message)
                    t.message
                }
            })
    }

    fun onSendCommentSuccess(bean : CommentBean.DataDTO){
        commentAdapter?.addData(bean)
        et_comment_enter.setText("")
        et_comment_enter.clearFocus()
        BaseVideoFragment.commentTitle = (BaseVideoFragment.commentTitle.toInt() + 1 ).toString()
        tv_dialog_comment_title.setText(BaseVideoFragment.commentTitle + "条评论")
    }

}