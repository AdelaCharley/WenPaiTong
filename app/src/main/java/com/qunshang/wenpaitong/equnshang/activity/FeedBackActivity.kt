package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.gson.Gson
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_feed_back.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.FeedBackAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.FileUtils
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.io.File
import java.io.IOException

class FeedBackActivity : BaseActivity() {

    companion object{
        val REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("问题反馈")
        adapter = FeedBackAdapter(this)
        images.adapter = adapter
        submit.setOnClickListener {
            if (StringUtils.isEmpty(content.text.toString())){
                DialogUtil.showWarnDialog(this,"请输入内容")
                return@setOnClickListener
            }
            if (StringUtils.isEmpty(contact.text.toString())){
                DialogUtil.showWarnDialog(this,"请输入联系方式")
                return@setOnClickListener
            }
            WaitDialog.show(this,"正在上传")
            var data = adapter.getData()
            var files = arrayOfNulls<File>(data.size)
            if (data.size != 0){
                for (i in data.indices){
                    files[i] = FileUtils.getFileByUri(this,data[i])
                }
            }
            ImageUtil.upFeedBack(content.text.toString(),contact.text.toString(),object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    WaitDialog.dismiss()
                }

                override fun onResponse(call: Call, response: Response) {
                    WaitDialog.dismiss()
                    val str = response.body?.string()
                    StringUtils.log(str)
                    if (str != null) {
                        val gson = Gson()
                        var bean = gson.fromJson(str, BaseHttpBean::class.java)
                        //val json = JSONObject(response.body?.string()!!)
                        if (bean.code == 200) {
                            TipDialog.show(
                                this@FeedBackActivity, "提交成功", TipDialog.TYPE.SUCCESS
                            ).setOnDismissListener {
                                finish()
                            }
                        }
                    }
                }

            },files)
        }
    }

    lateinit var adapter : FeedBackAdapter

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            var mSelected: List<Uri>? = null
            mSelected = Matisse.obtainResult(data)
            adapter.add(mSelected)
        }
    }

}