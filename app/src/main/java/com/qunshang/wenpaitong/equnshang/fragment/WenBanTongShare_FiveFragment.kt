package com.qunshang.wenpaitong.equnshang.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.CropSquareTransformation
import kotlinx.android.synthetic.main.fragment_wenbantong_share_five.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import uni.yzq.zxinglibrary.encode.CodeCreator

class WenBanTongShare_FiveFragment(val bean : WenBanTongProductBean) : BaseShareFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wenbantong_share_five, container, false)
    }

    override fun getRootBitmap(): Bitmap? {
        return BitmapUtils.getCacheBitmapFromView(root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        init()
    }

    fun init(){
        Glide.with(requireContext()).load(UserInfoViewModel.getUserInfo()?.headimage).into(circleimage)
        username.setText("您的群票好友" + UserInfoViewModel.getUserInfo()?.uname)
        Glide.with(requireContext()).load(bean.data.productPosterUrl).into(image_product)
        labelnew_wenbantong_count.setText(bean.data.tag)
        label_new_wenbantong_stock.setText(bean.data.initStock.toString() + "份")
        productname.setText(bean.data.productName)
        originalprice.setText("原价" + bean.data.evaluationPriceRes.format)
        val layoutParams = labelnew_wenbantong_count.layoutParams as ViewGroup.MarginLayoutParams
        if (bean.data.discount != null){
            product_price.setText(bean.data.discount.productFormatDiscountPriceStr)
            label_discount.visibility = View.VISIBLE
            label_discount.setText(bean.data.discount.discountStr)
            layoutParams.setMargins(CommonUtil.dp2px(requireContext(),5),layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin)
        } else {
            product_price.setText(bean.data.priceRes.format.toString())
            label_discount.setText("")
            label_discount.width = 0
            layoutParams.setMargins(CommonUtil.dp2px(requireContext(),0),layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin)
        }
        val time = System.currentTimeMillis()

        val mode = 6
        val invitecode = UserInfoViewModel.getUserInfo()!!.invitecode
        val str = Constants.baseURL + "/web/register.html?time=" + time + "&&mode=" + mode + "&&invitecode=" + invitecode
        Glide.with(this).asBitmap().load(UserInfoViewModel.getUserInfo()!!.headimage)
            .transform(CropSquareTransformation())
            .into(object :
                SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmap = CodeCreator.createQRCode(str, 400, 400, resource)
                    image_qrcode.setImageBitmap(bitmap)
                }
            })
    }

    fun initView(){
        bg.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                try {
                    if (current >= maxcount){
                        return
                    }
                    val bgwidth = bg.width  //获取背景图片的宽度
                    val layoutParams = image_product.layoutParams as ViewGroup.MarginLayoutParams //593  357
                    val percent : Double = 483.0/616.0
                    layoutParams.width = (bgwidth * percent).toInt()
                    layoutParams.height = (bgwidth * percent).toInt()

                    val margintoppercent = 142.0/983.0
                    val topheight = margintoppercent * bg.height
                    layoutParams.setMargins(0,topheight.toInt(),0,0)
                    image_product.layoutParams = layoutParams

                    val layoutparamsofview = view1.layoutParams as ViewGroup.MarginLayoutParams
                    val percentofview1width : Double = 476.0/616.0
                    layoutparamsofview.width = (bgwidth * percentofview1width).toInt()
                    val marginbottomofview1 = (172.0/983.0 * bg.height).toInt()
                    layoutparamsofview.setMargins(0,0,0,marginbottomofview1)
                    view1.layoutParams = layoutparamsofview

                    val layoutparamsofqrcode = parent1.layoutParams as ViewGroup.MarginLayoutParams
                    val marginrightofqr = 73.0/615.0
                    val marginrightheight = marginrightofqr * bg.width
                    val marginbottomtofqr = 69.0/983.0
                    val marginbottomtheight = marginbottomtofqr * bg.height
                    layoutparamsofqrcode.setMargins(0,0,marginrightheight.toInt(),marginbottomtheight.toInt())
                    parent1.layoutParams = layoutparamsofqrcode

                    val layoutparamsofqr = image_qrcode.layoutParams as ViewGroup.MarginLayoutParams
                    layoutparamsofqr.width = (77.0/615.0 * bg.width).toInt()
                    layoutparamsofqr.height = (77.0/615.0 * bg.width).toInt()
                    image_qrcode.layoutParams = layoutparamsofqr

                    current++
                } catch (e : Exception){
                    e.printStackTrace()
                }

            }
        })
    }

    var current = 0

    val maxcount = 3

}