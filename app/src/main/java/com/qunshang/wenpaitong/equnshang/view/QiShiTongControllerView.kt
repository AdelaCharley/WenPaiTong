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
import com.bumptech.glide.Glide
//import kotlinx.android.synthetic.main.fragment_login_enter_message.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.NumUtil
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.layout_controllerview_product.view.*
import kotlinx.android.synthetic.main.layout_controllerview_titlename.view.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.view.*
import kotlinx.android.synthetic.main.view_controller_qishitong.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.QiShiTongVideoBean
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 * create by libo
 * create on 2020-05-20
 * modifier 何姝霖
 * last-modified 2021-08-09
 */
class QiShiTongControllerView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs), View.OnClickListener {
    private var listener: OnVideoControllerListener? = null
    private var videoDataBean: QiShiTongVideoBean.DataBean? = null

    private val TAG = "TEST_ControllerView"

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_controller_qishitong, this)
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
    }

    fun followStatusChanged(bean : ManufactureChangeBean){
        if (bean.follow) {
            ivFocus!!.visibility = VISIBLE
        } else {
            ivFocus!!.visibility = GONE
        }
    }

    fun update(){
        tvCommentCount!!.text = NumUtil.numberFilter(BaseVideoFragment.commentTitle.toInt())        //评论数（图标下方文字）
    }

    fun goToLieBian(){
        if (!UserHelper.isLogin(context)){
            context.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        val intent = Intent(context, LieBianActivity::class.java)
        context.startActivity(intent)
    }

    fun setVideoData(dataBean: QiShiTongVideoBean.DataBean) {
        this.videoDataBean = dataBean

        //左下角控件
        tvNickname!!.text = "@" + dataBean.vendorName                        //用户名
        AutoLinkHerfManager.setContent(dataBean.videoDesc, autoLinkTextView)//视频简介
        tvCreateTime!!.text = "发布于：" + dataBean.createTime                //发布日期
        Log.d("testCreateTime","createTime:" + dataBean.createTime)

        //右侧控件
        iv_show_head!!.load(dataBean.manufacturerHeadImgUrl)                           //头像
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

        if (!StringUtils.isEmpty(videoDataBean!!.productPosterUrl)){
            Glide.with(context).load(videoDataBean!!.productPosterUrl).into(product_image)
            product_name.setText(videoDataBean!!.productName)
            product_price.setText( "预售团购价" + "￥" + videoDataBean!!.vipGroupPrice.toString() + "起")
            onethoundtobuy.setText("")//这里设为空字符串是为了防止间距错乱的
            label_buycart.visibility = View.VISIBLE
            onethoundtobuy.visibility = View.INVISIBLE
            if (ProcureActivity.qishitongtype == 2){
                onethoundtobuy.setText("1000元起购")
                label_buycart.visibility = View.INVISIBLE
                onethoundtobuy.visibility = View.VISIBLE
                product_price.setText("拼团采购价￥" + videoDataBean!!.productPurchasePrice.toString() + "起")
                layout_lottery.setOnClickListener {
                    val intent = Intent(context, BMallProductDetailActivity::class.java)
                    intent.putExtra("productId", videoDataBean!!.getProductId())
                    context.startActivity(intent)
                }
            } else {
                layout_lottery.setOnClickListener {
                    val intent = Intent(context, ProductDetailActivity::class.java)
                    intent.putExtra("productId", videoDataBean!!.getProductId())
                    context.startActivity(intent)
                }
            }

        } else {
            layout_lottery.visibility = View.GONE
        }

    }

    fun setListener(listener: OnVideoControllerListener?) {
        this.listener = listener
    }

    var type = "bmall"

    fun put(str : String){
        this.type = str
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
                if (Constants.isNewPinHaoHuo){
                    if ("bmall".equals(Constants.shopflag)){
                        val intent = Intent(context,StoreDetailActivity::class.java)
                        intent.putExtra("manfactureId",videoDataBean?.manufacturerId)
                        intent.putExtra("type",type)
                        context.startActivity(intent)
                    } else {
                        val intent = Intent(context,StoreDetailActivityV2::class.java)
                        intent.putExtra("manfactureId",videoDataBean?.manufacturerId)
                        intent.putExtra("type",type)
                        context.startActivity(intent)
                    }
                } else {
                    val intent = Intent(context,StoreDetailActivity::class.java)
                    intent.putExtra("manfactureId",videoDataBean?.manufacturerId)
                    intent.putExtra("type",type)
                    context.startActivity(intent)
                }

                /*if (videoDataBean!!.isFocus != 0){

                } else {

                }*/
            }

            R.id.ivFocus -> {
                if (!UserHelper.isLogin(context)){
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return
                }
                ApiManager.getInstance().getApiVideoTest().focusManufactureWriter(UserInfoViewModel.getUserId(),videoDataBean?.manufacturerId.toString()).enqueue(object :
                    Callback<BaseHttpBean<Boolean>> {
                    override fun onResponse(call: Call<BaseHttpBean<Boolean>>, response: Response<BaseHttpBean<Boolean>>) {
                        if (response.body() != null){
                            if (response.body()!!.data!!){
                                ivFocus.visibility = View.VISIBLE
                            } else {
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
        }
    }

}