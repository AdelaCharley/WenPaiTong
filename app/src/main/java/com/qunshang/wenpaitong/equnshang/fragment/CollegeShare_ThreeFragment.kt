package com.qunshang.wenpaitong.equnshang.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.CropSquareTransformation
import kotlinx.android.synthetic.main.fragment_college_share__one.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.CollegeNewsShareBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import uni.yzq.zxinglibrary.encode.CodeCreator

class CollegeShare_ThreeFragment(val bean : CollegeNewsShareBean) : BaseShareFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_college_share__three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){
        Glide.with(requireContext()).load(UserInfoViewModel.getUserInfo()?.headimage).into(circleimage)
        //username.setText("您的群票好友" + UserInfoViewModel.getUserInfo()?.uname)

        title?.setText(bean.title)
        subtitle?.setText(bean.subTitle)

        val time = System.currentTimeMillis()

        val mode = 7
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

    override fun getRootBitmap(): Bitmap {
        return BitmapUtils.getCacheBitmapFromView(root)
    }
}