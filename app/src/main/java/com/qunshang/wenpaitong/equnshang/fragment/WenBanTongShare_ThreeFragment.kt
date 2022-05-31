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
import kotlinx.android.synthetic.main.fragment_wenbantong_share_three.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import uni.yzq.zxinglibrary.encode.CodeCreator
import java.lang.Exception

class WenBanTongShare_ThreeFragment(val bean : WenBanTongProductBean) : BaseShareFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wenbantong_share_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        init()
    }

    override fun getRootBitmap(): Bitmap? {
        return BitmapUtils.getCacheBitmapFromView(root)
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
            label_discount.visibility = View.GONE //本来是Visible
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

    fun initView() {
        bg.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                try {
                    if (current >= maxcount){
                        return
                    }
                    val bgwidth = bg.width  //获取背景图片的宽度
                    val layoutParams =
                        layout_image.layoutParams as ViewGroup.MarginLayoutParams //593  357
                    val percent: Double = 403.0 / 616.0
                    layoutParams.width = (bgwidth * percent).toInt()

                    val margintoppercent = 166.0 / 984.0
                    val topheight = margintoppercent * bg.height
                    layoutParams.setMargins(0, topheight.toInt(), 0, 0)
                    layout_image.layoutParams = layoutParams

                    val layourparamsofprice = product_price.layoutParams as ViewGroup.MarginLayoutParams
                    val margintoppercentoftextprice = 205.0 / 984.0 //其实是Bottom，我起名字的时候起搓了
                    val topheightoftextprice = (margintoppercentoftextprice * bg.height).toInt()
                    val marginleftpercentoftextprice = 140.0 / 615.0
                    val leftwidthoftextprice = (marginleftpercentoftextprice * bg.width).toInt()
                    layourparamsofprice.setMargins(leftwidthoftextprice,0, 0, topheightoftextprice)
                    product_price.layoutParams = layourparamsofprice

                    val layoutparamsofqrcode = parent1.layoutParams as ViewGroup.MarginLayoutParams
                    val marginrightofqr = 137.0/615.0
                    val marginrightheight = marginrightofqr * bg.width
                    layoutparamsofqrcode.setMargins(0,0,marginrightheight.toInt(),0)
                    parent1.layoutParams = layoutparamsofqrcode

                    val layoutparamsofqr = image_qrcode.layoutParams as ViewGroup.MarginLayoutParams
                    layoutparamsofqr.width = (100.0/615.0 * bg.width).toInt()
                    layoutparamsofqr.height = (100.0/615.0 * bg.width).toInt()
                    image_qrcode.layoutParams = layoutparamsofqr

                    current++
                    /*val layoutparamsofimageqrcode = image_qrcode.layoutParams as ViewGroup.MarginLayoutParams
                    layoutparamsofimageqrcode.width = (bgwidth * 131/613.0).toInt()
                    layoutparamsofimageqrcode.height = (bgwidth * 131/613.0).toInt()
                    val bottomofimageqrcode = (104.0/980.0 * bg.height).toInt()
                    val rigtofimageqrcode = (84.0/613.0 * bg.width).toInt()
                    layoutparamsofimageqrcode.setMargins(0,0,rigtofimageqrcode,bottomofimageqrcode)
                    image_qrcode.layoutParams = layoutparamsofimageqrcode*/
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    var current = 0

    val maxcount = 6

}