package com.qunshang.wenpaitong.equnshang.view
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.dialog_comment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * 视频页评论框
 * create by libo
 * create on 2020-05-24
 * modifier 何姝霖
 * last-modified 2021-08-05
 */
class ManufactureCommentDialog(//当前视频id
    private val videoId: Int, //评论框标题（“n条评论”）
    private var commentTitle: String
) : BaseBottomSheetDialog(), View.OnClickListener {

    private val apiVideoTest = ApiManager.getInstance().getApiVideoTest()!!
    private var commentAdapter: com.qunshang.wenpaitong.equnshang.adapter.ManufactureCommentAdapter? = null

    private var datas = ArrayList<com.qunshang.wenpaitong.equnshang.data.CommentBean.DataDTO>()    //加载的评论数据
    private var replyContent: String? = null                //自己写的评论内容

    val TAG = "TEST_CommentDialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_comment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        createDialog()
    }

    fun initView(){
        isCancelable = true
        tv_dialog_comment_title.text = commentTitle + "条评论"
        tv_comment_send.setOnClickListener(this)
        dialog_comment_close.setOnClickListener(this)
    }

    override val height: Int
        get() = resources.displayMetrics.heightPixels - 600

    override fun onClick(p0: View?) {

        if (p0!! == tv_comment_send){
            replyContent = et_comment_enter.text.toString()
            if (replyContent != null){
                sendComment()
            }
        } else if (p0!! == dialog_comment_close){
            this.dismiss()
        }
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

    /**
     * 加载评论信息
     */
    private fun loadData() {
        apiVideoTest.loadManufactureComments(UserInfoViewModel.getUserId(),this.videoId).enqueue(object: Callback<com.qunshang.wenpaitong.equnshang.data.CommentBean>{
            override fun onResponse(call: Call<com.qunshang.wenpaitong.equnshang.data.CommentBean>, response: retrofit2.Response<com.qunshang.wenpaitong.equnshang.data.CommentBean>) {
                val dataList = response.body()!!.data
                for (data in dataList) {
                    datas.add(data)
                }
                commentAdapter!!.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.CommentBean>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    /**
     * 上传评论信息
     */
    private fun sendComment() {
        //Toast.makeText(context,"点击评论",Toast.LENGTH_SHORT).show()
        apiVideoTest.sendManufactureComment(UserInfoViewModel.getUserId() ,videoId, publishId ="1", replyContent!!)
            .enqueue(object : Callback<BaseHttpBean<com.qunshang.wenpaitong.equnshang.data.CommentBean.DataDTO>>{
                override fun onResponse(call: Call<BaseHttpBean<com.qunshang.wenpaitong.equnshang.data.CommentBean.DataDTO>>, response: Response<BaseHttpBean<com.qunshang.wenpaitong.equnshang.data.CommentBean.DataDTO>>) {
                    Log.i(TAG, "onResponse: " + response.body()?.msg)
                    if (response.body() != null){
                        onSendCommentSuccess(response.body()!!.data!!)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<com.qunshang.wenpaitong.equnshang.data.CommentBean.DataDTO>>, t: Throwable) {
                    Log.i(TAG, "onResponse: " + "error eo" + t.message)
                    t.message
                }
            })
    }

    fun onSendCommentSuccess(bean : com.qunshang.wenpaitong.equnshang.data.CommentBean.DataDTO){
        commentAdapter?.addData(bean)
        et_comment_enter.setText("")
        et_comment_enter.clearFocus()
        commentTitle = (commentTitle.toInt() + 1 ).toString()
        tv_dialog_comment_title.setText(commentTitle + "条评论")
    }

}