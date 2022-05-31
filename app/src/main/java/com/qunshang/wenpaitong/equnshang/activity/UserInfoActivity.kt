package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.InputDialog
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.IdentityInfoBean
import com.qunshang.wenpaitong.equnshang.data.LogOutStatusBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import java.io.File
import java.io.IOException

class UserInfoActivity: BaseActivity() {

    companion object {
        private const val DIALOG_SEND_CODE_TITLE = "重设密码"
        private var DIALOG_SEND_CODE_CONTENT = "将验证码发送至${UserInfoViewModel.getUserInfo()?.utel}"
    }

    val PICK_PHOTO = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        DIALOG_SEND_CODE_CONTENT = "将验证码发送至${UserInfoViewModel.getUserInfo()?.utel}"
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("用户信息")
        ApiManager.getInstance().getApiOld().identifyedBean(UserInfoViewModel.getUserId())
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>,
                                        response: Response<ResponseBody>) {
                    if (response.body() == null) {
                        Log.i("authiii", "njlla")
                        return
                    }
                    val jsonstr = response.body()!!.string()
                    val json = JSONObject(jsonstr)
                    if (json.getInt("status") == 0) {
                        layout_auth.setOnClickListener {
                            val intent = Intent(this@UserInfoActivity, AuthInfoActivity::class.java)
                            val gson = Gson()
                            val realInfo = gson.fromJson(json.getJSONObject("realInfo").toString(),
                                                         IdentityInfoBean::class.java)
                            intent.putExtra("info", realInfo)
                            startActivity(intent)
                        }
                    } else {
                        layout_auth.setOnClickListener {
                            val intent =
                                Intent(this@UserInfoActivity, WenBanTongAuthActivity::class.java)
                            intent.putExtra("isGoInfo", true)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
        initView()
    }

    fun initView() {
        layout_mycode.setOnClickListener {
            val intent = Intent(this,QrCodeShareActivity::class.java)
            startActivity(intent)
        }
        Glide.with(this).load(UserInfoViewModel.getUserInfo()?.headimage).into(userimage)
        username.setText(UserInfoViewModel.getUserInfo()?.uname)
        labelidentity.setText(UserInfoViewModel.getUserInfo()?.identity)
        if (UserInfoViewModel.getUserInfo()?.gender == 1) { //0是男，1是女
            sex.setText("女")
        } else {
            sex.setText("男")
        }
        signature.setText(UserInfoViewModel.getUserInfo()?.introduce)
        userid.setText(UserInfoViewModel.getUserId())
        phone.setText(UserInfoViewModel.getUserInfo()?.utel)
        layout_username.setOnClickListener {
            updateUserName()
        }
        birthday.setText(UserInfoViewModel.getUserInfo()?.birthday)
        layout_sex.setOnClickListener {
            updateSex()
        }
        userimage.setOnClickListener {
            openAlbum()
        }
        layout_sig.setOnClickListener {
            updateSignature()
        }
        layout_reset_passwd.setOnClickListener {
            MessageDialog.show(this, DIALOG_SEND_CODE_TITLE, DIALOG_SEND_CODE_CONTENT, "确认", "取消")
                .setOkButton(OnDialogButtonClickListener() { baseDialog, _ ->
                    baseDialog.doDismiss()
                    val intent = Intent(this, ResetPasswdActivity::class.java)
                    startActivity(intent)
                    return@OnDialogButtonClickListener true
                })
                .setCancelButton(OnDialogButtonClickListener() { baseDialog, _ ->
                    baseDialog.doDismiss()
                    return@OnDialogButtonClickListener true
                })
        }
        layout_zhuxiao.setOnClickListener {
            WaitDialog.show(this, "正在获取...")
            ApiManager.getInstance().getApiPersonalTest().loadLogOutStatus(UserInfoViewModel.getUserId()).enqueue(object : Callback<LogOutStatusBean>{
                override fun onResponse(
                    call: Call<LogOutStatusBean>,
                    response: Response<LogOutStatusBean>
                ) {
                    WaitDialog.dismiss()
                    if (response.body() == null){
                        return
                    }
                    if (response.body()!!.code == 200){
                        when(response.body()!!.data.status){
                            0 -> {
                                val intent =
                                    Intent(this@UserInfoActivity, ApplyLogoutActivity::class.java)
                                startActivity(intent)
                            }
                            10 -> {
                                val intent =
                                    Intent(this@UserInfoActivity, ApplyLogoutingActivity::class.java)
                                startActivity(intent)
                            }
                            20 -> {
                                val intent =
                                    Intent(this@UserInfoActivity, ApplyLogOutFailedActivity::class.java)
                                intent.putExtra("reason",response.body()!!.data.reason)
                                startActivity(intent)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<LogOutStatusBean>, t: Throwable) {

                }

            })
            /*val thread = Thread(object: Runnable {
                override fun run() {
                    Thread.sleep(500)
                    runOnUiThread {
                        WaitDialog.dismiss()
                        if (UserHelper.getCurrentLogoutingStatus(this@UserInfoActivity)) {
                            val intent =
                                Intent(this@UserInfoActivity, ApplyLogoutingActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent =
                                Intent(this@UserInfoActivity, ApplyLogoutActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            })
            thread.start()*/
        }

        var identity = ""

        if (!(UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and !(UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3) {
                identity = "会员,主任"
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                identity = "会员,总裁"
            } else {
                identity = "会员,店主"
            }
            //identity = "会员,合伙人"
        }
        if (!(UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and (UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            identity = "会员"
        }
        if ((UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and !(UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3) {
                identity = "主任"
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                identity = "总裁"
            } else {
                identity = "店主"
            }
        }
        if ((UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and (UserInfoViewModel.getUserInfo()!!.is_partner <= 1)) { //既是会员又是合伙人
            identity = "粉丝"
        }
        labelidentity.setText(identity)
    }

    fun openAlbum() {
        if (!PermissionUtil.checkStoragePermission(this)) {
            PermissionUtil.requireStoragePermission(this)
            return
        }
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO) {
            if (data == null) {
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
        uploadImage(imagePath)
    }

    fun uploadImage(imagePath: String) {
        WaitDialog.show(this,"正在上传")
        ImageUtil.uploadImage(UserInfoViewModel.getUserId(),
                                                             File(imagePath),
                                                             object: okhttp3.Callback {
                                                                 override fun onFailure(call: okhttp3.Call,
                                                                                        e: IOException) {
                                                                     WaitDialog.dismiss()
                                                                     DialogUtil.toast(this@UserInfoActivity,"上传图片出错")
                                                                 }

                                                                 override fun onResponse(call: okhttp3.Call,
                                                                                         response: okhttp3.Response) {
                                                                     val result =
                                                                         response.body?.string()
                                                                     if (result != null) {
                                                                         val json =
                                                                             JSONObject(result)
                                                                         if (json.getInt("code") == 200) {
                                                                             updateImageURL(json.getString(
                                                                                     "data"))
                                                                         }
                                                                     }
                                                                 }

                                                             })
    }

    @SuppressLint("Range")
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

    fun updateSex() {
        BottomMenu.show(
                this,
                arrayOf("男", "女"),
                object: OnMenuItemClickListener {
                    override fun onClick(text: String?, index: Int) {

                        var gender = 0

                        if (index == 0) {
                            gender = 0
                        } else if (index == 1) {
                            gender = 1
                        }

                        ApiManager.getInstance().getApiPersonalTest().updatePersonalData(
                                UserInfoViewModel.getUserId(),
                                UserInfoViewModel.getUserInfo()!!.uname,
                                gender.toString(),
                                //UserInfoViewModel.getUserInfo()!!.birthday,
                                //UserInfoViewModel.getUserInfo()!!.tags,
                                UserInfoViewModel.getUserInfo()!!.headimage)
                            .enqueue(object: Callback<UserMsgBean> {
                                override fun onResponse(call: Call<UserMsgBean>,
                                                        response: Response<UserMsgBean>) {
                                    Log.i("zhangjuniii",
                                          UserInfoViewModel.getUserInfo()!!.headimage)
                                    if (response.body() == null) {
                                        DialogUtil.toast(this@UserInfoActivity, "出错了")
                                    }
                                    if (response.body()!!.code == 200) {
                                        UserInfoViewModel.getUserInfo()!!.gender =
                                            response.body()!!.data.gender
                                        if (UserInfoViewModel.getUserInfo()!!.gender == 0) {
                                            sex.setText("男")
                                        } else {
                                            sex.setText("女")
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {

                                }

                            })

                    }

                })
    }

    fun updateSignature() {
        //需要更改，先别用
        InputDialog.show(this, "修改签名", "请输入新的签名", "确定", "取消")
            .setOnOkButtonClickListener(object: OnInputDialogButtonClickListener {

                override fun onClick(baseDialog: BaseDialog?,
                                     v: View?,
                                     inputStr: String?): Boolean {

                    ApiManager.getInstance().getApiPersonalTest().updatePersonalData(
                            UserInfoViewModel.getUserId(),
                            UserInfoViewModel.getUserInfo()!!.uname,
                            UserInfoViewModel.getUserInfo()!!.gender.toString(),
                            //UserInfoViewModel.getUserInfo()!!.birthday,
                            //UserInfoViewModel.getUserInfo()!!.tags,
                            UserInfoViewModel.getUserInfo()!!.headimage)
                        .enqueue(object: Callback<UserMsgBean> {
                            override fun onResponse(call: Call<UserMsgBean>,
                                                    response: Response<UserMsgBean>) {
                                Log.i("zhangjuniii", UserInfoViewModel.getUserInfo()!!.headimage)
                                if (response.body() != null) {
                                    //DialogUtil.toast(this@UserInfoActivity,"" + response.body()!!.data.uname)
                                }
                            }

                            override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {

                            }

                        })

                    return false
                }

            })
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun updateUserName() {
        val intent = Intent(this,ChangeUserNameActivity::class.java)
        startActivity(intent)
        return
        InputDialog.show(this, "更改用户名", "请输入新的用户名", "确定", "取消")
            .setCancelable(false)
            .setOnOkButtonClickListener(object: OnInputDialogButtonClickListener {

                override fun onClick(baseDialog: BaseDialog?,
                                     v: View?,
                                     inputStr: String?): Boolean {

                    ApiManager.getInstance().getApiPersonalTest().updatePersonalData(
                            UserInfoViewModel.getUserId(),
                            inputStr!!,
                            UserInfoViewModel.getUserInfo()!!.gender.toString(),
                            //UserInfoViewModel.getUserInfo()!!.birthday,
                            //UserInfoViewModel.getUserInfo()!!.tags,
                            UserInfoViewModel.getUserInfo()!!.headimage)
                        .enqueue(object: Callback<UserMsgBean> {
                            override fun onResponse(call: Call<UserMsgBean>,
                                                    response: Response<UserMsgBean>) {
                                Log.i("zhangjuniii", UserInfoViewModel.getUserInfo()!!.headimage)
                                if (response.body() == null) {
                                    return
                                    //ToastUtil.toast(this@UserInfoActivity,"" + response.body()!!.data.uname)
                                }
                                if (response.body()!!.code == 200) {
                                    DialogUtil.toast(this@UserInfoActivity,"修改成功")
                                    username.setText(inputStr)
                                    UserInfoViewModel.getUserInfo()?.uname = inputStr
                                } else {
                                    DialogUtil.toast(this@UserInfoActivity,"修改失败")
                                }
                            }

                            override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {

                            }

                        })

                    return false
                }

            })
    }

    fun updateImageURL(url: String) {
        runOnUiThread {

            ApiManager.getInstance().getApiPersonalTest().updatePersonalData(
                    UserInfoViewModel.getUserId(),
                    UserInfoViewModel.getUserInfo()!!.uname!!,
                    UserInfoViewModel.getUserInfo()!!.gender.toString(),
                    //UserInfoViewModel.getUserInfo()!!.birthday,
                    //UserInfoViewModel.getUserInfo()!!.tags,
                    url).enqueue(object: Callback<UserMsgBean> {
                override fun onResponse(call: Call<UserMsgBean>, response: Response<UserMsgBean>) {
                    WaitDialog.dismiss()
                    Log.i("zhangjuniii", UserInfoViewModel.getUserInfo()!!.headimage)
                    if (response.body() != null) {
                        //DialogUtil.toast(this@UserInfoActivity,"" + response.body()!!.data.headimage)
                        Log.i("zhangjuniii", "" + response.body()!!.data.headimage)
                    } else {
                        return
                    }
                    if (response.body()?.code == 200) {
                        Glide.with(this@UserInfoActivity).load(response.body()?.data!!.headimage)
                            .into(userimage)
                        UserInfoViewModel.getUserInfo()?.headimage =
                            response.body()?.data!!.headimage
                    }
                }

                override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                    WaitDialog.dismiss()
                }

            })
        }

    }

}