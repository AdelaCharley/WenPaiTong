package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View

import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_publish_experience.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.PublishExperienceImageAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.io.IOException

class PublishExperienceActivity : BaseActivity() {

    companion object{
        val REQUEST_CODE = 200
    }

    var prizeId : Int = 0

    var type : Int = 0

    var relationId : Long = 0

    var imageUrl : String? = ""

    var winTime : String ? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_experience)
        adapter = PublishExperienceImageAdapter(this)
        toolbar_back.setOnClickListener { finish() }
        prizeId = intent.getIntExtra("prizeId",0)
        type = intent.getIntExtra("type",0)
        relationId = intent.getLongExtra("relationId",0)
        imageUrl =  intent.getStringExtra("imageUrl")
        winTime = intent.getStringExtra("winTime")
        toolbar_title.setText("发布心得")
        images.adapter = adapter
        toolbar_right_image.visibility = View.VISIBLE
        toolbar_right_image.setImageDrawable(getDrawable(R.mipmap.publish_experience))
        initView()
    }

    lateinit var adapter : PublishExperienceImageAdapter

    fun initView(){
        title_close.setOnClickListener {
            input_title.setText("")
        }
        content_close.setOnClickListener {
            input_content.setText("")
        }
        text_zhiliang.setOnClickListener {
            input_content.setText(input_content.text.toString() + "\n" + "质量：" + "\n")
        }
        text_waixing.setOnClickListener {
            input_content.setText(input_content.text.toString() + "\n" + "外形外观：" + "\n")
        }
        text_shushidu.setOnClickListener {
            input_content.setText(input_content.text.toString() + "\n" + "舒适度：" + "\n")
        }
        toolbar_right_image.setOnClickListener {
            submit()
        }
    }

    fun submit(){
        if (StringUtils.isEmpty(input_title.text.toString())){
            TipDialog.show(this,"请填写标题",TipDialog.TYPE.WARNING)
            return
        }
        if (StringUtils.isEmpty(input_content.text.toString())){
            TipDialog.show(this,"请填写评价",TipDialog.TYPE.WARNING)
            return
        }
        if (adapter.getData().size == 0){
            ApiManager.getInstance().getApiLotteryTest().publishExperienceNoImage(prizeId.toString(),input_title.text.toString(),input_content.text.toString(),
            UserInfoViewModel.getUserId(),type.toString(),relationId.toString(),winTime.toString(),imageUrl!!).enqueue(object : Callback<BaseHttpBean<String>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<String>>,
                    response: Response<BaseHttpBean<String>>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        TipDialog.show(this@PublishExperienceActivity,"发布成功",TipDialog.TYPE.SUCCESS).setOnDismissListener {
                            val intent = Intent(this@PublishExperienceActivity, ExperienceActivity::class.java)
                            intent.putExtra("experienceid",response.body()!!.data!!.toInt())
                            startActivity(intent)
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

                }

            })
        } else {
            WaitDialog.show(this,"正在上传")
            ApiManager.getInstance().getApiLotteryTest().publishExperience(prizeId.toString(),input_title.text.toString(),input_content.text.toString(),
                UserInfoViewModel.getUserId(),type.toString(),relationId.toString(),winTime.toString()).enqueue(object : Callback<BaseHttpBean<String>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<String>>,
                    response: Response<BaseHttpBean<String>>
                ) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        uploadImages(response.body()!!.data!!)
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                }

            })
        }
    }

    var uploadedCount = 0

    fun uploadImages(newid : String){

        var isError = false

        if (adapter.getData().size == 0){
            return
        }

        ImageUtil.uploadExperienceImages(this,newid,adapter.getData().get(uploadedCount),object : okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                if (!isError){
                    WaitDialog.dismiss()
                    TipDialog.show(this@PublishExperienceActivity,"上传失败",TipDialog.TYPE.SUCCESS)
                    isError = true
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.body == null){
                    return
                }
                val res = response.body!!.string()
                val json = JSONObject(res)
                if (json.getInt("code") == 200){
                    uploadedCount ++
                    if (uploadedCount == adapter.getData().size){
                        WaitDialog.dismiss()
                        TipDialog.show(this@PublishExperienceActivity,"上传成功",TipDialog.TYPE.SUCCESS)
                        val intent = Intent(this@PublishExperienceActivity, ExperienceActivity::class.java)
                        intent.putExtra("experienceid",newid.toInt())
                        startActivity(intent)
                        finish()
                    } else {
                        uploadImages(newid)
                    }
                } else {
                    if (!isError){
                        WaitDialog.dismiss()
                        TipDialog.show(this@PublishExperienceActivity,"上传失败",TipDialog.TYPE.SUCCESS)
                        isError = true
                    }
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            var mSelected: List<Uri>? = null
            mSelected = Matisse.obtainResult(data)
            adapter.add(mSelected)
        }
    }

}