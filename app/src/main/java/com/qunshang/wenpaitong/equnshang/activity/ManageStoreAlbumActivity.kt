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

import com.kongzue.dialog.v3.BottomMenu
import kotlinx.android.synthetic.main.activity_manage_store_album.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ManageStoreAlbumAdapter
import com.qunshang.wenpaitong.equnshang.data.VendorModel
import com.qunshang.wenpaitong.equnshang.data.VendorPictureBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.FileUtils
import com.qunshang.wenpaitong.equnshang.utils.FileUtils.createImageUri
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ManageStoreAlbumActivity : BaseActivity (){

    val PICK_PHOTO_ALBUM = 100

    val PICK_PHOTO_CAMERA = 200

    val UPLOAD_IMAGE = 300

    companion object{
        val RESULT_UPLOAD_IMAGE = 401
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_store_album)
        toolbar_back.setOnClickListener { finish() }
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initView(){
        addtoalbum.setOnClickListener {
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
                    imageUri = createImageUri(this)
                    val intent = Intent()
                    intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        imageUri
                    ) //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的

                    startActivityForResult(intent, PICK_PHOTO_CAMERA)
                }
                if (index == 1){
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
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(intent, PICK_PHOTO_ALBUM)
                }
            }
        }

        loadImages()

    }

    fun loadImages(){
        ApiManager.getInstance().getApiStore().loadVendorPictures(VendorModel.getVendorId()).enqueue(object : Callback<VendorPictureBean>{
            override fun onResponse(
                call: Call<VendorPictureBean>,
                response: Response<VendorPictureBean>
            ) {

                if (response.body() == null){
                    return
                }
                list.adapter = ManageStoreAlbumAdapter(this@ManageStoreAlbumActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<VendorPictureBean>, t: Throwable) {

            }

        })
    }

    var imageUri: Uri? = null


    var imagePath = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO_ALBUM){
            if (data == null){
                return
            }
            handleImageOnKitKat(data)
        }
        if (requestCode == PICK_PHOTO_CAMERA){
            if (resultCode == RESULT_OK){
                if (imageUri == null){
                    return
                }
                val file = FileUtils.getFileByUri(this,imageUri)
                if (file == null){
                    return
                }
                if (StringUtils.isEmpty(file.absolutePath)){
                    return
                }
                this.imagePath = file.absolutePath
                handleImage()
            }
        }
        if (requestCode == UPLOAD_IMAGE){
            if (resultCode == RESULT_UPLOAD_IMAGE){
                loadImages()
            }
        }
    }

    fun handleImage(){
        if (!StringUtils.isEmpty(imagePath)){
            val intent = Intent(this,AddImageToStoreAlbumActivity::class.java)
            intent.putExtra("image",imagePath)
            startActivityForResult(intent,UPLOAD_IMAGE)
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
        //Glide.with(this).load(imagePath).into(image)
        handleImage()
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

}