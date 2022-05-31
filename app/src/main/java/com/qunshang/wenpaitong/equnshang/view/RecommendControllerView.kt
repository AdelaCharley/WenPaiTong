package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import butterknife.ButterKnife
import coil.load
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_controllerview_titlename.view.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.view.*
import org.greenrobot.eventbus.EventBus
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.LieBianActivity
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.activity.V300PublisherMainHomeActivity
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel.getUserId
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager.Companion.manager
import com.qunshang.wenpaitong.equnshang.utils.NumUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 * create by libo
 * create on 2020-05-20
 * modifier 何姝霖
 * last-modified 2021-08-09
 */
class RecommendControllerView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs), View.OnClickListener {
    private var listener: OnVideoControllerListener? = null
    private var videoDataBean: RecommendVideoBean.DataBean? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_controller_recommend, this)
        ButterKnife.bind(this, rootView)
        iv_show_head!!.setOnClickListener(this)
        ivFocus!!.setOnClickListener(this)
        ivUp!!.setOnClickListener(this)
        cbLike!!.setOnClickListener(this)
        ivComment!!.setOnClickListener(this)
        ivShare.setOnClickListener(this)
        tvShare!!.setOnClickListener(this)
        //tvInvite.visibility = View.GONE
        //ivInvite.visibility = View.GONE
        ivLiebian.setOnClickListener{
            goToLieBian()
        }
        tvLieBian.setOnClickListener {
            goToLieBian()
        }
    }

    fun goToLieBian(){
        if (!UserHelper.isLogin(context)){
            context.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        val intent = Intent(context,LieBianActivity::class.java)
        context.startActivity(intent)
    }

    fun setVideoData(dataBean: RecommendVideoBean.DataBean) {
        this.videoDataBean = dataBean

        //左下角控件
        tvNickname!!.text = "@" + dataBean.agencyName                        //用户名
        AutoLinkHerfManager.setContent(dataBean.videoDesc, autoLinkTextView)//视频简介
        tvCreateTime!!.text = "发布于：" + dataBean.createTime                //发布日期
        Log.d("testCreateTime","createTime:" + dataBean.createTime)

        //右侧控件
        iv_show_head!!.load(dataBean.agencyAvatar)                           //头像
        tvUpCount!!.text = NumUtil.numberFilter(dataBean.upCount)                //点赞数
        tvLikeCount!!.text = NumUtil.numberFilter(dataBean.likeCount)            //喜爱数
        tvCommentCount!!.text = NumUtil.numberFilter(dataBean.commentCount)      //评论数（图标下方文字）
        //关注状态
        if (dataBean.isFocus != 0) {//1是已关注
            ivFocus!!.visibility = GONE
        } else {
            ivFocus!!.visibility = VISIBLE//0是未关注
        }
        updateFocusStatus()
    }

    fun updateFocusStatus(){
        if (videoDataBean!!.isUp == 0) {
            ivUp.setImageResource(R.mipmap.btn_main_up_false)
        } else {
            ivUp.setImageResource(R.mipmap.btn_main_up_true)
        }
    }

    fun update(){
        tvCommentCount!!.text = NumUtil.numberFilter(BaseVideoFragment.commentTitle.toInt())        //评论数（图标下方文字）
    }

    fun followStatusChanged(bean : FollowChangeBean){
        if (bean.isFollow) {
            ivFocus!!.visibility = VISIBLE
        } else {
            ivFocus!!.visibility = GONE
        }
    }

    fun setListener(listener: OnVideoControllerListener?) {
        this.listener = listener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //ToastUtil.toast(context,"附着窗口" + videoDataBean!!.videoDesc)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //ToastUtil.toast(context,"离开窗口" + videoDataBean!!.videoDesc)
    }

    override fun onClick(v: View) {
        if (listener == null) {
            return
        }
        when (v.id) {
            R.id.iv_show_head -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                /*val intent = Intent(context, PersonalHomepageActivity::class.java)
                intent.putExtra("userId",videoDataBean?.agencyId.toString())
                intent.putExtra("ivFocus",videoDataBean!!.isFocus)
                context.startActivity(intent)*/
                val intent =
                    Intent(context, V300PublisherMainHomeActivity::class.java)
                intent.putExtra("agencyId", videoDataBean?.agencyId)
                context.startActivity(intent)
                return
                //Toast.makeText(context,"我点击了",Toast.LENGTH_SHORT).show()
                /*if (videoDataBean!!.isFocus != 0){

                }*/

            }
            R.id.ivFocus -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                manager.getApiWenBanTong_ZhangJun().concernCollegeOrUnConcern(getUserId(), videoDataBean!!.agencyId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { stringBaseHttpBean ->
                        if (stringBaseHttpBean != null) {
                            if (stringBaseHttpBean.code == 200) {
                                val followChangeBean = FollowChangeBean()
                                followChangeBean.id = videoDataBean?.agencyId!!
                                if ("1" == stringBaseHttpBean.data) {
                                    videoDataBean?.isFocus = 200
                                    ivFocus.visibility = View.GONE
                                    followChangeBean.isFollow = false
                                } else {
                                    videoDataBean?.isFocus = 0
                                    ivFocus.visibility = View.VISIBLE
                                    followChangeBean.isFollow = true
                                }
                                EventBus.getDefault().post(followChangeBean)
                            }
                        }
                    }
                /*ApiManager.getInstance().getApiVideoTest().focusWriter(UserInfoViewModel.getUserId(),videoDataBean?.agencyId.toString()).enqueue(object : Callback<BaseHttpBean<Boolean>>{
                    override fun onResponse(call: Call<BaseHttpBean<Boolean>>, response: Response<BaseHttpBean<Boolean>>) {
                        //Log.i("zhangjuniii",response.body()?.msg)
                        if (response.body() != null){
                            if (response.body()!!.data!!){
                                videoDataBean?.isFocus = 0
                                ivFocus.visibility = View.VISIBLE
                            } else {
                                videoDataBean?.isFocus = 200
                                ivFocus.visibility = View.GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseHttpBean<Boolean>>, t: Throwable) {
                    }

                })*/
            }
            R.id.ivUp -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                listener!!.onUpClick()
            }
            R.id.cbLike -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                listener!!.onLikeClick()
            }
            R.id.ivComment -> listener!!.onCommentClick()
            R.id.ivShare -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                listener!!.onShareClick()
            }
        }
    }

}