package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.activity_add_video_to_store_videos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.Signature
import com.qunshang.wenpaitong.equnshang.data.VendorModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.videoupload.TXUGCPublish
import com.qunshang.wenpaitong.equnshang.utils.videoupload.TXUGCPublishTypeDef

class AddVideoToStoreVideosActivity : BaseActivity(),TXUGCPublishTypeDef.ITXVideoPublishListener {

    var videoPath : String? = ""

    var coverPath : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video_to_store_videos)
        toolbar_back.setOnClickListener { finish() }
        this.videoPath = intent.getStringExtra("videoPath")
        this.coverPath = intent.getStringExtra("coverPath")
        if (StringUtils.isEmpty(videoPath)){
            TipDialog.show(this,"出错了", TipDialog.TYPE.ERROR)
        }
        Glide.with(this).load(videoPath).into(image)
        ApiManager.getInstance().getApiPersonalTest().getSig().enqueue(object : Callback<Signature>{
            override fun onResponse(call: Call<Signature>, response: Response<Signature>) {
                if (response.body() == null){
                    return
                }
                signature = response.body()!!.data.sign
            }

            override fun onFailure(call: Call<Signature>, t: Throwable) {

            }
        })
        upload.setOnClickListener {
            upload()
        }
    }

    var signature = ""

    fun upload(){
        if (StringUtils.isEmpty(videoPath) or StringUtils.isEmpty(coverPath)){
            return
        }
        if (StringUtils.isEmpty(signature)){
            DialogUtil.toast(this,"出错了")
            return
        }
        val mVideoPublish = TXUGCPublish(
            this,
            "independence_android"
        )
        mVideoPublish.setListener(this)

        val param: TXUGCPublishTypeDef.TXPublishParam = TXUGCPublishTypeDef.TXPublishParam()
        param.signature = signature
        param.videoPath = videoPath
        param.coverPath = coverPath
        val publishCode: Int = mVideoPublish.publishVideo(param)
        if (publishCode != 0) {
            DialogUtil.toast(this,"出错了")
            return
        }
    }

    override fun onPublishProgress(uploadBytes: Long, totalBytes: Long) {
        WaitDialog.show(this,"正在上传")
    }

    override fun onPublishComplete(result: TXUGCPublishTypeDef.TXPublishResult?) {
        WaitDialog.dismiss()
        if (result == null){
            return
        }
        if (StringUtils.isEmpty(result.videoURL)){
            return
        }
        if (StringUtils.isEmpty(result.coverURL)){
            return
        }
        ApiManager.getInstance().getApiStore().addVendorVideo(VendorModel.getVendorId(),tittestr.text.toString(),
            result.videoId.toString(),result.coverURL,result.videoURL).enqueue(object : Callback<BaseHttpBean<String>>{
            override fun onResponse(call: Call<BaseHttpBean<String>>, response: Response<BaseHttpBean<String>>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    TipDialog.show(this@AddVideoToStoreVideosActivity,"上传成功",TipDialog.TYPE.SUCCESS).setOnDismissListener {
                        setResult(ManageStoreVideoActivity.RESULT_UPLOAD_VIDEO)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

            }

        })
    }
}