package com.qunshang.wenpaitong.equnshang.fragment
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.fragment_up_load_video.*
import android.provider.MediaStore
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.kongzue.dialog.v3.WaitDialog
import com.zhihu.matisse.Matisse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.SeleRegulationActivity
import com.qunshang.wenpaitong.equnshang.data.Signature
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.utils.videoupload.TXUGCPublish
import com.qunshang.wenpaitong.equnshang.utils.videoupload.TXUGCPublishTypeDef
import java.lang.Exception

class UpLoadVideoFragment : MyBaseFragment(),
    TXUGCPublishTypeDef.ITXVideoPublishListener{

    var signature = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_up_load_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //???????????????????????????????????????????????????????????????????????????????????????
        paishe.setOnClickListener {
            capture()
        }
        xiangce.setOnClickListener {
            chooseFromAlbum()
        }
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
        rule1.setOnClickListener {
            val intent = Intent(requireContext(),
                SeleRegulationActivity::class.java)
            intent.putExtra("title","????????????????????????")
            //intent.putExtra("id",R.mipmap.selfregulationcontent)
            startActivity(intent)
        }
        rule2.setOnClickListener {
            val intent = Intent(requireContext(),
                SeleRegulationActivity::class.java)
            intent.putExtra("title","???????????????????????????")
            //intent.putExtra("id",R.mipmap.video_rule)
            startActivity(intent)
        }
    }

    fun chooseFromAlbum(){
        if (!PermissionUtil.checkCameraPermission(requireActivity())){
            PermissionUtil.requireCameraPermission(requireActivity())
            return
        }
        if (!PermissionUtil.checkStoragePermission(requireActivity())){
            PermissionUtil.requireStoragePermission(requireActivity())
            return
        }
        /*val intent = Intent()
         ??????Pictures??????Type?????????image */
        //intent.setType("image/*");
        // intent.setType("audio/*"); //????????????
        //intent.setType("video/*")//???????????? ???mp4 3gp ???android????????????????????????

        // intent.setType("video/*;image/*");//???????????????????????????

        /* ??????Intent.ACTION_GET_CONTENT??????Action
        intent.setAction(Intent.ACTION_GET_CONTENT)
         ??????????????????????????????
        startActivityForResult(intent, 1)*/
        /*val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)*/
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 1)
        /*Matisse.from(requireActivity())
            .choose(MimeType.ofVideo())
            .countable(true)
            .maxSelectable(1) //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(true) // Default is `true`
            .forResult(VIDEO_PICKE_REQUEST_CODE)*/
    }

    fun capture(){
        if (!PermissionUtil.checkCameraPermission(requireActivity())){
            PermissionUtil.requireCameraPermission(requireActivity())
            return
        }
        if (!PermissionUtil.checkStoragePermission(requireActivity())){
            PermissionUtil.requireStoragePermission(requireActivity())
            return
        }
        //val filePath: String = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera/" + System.currentTimeMillis() + ".mp4" // ????????????

        //val uri: Uri = FileProvider.getUriForFile(requireContext(),requireActivity().getPackageName() + ".fileprovider", File(filePath)) // ??????????????????Uri??????

        //val intent = Intent(context,StarActivity::class.java)

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE) // ???????????????????????????????????????

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0)

        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri) // ???????????????????????????????????????????????????????????????????????????????????????onActivityResult()??????????????????intent.getData????????????????????????

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60) // ?????????????????????????????????

        startActivityForResult(intent, 0) // ??????
        /*Matisse.from(requireActivity())
            .choose(MimeType.ofImage())
            .countable(true)
            .maxSelectable(1) //.gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(false) // Default is `true`
            .forResult(VIDEO_PICKE_REQUEST_CODE)*/

    }

    val VIDEO_PICKE_REQUEST_CODE = 985

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var uri : Uri ?= null
        if (requestCode == 0) {
            uri = data?.data // ?????????????????????
        }

        if (requestCode == 1){
            uri = data?.data

            if (uri == null){
                //DialogUtil.toast(context,"?????????")
                return
            }
            //DialogUtil.toast(context, uri.path)
        }

        if (requestCode == VIDEO_PICKE_REQUEST_CODE) {
            if (Matisse.obtainResult(data).size == 0) {
                return
            }
            val uri = Matisse.obtainResult(data).get(0)
            try {
                val cursor = context?.contentResolver?.query(uri!!, null, null, null, null)
                if (cursor == null) {
                    //DialogUtil.toast(context,"Cursor?????????")
                    Log.i(Constants.logtag, "Cursor?????????")
                    return
                } else {
                    Log.i(Constants.logtag, "?????????")
                }
                if (cursor.moveToFirst()) {
                    Log.i(Constants.logtag, "??????")
                    val videoPath =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    onVideoLoaded(videoPath)
                } else {
                    Log.i(Constants.logtag, "?????????")
                    //DialogUtil.toast(context,"???????????????")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }

        if ((resultCode == Activity.RESULT_OK) and (uri != null)) {
            val videoPath = FileUtils.getPath(requireContext(),uri)
            onVideoLoaded(videoPath)
            //try {
                /*val cursor = context?.contentResolver?.query(uri!!, null, null, null, null)
                if (cursor == null) {
                    //DialogUtil.toast(context,"Cursor?????????")
                        Log.i(Constants.logtag,"Cursor?????????")
                    return
                } else {
                    Log.i(Constants.logtag,"?????????")
                }
                if (cursor.moveToFirst()) {
                    Log.i(Constants.logtag,"??????")
                    val videoPath = cursor.getString(cursor.getColumnIndex("_data"))
                    onVideoLoaded(videoPath)
                } else {
                    Log.i(Constants.logtag,"?????????")
                    //DialogUtil.toast(context,"???????????????")
                }*/
            /*} catch (e : Exception){
                e.printStackTrace()
            }*/
        }
    }

    fun onVideoLoaded(videoPath : String){
        Log.i(Constants.logtag,"??????")
        paishe.visibility = View.GONE
        xiangce.visibility = View.GONE
        rule1.visibility = View.GONE
        rule2.visibility = View.GONE
        layout.visibility = View.VISIBLE

        val retriever = MediaMetadataRetriever()
        try {
            //??????url???????????????
            retriever.setDataSource(videoPath)
            //?????????????????????
            val bitmap = retriever.getFrameAtTime()
            //paishe.setImageBitmap(bitmap)
            image.setImageBitmap(bitmap)
            cancel.setOnClickListener {
                layout.visibility = View.GONE
                paishe.visibility = View.VISIBLE
                xiangce.visibility = View.VISIBLE
                rule1.visibility = View.VISIBLE
                rule2.visibility = View.VISIBLE
            }
            confirm.setOnClickListener {

                var coverPath = ""

                if (StringUtils.isEmpty(videoname.text.toString())){
                    DialogUtil.toast(context,"?????????????????????")
                    return@setOnClickListener
                }
                coverPath = BitmapUtils.saveBitmap(System.currentTimeMillis().toString() + ".jpg",bitmap,context)
                if (StringUtils.isEmpty(coverPath)){
                    DialogUtil.toast(context,"?????????")
                    return@setOnClickListener
                }
                //BindAddressBean().

                /*val endVideo = File(videoPath).parent + "/" + "equnshangvideoofend.mp4"
                if (!File(endVideo).exists()){
                    FileUtils.doCopy(requireContext(),"video",endVideo)
                }

                var outPutFile = File(videoPath).parent + File.separator + System.currentTimeMillis() + ".mp4"

                val list =  ArrayList<String>()
                list.add(videoPath)
                list.add(endVideo)
                FileUtils.mergeVideos(list,outPutFile)*/

                doUpload(videoPath,coverPath)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(str : String){}

    fun doUpload(videoPath: String,coverPath : String){
        WaitDialog.show(requireActivity() as AppCompatActivity,"????????????")
        if (StringUtils.isEmpty(signature)){
            requireActivity().runOnUiThread {
                DialogUtil.toast(context,"?????????")
            }
            return
        }
        val mVideoPublish = TXUGCPublish(
            this.context,
            "independence_android"
        )
        // mVideoPublish.setAppId(123456);
        mVideoPublish.setListener(this)

        val param: TXUGCPublishTypeDef.TXPublishParam = TXUGCPublishTypeDef.TXPublishParam()
        param.signature = signature
        param.videoPath = videoPath
        param.coverPath = coverPath
        val publishCode: Int = mVideoPublish.publishVideo(param)
        if (publishCode != 0) {
            requireActivity().runOnUiThread {
                DialogUtil.toast(context,"?????????")
            }
            return
        }
    }

    override fun onPublishProgress(uploadBytes: Long, totalBytes: Long) {
        requireActivity().runOnUiThread {
            WaitDialog.show(requireActivity() as AppCompatActivity,"????????????").setMessage("????????????" + ((uploadBytes*100)/totalBytes) + "%")
                .refreshView()
            //WaitDialog.show(requireActivity() as AppCompatActivity,"????????????")
        }
    }

    override fun onPublishComplete(result: TXUGCPublishTypeDef.TXPublishResult?) {

        requireActivity().runOnUiThread {
            WaitDialog.dismiss()
            if (result == null) {
                DialogUtil.toast(context, "?????????")
                return@runOnUiThread
            }
            var isprivate = 0
            if (switch_btn.isChecked){
                isprivate = 0
            } else {
                isprivate = 1
            }
            Log.i("resultaaa",result.videoId + "a" + result.coverURL + "b" + result.videoURL)
            ApiManager.getInstance().getApiPersonalTest().uploadData(result.videoId,
                result.coverURL,result.videoURL,
                UserInfoViewModel.getUserId(),videoname.text.toString(),
                "",isprivate).enqueue(object : Callback<BaseHttpBean<Int>>{
                override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {
                    if (response.body() == null){
                        DialogUtil.toast(context,"?????????")
                        return
                    }
                    if (response.body()!!.code == 200){
                        DialogUtil.toast(context,"????????????")
                        layout.visibility = View.GONE
                        paishe.visibility = View.VISIBLE
                        xiangce.visibility = View.VISIBLE
                        rule1.visibility = View.VISIBLE
                        rule2.visibility = View.VISIBLE
                        EventBus.getDefault().post("refreshVideo")

                    } else {
                        DialogUtil.toast(context,"????????????")
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                }

            })
        }
    }

}