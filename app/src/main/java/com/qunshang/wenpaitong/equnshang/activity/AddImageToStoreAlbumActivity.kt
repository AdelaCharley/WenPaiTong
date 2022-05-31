package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.activity_add_image_to_store_album.*
import okhttp3.Callback
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.VendorModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.io.File
import java.io.IOException

class AddImageToStoreAlbumActivity : BaseActivity() {

    var imagePath : String ? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image_to_store_album)
        this.imagePath = intent.getStringExtra("image")
        toolbar_back.setOnClickListener { finish() }
        if (StringUtils.isEmpty(imagePath)){
            TipDialog.show(this,"出错了",TipDialog.TYPE.ERROR)
        }
        Glide.with(this).load(imagePath).into(image)
        upload.setOnClickListener {
            upload()
        }
    }

    fun upload(){
        ImageUtil.uploadStoreImage(OpenStoreActivity.IMAGE_TYPE_HENDIMAGE, File(imagePath),object :
            Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.body == null){
                    return
                }
                val str : String = response.body!!.string()
                if (StringUtils.isEmpty(str)){
                    return
                }
                val json = JSONObject(str)
                if (json.getInt("code") == 200){
                    submitImage(json.getString("data"))
                }
            }

        })
    }

    fun submitImage(url : String){
        WaitDialog.show(this,"正在上传")
        ApiManager.getInstance().getApiStore().addVendorPicture(VendorModel.getVendorId(),tittestr.text.toString(),url).enqueue(object : retrofit2.Callback<BaseHttpBean<String>>{
            override fun onResponse(
                call: Call<BaseHttpBean<String>>,
                response: Response<BaseHttpBean<String>>
            ) {
                WaitDialog.dismiss()
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    TipDialog.show(this@AddImageToStoreAlbumActivity,"上传成功",TipDialog.TYPE.SUCCESS).setOnDismissListener {
                        setResult(ManageStoreAlbumActivity.RESULT_UPLOAD_IMAGE)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

            }

        })
    }

}