package com.qunshang.wenpaitong.equnshang.activity

/*
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_back
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_title
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.IdBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.IDInfoDialog
import java.io.File
import java.io.IOException

class AuthActivity : BaseActivity() {

    var backstr = ""

    var renmianstr = ""

    var backImageUri: Uri? = null

    var backImagePath = ""

    var renmianImageUri: Uri? = null

    var renmianImagePath = ""

    val PICK_BACK_CAMERA = 20

    val PICK_BACK_ALBUM = 22

    val PICK_RENMIAN_CAMERA = 25

    val PICK_RENMIAN_ALBUM = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("实名认证")
        back.setOnClickListener {
            BottomMenu.show(
                this, arrayOf("拍照","从相册中选择")
            ) { text: String?, index: Int ->
                if (index == 0) {
                    if (PermissionUtil.checkCameraPermission(this)){

                    } else {
                        PermissionUtil.requireCameraPermission(this)
                        return@show
                    }
                    if (PermissionUtil.checkStoragePermission(this)){

                    } else {
                        PermissionUtil.requireStoragePermission(this)
                        return@show
                    }
                    backImageUri = FileUtils.createImageUri(this)
                    val intent = Intent()
                    intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        backImageUri
                    ) //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的

                    startActivityForResult(intent, PICK_BACK_CAMERA)
                }
                if (index == 1){
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(intent, PICK_BACK_ALBUM)
                }
            }
        }
        renmian.setOnClickListener {
            BottomMenu.show(
                this, arrayOf("拍照","从相册中选择")
            ) { text: String?, index: Int ->
                if (index == 0) {
                    if (PermissionUtil.checkCameraPermission(this)){

                    } else {
                        PermissionUtil.requireCameraPermission(this)
                        return@show
                    }
                    if (PermissionUtil.checkStoragePermission(this)){

                    } else {
                        PermissionUtil.requireStoragePermission(this)
                        return@show
                    }
                    renmianImageUri = FileUtils.createImageUri(this)
                    val intent = Intent()
                    intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        renmianImageUri
                    ) //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的

                    startActivityForResult(intent, PICK_RENMIAN_CAMERA)
                }
                if (index == 1){
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(intent, PICK_RENMIAN_ALBUM)
                }
            }
        }
        submit.setOnClickListener {
            if (StringUtils.isEmpty(backImagePath) or  StringUtils.isEmpty(backImagePath)){
                TipDialog.show(this,"请先选择图片",TipDialog.TYPE.WARNING)
                return@setOnClickListener
            }
            uploadImages()
        }
    }

    fun uploadImages(){
        if (StringUtils.isEmpty(backImagePath) or  StringUtils.isEmpty(renmianImagePath)){
            TipDialog.show(this@AuthActivity,"请选择图片",TipDialog.TYPE.ERROR)
        }
        WaitDialog.show(this,"正在加载")
        ImageUtil.uploadIdentityImage("back", File(backImagePath),object : Callback{
            override fun onFailure(call: Call, e: IOException) {

            }
            override fun onResponse(call: Call, response: Response) {
                if (response.body == null){
                    return
                }
                val string = response.body!!.string()
                Log.i("authiii",string)
                if (!StringUtils.isEmpty(string)){
                    val json = JSONObject(string)
                    if (json.getInt("code") == 200){
                        this@AuthActivity.backstr = json.getString("data")
                        loadIdData()
                    } else {
                        WaitDialog.dismiss()
                        if (!isTipDialogShowing){
                            isTipDialogShowing = true
                            TipDialog.show(this@AuthActivity,json.getString("msg"),TipDialog.TYPE.ERROR).setOnDismissListener {
                                isTipDialogShowing = false
                            }
                        }
                    }
                }
            }

        })
        ImageUtil.uploadIdentityImage("face", File(renmianImagePath),object : Callback{
            override fun onFailure(call: Call, e: IOException) {

            }
            override fun onResponse(call: Call, response: Response) {
                if (response.body == null){
                    return
                }
                val string = response.body!!.string()
                Log.i("authiii",string)
                if (!StringUtils.isEmpty(string)){
                    val json = JSONObject(string)
                    if (json.getInt("code") == 200){
                        this@AuthActivity.renmianstr = json.getString("data")
                        loadIdData()
                    } else {
                        WaitDialog.dismiss()
                        if (!isTipDialogShowing){
                            isTipDialogShowing = true
                            TipDialog.show(this@AuthActivity,json.getString("msg"),TipDialog.TYPE.ERROR).setOnDismissListener {
                                isTipDialogShowing = false
                            }
                        }

                    }
                }
            }

        })
    }

    var isTipDialogShowing = false

    fun loadIdData(){
        if (!StringUtils.isEmpty(backstr) and  !StringUtils.isEmpty(renmianstr)){
            ApiManager.getInstance().getApiPersonalTest().identifyIdCard(UserInfoViewModel.getUserId(),renmianstr,backstr).enqueue(object : retrofit2.Callback<IdBean>{
                override fun onResponse(call: retrofit2.Call<IdBean>, response: retrofit2.Response<IdBean>) {
                    WaitDialog.dismiss()
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        val customPopup =
                            IDInfoDialog(this@AuthActivity,response.body()!!)
                        XPopup.Builder(this@AuthActivity)
                            .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                            .autoOpenSoftInput(false)
                            .asCustom(customPopup)
                            .show()
                        backstr = ""
                        renmianstr = ""
                    } else {
                        DialogUtil.toast(this@AuthActivity,response.body()!!.msg)
                    }
                }

                override fun onFailure(call: retrofit2.Call<IdBean>, t: Throwable) {

                }

            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_BACK_ALBUM){
            if (data == null){
                return
            }
            handleImageOnKitKat(data,BACK)
        }
        if (requestCode == PICK_BACK_CAMERA){
            if (resultCode == RESULT_OK){
                if (backImageUri == null){
                    return
                }
                val file = FileUtils.getFileByUri(this,backImageUri)
                if (file == null){
                    return
                }
                if (StringUtils.isEmpty(file.absolutePath)){
                    return
                }
                this.backImagePath = file.absolutePath
                handleImage()
            }
        }

        if (requestCode == PICK_RENMIAN_ALBUM){
            if (data == null){
                return
            }
            handleImageOnKitKat(data,RENMIAN)
        }
        if (requestCode == PICK_RENMIAN_CAMERA){
            if (resultCode == RESULT_OK){
                if (renmianImageUri == null){
                    return
                }
                val file = FileUtils.getFileByUri(this,renmianImageUri)
                if (file == null){
                    return
                }
                if (StringUtils.isEmpty(file.absolutePath)){
                    return
                }
                this.renmianImagePath = file.absolutePath
                handleImage()
            }
        }
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

    fun handleImage(){
        if (!StringUtils.isEmpty(backImagePath)){
            Glide.with(this).load(backImagePath).into(back)
        }
        if (!StringUtils.isEmpty(renmianImagePath)){
            Glide.with(this).load(renmianImagePath).into(renmian)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun handleImageOnKitKat(data: Intent,type : Int) {
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
        if (type == BACK){
            this.backImagePath = imagePath
        } else if (type == RENMIAN){
            this.renmianImagePath = imagePath
        }
        handleImage()
    }

    val BACK = 1

    val RENMIAN = 2

}*/
