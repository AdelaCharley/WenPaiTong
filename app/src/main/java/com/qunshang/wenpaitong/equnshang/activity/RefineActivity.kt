package com.qunshang.wenpaitong.equnshang.activity

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi

import com.bumptech.glide.Glide
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.kongzue.dialog.v3.TipDialog
import kotlinx.android.synthetic.main.activity_refine.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.RefineSelectedTagsAdapter
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel.setUserId
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel.setUserInfo
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.view.AutoLineFeedLayoutManager
import com.qunshang.wenpaitong.equnshang.view.SpaceItemDecoration
import java.io.File
import java.io.IOException

class RefineActivity : BaseActivity() {

    val PICK_PHOTO = 100

    var imagePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refine)
        initView()
    }

    fun openAlbum(){
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_PHOTO)
    }

    fun initView(){
        image.setOnClickListener {
            openAlbum()
        }

        check_man.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                check_woman.isChecked = false
            } else {
                check_woman.isChecked = true
            }
        }
        check_woman.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                check_man.isChecked = false
            } else {
                check_man.isChecked = true
            }
        }

        layout_birthday.setOnClickListener {
            pickTime()
        }

        val flowLayoutManager = AutoLineFeedLayoutManager()
//设置每一个item间距
        list_selectedtags.addItemDecoration(
            SpaceItemDecoration(
                com.qunshang.wenpaitong.equnshang.utils.CommonUtil.dp2px(
                    this,
                    10
                )
            )
        )
        list_selectedtags.setLayoutManager(flowLayoutManager)

        val flowLayoutManagerForAllTags = AutoLineFeedLayoutManager()
//设置每一个item间距
        alltags.addItemDecoration(
            SpaceItemDecoration(
                com.qunshang.wenpaitong.equnshang.utils.CommonUtil.dp2px(
                    this,
                    10
                )
            )
        );
        alltags.setLayoutManager(flowLayoutManagerForAllTags)

        val listforselected = ArrayList<String>()
        selectedTagsAdapter =
            com.qunshang.wenpaitong.equnshang.adapter.RefineSelectedTagsAdapter(this, listforselected)
        list_selectedtags.adapter = selectedTagsAdapter

        val list = listOf("教育","旅游","金融","汽车","房产","服饰鞋帽箱包","餐饮美食","商务服务","生活服务","美容","家居","电子产品")
        val allTagsAdapter = com.qunshang.wenpaitong.equnshang.adapter.RefineAllTagsAdapter(this, list)
        alltags.adapter = allTagsAdapter

        done.setOnClickListener {
            if (StringUtils.isEmpty(nickname.text.toString())){
                TipDialog.show(this,"请输入昵称",TipDialog.TYPE.WARNING)
                //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this,"请输入昵称")
                return@setOnClickListener
            }
            if (nickname.text.length > 6){
                TipDialog.show(this,"昵称最多6个字符",TipDialog.TYPE.WARNING)
                return@setOnClickListener
            }
            if (StringUtils.isEmpty(imagePath)){
                TipDialog.show(this,"请选择头像",TipDialog.TYPE.WARNING)
            } else {
                uploadImage(imagePath)
            }
        }
    }

    lateinit var selectedTagsAdapter : RefineSelectedTagsAdapter

    fun pickTime(){
        val picker = DatePicker(this)
        picker.setOnDatePickedListener { year, month, day ->
            birthday.setText(year.toString() + "-" + month + "-" + day)
        }
        val layout = picker.wheelLayout
        layout.setRange(DateEntity.yearOnFuture(-120), DateEntity.yearOnFuture(0), DateEntity.yearOnFuture(0))
        picker.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO){
            if (data == null){
                return
            }
            handleImageOnKitKat(data)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun handleImageOnKitKat(data: Intent) {
        var imagePath = ""
        val uri: Uri? = data.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.getAuthority()) {
                val id = docId.split(":").toTypedArray()[1] //解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri?.getAuthority()) {
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public downloads"),
                    java.lang.Long.valueOf(docId)
                )
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri?.getScheme(), ignoreCase = true)) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = getImagePath(uri!!, null)
        } else if ("file".equals(uri?.getScheme(), ignoreCase = true)) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri?.getPath()!!
        }
        this.imagePath = imagePath
        Glide.with(this).load(imagePath).into(image)
        //uploadImage(imagePath)
    }

    private fun getImagePath(uri: Uri, selection: String?): String {
        var path: String? = null
        //通过Uri和selection来获取真实的图片路径
        val cursor: Cursor? = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    fun uploadImage(imagePath : String){

        ImageUtil.uploadImage(UserInfoViewModel.getUserId(), File(imagePath),object : okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {

            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    val result = response.body?.string()
                    if (result != null){
                        val json = JSONObject(result)
                        if (json.getInt("code") == 200){
                            DialogUtil.toast(this@RefineActivity,"上传成功" + json.getString("data"))
                            updateInfo(json.getString("data"))
                        }
                    }
                }
            }

        })
    }

    fun updateInfo(url : String){
        runOnUiThread {
            var gender = 0
            if (check_man.isChecked){
                gender = 0
            } else {
                gender = 1
            }

            val tags = selectedTagsAdapter.selectedTags

            ApiManager.getInstance().getApiPersonalTest().updatePersonalData(
                UserInfoViewModel.getUserId(),
                nickname.text.toString(),
                gender.toString(),
                //birthday.text.toString(),
                //tags,
                url).enqueue(object : Callback<UserMsgBean> {
                override fun onResponse(call: Call<UserMsgBean>, response: Response<UserMsgBean>) {
                    if (response.body() != null){
                        if (response.body()!!.code == 200){
                            setUserInfo(response.body()!!.data)
                            setUserId(response.body()!!.getData().getUid().toString())
                            val intent: Intent = Intent(
                                this@RefineActivity,
                                MainActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else {
                            TipDialog.show(this@RefineActivity,response.body()!!.msg,TipDialog.TYPE.WARNING)
                        }
                    }
                }

                override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                }

            })
        }

    }

}