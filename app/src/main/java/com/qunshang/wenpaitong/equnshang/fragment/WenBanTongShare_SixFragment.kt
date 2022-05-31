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
import kotlinx.android.synthetic.main.fragment_wenbantong_share_six.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import uni.yzq.zxinglibrary.encode.CodeCreator
import java.lang.Exception

class WenBanTongShare_SixFragment(val bean : WenBanTongProductBean) : BaseShareFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wenbantong_share_six, container, false)
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
        productname.setText(bean.data.productName)
        originalprice.setText("原价" + bean.data.evaluationPriceRes.format)
        if (bean.data.discount != null){
            product_price.setText(bean.data.discount.productFormatDiscountPriceStr)
        } else {
            product_price.setText(bean.data.priceRes.format.toString())
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
                    val layoutParamsLayout1 = layout1.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParamsLayout1.setMargins(0,(151.0/981.0 * bg.height).toInt(),0,0)
                    layout1.layoutParams = layoutParamsLayout1

                    val layoutParamsImageProduct = image_product.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParamsImageProduct.width = (503.0/616.0 * bg.width).toInt()
                    layoutParamsImageProduct.height = (503.0/616.0 * bg.width).toInt()
                    layoutParamsImageProduct.setMargins(0,(250.0/981.0 * bg.height).toInt(),0,0)
                    image_product.layoutParams = layoutParamsImageProduct

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