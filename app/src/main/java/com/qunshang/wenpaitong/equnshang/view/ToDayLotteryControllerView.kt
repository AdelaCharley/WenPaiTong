package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.ButterKnife
import coil.load
import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.NumUtil
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.layout_controllerview_lottery.view.*
import kotlinx.android.synthetic.main.layout_controllerview_lottery.view.product_image
import kotlinx.android.synthetic.main.layout_controllerview_lottery.view.product_name
import kotlinx.android.synthetic.main.layout_controllerview_lottery.view.product_price
import kotlinx.android.synthetic.main.layout_controllerview_product.view.*
import kotlinx.android.synthetic.main.view_controller_today_lottery.view.*
import kotlinx.android.synthetic.main.layout_controllerview_titlename.view.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.view.*
import kotlinx.android.synthetic.main.view_controller_last_lottery.view.*
import kotlinx.android.synthetic.main.view_controller_today_lottery.view.layout_brand
import kotlinx.android.synthetic.main.view_controller_today_lottery.view.layout_lottery
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class ToDayLotteryControllerView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs), View.OnClickListener {
    private var listener: OnVideoControllerListener? = null
    private var videoDataBean: com.qunshang.wenpaitong.equnshang.data.LotteryVideoBean.DataBean? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_controller_today_lottery, this)
        ButterKnife.bind(this, rootView)
        iv_show_head!!.setOnClickListener(this)
        ivFocus!!.setOnClickListener(this)
        ivUp!!.setOnClickListener(this)
        cbLike!!.setOnClickListener(this)
        ivComment!!.setOnClickListener(this)
        tvShare.visibility = View.GONE
        ivShare.visibility = View.GONE
        ivLiebian.setOnClickListener{
            goToLieBian()
        }
        tvLieBian.setOnClickListener {
            goToLieBian()
        }
        layout_brand.setOnClickListener { gotoAMallActivity() }
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

    fun gotoAMallActivity(){
        if (!UserHelper.isLogin(context)){
            context.startActivity(Intent(context,LoginActivity::class.java))
            return
        }
        if (Constants.isNewPinHaoHuo){
            val intent = Intent(context,
                    AMallActivityV3_NewYear::class.java)
            context.startActivity(intent)
        } else {
            /*val intent = Intent(context,
                AMallActivity::class.java)
            context.startActivity(intent)*/
        }
        /*val intent = Intent(context, AMallActivityV3::class.java)
        context.startActivity(intent)*/
    }

    fun goToLieBian(){
        if (!UserHelper.isLogin(context)){
            context.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        val intent = Intent(context, LieBianActivity::class.java)
        context.startActivity(intent)
    }

    fun setVideoData(dataBean: com.qunshang.wenpaitong.equnshang.data.LotteryVideoBean.DataBean) {
        this.videoDataBean = dataBean

        //左下角控件
        tvNickname!!.text = "@" + dataBean.userUname                        //用户名
        AutoLinkHerfManager.setContent(dataBean.videoDesc, autoLinkTextView)//视频简介
        tvCreateTime!!.text = "发布于：" + dataBean.createTime                //发布日期
        Log.d("testCreateTime","createTime:" + dataBean.createTime)

        //右侧控件
        iv_show_head!!.load(dataBean.userHeadimageUrl)                           //头像
        tvUpCount!!.text = NumUtil.numberFilter(dataBean.upCount)                //点赞数
        tvLikeCount!!.text = NumUtil.numberFilter(dataBean.likeCount)            //喜爱数
        tvCommentCount!!.text = NumUtil.numberFilter(dataBean.commentCount)      //评论数（图标下方文字）
        //关注状态
        if (dataBean.isFocus != 0) {
            ivFocus!!.visibility = GONE
        } else {
            ivFocus!!.visibility = VISIBLE
        }

        if (dataBean!!.isUp == 0) {
            ivUp.setImageResource(R.mipmap.btn_main_up_false)
        } else {
            ivUp.setImageResource(R.mipmap.btn_main_up_true)
        }

        if (!com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(videoDataBean!!.image)){
            Glide.with(context).load(videoDataBean!!.image).into(product_image)
            product_name.setText(videoDataBean!!.prizeDescription)
            product_price.setText("￥" + (videoDataBean!!.price/100).toString())
            layout_lottery.setOnClickListener {
                val intent = Intent(context, PrizeInfoDetailActivity::class.java)
                intent.putExtra("prizeId", videoDataBean!!.getProductId())
                context.startActivity(intent)
            }
        } else {
            layout_lottery.visibility = View.GONE
        }
    }

    fun setListener(listener: OnVideoControllerListener?) {
        this.listener = listener
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
                val intent = Intent(context, PersonalHomepageActivity::class.java)
                intent.putExtra("userId",videoDataBean?.userId.toString())
                intent.putExtra("ivFocus",videoDataBean!!.isFocus)
                context.startActivity(intent)
                return
                /*if (videoDataBean!!.isFocus != 0){

                }*/

            }
            R.id.ivFocus -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                ApiManager.getInstance().getApiVideoTest().focusWriter(UserInfoViewModel.getUserId(),videoDataBean?.userId.toString()).enqueue(object :
                    Callback<BaseHttpBean<Boolean>> {
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

                })
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
            R.id.tvShare -> listener!!.onShareClick()
        }
    }

}