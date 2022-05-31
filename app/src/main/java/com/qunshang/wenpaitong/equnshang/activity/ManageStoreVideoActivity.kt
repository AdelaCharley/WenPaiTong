package com.qunshang.wenpaitong.equnshang.activity

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import com.kongzue.dialog.v3.BottomMenu
import kotlinx.android.synthetic.main.activity_manage_store_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ManageStoreVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.VendorModel
import com.qunshang.wenpaitong.equnshang.data.VendorVideoBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.BitmapUtils
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ManageStoreVideoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_store_video)
        toolbar_back.setOnClickListener { finish() }
        initView()
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
                    capture()
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
                    chooseFromAlbum()
                }
            }
        }

        loadVideos()

    }

    fun chooseFromAlbum(){
        if (!PermissionUtil.checkStoragePermission(this)){
            PermissionUtil.requireStoragePermission(this)
            return
        }
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun capture(){
        if (!PermissionUtil.checkCameraPermission(this)){
            PermissionUtil.requireCameraPermission(this)
            return
        }
        if (!PermissionUtil.checkStoragePermission(this)){
            PermissionUtil.requireStoragePermission(this)
            return
        }

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE) // 表示跳转至相机的录视频界面

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0)

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60) // 设置视频录制的最长时间

        startActivityForResult(intent, 0) // 跳转

    }

    fun loadVideos(){
        ApiManager.getInstance().getApiStore().loadVendorVideos(VendorModel.getVendorId()).enqueue(object : Callback<VendorVideoBean>{
            override fun onResponse(
                call: Call<VendorVideoBean>,
                response: Response<VendorVideoBean>
            ) {

                if (response.body() == null){
                    return
                }
                list.adapter = ManageStoreVideoAdapter(this@ManageStoreVideoActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<VendorVideoBean>, t: Throwable) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri : Uri ?= null
        if (requestCode == 0) {
            uri = data?.data // 视频的保存路径
        }

        if (requestCode == 1){
            uri = data?.data

            if (uri == null){
                DialogUtil.toast(this,"出错了")
                return
            }
            //DialogUtil.toast(this, uri.path)
        }

        if ((resultCode == Activity.RESULT_OK) and (uri != null)) {
            try {
                val cursor = this.contentResolver?.query(uri!!, null, null, null, null)
                if (cursor == null) {
                    //DialogUtil.toast(this,"Cursor出错了")
                    return
                } else {

                }
                if (cursor.moveToFirst()) {
                    val videoPath = cursor.getString(cursor.getColumnIndex("_data"))
                    onVideoLoaded(videoPath)
                } else {
                    DialogUtil.toast(this,"未找到数据")
                }
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
        if (requestCode == REQUEST_UPLOAD_VIDEO){
            if (resultCode == RESULT_UPLOAD_VIDEO){
                loadVideos()
            }
        }
    }

    fun onVideoLoaded(videoPath : String){
        val retriever = MediaMetadataRetriever()
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoPath)
            //获得第一帧图片
            val bitmap = retriever.getFrameAtTime()

            var coverPath = ""

            coverPath = BitmapUtils.saveBitmap(System.currentTimeMillis().toString() + ".jpg",bitmap,this)
            if (StringUtils.isEmpty(coverPath)){
                DialogUtil.toast(this,"出错了")
                return
            }
            if (StringUtils.isEmpty(videoPath) or StringUtils.isEmpty(coverPath)){
                return
            }
            val intent = Intent(this,AddVideoToStoreVideosActivity::class.java)
            intent.putExtra("videoPath",videoPath)
            intent.putExtra("coverPath" , coverPath)
            startActivityForResult(intent, REQUEST_UPLOAD_VIDEO)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
    }

    companion object{
        val REQUEST_UPLOAD_VIDEO = 900

        val RESULT_UPLOAD_VIDEO = 700
    }

}